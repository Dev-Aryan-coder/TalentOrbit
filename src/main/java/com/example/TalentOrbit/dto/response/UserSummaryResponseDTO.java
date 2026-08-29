package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;

public class UserSummaryResponseDTO {
    private Long id;
    private String email;
    private Role role;
    private UserStatus status;
    private String name;

    public UserSummaryResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
