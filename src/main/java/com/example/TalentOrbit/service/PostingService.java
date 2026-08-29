package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.PostingCreateDTO;
import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.PostingSkill;
import com.example.TalentOrbit.entity.Skill;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.PostingSkillRepository;
import com.example.TalentOrbit.repository.SkillRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostingService {
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SkillRepository skillRepository;

    public PostingResponseDTO createPosting(PostingCreateDTO req) {
        User user = userRepository.findById(req.getPostedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Posting p = new Posting();
        p.setPostedBy(user);
        p.setTitle(req.getTitle());
        p.setPostingType(req.getPostingType());
        p.setDescription(req.getDescription());
        p.setLocation(req.getLocation());
        p.setStipend(req.getStipend());
        p.setDeadline(req.getDeadline());
        p.setIsActive(true);
        Posting saved = postingRepository.save(p);

        List<String> skillNames = new ArrayList<>();
        if (req.getSkillIds() != null) {
            for (Long sid : req.getSkillIds()) {
                Skill skill = skillRepository.findById(sid).orElse(null);
                if (skill != null) {
                    PostingSkill ps = new PostingSkill();
                    ps.setPosting(saved);
                    ps.setSkill(skill);
                    postingSkillRepository.save(ps);
                    skillNames.add(skill.getName());
                }
            }
        }
        PostingResponseDTO dto = new PostingResponseDTO();
        dto.setId(saved.getId());
        dto.setPostedByUserId(user.getId());
        dto.setTitle(saved.getTitle());
        dto.setPostingType(saved.getPostingType());
        dto.setDescription(saved.getDescription());
        dto.setLocation(saved.getLocation());
        dto.setStipend(saved.getStipend());
        dto.setDeadline(saved.getDeadline());
        dto.setIsActive(saved.getIsActive());
        dto.setRequiredSkills(skillNames);
        return dto;
    }

    public List<PostingResponseDTO> getAllActivePostings() {
        return postingRepository.findByIsActiveTrue().stream().map(p -> {
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
            List<String> skills = postingSkillRepository.findByPosting(p).stream()
                    .map(ps -> ps.getSkill().getName()).collect(Collectors.toList());
            dto.setRequiredSkills(skills);
            return dto;
        }).collect(Collectors.toList());
    }
}
