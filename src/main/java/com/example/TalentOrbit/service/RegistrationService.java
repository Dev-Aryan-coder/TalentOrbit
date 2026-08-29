package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.RoleDetailsRequestDTO;
import com.example.TalentOrbit.dto.response.RegistrationStatusResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import com.example.TalentOrbit.util.FormatValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private CompanyDetailsRepository companyDetailsRepository;
    @Autowired private AcademicianDetailsRepository academicianDetailsRepository;
    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;

    public RegistrationStatusResponseDTO completeRegistration(RoleDetailsRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = user.getRole();
        switch (role) {
            case STUDENT:
                StudentDetails sd = new StudentDetails();
                sd.setUser(user);
                sd.setName(req.getName());
                sd.setInstitutionName(req.getInstitutionName());
                sd.setAisheCode(req.getAisheCode());
                sd.setBranch(req.getBranch());
                sd.setGradYear(req.getGradYear());
                sd.setCgpa(req.getCgpa());
                sd.setTargetRole(req.getTargetRole() != null ? req.getTargetRole() : "Software Engineer");
                sd.setEmployabilityScore(75);
                studentDetailsRepository.save(sd);
                user.setStatus(UserStatus.VERIFIED);
                break;

            case INDUSTRY:
                if (req.getCinNumber() != null && !req.getCinNumber().trim().isEmpty()) {
                    if (!FormatValidators.isValidCin(req.getCinNumber())) {
                        throw new IllegalArgumentException("Invalid MCA CIN format. Must be a 21-character corporate identification number (e.g., U72900MH2018PTC312450).");
                    }
                }
                CompanyDetails cd = new CompanyDetails();
                cd.setUser(user);
                cd.setCompanyName(req.getCompanyName());
                cd.setCinNumber(req.getCinNumber());
                cd.setSector(req.getSector());
                cd.setDescription(req.getDescription());
                cd.setWebsiteUrl(req.getWebsiteUrl());
                cd.setVerificationDocPath(req.getVerificationDocPath());
                companyDetailsRepository.save(cd);
                user.setStatus(UserStatus.PENDING_VERIFICATION);
                break;

            case ACADEMICIAN:
                AcademicianDetails ad = new AcademicianDetails();
                ad.setUser(user);
                ad.setName(req.getName());
                ad.setInstitutionName(req.getInstitutionName());
                ad.setAisheCode(req.getAisheCode());
                ad.setDepartment(req.getDepartment());
                ad.setDesignation(req.getDesignation());
                ad.setEmployeeId(req.getEmployeeId());
                ad.setBio(req.getBio());
                academicianDetailsRepository.save(ad);
                user.setStatus(UserStatus.PENDING_VERIFICATION);
                break;

            case INSTITUTION_ADMIN:
                if (req.getAisheCode() != null && !req.getAisheCode().trim().isEmpty()) {
                    if (!FormatValidators.isValidAisheCode(req.getAisheCode())) {
                        throw new IllegalArgumentException("Invalid AISHE Code format. Must be formatted as State/Type prefix followed by numbers (e.g., C-33772).");
                    }
                }
                InstitutionDetails id = new InstitutionDetails();
                id.setUser(user);
                id.setInstitutionName(req.getInstitutionName());
                id.setAisheCode(req.getAisheCode());
                id.setState(req.getState());
                id.setCity(req.getCity());
                id.setContactPerson(req.getContactPerson());
                id.setContactEmail(req.getContactEmail());
                id.setNaacGrade(req.getNaacGrade());
                institutionDetailsRepository.save(id);
                user.setStatus(UserStatus.PENDING_VERIFICATION);
                break;

            default:
                break;
        }

        userRepository.save(user);
        return new RegistrationStatusResponseDTO(user.getId(), user.getEmail(), user.getRole(), user.getStatus(), "Details saved successfully");
    }
}
