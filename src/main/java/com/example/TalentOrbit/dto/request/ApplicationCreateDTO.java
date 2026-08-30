package com.example.TalentOrbit.dto.request;

import jakarta.validation.constraints.NotNull;

public class ApplicationCreateDTO {
    @NotNull(message = "Posting ID is required")
    private Long postingId;

    @NotNull(message = "User ID is required")
    private Long userId;

    public ApplicationCreateDTO() {}

    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
