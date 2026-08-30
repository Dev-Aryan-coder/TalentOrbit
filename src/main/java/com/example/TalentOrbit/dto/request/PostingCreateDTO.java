package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.PostingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PostingCreateDTO {
    @NotNull(message = "PostedBy User ID is required")
    private Long postedByUserId;

    @NotBlank(message = "Opportunity title is required")
    private String title;

    @NotNull(message = "Posting type is required")
    private PostingType postingType;

    @NotBlank(message = "Description is required")
    private String description;

    private String location;
    private String stipend;
    private BigDecimal stipendAmount;
    private LocalDate deadline;
    private Map<Long, Integer> skillWeights;
    private List<PostingSkillDTO> requiredSkills;

    public PostingCreateDTO() {}

    public Long getPostedByUserId() { return postedByUserId; }
    public void setPostedByUserId(Long postedByUserId) { this.postedByUserId = postedByUserId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public PostingType getPostingType() { return postingType; }
    public void setPostingType(PostingType postingType) { this.postingType = postingType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }

    public BigDecimal getStipendAmount() { return stipendAmount; }
    public void setStipendAmount(BigDecimal stipendAmount) { this.stipendAmount = stipendAmount; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public Map<Long, Integer> getSkillWeights() { return skillWeights; }
    public void setSkillWeights(Map<Long, Integer> skillWeights) { this.skillWeights = skillWeights; }

    public List<PostingSkillDTO> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<PostingSkillDTO> requiredSkills) { this.requiredSkills = requiredSkills; }
}
