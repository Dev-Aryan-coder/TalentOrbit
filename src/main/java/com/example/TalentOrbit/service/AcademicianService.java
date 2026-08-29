package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.InterestTagUpdateDTO;
import com.example.TalentOrbit.dto.response.AcademicianInterestResponseDTO;
import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.CollaborationStatus;
import com.example.TalentOrbit.enums.PostingType;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicianService {

    @Autowired private AcademicianInterestRepository academicianInterestRepository;
    @Autowired private AcademicianInterestTagRepository academicianInterestTagRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private SkillRepository skillRepository;

    public void expressInterest(Long userId, Long postingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("Posting not found"));

        AcademicianInterest interest = new AcademicianInterest();
        interest.setUser(user);
        interest.setPosting(posting);
        interest.setStatus(CollaborationStatus.INTERESTED);
        academicianInterestRepository.save(interest);
    }

    public List<AcademicianInterestResponseDTO> getInterests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return academicianInterestRepository.findByUser(user).stream().map(ai -> {
            AcademicianInterestResponseDTO dto = new AcademicianInterestResponseDTO();
            dto.setId(ai.getId());
            dto.setPostingId(ai.getPosting().getId());
            dto.setPostingTitle(ai.getPosting().getTitle());
            dto.setCompanyName(ai.getPosting().getPostedBy().getEmail());
            dto.setStatus(ai.getStatus());
            dto.setExpressedAt(ai.getExpressedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void updateInterestTags(InterestTagUpdateDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        academicianInterestTagRepository.deleteByUser(user);

        if (req.getSkillIds() != null) {
            for (Long skillId : req.getSkillIds()) {
                Skill skill = skillRepository.findById(skillId).orElse(null);
                if (skill != null) {
                    AcademicianInterestTag tag = new AcademicianInterestTag();
                    tag.setUser(user);
                    tag.setSkill(skill);
                    academicianInterestTagRepository.save(tag);
                }
            }
        }
    }

    public List<PostingResponseDTO> getMatchedResearchOpportunities(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<AcademicianInterestTag> facultyTags = academicianInterestTagRepository.findByUser(user);
        Set<String> facultySkillNames = facultyTags.stream()
                .map(t -> t.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        List<Posting> researchPostings = postingRepository.findByPostingTypeAndIsActiveTrue(PostingType.RESEARCH);
        List<PostingResponseDTO> results = new ArrayList<>();

        for (Posting p : researchPostings) {
            List<PostingSkill> reqSkills = postingSkillRepository.findByPosting(p);
            List<String> matched = new ArrayList<>();
            List<String> missing = new ArrayList<>();
            List<String> allReq = new ArrayList<>();

            int totalWeight = 0;
            int matchedWeight = 0;

            for (PostingSkill ps : reqSkills) {
                String skName = ps.getSkill().getName().trim();
                allReq.add(skName);
                int weight = (ps.getWeight() != null && ps.getWeight() > 0) ? ps.getWeight() : 1;
                totalWeight += weight;

                if (facultySkillNames.contains(skName.toLowerCase())) {
                    matchedWeight += weight;
                    matched.add(skName);
                } else {
                    missing.add(skName);
                }
            }

            int score = totalWeight > 0 ? (int) Math.round(((double) matchedWeight / totalWeight) * 100.0) : 0;

            PostingResponseDTO dto = new PostingResponseDTO();
            dto.setId(p.getId());
            dto.setPostedByUserId(p.getPostedBy().getId());
            dto.setTitle(p.getTitle());
            dto.setPostingType(p.getPostingType());
            dto.setDescription(p.getDescription());
            dto.setLocation(p.getLocation());
            dto.setStipend(p.getStipend());
            dto.setDeadline(p.getDeadline());
            dto.setIsActive(p.getIsActive());
            dto.setRequiredSkills(allReq);
            dto.setMatchScore(score);
            dto.setMatchedSkills(matched);
            dto.setMissingSkills(missing);
            results.add(dto);
        }

        results.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        return results;
    }
}
