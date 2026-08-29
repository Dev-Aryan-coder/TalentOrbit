package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.UserStatus;

public class RegistrationStatusResponseDTO {
    private Long userId;
    private String email;
    private UserStatus status;
    private String message;

    public RegistrationStatusResponseDTO() {}
    public RegistrationStatusResponseDTO(Long userId, String email, UserStatus status, String message) {
        this.userId = userId;
        this.email = email;
        this.status = status;
        this.message = message;
    }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
