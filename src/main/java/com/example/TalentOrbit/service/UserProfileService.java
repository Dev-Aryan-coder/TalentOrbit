package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.ChangePasswordRequestDTO;
import com.example.TalentOrbit.dto.request.UserProfileUpdateDTO;
import com.example.TalentOrbit.dto.response.UserProfileResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import com.example.TalentOrbit.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private CompanyDetailsRepository companyDetailsRepository;
    @Autowired private AcademicianDetailsRepository academicianDetailsRepository;
    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private EmailService emailService;

    public UserProfileResponseDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getRole() == Role.STUDENT) {
            studentDetailsRepository.findById(userId).ifPresent(sd -> {
                dto.setInstitutionName(sd.getInstitutionName());
                dto.setAisheCode(sd.getAisheCode());
                dto.setBranch(sd.getBranch());
                dto.setGradYear(sd.getGradYear());
                dto.setCgpa(sd.getCgpa());
                dto.setTargetRole(sd.getTargetRole());
                dto.setEmployabilityScore(sd.getEmployabilityScore());
                dto.setSkills(studentSkillRepository.findByUserId(userId).stream()
                        .map(ss -> ss.getSkill().getName())
                        .collect(Collectors.toList()));
            });
        } else if (user.getRole() == Role.INDUSTRY) {
            companyDetailsRepository.findById(userId).ifPresent(cd -> {
                dto.setCompanyName(cd.getCompanyName());
                dto.setCinNumber(cd.getCinNumber());
                dto.setSector(cd.getSector());
                dto.setWebsiteUrl(cd.getWebsiteUrl());
                dto.setDescription(cd.getDescription());
            });
        } else if (user.getRole() == Role.ACADEMICIAN) {
            academicianDetailsRepository.findById(userId).ifPresent(ad -> {
                dto.setInstitutionName(ad.getInstitutionName());
                dto.setAisheCode(ad.getAisheCode());
                dto.setDepartment(ad.getDepartment());
            });
        } else if (user.getRole() == Role.INSTITUTION_ADMIN) {
            institutionDetailsRepository.findById(userId).ifPresent(id -> {
                dto.setInstitutionName(id.getInstitutionName());
                dto.setAisheCode(id.getAisheCode());
                dto.setState(id.getState());
                dto.setContactPerson(id.getContactPerson());
            });
        }

        return dto;
    }

    public UserProfileResponseDTO updateProfile(Long userId, UserProfileUpdateDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (req.getFullName() != null && !req.getFullName().trim().isEmpty()) {
            user.setFullName(req.getFullName().trim());
        }
        if (req.getAvatarUrl() != null) {
            user.setAvatarUrl(req.getAvatarUrl().trim());
        }
        userRepository.save(user);

        if (user.getRole() == Role.STUDENT) {
            StudentDetails sd = studentDetailsRepository.findById(userId).orElseGet(() -> {
                StudentDetails created = new StudentDetails();
                created.setUser(user);
                return created;
            });
            if (req.getFullName() != null) sd.setName(req.getFullName().trim());
            if (req.getInstitutionName() != null) sd.setInstitutionName(req.getInstitutionName());
            if (req.getBranch() != null) sd.setBranch(req.getBranch());
            if (req.getGradYear() != null) sd.setGradYear(req.getGradYear());
            if (req.getCgpa() != null) sd.setCgpa(req.getCgpa());
            if (req.getTargetRole() != null) sd.setTargetRole(req.getTargetRole());
            studentDetailsRepository.save(sd);
        } else if (user.getRole() == Role.INDUSTRY) {
            CompanyDetails cd = companyDetailsRepository.findById(userId).orElseGet(() -> {
                CompanyDetails created = new CompanyDetails();
                created.setUser(user);
                return created;
            });
            if (req.getCompanyName() != null) cd.setCompanyName(req.getCompanyName());
            if (req.getSector() != null) cd.setSector(req.getSector());
            if (req.getWebsiteUrl() != null) cd.setWebsiteUrl(req.getWebsiteUrl());
            if (req.getDescription() != null) cd.setDescription(req.getDescription());
            companyDetailsRepository.save(cd);
        } else if (user.getRole() == Role.ACADEMICIAN) {
            AcademicianDetails ad = academicianDetailsRepository.findById(userId).orElseGet(() -> {
                AcademicianDetails created = new AcademicianDetails();
                created.setUser(user);
                return created;
            });
            if (req.getFullName() != null) ad.setName(req.getFullName());
            if (req.getDepartment() != null) ad.setDepartment(req.getDepartment());
            if (req.getInstitutionName() != null) ad.setInstitutionName(req.getInstitutionName());
            academicianDetailsRepository.save(ad);
        } else if (user.getRole() == Role.INSTITUTION_ADMIN) {
            InstitutionDetails id = institutionDetailsRepository.findById(userId).orElseGet(() -> {
                InstitutionDetails created = new InstitutionDetails();
                created.setUser(user);
                return created;
            });
            if (req.getInstitutionName() != null) id.setInstitutionName(req.getInstitutionName());
            if (req.getState() != null) id.setState(req.getState());
            if (req.getContactPerson() != null) id.setContactPerson(req.getContactPerson());
            institutionDetailsRepository.save(id);
        }

        return getProfile(userId);
    }

    public void changePassword(Long userId, ChangePasswordRequestDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!user.getPasswordHash().equals(req.getCurrentPassword())) {
            throw new IllegalArgumentException("Current password does not match our records.");
        }

        if (req.getNewPassword() == null || req.getNewPassword().trim().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }

        user.setPasswordHash(req.getNewPassword().trim());
        userRepository.save(user);

        // Security notification email dispatch
        try {
            emailService.sendPasswordChangedEmail(user.getEmail(), user.getFullName());
        } catch (Exception ex) {
            System.err.println("Password change email dispatch notice: " + ex.getMessage());
        }
    }
}