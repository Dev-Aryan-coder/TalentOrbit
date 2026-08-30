package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.AiAssessmentRequestDTO;
import com.example.TalentOrbit.dto.request.AssessmentQuestionAnswerDTO;
import com.example.TalentOrbit.dto.response.AiAssessmentResponseDTO;
import com.example.TalentOrbit.dto.response.QuestionResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ProficiencyLevel;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiAssessmentService {

    @Autowired private QuestionRepository questionRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private AssessmentRepository assessmentRepository;
    @Autowired private BadgeService badgeService;
    @Autowired private RestTemplate restTemplate;

    @Value("${groq.api.key:gsk_default_demo_key}")
    private String groqApiKey;

    @Value("${groq.model:qwen/qwen3.8-27b}")
    private String groqModel;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    public List<QuestionResponseDTO> getQuestionsForSkill(Long skillId) {
        List<Question> questions = questionRepository.findBySkillId(skillId);
        return questions.stream().map(q -> new QuestionResponseDTO(
                q.getId(),
                q.getTopic(),
                q.getText(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
        )).collect(Collectors.toList());
    }

    public AiAssessmentResponseDTO evaluateAssessmentWithAi(AiAssessmentRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Skill skill = skillRepository.findById(req.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        StudentDetails studentDetails = studentDetailsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student details not found"));

        List<Question> questions = questionRepository.findBySkillId(skill.getId());
        Map<Long, Question> questionMap = questions.stream().collect(Collectors.toMap(Question::getId, q -> q));

        int totalQuestions = req.getAnswers().size();
        int totalCorrect = 0;
        Map<String, Integer> topicTotal = new HashMap<>();
        Map<String, Integer> topicCorrect = new HashMap<>();

        for (AssessmentQuestionAnswerDTO ans : req.getAnswers()) {
            Question q = questionMap.get(ans.getQuestionId());
            if (q != null) {
                String topic = q.getTopic() != null ? q.getTopic() : "General";
                topicTotal.put(topic, topicTotal.getOrDefault(topic, 0) + 1);

                if (q.getCorrectOption().trim().equalsIgnoreCase(ans.getSelectedOption().trim())) {
                    totalCorrect++;
                    topicCorrect.put(topic, topicCorrect.getOrDefault(topic, 0) + 1);
                }
            }
        }

        int actualPercentage = totalQuestions > 0 ? (int) Math.round(((double) totalCorrect / totalQuestions) * 100.0) : 0;
        int selfRatingPercentage = req.getSelfRating() != null ? req.getSelfRating() * 10 : 70;
        int confidenceGap = selfRatingPercentage - actualPercentage;

        Map<String, Integer> topicBreakdown = new HashMap<>();
        for (String topic : topicTotal.keySet()) {
            int tTot = topicTotal.get(topic);
            int tCor = topicCorrect.getOrDefault(topic, 0);
            topicBreakdown.put(topic, (int) Math.round(((double) tCor / tTot) * 100.0));
        }

        String gapCategory;
        if (confidenceGap > 20) {
            gapCategory = "SIGNIFICANT_GAP (Overconfident in " + skill.getName() + ")";
        } else if (confidenceGap > 5) {
            gapCategory = "MODERATE_GAP (Targeted revision needed in weak subtopics)";
        } else if (confidenceGap < -10) {
            gapCategory = "UNDERESTIMATED (Stronger practical competence than self-perception)";
        } else {
            gapCategory = "GOOD_ALIGNMENT (Realistic self-awareness of skill proficiency)";
        }

        Assessment assessment = new Assessment();
        assessment.setUser(user);
        assessment.setSkill(skill);
        assessment.setScore(actualPercentage);
        assessmentRepository.save(assessment);

        Optional<StudentSkill> optSkill = studentSkillRepository.findByUserAndSkill(user, skill);
        StudentSkill studentSkill = optSkill.orElse(new StudentSkill());
        studentSkill.setUser(user);
        studentSkill.setSkill(skill);
        studentSkill.setLastAssessed(LocalDate.now());
        if (actualPercentage >= 75) {
            studentSkill.setProficiency(ProficiencyLevel.ADVANCED);
            studentSkill.setIsVerified(true);
        } else if (actualPercentage >= 50) {
            studentSkill.setProficiency(ProficiencyLevel.INTERMEDIATE);
            studentSkill.setIsVerified(true);
        } else {
            studentSkill.setProficiency(ProficiencyLevel.BEGINNER);
            studentSkill.setIsVerified(false);
        }
        studentSkillRepository.save(studentSkill);

        studentDetails.setEmployabilityScore(Math.min(98, Math.max(50, (studentDetails.getEmployabilityScore() + actualPercentage) / 2)));
        studentDetailsRepository.save(studentDetails);

        badgeService.checkAndAwardBadges(user.getId(), "ASSESSMENT_COMPLETED");
        if (studentSkill.getIsVerified()) {
            badgeService.checkAndAwardBadges(user.getId(), "SKILL_VERIFIED");
        }

        String aiExplanation = generateAiGapAnalysis(
                skill.getName(),
                studentDetails.getTargetRole(),
                selfRatingPercentage,
                actualPercentage,
                confidenceGap,
                topicBreakdown
        );

        AiAssessmentResponseDTO dto = new AiAssessmentResponseDTO();
        dto.setSkillName(skill.getName());
        dto.setSelfRating(req.getSelfRating());
        dto.setSelfRatingPercentage(selfRatingPercentage);
        dto.setActualScorePercentage(actualPercentage);
        dto.setConfidenceGapPercentage(confidenceGap);
        dto.setGapCategory(gapCategory);
        dto.setTopicBreakdown(topicBreakdown);
        dto.setAiExplanationAndActionPlan(aiExplanation);
        dto.setUpdatedProficiency(studentSkill.getProficiency().name());
        dto.setIsVerified(studentSkill.getIsVerified());
        dto.setNewEmployabilityScore(studentDetails.getEmployabilityScore());
        return dto;
    }

    private String generateAiGapAnalysis(String skillName, String targetRole, int selfRating, int actualScore, int gap, Map<String, Integer> breakdown) {
        if (groqApiKey == null || groqApiKey.startsWith("gsk_default") || groqApiKey.trim().isEmpty()) {
            return generateDeterministicFallbackAnalysis(skillName, selfRating, actualScore, gap, breakdown);
        }

        try {
            String systemPrompt = "You are TalentOrbit's Lead Technical Evaluator. Analyze a student's diagnostic test results. Provide an objective gap diagnostic, explain why the gap exists based on topic breakdown, and give a 5-step concrete study roadmap.";
            String userPrompt = String.format(
                "Student Target Role: %s\nSkill Assessed: %s\nSelf-Rating: %d%%\nActual Test Score: %d%%\nConfidence Gap: %d%%\nPer-Topic Breakdown: %s\n" +
                "Generate a crisp, professional diagnostic with actionable learning milestones to reach 85%%+ proficiency.",
                targetRole != null ? targetRole : "Software Engineer",
                skillName, selfRating, actualScore, gap, breakdown.toString()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", groqModel);
            requestBody.put("temperature", 0.3);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", userPrompt));
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(groqApiUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            System.err.println("Groq AI assessment notice: " + e.getMessage() + " - using deterministic evaluation.");
        }

        return generateDeterministicFallbackAnalysis(skillName, selfRating, actualScore, gap, breakdown);
    }

    private String generateDeterministicFallbackAnalysis(String skillName, int selfRating, int actualScore, int gap, Map<String, Integer> breakdown) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("### Diagnostic Gap Analysis for %s\n\n", skillName));
        sb.append(String.format("- **Self Perception**: %d%% | **Verified Score**: %d%% | **Net Gap**: %+d%%\n\n", selfRating, actualScore, gap));
        sb.append("#### Topic Mastery Diagnostic:\n");
        for (Map.Entry<String, Integer> entry : breakdown.entrySet()) {
            String status = entry.getValue() >= 75 ? "🟢 Strong" : (entry.getValue() >= 50 ? "🟠 Needs Practice" : "🔴 Critical Deficit");
            sb.append(String.format("- **%s**: %d%% — %s\n", entry.getKey(), entry.getValue(), status));
        }
        sb.append("\n#### Actionable 5-Step Learning Roadmap:\n");
        sb.append(String.format("1. Revise fundamental syntax and optimization patterns for %s.\n", skillName));
        sb.append("2. Practice 15+ complex multi-condition queries and join scenarios.\n");
        sb.append("3. Review indexing, partition strategies, and query execution plans.\n");
        sb.append("4. Retake the TalentOrbit diagnostic assessment to verify 80%+ benchmark.\n");
        sb.append("5. Build a verified portfolio project demonstrating end-to-end database integration.\n");
        return sb.toString();
    }
}
