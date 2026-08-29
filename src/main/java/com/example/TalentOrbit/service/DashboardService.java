package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.DashboardStatsResponseDTO;
import com.example.TalentOrbit.dto.response.SkillGapRowDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;

    public DashboardStatsResponseDTO getStatsForRole(Role role) {
        long totalPostings = postingRepository.count();
        long totalApplications = applicationRepository.count();
        long totalSkills = skillRepository.count();

        DashboardStatsResponseDTO dto = new DashboardStatsResponseDTO();
        dto.setRole(role);
        dto.setTotalActivePostings(totalPostings);
        dto.setTotalApplications(totalApplications);
        dto.setTotalSkillsInMaster(totalSkills);
        return dto;
    }

    public List<SkillGapRowDTO> getSkillGapTable(Long institutionId) {
        List<Skill> allSkills = skillRepository.findAll();
        List<Posting> activePostings = postingRepository.findByIsActiveTrue();

        // 1. Compute Demand: sum of weights across active postings
        Map<Long, Integer> demandMap = new HashMap<>();
        for (Posting p : activePostings) {
            List<PostingSkill> pSkills = postingSkillRepository.findByPosting(p);
            for (PostingSkill ps : pSkills) {
                int weight = (ps.getWeight() != null && ps.getWeight() > 0) ? ps.getWeight() : 1;
                demandMap.merge(ps.getSkill().getId(), weight, Integer::sum);
            }
        }

        // 2. Compute Supply: count of verified student skills
        Map<Long, Integer> supplyMap = new HashMap<>();
        List<StudentSkill> studentSkills;
        if (institutionId != null) {
            InstitutionDetails inst = institutionDetailsRepository.findById(institutionId).orElse(null);
            if (inst != null) {
                List<StudentDetails> cohort = studentDetailsRepository.findByAisheCode(inst.getAisheCode());
                Set<Long> cohortUserIds = cohort.stream().map(s -> s.getUser().getId()).collect(java.util.stream.Collectors.toSet());
                studentSkills = studentSkillRepository.findAll().stream()
                        .filter(ss -> cohortUserIds.contains(ss.getUser().getId()))
                        .collect(java.util.stream.Collectors.toList());
            } else {
                studentSkills = studentSkillRepository.findAll();
            }
        } else {
            studentSkills = studentSkillRepository.findAll();
        }

        for (StudentSkill ss : studentSkills) {
            supplyMap.merge(ss.getSkill().getId(), 1, Integer::sum);
        }

        // 3. Calculate Deficit %
        List<SkillGapRowDTO> result = new ArrayList<>();
        for (Skill sk : allSkills) {
            int demand = demandMap.getOrDefault(sk.getId(), 0);
            int supply = supplyMap.getOrDefault(sk.getId(), 0);

            int deficit = 0;
            if (demand > 0) {
                deficit = (int) Math.max(0, Math.round(((double) (demand - supply) / demand) * 100.0));
            }

            int affectedStudents = Math.max(0, demand - supply) * 10;
            result.add(new SkillGapRowDTO(sk.getName(), demand, supply, -deficit, affectedStudents));
        }

        result.sort((a, b) -> Integer.compare(a.getDeficitPercentage(), b.getDeficitPercentage()));
        return result;
    }
}
