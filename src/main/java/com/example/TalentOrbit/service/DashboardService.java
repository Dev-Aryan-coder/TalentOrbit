package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.DashboardStatsResponseDTO;
import com.example.TalentOrbit.dto.response.SkillGapRowDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ApplicationStatus;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;
    @Autowired private InterviewRepository interviewRepository;
    @Autowired private CompanyDetailsRepository companyDetailsRepository;
    @Autowired private AcademicianDetailsRepository academicianDetailsRepository;
    @Autowired private MatchingService matchingService;

    public DashboardStatsResponseDTO getStatsForRole(Role role, Long userId) {
        DashboardStatsResponseDTO dto = new DashboardStatsResponseDTO();
        dto.setRole(role);
        Map<String, Object> stats = new LinkedHashMap<>();

        if (role == null) role = Role.STUDENT;

        switch (role) {
            case STUDENT:
                if (userId != null) {
                    User user = userRepository.findById(userId).orElse(null);
                    List<StudentSkill> heldSkills = studentSkillRepository.findByUserId(userId);
                    long verifiedSkillsCount = heldSkills.stream().filter(StudentSkill::getIsVerified).count();
                    long applicationCount = user != null ? applicationRepository.findByUser(user).size() : 0;
                    long interviewCount = user != null ? interviewRepository.findAll().stream().filter(i -> i.getApplication().getUser().getId().equals(userId)).count() : 0;

                    List<Posting> activePostings = postingRepository.findByIsActiveTrue();
                    long recommendedMatches = 0;
                    for (Posting p : activePostings) {
                        if (matchingService.calculateMatch(userId, p).score >= 70) {
                            recommendedMatches++;
                        }
                    }

                    stats.put("skillsAcquiredCount", heldSkills.size());
                    stats.put("verifiedSkillsCount", verifiedSkillsCount);
                    stats.put("recommendedMatchCount", recommendedMatches);
                    stats.put("applicationCount", applicationCount);
                    stats.put("interviewCount", interviewCount);

                    dto.setTotalSkillsInMaster((long) heldSkills.size());
                    dto.setTotalApplications(applicationCount);
                    dto.setTotalActivePostings(recommendedMatches);
                } else {
                    stats.put("skillsAcquiredCount", 0);
                    stats.put("verifiedSkillsCount", 0);
                    stats.put("recommendedMatchCount", postingRepository.count());
                    stats.put("applicationCount", 0);
                    stats.put("interviewCount", 0);
                }
                break;

            case INDUSTRY:
                if (userId != null) {
                    User recruiter = userRepository.findById(userId).orElse(null);
                    List<Posting> myPostings = recruiter != null ? postingRepository.findAll().stream().filter(p -> p.getPostedBy().getId().equals(userId)).collect(Collectors.toList()) : Collections.emptyList();
                    long activePostings = myPostings.stream().filter(Posting::getIsActive).count();

                    List<Application> receivedApps = applicationRepository.findAll().stream()
                            .filter(a -> a.getPosting().getPostedBy().getId().equals(userId))
                            .collect(Collectors.toList());

                    long applied = receivedApps.stream().filter(a -> a.getStatus() == ApplicationStatus.APPLIED).count();
                    long shortlisted = receivedApps.stream().filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED).count();
                    long selected = receivedApps.stream().filter(a -> a.getStatus() == ApplicationStatus.SELECTED || a.getStatus() == ApplicationStatus.COMPLETED).count();
                    long scheduledInterviews = interviewRepository.findAll().stream().filter(i -> i.getApplication().getPosting().getPostedBy().getId().equals(userId)).count();

                    stats.put("activePostingCount", activePostings);
                    stats.put("totalApplicationsReceived", receivedApps.size());
                    stats.put("appliedCount", applied);
                    stats.put("shortlistedCount", shortlisted);
                    stats.put("selectedCount", selected);
                    stats.put("scheduledInterviews", scheduledInterviews);

                    dto.setTotalActivePostings(activePostings);
                    dto.setTotalApplications((long) receivedApps.size());
                }
                break;

            case INSTITUTION_ADMIN:
                String aisheCode = null;
                if (userId != null) {
                    InstitutionDetails inst = institutionDetailsRepository.findAll().stream().filter(i -> i.getUser().getId().equals(userId)).findFirst().orElse(null);
                    if (inst != null) aisheCode = inst.getAisheCode();
                }
                List<StudentDetails> cohort = (aisheCode != null) ? studentDetailsRepository.findByAisheCode(aisheCode) : studentDetailsRepository.findAll();
                Set<Long> cohortUserIds = cohort.stream().map(s -> s.getUser().getId()).collect(Collectors.toSet());

                double avgEmployability = cohort.stream()
                        .filter(s -> s.getEmployabilityScore() != null)
                        .mapToInt(StudentDetails::getEmployabilityScore)
                        .average().orElse(0.0);

                List<Application> cohortApps = applicationRepository.findAll().stream()
                        .filter(a -> cohortUserIds.contains(a.getUser().getId()))
                        .collect(Collectors.toList());

                long placedThisYear = cohortApps.stream()
                        .filter(a -> a.getStatus() == ApplicationStatus.SELECTED || a.getStatus() == ApplicationStatus.COMPLETED)
                        .count();

                stats.put("totalStudentsInCohort", cohort.size());
                stats.put("placementReadinessPercentage", Math.round(avgEmployability));
                stats.put("activeInternshipApplications", cohortApps.size());
                stats.put("placedStudentsCount", placedThisYear);

                dto.setTotalApplications((long) cohortApps.size());
                dto.setTotalSkillsInMaster((long) cohort.size());
                break;

            case SUPERADMIN:
                long totalStudents = userRepository.findByRole(Role.STUDENT).size();
                long totalIndustry = userRepository.findByRole(Role.INDUSTRY).size();
                long totalAcademia = userRepository.findByRole(Role.ACADEMICIAN).size();
                long totalInstitutions = userRepository.findByRole(Role.INSTITUTION_ADMIN).size();
                long pendingKyc = userRepository.findByStatus(UserStatus.PENDING_VERIFICATION).size();
                long activePostingsTotal = postingRepository.findByIsActiveTrue().size();

                stats.put("totalStudents", totalStudents);
                stats.put("totalIndustryEmployers", totalIndustry);
                stats.put("totalAcademicians", totalAcademia);
                stats.put("totalInstitutions", totalInstitutions);
                stats.put("pendingKycVerifications", pendingKyc);
                stats.put("activeOpportunitiesPlatformWide", activePostingsTotal);

                dto.setTotalActivePostings(activePostingsTotal);
                dto.setTotalApplications(applicationRepository.count());
                dto.setTotalSkillsInMaster(skillRepository.count());
                break;
        }

        dto.setStats(stats);
        return dto;
    }

    public List<SkillGapRowDTO> getSkillGapTable(Long institutionId) {
        List<Skill> allSkills = skillRepository.findAll();
        List<Posting> activePostings = postingRepository.findByIsActiveTrue();

        Map<Long, Integer> demandMap = new HashMap<>();
        for (Posting p : activePostings) {
            List<PostingSkill> pSkills = postingSkillRepository.findByPosting(p);
            for (PostingSkill ps : pSkills) {
                int weight = (ps.getWeight() != null && ps.getWeight() > 0) ? ps.getWeight() : 1;
                demandMap.merge(ps.getSkill().getId(), weight, Integer::sum);
            }
        }

        List<StudentDetails> cohort;
        if (institutionId != null) {
            InstitutionDetails inst = institutionDetailsRepository.findById(institutionId).orElse(null);
            if (inst != null) {
                cohort = studentDetailsRepository.findByAisheCode(inst.getAisheCode());
            } else {
                cohort = studentDetailsRepository.findAll();
            }
        } else {
            cohort = studentDetailsRepository.findAll();
        }

        Set<Long> cohortUserIds = cohort.stream().map(s -> s.getUser().getId()).collect(Collectors.toSet());
        List<StudentSkill> allStudentSkills = studentSkillRepository.findAll().stream()
                .filter(ss -> cohortUserIds.contains(ss.getUser().getId()))
                .collect(Collectors.toList());

        Map<Long, Set<Long>> studentHeldSkillsMap = new HashMap<>();
        Map<Long, Integer> supplyMap = new HashMap<>();

        for (StudentSkill ss : allStudentSkills) {
            supplyMap.merge(ss.getSkill().getId(), 1, Integer::sum);
            studentHeldSkillsMap.computeIfAbsent(ss.getUser().getId(), k -> new HashSet<>()).add(ss.getSkill().getId());
        }

        List<SkillGapRowDTO> result = new ArrayList<>();
        for (Skill sk : allSkills) {
            int demand = demandMap.getOrDefault(sk.getId(), 0);
            int supply = supplyMap.getOrDefault(sk.getId(), 0);

            int deficit = 0;
            if (demand > 0) {
                deficit = (int) Math.max(0, Math.round(((double) (demand - supply) / demand) * 100.0));
            }

            int studentsMissingSkill = 0;
            for (Long sId : cohortUserIds) {
                Set<Long> held = studentHeldSkillsMap.getOrDefault(sId, Collections.emptySet());
                if (!held.contains(sk.getId())) {
                    studentsMissingSkill++;
                }
            }

            result.add(new SkillGapRowDTO(sk.getName(), demand, supply, deficit, studentsMissingSkill));
        }

        result.sort((a, b) -> Integer.compare(b.getDeficitPercentage(), a.getDeficitPercentage()));
        return result;
    }
}
