package com.example.TalentOrbit.dto.response;

import java.util.List;

public class AcademicianMatchedPostingResponseDTO {
    private Long postingId;
    private String title;
    private String organization;
    private String postingType;
    private Integer matchScore;
    private List<String> matchedTags;

    public AcademicianMatchedPostingResponseDTO() {}
    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getPostingType() { return postingType; }
    public void setPostingType(String postingType) { this.postingType = postingType; }
    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }
    public List<String> getMatchedTags() { return matchedTags; }
    public void setMatchedTags(List<String> matchedTags) { this.matchedTags = matchedTags; }
}
