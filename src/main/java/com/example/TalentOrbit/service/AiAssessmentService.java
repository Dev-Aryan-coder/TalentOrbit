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
import org.springframework.http.*;
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
    @Autowired private RestTemplate restTemplate;

    @Value("${groq.api.key:}")
    private String groqApiKey;

    @Value("${groq.model:qwen/qwen3.8-27b}")
    private String groqModel;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    public List<QuestionResponseDTO> getQuestionsForSkill(Long skillId) {
        List<Question> questions = questionRepository.findBySkillId(skillId);
        return questions.stream().map(q -> new QuestionResponseDTO(
                q.getId(), q.getTopic(), q.getText(), q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()
        )).collect(Collectors.toList());
    }

    public AiAssessmentResponseDTO evaluateAssessmentWithAi(AiAssessmentRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Skill skill = skillRepository.findById(req.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        StudentDetails studentDetails = studentDetailsRepository.findByUser(user).orElse(null);

        // 1. Fetch questions and grade deterministically
        List<Question> skillQuestions = questionRepository.findBySkillId(skill.getId());
        Map<Long, Question> questionMap = skillQuestions.stream().collect(Collectors.toMap(Question::getId, q -> q));

        Map<String, Integer> topicTotal = new LinkedHashMap<>();
        Map<String, Integer> topicCorrect = new LinkedHashMap<>();

        int totalScore = 0;
        int maxScore = req.getAnswers().size();

        for (AssessmentQuestionAnswerDTO ans : req.getAnswers()) {
            Question q = questionMap.get(ans.getQuestionId());
            if (q == null) continue;

            String topic = q.getTopic();
            topicTotal.put(topic, topicTotal.getOrDefault(topic, 0) + 1);

            if (q.getCorrectOption().trim().equalsIgnoreCase(ans.getSelectedOption().trim())) {
                totalScore++;
                topicCorrect.put(topic, topicCorrect.getOrDefault(topic, 0) + 1);
            }
        }

        int assessmentPercentage = maxScore > 0 ? (int) Math.round(((double) totalScore / maxScore) * 100.0) : 0;
        int selfRatingPercentage = req.getSelfRatingOutOf10() != null ? req.getSelfRatingOutOf10() * 10 : 70;
        int confidenceGap = selfRatingPercentage - assessmentPercentage;

        String gapCategory;
        if (Math.abs(confidenceGap) <= 10) {
            gapCategory = "GOOD_ALIGNMENT";
        } else if (confidenceGap > 10 && confidenceGap <= 20) {
            gapCategory = "MODERATE_GAP";
        } else if (confidenceGap > 20) {
            gapCategory = "SIGNIFICANT_GAP";
        } else {
            gapCategory = "UNDERESTIMATED_SELF";
        }

        // Calculate per-topic breakdown
        Map<String, Integer> topicBreakdown = new LinkedHashMap<>();
        for (String topic : topicTotal.keySet()) {
            int totalInTopic = topicTotal.get(topic);
            int correctInTopic = topicCorrect.getOrDefault(topic, 0);
            int topicPct = (int) Math.round(((double) correctInTopic / totalInTopic) * 100.0);
            topicBreakdown.put(topic, topicPct);
        }

        // 2. Determine Proficiency Level
        ProficiencyLevel proficiency;
        if (assessmentPercentage >= 80) {
            proficiency = ProficiencyLevel.ADVANCED;
        } else if (assessmentPercentage >= 50) {
            proficiency = ProficiencyLevel.INTERMEDIATE;
        } else {
            proficiency = ProficiencyLevel.BEGINNER;
        }

        // 3. Persist Assessment & Update StudentSkill
        Assessment assessment = new Assessment();
        assessment.setUser(user);
        assessment.setSkill(skill);
        assessment.setScore(assessmentPercentage);
        assessment.setMaxScore(100);
        assessmentRepository.save(assessment);

        Optional<StudentSkill> existingSkill = studentSkillRepository.findByUserAndSkill(user, skill);
        StudentSkill studentSkill = existingSkill.orElse(new StudentSkill());
        studentSkill.setUser(user);
        studentSkill.setSkill(skill);
        studentSkill.setProficiency(proficiency);
        studentSkill.setIsVerified(assessmentPercentage >= 50);
        studentSkill.setLastAssessed(LocalDate.now());
        studentSkillRepository.save(studentSkill);

        // Update overall employability score
        int updatedEmployabilityScore = 85;
        if (studentDetails != null) {
            List<StudentSkill> allSkills = studentSkillRepository.findByUserId(user.getId());
            long verifiedCount = allSkills.stream().filter(StudentSkill::getIsVerified).count();
            updatedEmployabilityScore = (int) Math.min(100, 70 + (verifiedCount * 6));
            studentDetails.setEmployabilityScore(updatedEmployabilityScore);
            studentDetailsRepository.save(studentDetails);
        }

        // 4. Generate AI Explanation & Action Plan via Groq Qwen
        String targetRole = (studentDetails != null && studentDetails.getTargetRole() != null)
                ? studentDetails.getTargetRole() : "Software Engineer";

        String[] aiResults = callGroqForAssessmentFeedback(
                skill.getName(), req.getSelfRatingOutOf10(), assessmentPercentage, confidenceGap,
                gapCategory, topicBreakdown, targetRole
        );

        AiAssessmentResponseDTO response = new AiAssessmentResponseDTO();
        response.setSkillName(skill.getName());
        response.setSelfRatingPercentage(selfRatingPercentage);
        response.setAssessmentScorePercentage(assessmentPercentage);
        response.setConfidenceGap(confidenceGap);
        response.setGapCategory(gapCategory);
        response.setTopicBreakdown(topicBreakdown);
        response.setCalculatedProficiency(proficiency);
        response.setUpdatedEmployabilityScore(updatedEmployabilityScore);
        response.setAiAnalysisExplanation(aiResults[0]);
        response.setAiActionPlan(aiResults[1]);

        return response;
    }

    private String[] callGroqForAssessmentFeedback(String skillName, int selfRating, int actualScore, int gap,
                                                  String gapCategory, Map<String, Integer> topicBreakdown, String targetRole) {
        String defaultExplanation = String.format(
                "Your %s self-rating was %d/10 (%d%%) while your demonstrated assessment score is %d%% (%s). " +
                "Your topic-wise performance indicates specific areas needing reinforcement to meet industry benchmarks for %s.",
                skillName, selfRating, selfRating * 10, actualScore, gapCategory.replace("_", " "), targetRole
        );

        String defaultPlan = String.format(
                "1. Focus on weak topics (%s).\n" +
                "2. Complete 15-20 practical exercises targeting low-scoring concepts.\n" +
                "3. Build a microservice or project component applying these concepts.\n" +
                "4. Retake the %s assessment to bridge the %d%% confidence gap.\n" +
                "5. Target score: 80%%+ to achieve Advanced Verified status for %s.",
                topicBreakdown.entrySet().stream().filter(e -> e.getValue() < 60).map(Map.Entry::getKey).collect(Collectors.joining(", ")),
                skillName, Math.abs(gap), targetRole
        );

        if (groqApiKey == null || groqApiKey.trim().isEmpty() || groqApiKey.startsWith("gsk_default")) {
            return new String[]{defaultExplanation, defaultPlan};
        }

        try {
            String prompt = String.format(
                    "You are the TalentOrbit AI Skill Diagnostic Engine. Analyze the following technical assessment:\n" +
                    "- Skill: %s\n" +
                    "- Student Self-Rating: %d/10 (%d%%)\n" +
                    "- Actual Assessment Score: %d%%\n" +
                    "- Confidence Gap: %d%% (%s)\n" +
                    "- Per-Topic Performance: %s\n" +
                    "- Student Target Role: %s\n\n" +
                    "Generate a 2-part response formatted as:\n" +
                    "EXPLANATION: [A concise 2-sentence objective explanation of their performance and confidence gap]\n" +
                    "ACTION_PLAN: [A numbered 5-step concrete, actionable study and coding improvement plan with target metrics]",
                    skillName, selfRating, selfRating * 10, actualScore, gap, gapCategory, topicBreakdown.toString(), targetRole
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey.trim());

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body = new HashMap<>();
            body.put("model", groqModel);
            body.put("messages", Collections.singletonList(message));
            body.put("temperature", 0.3);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(groqApiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List choices = (List) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map msg = (Map) choice.get("message");
                    String raw = (String) msg.get("content");
                    if (raw != null && raw.contains("ACTION_PLAN:")) {
                        String[] parts = raw.split("ACTION_PLAN:");
                        String exp = parts[0].replace("EXPLANATION:", "").trim();
                        String plan = parts[1].trim();
                        return new String[]{exp, plan};
                    }
                    return new String[]{raw, defaultPlan};
                }
            }
        } catch (Exception e) {
            System.err.println("Groq AI Assessment call error: " + e.getMessage());
        }

        return new String[]{defaultExplanation, defaultPlan};
    }
}
