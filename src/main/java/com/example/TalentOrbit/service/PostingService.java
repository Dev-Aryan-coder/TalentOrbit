package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.PostingCreateDTO;
import com.example.TalentOrbit.dto.request.PostingSkillDTO;
import com.example.TalentOrbit.dto.request.SkillPreviewRequestDTO;
import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.dto.response.SkillPreviewResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.FlagItemType;
import com.example.TalentOrbit.enums.FlagStatus;
import com.example.TalentOrbit.enums.PostingType;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostingService {
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private PlatformSettingRepository platformSettingRepository;
    @Autowired private FlagRepository flagRepository;
    @Autowired private AuditLogService auditLogService;

    public SkillPreviewResponseDTO previewMatchCount(SkillPreviewRequestDTO req) {
        if (req == null || req.getSkillIds() == null || req.getSkillIds().isEmpty()) {
            return new SkillPreviewResponseDTO(0, 0);
        }
        return countMatchingStudents(req.getSkillIds());
    }

    public SkillPreviewResponseDTO countMatchingStudents(List<Long> skillIds) {
        if (skillIds == null || skillIds.isEmpty()) {
            return new SkillPreviewResponseDTO(0, 0);
        }

        Map<Long, Set<Long>> studentSkillsMap = new HashMap<>();
        List<StudentSkill> allStudentSkills = studentSkillRepository.findAll();
        for (StudentSkill ss : allStudentSkills) {
            Long studentId = ss.getUser().getId();
            studentSkillsMap.computeIfAbsent(studentId, k -> new HashSet<>()).add(ss.getSkill().getId());
        }

        Set<Long> reqSet = new HashSet<>(skillIds);
        int allMatch = 0;
        int anyMatch = 0;

        for (Set<Long> heldSkills : studentSkillsMap.values()) {
            if (heldSkills.containsAll(reqSet)) {
                allMatch++;
            }
            if (!Collections.disjoint(heldSkills, reqSet)) {
                anyMatch++;
            }
        }

        return new SkillPreviewResponseDTO(allMatch, anyMatch);
    }

    public PostingResponseDTO createPosting(PostingCreateDTO req, HttpServletRequest request) {
        User user = userRepository.findById(req.getPostedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Posting posting = new Posting();
        posting.setPostedBy(user);
        posting.setTitle(req.getTitle());
        posting.setPostingType(req.getPostingType());
        posting.setDescription(req.getDescription());
        posting.setLocation(req.getLocation());
        posting.setStipend(req.getStipend());
        posting.setStipendAmount(req.getStipendAmount());
        posting.setDeadline(req.getDeadline());
        posting.setIsActive(true);

        Posting saved = postingRepository.save(posting);

        List<String> skillNames = new ArrayList<>();
        if (req.getSkillWeights() != null) {
            for (Map.Entry<Long, Integer> entry : req.getSkillWeights().entrySet()) {
                Skill skill = skillRepository.findById(entry.getKey()).orElse(null);
                if (skill != null) {
                    PostingSkill ps = new PostingSkill();
                    ps.setPosting(saved);
                    ps.setSkill(skill);
                    ps.setIsMandatory(true);
                    int weight = (entry.getValue() != null && entry.getValue() >= 1 && entry.getValue() <= 5) ? entry.getValue() : 3;
                    ps.setWeight(weight);
                    postingSkillRepository.save(ps);
                    skillNames.add(skill.getName());
                }
            }
        } else if (req.getRequiredSkills() != null) {
            for (PostingSkillDTO psDto : req.getRequiredSkills()) {
                Skill skill = (psDto.getSkillId() != null) 
                        ? skillRepository.findById(psDto.getSkillId()).orElse(null) 
                        : (psDto.getSkillName() != null ? skillRepository.findByName(psDto.getSkillName()).orElse(null) : null);
                if (skill != null) {
                    PostingSkill ps = new PostingSkill();
                    ps.setPosting(saved);
                    ps.setSkill(skill);
                    ps.setIsMandatory(psDto.getIsMandatory() != null ? psDto.getIsMandatory() : true);
                    int weight = (psDto.getWeight() != null && psDto.getWeight() >= 1 && psDto.getWeight() <= 5) ? psDto.getWeight() : 3;
                    ps.setWeight(weight);
                    postingSkillRepository.save(ps);
                    skillNames.add(skill.getName());
                }
            }
        }

        // Auto-flag below minimum stipend policy
        if (req.getStipendAmount() != null) {
            Optional<PlatformSetting> minStipendSetting = platformSettingRepository.findBySettingKey("min_stipend_amount");
            if (minStipendSetting.isPresent()) {
                try {
                    BigDecimal minStipend = new BigDecimal(minStipendSetting.get().getSettingValue().trim());
                    if (req.getStipendAmount().compareTo(minStipend) < 0) {
                        Flag autoFlag = new Flag();
                        autoFlag.setItemType(FlagItemType.POSTING);
                        autoFlag.setItemId(saved.getId());
                        autoFlag.setReportedBy(user);
                        autoFlag.setReason(String.format("Offered stipend (₹%s) is below the configured platform policy threshold (₹%s).", req.getStipendAmount(), minStipend));
                        autoFlag.setStatus(FlagStatus.PENDING);
                        flagRepository.save(autoFlag);
                    }
                } catch (Exception ignored) {}
            }
        }

        if (request != null) {
            auditLogService.log(user, "POSTING_CREATED", "POSTING", saved.getId(), request);
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

    public PostingResponseDTO createPosting(PostingCreateDTO req) {
        return createPosting(req, (HttpServletRequest) null);
    }

    public List<PostingResponseDTO> getActivePostings() {
        return getAllActivePostings();
    }

    public List<PostingResponseDTO> getAllActivePostings() {
        return postingRepository.findByIsActiveTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PostingResponseDTO> getPostingsByType(PostingType type) {
        return postingRepository.findByPostingTypeAndIsActiveTrue(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PostingResponseDTO toDTO(Posting p) {
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
        List<PostingSkill> skills = postingSkillRepository.findByPosting(p);
        dto.setRequiredSkills(skills.stream().map(ps -> ps.getSkill().getName()).collect(Collectors.toList()));
        return dto;
    }
}
