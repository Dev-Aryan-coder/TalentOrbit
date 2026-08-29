package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.PostingType;
import java.time.LocalDate;
import java.util.List;

public class PostingResponseDTO {
    private Long id;
    private Long postedByUserId;
    private String postedByName;
    private String title;
    private PostingType postingType;
    private String description;
    private String location;
    private String stipend;
    private LocalDate deadline;
    private Boolean isActive;
    private List<String> requiredSkills;
    private Integer matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public PostingResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostedByUserId() { return postedByUserId; }
    public void setPostedByUserId(Long postedByUserId) { this.postedByUserId = postedByUserId; }
    public String getPostedByName() { return postedByName; }
    public void setPostedByName(String postedByName) { this.postedByName = postedByName; }
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
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }
    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }
    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
}
