package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.PostingType;
import java.time.LocalDate;
import java.util.List;

public class PostingCreateDTO {
    private Long postedByUserId;
    private String title;
    private PostingType postingType;
    private String description;
    private String location;
    private String stipend;
    private LocalDate deadline;
    private List<Long> skillIds;

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
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public List<Long> getSkillIds() { return skillIds; }
    public void setSkillIds(List<Long> skillIds) { this.skillIds = skillIds; }
}
