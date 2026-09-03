package com.example.TalentOrbit.dto.response;

import java.time.LocalDateTime;

public class VerifyBadgeResponseDTO {
    private String candidateName;
    private String candidateEmail;
    private String badgeName;
    private String description;
    private String category;
    private Integer score;
    private LocalDateTime earnedAt;
    private String verificationHash;
    private String sha256Digest;
    private String status;

    public VerifyBadgeResponseDTO() {}

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }

    public String getBadgeName() { return badgeName; }
    public void setBadgeName(String badgeName) { this.badgeName = badgeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public LocalDateTime getEarnedAt() { return earnedAt; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }

    public String getVerificationHash() { return verificationHash; }
    public void setVerificationHash(String verificationHash) { this.verificationHash = verificationHash; }

    public String getSha256Digest() { return sha256Digest; }
    public void setSha256Digest(String sha256Digest) { this.sha256Digest = sha256Digest; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}