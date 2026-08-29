package com.example.TalentOrbit.dto.request;

public class ApplicationCreateDTO {
    private Long postingId;
    private Long userId;

    public ApplicationCreateDTO() {}
    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
