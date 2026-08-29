package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.DemandTrendDTO;
import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.PostingSkill;
import com.example.TalentOrbit.repository.CompanyDetailsRepository;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.PostingSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalyticsService {

    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private CompanyDetailsRepository companyDetailsRepository;

    public long getTotalActiveCompanyCount() {
        return companyDetailsRepository.count();
    }

    public List<DemandTrendDTO> getSkillDemandTrend(int months) {
        LocalDateTime cutoff = LocalDateTime.now().minusMonths(months > 0 ? months : 6);
        List<Posting> recentPostings = postingRepository.findAll().stream()
                .filter(p -> p.getCreatedAt().isAfter(cutoff))
                .collect(java.util.stream.Collectors.toList());

        Map<String, Map<String, Long>> monthlySkillCounts = new TreeMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy");

        for (Posting p : recentPostings) {
            String monthKey = p.getCreatedAt().format(fmt);
            List<PostingSkill> pSkills = postingSkillRepository.findByPosting(p);
            for (PostingSkill ps : pSkills) {
                String skillName = ps.getSkill().getName();
                monthlySkillCounts.computeIfAbsent(monthKey, k -> new HashMap<>())
                        .merge(skillName, 1L, Long::sum);
            }
        }

        List<DemandTrendDTO> results = new ArrayList<>();
        for (Map.Entry<String, Map<String, Long>> monthEntry : monthlySkillCounts.entrySet()) {
            for (Map.Entry<String, Long> skillEntry : monthEntry.getValue().entrySet()) {
                results.add(new DemandTrendDTO(monthEntry.getKey(), skillEntry.getKey(), skillEntry.getValue()));
            }
        }

        return results;
    }
}
