package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.PostingType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class PostingCreateDTO {
    private Long postedByUserId;
    private PostingType postingType;
    private String title;
    private String description;
    private String location;
    private String stipend;
    private BigDecimal stipendAmount;
    private LocalDate deadline;
    private Map<Long, Integer> skillWeights; // skillId -> weight (1-5)

    public PostingCreateDTO() {}

    public Long getPostedByUserId() { return postedByUserId; }
    public void setPostedByUserId(Long postedByUserId) { this.postedByUserId = postedByUserId; }

    public PostingType getPostingType() { return postingType; }
    public void setPostingType(PostingType postingType) { this.postingType = postingType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

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
}
