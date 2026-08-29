package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.InterestTagUpdateDTO;
import com.example.TalentOrbit.dto.response.AcademicianInterestResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.CollaborationStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicianService {
    @Autowired private AcademicianInterestRepository interestRepository;
    @Autowired private AcademicianInterestTagRepository interestTagRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private UserRepository userRepository;
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
        interestRepository.save(interest);
    }

    public List<AcademicianInterestResponseDTO> getInterests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return interestRepository.findByUser(user).stream().map(ai -> {
            AcademicianInterestResponseDTO dto = new AcademicianInterestResponseDTO();
            dto.setId(ai.getId());
            dto.setPostingId(ai.getPosting().getId());
            dto.setPostingTitle(ai.getPosting().getTitle());
            dto.setPostingType(ai.getPosting().getPostingType().name());
            dto.setStatus(ai.getStatus());
            dto.setExpressedAt(ai.getExpressedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void updateInterestTags(InterestTagUpdateDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<AcademicianInterestTag> existing = interestTagRepository.findByUser(user);
        interestTagRepository.deleteAll(existing);

        if (req.getSkillIds() != null) {
            for (Long sid : req.getSkillIds()) {
                Skill sk = skillRepository.findById(sid).orElse(null);
                if (sk != null) {
                    AcademicianInterestTag tag = new AcademicianInterestTag();
                    tag.setUser(user);
                    tag.setSkill(sk);
                    interestTagRepository.save(tag);
                }
            }
        }
    }
}
