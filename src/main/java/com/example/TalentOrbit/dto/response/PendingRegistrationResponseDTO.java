package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import java.time.LocalDateTime;

public class PendingRegistrationResponseDTO {
    private Long userId;
    private String email;
    private Role role;
    private UserStatus status;
    private String organizationOrName;
    private LocalDateTime createdAt;

    public PendingRegistrationResponseDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public String getOrganizationOrName() { return organizationOrName; }
    public void setOrganizationOrName(String organizationOrName) { this.organizationOrName = organizationOrName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
