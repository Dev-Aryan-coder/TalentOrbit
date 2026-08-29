package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.RoleDetailsRequestDTO;
import com.example.TalentOrbit.dto.response.RegistrationStatusResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentRepo;
    @Autowired private CompanyDetailsRepository companyRepo;
    @Autowired private AcademicianDetailsRepository academicianRepo;
    @Autowired private InstitutionDetailsRepository institutionRepo;

    public RegistrationStatusResponseDTO completeRegistration(RoleDetailsRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        switch (user.getRole()) {
            case STUDENT:
                StudentDetails sd = new StudentDetails();
                sd.setUser(user);
                sd.setName(req.getName());
                sd.setInstitutionName(req.getInstitutionName());
                sd.setAisheCode(req.getAisheCode());
                sd.setBranch(req.getBranch());
                sd.setGradYear(req.getGradYear());
                sd.setCgpa(req.getCgpa());
                sd.setTargetRole(req.getTargetRole());
                studentRepo.save(sd);
                break;
            case INDUSTRY:
                CompanyDetails cd = new CompanyDetails();
                cd.setUser(user);
                cd.setCompanyName(req.getCompanyName());
                cd.setCinNumber(req.getCinNumber());
                cd.setSector(req.getSector());
                cd.setDescription(req.getDescription());
                companyRepo.save(cd);
                break;
            case ACADEMICIAN:
                AcademicianDetails ad = new AcademicianDetails();
                ad.setUser(user);
                ad.setName(req.getName());
                ad.setInstitutionName(req.getInstitutionName());
                ad.setAisheCode(req.getAisheCode());
                ad.setDepartment(req.getDepartment());
                ad.setDesignation(req.getDesignation());
                academicianRepo.save(ad);
                break;
            case INSTITUTION_ADMIN:
                InstitutionDetails id = new InstitutionDetails();
                id.setUser(user);
                id.setInstitutionName(req.getInstitutionName());
                id.setAisheCode(req.getAisheCode());
                id.setContactPerson(req.getContactPerson());
                id.setContactEmail(req.getContactEmail());
                institutionRepo.save(id);
                break;
            default:
                break;
        }
        return new RegistrationStatusResponseDTO(user.getId(), user.getEmail(), user.getStatus(), "Role profile registered and submitted for verification");
    }
}
