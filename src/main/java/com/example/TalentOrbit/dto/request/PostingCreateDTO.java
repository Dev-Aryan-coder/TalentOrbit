package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.PostingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private BigDecimal stipend;
    private LocalDate deadline;
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

    public BigDecimal getStipend() { return stipend; }
    public void setStipend(BigDecimal stipend) { this.stipend = stipend; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public List<PostingSkillDTO> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<PostingSkillDTO> requiredSkills) { this.requiredSkills = requiredSkills; }
}
