package com.example.TalentOrbit.dto.response;

import java.time.LocalDateTime;

public class BadgeResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private Boolean isEarned;
    private LocalDateTime earnedAt;
    private String verificationHash;
    private String sha256Digest;
    private Integer score;

    public BadgeResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Boolean getIsEarned() { return isEarned; }
    public void setIsEarned(Boolean isEarned) { this.isEarned = isEarned; }

    public LocalDateTime getEarnedAt() { return earnedAt; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }

    public String getVerificationHash() { return verificationHash; }
    public void setVerificationHash(String verificationHash) { this.verificationHash = verificationHash; }

    public String getSha256Digest() { return sha256Digest; }
    public void setSha256Digest(String sha256Digest) { this.sha256Digest = sha256Digest; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}