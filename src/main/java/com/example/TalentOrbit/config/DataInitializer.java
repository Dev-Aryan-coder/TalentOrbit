package com.example.TalentOrbit.config;

import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.*;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private CompanyDetailsRepository companyDetailsRepository;
    @Autowired private AcademicianDetailsRepository academicianDetailsRepository;
    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private PortfolioRepository portfolioRepository;
    @Autowired private RoadmapStepRepository roadmapStepRepository;
    @Autowired private BadgeRepository badgeRepository;
    @Autowired private StudentBadgeRepository studentBadgeRepository;
    @Autowired private InterviewRepository interviewRepository;
    @Autowired private AcademicianInterestRepository academicianInterestRepository;
    @Autowired private AcademicianInterestTagRepository academicianInterestTagRepository;
    @Autowired private TrainingProgramRepository trainingProgramRepository;
    @Autowired private PlatformSettingRepository platformSettingRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private ChatSessionRepository chatSessionRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            // If users exist but questions haven't been seeded yet, seed questions
            if (questionRepository.count() == 0) {
                seedQuestions();
            }
            System.out.println("TalentOrbit Data already initialized. Skipping seed.");
            return;
        }

        System.out.println("Seeding TalentOrbit demo data for all 5 roles...");

        // 1. Seed Skills Master
        Skill java = skillRepository.save(new Skill("Java 21", "Programming Language"));
        Skill spring = skillRepository.save(new Skill("Spring Boot 3.3", "Backend Framework"));
        Skill mysql = skillRepository.save(new Skill("MySQL", "Database"));
        Skill docker = skillRepository.save(new Skill("Docker", "DevOps & Cloud"));
        Skill k8s = skillRepository.save(new Skill("Kubernetes", "DevOps & Cloud"));
        Skill react = skillRepository.save(new Skill("React 19", "Frontend Framework"));
        Skill python = skillRepository.save(new Skill("Python", "Programming Language"));
        Skill ayush = skillRepository.save(new Skill("Ayush ABDM Standards", "Ayush Health Informatics"));
        Skill kafka = skillRepository.save(new Skill("Apache Kafka", "Distributed Systems"));

        // 2. Seed Users across all 5 Roles
        // Student
        User student = userRepository.save(new User("aryan.sharma@vsit.edu.in", "password123", Role.STUDENT, UserStatus.VERIFIED));
        StudentDetails sd = new StudentDetails();
        sd.setUser(student);
        sd.setName("Aryan Sharma");
        sd.setInstitutionName("Vidyalankar School of Information Technology (VSIT)");
        sd.setAisheCode("C-33772");
        sd.setBranch("B.Sc. Information Technology");
        sd.setGradYear(2026);
        sd.setCgpa(9.4);
        sd.setTargetRole("Backend Microservices Engineer");
        sd.setEmployabilityScore(92);
        studentDetailsRepository.save(sd);

        // Student Skills
        StudentSkill ss1 = new StudentSkill();
        ss1.setUser(student);
        ss1.setSkill(java);
        ss1.setProficiency(ProficiencyLevel.ADVANCED);
        ss1.setIsVerified(true);
        ss1.setLastAssessed(LocalDate.now().minusDays(10));
        studentSkillRepository.save(ss1);

        StudentSkill ss2 = new StudentSkill();
        ss2.setUser(student);
        ss2.setSkill(spring);
        ss2.setProficiency(ProficiencyLevel.ADVANCED);
        ss2.setIsVerified(true);
        ss2.setLastAssessed(LocalDate.now().minusDays(12));
        studentSkillRepository.save(ss2);

        StudentSkill ss3 = new StudentSkill();
        ss3.setUser(student);
        ss3.setSkill(mysql);
        ss3.setProficiency(ProficiencyLevel.INTERMEDIATE);
        ss3.setIsVerified(true);
        ss3.setLastAssessed(LocalDate.now().minusDays(15));
        studentSkillRepository.save(ss3);

        StudentSkill ss4 = new StudentSkill();
        ss4.setUser(student);
        ss4.setSkill(docker);
        ss4.setProficiency(ProficiencyLevel.BEGINNER);
        ss4.setIsVerified(false);
        studentSkillRepository.save(ss4);

        // Industry / Recruiter
        User recruiter = userRepository.save(new User("hr@techcorp.com", "password123", Role.INDUSTRY, UserStatus.VERIFIED));
        CompanyDetails cd = new CompanyDetails();
        cd.setUser(recruiter);
        cd.setCompanyName("TechCorp Solutions Pvt. Ltd.");
        cd.setCinNumber("U72900MH2018PTC312450");
        cd.setSector("Enterprise IT & Cloud Software");
        cd.setDescription("Leading software development firm specializing in scalable microservices architectures and health-tech integrations.");
        cd.setWebsiteUrl("https://techcorp.example.com");
        companyDetailsRepository.save(cd);

        // Academician / Faculty
        User academician = userRepository.save(new User("rajesh.sharma@vsit.edu.in", "password123", Role.ACADEMICIAN, UserStatus.VERIFIED));
        AcademicianDetails ad = new AcademicianDetails();
        ad.setUser(academician);
        ad.setName("Dr. Rajesh Sharma, Ph.D.");
        ad.setInstitutionName("Vidyalankar School of Information Technology (VSIT)");
        ad.setAisheCode("C-33772");
        ad.setDepartment("Computer Engineering");
        ad.setDesignation("Associate Professor & Research Lead");
        ad.setEmployeeId("VSIT-FAC-4012");
        ad.setBio("14+ years experience in distributed architectures, cloud computing, and healthcare informatics.");
        academicianDetailsRepository.save(ad);

        // Academician Interest Tags
        AcademicianInterestTag ait1 = new AcademicianInterestTag();
        ait1.setUser(academician);
        ait1.setSkill(java);
        academicianInterestTagRepository.save(ait1);

        AcademicianInterestTag ait2 = new AcademicianInterestTag();
        ait2.setUser(academician);
        ait2.setSkill(spring);
        academicianInterestTagRepository.save(ait2);

        // Institution / TPO
        User tpo = userRepository.save(new User("tpo@vsit.edu.in", "password123", Role.INSTITUTION_ADMIN, UserStatus.VERIFIED));
        InstitutionDetails id = new InstitutionDetails();
        id.setUser(tpo);
        id.setInstitutionName("Vidyalankar School of Information Technology (VSIT)");
        id.setAisheCode("C-33772");
        id.setState("Maharashtra");
        id.setCity("Mumbai");
        id.setContactPerson("Prof. Amit Kulkarni");
        id.setContactEmail("tpo@vsit.edu.in");
        id.setNaacGrade("A+");
        institutionDetailsRepository.save(id);

        // SuperAdmin
        User admin = userRepository.save(new User("superadmin@talentorbit.gov.in", "admin123", Role.SUPERADMIN, UserStatus.VERIFIED));

        // 3. Seed Postings (Industry & Academia)
        Posting p1 = new Posting();
        p1.setPostedBy(recruiter);
        p1.setTitle("Senior Java Microservices & Cloud Intern");
        p1.setPostingType(PostingType.INTERNSHIP);
        p1.setDescription("Build high-performance REST APIs and containerized microservices using Java 21, Spring Boot 3, and MySQL.");
        p1.setLocation("Mumbai (Hybrid)");
        p1.setStipend("₹35,000 / month");
        p1.setDeadline(LocalDate.now().plusMonths(2));
        p1.setIsActive(true);
        Posting savedP1 = postingRepository.save(p1);

        PostingSkill ps1 = new PostingSkill();
        ps1.setPosting(savedP1);
        ps1.setSkill(java);
        ps1.setIsMandatory(true);
        ps1.setWeight(3);
        postingSkillRepository.save(ps1);

        PostingSkill ps2 = new PostingSkill();
        ps2.setPosting(savedP1);
        ps2.setSkill(spring);
        ps2.setIsMandatory(true);
        ps2.setWeight(3);
        postingSkillRepository.save(ps2);

        PostingSkill ps3 = new PostingSkill();
        ps3.setPosting(savedP1);
        ps3.setSkill(mysql);
        ps3.setIsMandatory(false);
        ps3.setWeight(2);
        postingSkillRepository.save(ps3);

        PostingSkill ps4 = new PostingSkill();
        ps4.setPosting(savedP1);
        ps4.setSkill(docker);
        ps4.setIsMandatory(false);
        ps4.setWeight(2);
        postingSkillRepository.save(ps4);

        // FDP Posting
        Posting p2 = new Posting();
        p2.setPostedBy(recruiter);
        p2.setTitle("National Faculty Development Program on Generative AI & Cloud Architecture");
        p2.setPostingType(PostingType.FDP);
        p2.setDescription("Hands-on industry sabbatical and curriculum alignment bootcamp for engineering faculty.");
        p2.setLocation("Online / Hybrid");
        p2.setStipend("AICTE Certified");
        p2.setDeadline(LocalDate.now().plusMonths(1));
        p2.setIsActive(true);
        Posting savedP2 = postingRepository.save(p2);

        PostingSkill psFdp = new PostingSkill();
        psFdp.setPosting(savedP2);
        psFdp.setSkill(java);
        postingSkillRepository.save(psFdp);

        // 4. Seed Applications & Interviews
        Application app = new Application();
        app.setPosting(savedP1);
        app.setUser(student);
        app.setStatus(ApplicationStatus.SHORTLISTED);
        app.setMatchScore(92);
        Application savedApp = applicationRepository.save(app);

        Interview interview = new Interview();
        interview.setApplication(savedApp);
        interview.setScheduledAt(LocalDateTime.now().plusDays(2).withHour(10).withMinute(30));
        interview.setInterviewerName("Vikram Joshi (Engineering Lead)");
        interview.setMeetingLink("https://meet.google.com/abc-defg-hij");
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setNotes("Focus on Spring Boot architecture, MySQL query optimization, and REST API design.");
        interviewRepository.save(interview);

        // 5. Seed Portfolio & Roadmap
        Portfolio port1 = new Portfolio();
        port1.setUser(student);
        port1.setItemType(PortfolioItemType.PROJECT);
        port1.setTitle("TalentOrbit SIH Distributed Gateway");
        port1.setDescription("Production-grade microservices gateway with JWT authentication and Spring Cloud load balancing.");
        port1.setFileOrLink("https://github.com/aryan/talentorbit-gateway");
        port1.setIsVerified(true);
        portfolioRepository.save(port1);

        Portfolio port2 = new Portfolio();
        port2.setUser(student);
        port2.setItemType(PortfolioItemType.CERTIFICATE);
        port2.setTitle("Oracle Certified Professional: Java SE 21 Developer");
        port2.setDescription("Issued by Oracle University. Verification Hash: 0x9f8a...4b12");
        port2.setFileOrLink("https://catalog-education.oracle.com/ords/certview/cert?id=12345");
        port2.setIsVerified(true);
        portfolioRepository.save(port2);

        // Roadmap Steps
        RoadmapStep r1 = new RoadmapStep();
        r1.setUser(student);
        r1.setStepOrder(1);
        r1.setTitle("Master Java 21 Virtual Threads & Concurrency");
        r1.setDescription("Completed core modern Java paradigms.");
        r1.setStatus(RoadmapStepStatus.DONE);
        r1.setLinkedSkill(java);
        roadmapStepRepository.save(r1);

        RoadmapStep r2 = new RoadmapStep();
        r2.setUser(student);
        r2.setStepOrder(2);
        r2.setTitle("Spring Boot 3.3 Microservices Architecture");
        r2.setDescription("Build modular microservices with Hibernate ORM.");
        r2.setStatus(RoadmapStepStatus.DONE);
        r2.setLinkedSkill(spring);
        roadmapStepRepository.save(r2);

        RoadmapStep r3 = new RoadmapStep();
        r3.setUser(student);
        r3.setStepOrder(3);
        r3.setTitle("Docker Containerization & Multi-stage Builds");
        r3.setDescription("Learn Dockerfile creation, image optimization, and container deployment.");
        r3.setStatus(RoadmapStepStatus.IN_PROGRESS);
        r3.setLinkedSkill(docker);
        roadmapStepRepository.save(r3);

        RoadmapStep r4 = new RoadmapStep();
        r4.setUser(student);
        r4.setStepOrder(4);
        r4.setTitle("Kubernetes Cluster Orchestration & Helm");
        r4.setDescription("Deploy scalable workloads in production cluster.");
        r4.setStatus(RoadmapStepStatus.LOCKED);
        r4.setLinkedSkill(k8s);
        roadmapStepRepository.save(r4);

        // 6. Seed Badges
        Badge b1 = new Badge();
        b1.setName("Skill Profile Pioneer");
        b1.setDescription("Verified 3 or more technical skills through adaptive MCQ assessments.");
        b1.setIconUrl("verified_shield.svg");
        b1.setCriteriaType(BadgeCriteriaType.SKILLS_VERIFIED_COUNT);
        b1.setCriteriaValue(3);
        badgeRepository.save(b1);

        Badge b2 = new Badge();
        b2.setName("First Opportunity Applied");
        b2.setDescription("Submitted your first 1-Click matched internship application.");
        b2.setIconUrl("rocket_launch.svg");
        b2.setCriteriaType(BadgeCriteriaType.FIRST_APPLICATION);
        b2.setCriteriaValue(1);
        badgeRepository.save(b2);

        StudentBadge sb1 = new StudentBadge();
        sb1.setUser(student);
        sb1.setBadge(b1);
        sb1.setEarnedAt(LocalDateTime.now().minusDays(5));
        studentBadgeRepository.save(sb1);

        StudentBadge sb2 = new StudentBadge();
        sb2.setUser(student);
        sb2.setBadge(b2);
        sb2.setEarnedAt(LocalDateTime.now().minusDays(2));
        studentBadgeRepository.save(sb2);

        // 7. Seed Academician Collaborations
        AcademicianInterest ai = new AcademicianInterest();
        ai.setUser(academician);
        ai.setPosting(savedP2);
        ai.setStatus(CollaborationStatus.CONFIRMED);
        academicianInterestRepository.save(ai);

        // 8. Seed TPO Training Programs
        TrainingProgram tp = new TrainingProgram();
        tp.setInstitutionUser(tpo);
        tp.setTitle("3-Day Intensive Docker & Containerization Bootcamp");
        tp.setTargetSkill(docker);
        tp.setProgramDate(LocalDate.now().plusDays(15));
        tp.setStudentsRegistered(420);
        tp.setStatus(TrainingProgramStatus.PLANNED);
        trainingProgramRepository.save(tp);

        // 9. Seed Platform Settings & Audit Logs
        platformSettingRepository.save(new PlatformSetting("PLATFORM_NAME", "TalentOrbit"));
        platformSettingRepository.save(new PlatformSetting("SIH_PROBLEM_STATEMENT", "SIH-26044"));
        platformSettingRepository.save(new PlatformSetting("SMTP_HOST", "smtp.gmail.com:587"));

        auditLogRepository.save(new AuditLog(admin, "SUPERADMIN_LOGIN", "SYSTEM", 1L, "127.0.0.1"));
        auditLogRepository.save(new AuditLog(admin, "KYC_APPROVE_COMPANY", "COMPANY", cd.getId(), "127.0.0.1"));
        auditLogRepository.save(new AuditLog(admin, "KYC_APPROVE_INSTITUTION", "INSTITUTION", id.getId(), "127.0.0.1"));

        // 10. Seed Topic-Tagged Question Bank & Chatbot Demo
        seedQuestions();

        ChatSession chatSession = chatSessionRepository.save(new ChatSession(student, "Getting Started with Microservices"));
        chatMessageRepository.save(new ChatMessage(chatSession, Sender.USER, "How can I prepare for the Senior Java Intern role at TechCorp?", null));
        chatMessageRepository.save(new ChatMessage(chatSession, Sender.ASSISTANT, "Based on your 92% match score, you have strong Java 21 and Spring Boot foundations! To stand out, focus on bridging your Docker gap by practicing multi-stage Dockerfile builds and container networking.", null));

        System.out.println("TalentOrbit demo seed data with AI Question Bank & Chatbot successfully populated!");
    }

    private void seedQuestions() {
        Skill mysql = skillRepository.findByName("MySQL").orElse(null);
        if (mysql == null) return;

        // Topic-Tagged SQL Questions
        questionRepository.save(new Question(mysql, "JOIN", "Which SQL JOIN returns all rows from the left table, and matching rows from the right table?", "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN", "B", "LEFT JOIN preserves all records from the left table even when there are no matches on the right."));
        questionRepository.save(new Question(mysql, "JOIN", "What is the result of performing a CROSS JOIN between Table A (5 rows) and Table B (4 rows)?", "9 rows", "20 rows", "1 row", "Error", "B", "CROSS JOIN produces a Cartesian product multiplying 5 * 4 = 20 rows."));
        questionRepository.save(new Question(mysql, "JOIN", "When joining tables on non-primary key columns, which indexing strategy optimizes query latency?", "Composite B-Tree index on foreign key columns", "Full-text index", "Hash index on SELECT columns", "No index needed", "A", "B-Tree indexes on join condition columns prevent full table scans."));
        questionRepository.save(new Question(mysql, "JOIN", "In MySQL 8.0+, which join algorithm is automatically used for equi-joins without indexes?", "Nested Loop Join", "Hash Join", "Merge Join", "Bitmap Join", "B", "MySQL 8.0.18+ introduces Hash Joins to replace Block Nested Loop for non-indexed equi-joins."));

        questionRepository.save(new Question(mysql, "GROUP BY", "What is the key difference between WHERE and HAVING clauses in SQL?", "WHERE filters rows before aggregation; HAVING filters aggregated groups", "HAVING is faster than WHERE", "WHERE works only on numbers", "There is no difference", "A", "WHERE filters individual rows before GROUP BY; HAVING filters post-aggregated grouped data."));
        questionRepository.save(new Question(mysql, "GROUP BY", "Which clause is mandatory when using aggregate functions alongside non-aggregated columns in SELECT?", "ORDER BY", "GROUP BY", "DISTINCT", "LIMIT", "B", "All non-aggregated SELECT columns must appear in the GROUP BY clause under standard SQL mode."));

        questionRepository.save(new Question(mysql, "Subqueries", "What is a Correlated Subquery?", "A subquery that runs independently once", "A subquery that references columns from the outer query and executes per row", "A subquery in the FROM clause", "A materialized view", "B", "Correlated subqueries depend on the outer query row-by-row."));
        questionRepository.save(new Question(mysql, "Subqueries", "Which operator tests for the existence of rows returned by a subquery and short-circuits on first match?", "IN", "EXISTS", "ANY", "ALL", "B", "EXISTS stops scanning as soon as a single matching row is encountered."));

        questionRepository.save(new Question(mysql, "Window Functions", "Which window function assigns a unique sequential integer to rows within a partition?", "RANK()", "DENSE_RANK()", "ROW_NUMBER()", "NTILE()", "C", "ROW_NUMBER() guarantees unique sequential numbers without ties."));
        questionRepository.save(new Question(mysql, "Window Functions", "How does DENSE_RANK() differ from RANK() when duplicate values occur?", "DENSE_RANK skips rank numbers", "DENSE_RANK does not skip numbers after duplicate values", "DENSE_RANK only works on dates", "They are identical", "B", "DENSE_RANK maintains consecutive numbering (1, 2, 2, 3) whereas RANK skips (1, 2, 2, 4)."));
    }
}
