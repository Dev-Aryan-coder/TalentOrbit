package com.example.TalentOrbit.dto.response;

import java.util.List;

public class ProfileExtractionResponseDTO {
    private Long userId;
    private List<DetectedSkillDTO> detectedSkills;
    private String careerInterest;
    private List<String> projectsDetected;
    private String statusMessage;

    public ProfileExtractionResponseDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<DetectedSkillDTO> getDetectedSkills() { return detectedSkills; }
    public void setDetectedSkills(List<DetectedSkillDTO> detectedSkills) { this.detectedSkills = detectedSkills; }

    public String getCareerInterest() { return careerInterest; }
    public void setCareerInterest(String careerInterest) { this.careerInterest = careerInterest; }

    public List<String> getProjectsDetected() { return projectsDetected; }
    public void setProjectsDetected(List<String> projectsDetected) { this.projectsDetected = projectsDetected; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
