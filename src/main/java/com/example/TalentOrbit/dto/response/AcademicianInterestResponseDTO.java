package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.CollaborationStatus;
import java.time.LocalDateTime;

public class AcademicianInterestResponseDTO {
    private Long id;
    private Long postingId;
    private String postingTitle;
    private String postingType;
    private CollaborationStatus status;
    private LocalDateTime expressedAt;

    public AcademicianInterestResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }
    public String getPostingTitle() { return postingTitle; }
    public void setPostingTitle(String postingTitle) { this.postingTitle = postingTitle; }
    public String getPostingType() { return postingType; }
    public void setPostingType(String postingType) { this.postingType = postingType; }
    public CollaborationStatus getStatus() { return status; }
    public void setStatus(CollaborationStatus status) { this.status = status; }
    public LocalDateTime getExpressedAt() { return expressedAt; }
    public void setExpressedAt(LocalDateTime expressedAt) { this.expressedAt = expressedAt; }
}
