package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.AiAssessmentRequestDTO;
import com.example.TalentOrbit.dto.request.AssessmentQuestionAnswerDTO;
import com.example.TalentOrbit.dto.response.AiAssessmentResponseDTO;
import com.example.TalentOrbit.dto.response.QuestionResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ProficiencyLevel;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with ID: " + skillId));

        List<Question> questions = questionRepository.findBySkillId(skillId);

        // Dynamic On-Demand Question Generation for ANY IT Topic if not in DB
        if (questions == null || questions.isEmpty()) {
            questions = generateAndSaveQuestionsForSkill(skill);
        }

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

    private List<Question> generateAndSaveQuestionsForSkill(Skill skill) {
        List<Question> generatedList = new ArrayList<>();

        if (groqApiKey != null && !groqApiKey.startsWith("gsk_default") && !groqApiKey.trim().isEmpty()) {
            try {
                String systemPrompt = "You are a technical assessment architect. Return ONLY a valid JSON array of 3 distinct, topic-tagged MCQ questions for the requested skill. Format:\n" +
                        "[{\"topic\":\"TopicName\",\"text\":\"Question text?\",\"optionA\":\"...\",\"optionB\":\"...\",\"optionC\":\"...\",\"optionD\":\"...\",\"correctOption\":\"A|B|C|D\",\"explanation\":\"...\"}]";
                
                String userPrompt = "Generate 3 intermediate-to-advanced technical diagnostic MCQ questions for the skill: " + skill.getName();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(groqApiKey);

                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("model", groqModel);
                requestBody.put("temperature", 0.2);

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
                        String content = (String) message.get("content");

                        ObjectMapper mapper = new ObjectMapper();
                        String cleanJson = content.trim();
                        if (cleanJson.contains("[")) {
                            cleanJson = cleanJson.substring(cleanJson.indexOf("["), cleanJson.lastIndexOf("]") + 1);
                        }
                        JsonNode rootArray = mapper.readTree(cleanJson);
                        if (rootArray.isArray()) {
                            for (JsonNode node : rootArray) {
                                Question q = new Question();
                                q.setSkill(skill);
                                q.setTopic(node.path("topic").asText("Core Concepts"));
                                q.setText(node.path("text").asText());
                                q.setOptionA(node.path("optionA").asText());
                                q.setOptionB(node.path("optionB").asText());
                                q.setOptionC(node.path("optionC").asText());
                                q.setOptionD(node.path("optionD").asText());
                                q.setCorrectOption(node.path("correctOption").asText("A"));
                                q.setExplanation(node.path("explanation").asText("Core technical concept in " + skill.getName()));
                                generatedList.add(questionRepository.save(q));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Dynamic Question Generation notice: " + e.getMessage());
            }
        }

        // Deterministic Fallback if AI is offline
        if (generatedList.isEmpty()) {
            Question q1 = new Question();
            q1.setSkill(skill);
            q1.setTopic("Fundamentals & Architecture");
            q1.setText("What is the primary architectural principle and best practice when building solutions with " + skill.getName() + "?");
            q1.setOptionA("Modular separation of concerns and high cohesion");
            q1.setOptionB("Monolithic tightly-coupled components");
            q1.setOptionC("Direct hardcoded database connections in UI");
            q1.setOptionD("Ignoring exception boundaries");
            q1.setCorrectOption("A");
            q1.setExplanation("Modular separation of concerns promotes maintainability and scalability in " + skill.getName() + ".");
            generatedList.add(questionRepository.save(q1));

            Question q2 = new Question();
            q2.setSkill(skill);
            q2.setTopic("Performance & Optimization");
            q2.setText("How do you prevent memory leaks and performance bottlenecks in " + skill.getName() + "?");
            q2.setOptionA("By increasing CPU frequency indefinitely");
            q2.setOptionB("Proper lifecycle cleanup, resource management, and asynchronous non-blocking I/O");
            q2.setOptionC("Disabling garbage collection and error logs");
            q2.setOptionD("Using global static variables everywhere");
            q2.setCorrectOption("B");
            q2.setExplanation("Resource cleanup and asynchronous handling are critical for high throughput in " + skill.getName() + ".");
            generatedList.add(questionRepository.save(q2));

            Question q3 = new Question();
            q3.setSkill(skill);
            q3.setTopic("Security & Resilience");
            q3.setText("What is standard practice for securing data and handling failures in " + skill.getName() + "?");
            q3.setOptionA("Input validation, least-privilege access, and circuit-breaker resilience");
            q3.setOptionB("Exposing internal stack traces to users");
            q3.setOptionC("Storing plain-text secrets in code repositories");
            q3.setOptionD("Bypassing validation layers");
            q3.setCorrectOption("A");
            q3.setExplanation("Defensive programming, input validation, and least privilege are fundamental in " + skill.getName() + ".");
            generatedList.add(questionRepository.save(q3));
        }

        return generatedList;
    }

    public AiAssessmentResponseDTO evaluateAssessmentWithAi(AiAssessmentRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Skill skill = skillRepository.findById(req.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        StudentDetails studentDetails = studentDetailsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        List<Question> questions = questionRepository.findBySkillId(skill.getId());
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        int totalQuestions = (req.getAnswers() != null) ? req.getAnswers().size() : 0;
        int totalCorrect = 0;
        Map<String, Integer> topicTotal = new HashMap<>();
        Map<String, Integer> topicCorrect = new HashMap<>();

        if (req.getAnswers() != null) {
            for (AssessmentQuestionAnswerDTO ans : req.getAnswers()) {
                Question q = questionMap.get(ans.getQuestionId());
                if (q == null) continue;

                String topic = q.getTopic();
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
        sb.append("2. Practice 15+ complex multi-condition scenarios.\n");
        sb.append("3. Review performance, security best practices, and execution models.\n");
        sb.append("4. Retake the TalentOrbit diagnostic assessment to verify 80%+ benchmark.\n");
        sb.append("5. Build a verified portfolio project demonstrating end-to-end implementation.\n");
        return sb.toString();
    }
}
