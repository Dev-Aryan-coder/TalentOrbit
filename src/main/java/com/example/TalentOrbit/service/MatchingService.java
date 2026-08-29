package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.PostingSkill;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.PostingSkillRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;

    public static class MatchResult {
        public int score;
        public List<String> matchedSkills = new ArrayList<>();
        public List<String> missingSkills = new ArrayList<>();
    }

    public MatchResult calculateMatchForPosting(Long userId, Long postingId) {
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("Posting not found"));
        return calculateMatch(userId, posting);
    }

    public MatchResult calculateMatch(Long userId, Posting posting) {
        List<StudentSkill> studentSkills = studentSkillRepository.findByUserId(userId);
        Set<String> studentSkillNames = new HashSet<>();
        for (StudentSkill ss : studentSkills) {
            studentSkillNames.add(ss.getSkill().getName().trim().toLowerCase());
        }

        List<PostingSkill> reqSkills = postingSkillRepository.findByPosting(posting);
        MatchResult res = new MatchResult();
        if (reqSkills.isEmpty()) {
            res.score = 100;
            return res;
        }

        int totalWeight = 0;
        int matchedWeight = 0;

        for (PostingSkill ps : reqSkills) {
            String skName = ps.getSkill().getName().trim();
            int weight = (ps.getWeight() != null && ps.getWeight() > 0) ? ps.getWeight() : 1;
            totalWeight += weight;

            if (studentSkillNames.contains(skName.toLowerCase())) {
                matchedWeight += weight;
                res.matchedSkills.add(skName);
            } else {
                res.missingSkills.add(skName);
            }
        }

        res.score = totalWeight > 0 ? (int) Math.round(((double) matchedWeight / totalWeight) * 100.0) : 0;
        return res;
    }

    public List<PostingResponseDTO> getMatchedPostingsForStudent(Long userId) {
        List<Posting> activePostings = postingRepository.findByIsActiveTrue();
        List<PostingResponseDTO> results = new ArrayList<>();

        for (Posting p : activePostings) {
            List<PostingSkill> reqSkills = postingSkillRepository.findByPosting(p);
            if (reqSkills.isEmpty()) continue;

            MatchResult match = calculateMatch(userId, p);
            List<String> allReq = new ArrayList<>();
            for (PostingSkill ps : reqSkills) {
                allReq.add(ps.getSkill().getName());
            }

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
            dto.setMatchScore(match.score);
            dto.setMatchedSkills(match.matchedSkills);
            dto.setMissingSkills(match.missingSkills);
            results.add(dto);
        }

        results.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        return results;
    }
}
