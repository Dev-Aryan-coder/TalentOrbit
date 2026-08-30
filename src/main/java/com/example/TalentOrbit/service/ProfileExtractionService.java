package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.ProfileConfirmationRequestDTO;
import com.example.TalentOrbit.dto.response.DetectedSkillDTO;
import com.example.TalentOrbit.dto.response.ProfileExtractionResponseDTO;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProfileExtractionService {

    @Autowired private SkillRepository skillRepository;
    @Autowired private RoleSkillTemplateRepository roleSkillTemplateRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private RoadmapService roadmapService;
    @Autowired private RestTemplate restTemplate;

    @Value("${groq.api.key:gsk_default_demo_key}")
    private String groqApiKey;

    @Value("${groq.model:qwen/qwen3.8-27b}")
    private String groqModel;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    public ProfileExtractionResponseDTO extractProfile(Long userId, String freeText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Skill> masterSkills = skillRepository.findAll();
        List<String> masterSkillNames = masterSkills.stream().map(Skill::getName).collect(Collectors.toList());

        List<RoleSkillTemplate> roleTemplates = roleSkillTemplateRepository.findAll();
        List<String> validRoles = roleTemplates.stream().map(RoleSkillTemplate::getRoleName).collect(Collectors.toList());

        Set<String> detectedSkillNames = new LinkedHashSet<>();
        String detectedCareerInterest = null;
        List<String> detectedProjects = new ArrayList<>();

        if (freeText != null && !freeText.trim().isEmpty()) {
            boolean groqSuccess = false;

            if (groqApiKey != null && !groqApiKey.startsWith("gsk_default") && !groqApiKey.trim().isEmpty()) {
                try {
                    String systemPrompt = "You are TalentOrbit's AI Profile Extraction Agent. Extract skills, career interest, and projects from a student's self-introduction.\n" +
                            "STRICT RULES:\n" +
                            "1. ONLY detect skills from this exact approved list: " + masterSkillNames + "\n" +
                            "2. ONLY detect career interest from this exact approved roles list: " + validRoles + " (or null if none match).\n" +
                            "3. List detected projects in plain text.\n" +
                            "4. Do NOT guess or assign any proficiency level, percentage, or rating.\n" +
                            "5. Return output in STRICT JSON format only:\n" +
                            "{\"detectedSkills\": [\"Skill1\", \"Skill2\"], \"careerInterest\": \"RoleName\", \"projectsDetected\": [\"Project description\"]}";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBearerAuth(groqApiKey);

                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("model", groqModel);
                    requestBody.put("temperature", 0.1);
                    requestBody.put("response_format", Map.of("type", "json_object"));

                    List<Map<String, String>> messages = new ArrayList<>();
                    messages.add(Map.of("role", "system", "content", systemPrompt));
                    messages.add(Map.of("role", "user", "content", "Student introduction: \"" + freeText + "\""));
                    requestBody.put("messages", messages);

                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                    ResponseEntity<Map> response = restTemplate.postForEntity(groqApiUrl, entity, Map.class);

                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                            String jsonContent = (String) message.get("content");
                            parseGroqJsonResponse(jsonContent, detectedSkillNames, detectedProjects);
                            detectedCareerInterest = parseCareerInterest(jsonContent, validRoles);
                            groqSuccess = true;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Groq Profile Extraction notice: " + e.getMessage() + " - using deterministic parser.");
                }
            }

            // Fallback / Hybrid Verification
            if (!groqSuccess) {
                String lowerText = freeText.toLowerCase();
                for (Skill sk : masterSkills) {
                    if (lowerText.contains(sk.getName().toLowerCase())) {
                        detectedSkillNames.add(sk.getName());
                    }
                }
                for (RoleSkillTemplate rt : roleTemplates) {
                    if (lowerText.contains(rt.getRoleName().toLowerCase()) || lowerText.contains("backend") && rt.getRoleName().contains("Backend")) {
                        detectedCareerInterest = rt.getRoleName();
                        break;
                    }
                }
                if (lowerText.contains("project") || lowerText.contains("built") || lowerText.contains("developed") || lowerText.contains("website") || lowerText.contains("app")) {
                    detectedProjects.add("Self-reported development project mentioned in text");
                }
            }
        }

        // Defensive Check: Ensure only valid DB skills are returned
        Map<String, Skill> skillLookup = masterSkills.stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), s -> s, (k1, k2) -> k1));

        List<DetectedSkillDTO> finalSkills = new ArrayList<>();
        for (String detected : detectedSkillNames) {
            Skill match = skillLookup.get(detected.trim().toLowerCase());
            if (match != null) {
                finalSkills.add(new DetectedSkillDTO(match.getId(), match.getName()));
            }
        }

        ProfileExtractionResponseDTO resp = new ProfileExtractionResponseDTO();
        resp.setUserId(userId);
        resp.setDetectedSkills(finalSkills);
        resp.setCareerInterest(detectedCareerInterest);
        resp.setProjectsDetected(detectedProjects);
        resp.setStatusMessage("AI Profile Analysis complete. Please review and confirm your skills before self-rating.");
        return resp;
    }

    public ProfileExtractionResponseDTO confirmProfile(Long userId, ProfileConfirmationRequestDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        StudentDetails sd = studentDetailsRepository.findByUser(user).orElse(null);

        if (req.getCareerInterest() != null && !req.getCareerInterest().trim().isEmpty() && sd != null) {
            sd.setTargetRole(req.getCareerInterest().trim());
            studentDetailsRepository.save(sd);
        }

        List<DetectedSkillDTO> confirmedSkills = new ArrayList<>();
        if (req.getConfirmedSkillIds() != null) {
            for (Long skillId : req.getConfirmedSkillIds()) {
                Skill skill = skillRepository.findById(skillId).orElse(null);
                if (skill != null) {
                    Optional<StudentSkill> existing = studentSkillRepository.findByUserAndSkill(user, skill);
                    if (existing.isEmpty()) {
                        StudentSkill ss = new StudentSkill();
                        ss.setUser(user);
                        ss.setSkill(skill);
                        ss.setProficiency(ProficiencyLevel.BEGINNER); // Unverified until assessment
                        ss.setIsVerified(false);
                        studentSkillRepository.save(ss);
                    }
                    confirmedSkills.add(new DetectedSkillDTO(skill.getId(), skill.getName()));
                }
            }
        }

        // Auto-generate target roadmap steps for missing skills
        roadmapService.generateRoadmapForStudent(userId);

        ProfileExtractionResponseDTO resp = new ProfileExtractionResponseDTO();
        resp.setUserId(userId);
        resp.setDetectedSkills(confirmedSkills);
        resp.setCareerInterest(sd != null ? sd.getTargetRole() : req.getCareerInterest());
        resp.setStatusMessage("Profile skills confirmed. Proceed to self-rating and assessment to verify your skill proficiency.");
        return resp;
    }

    private void parseGroqJsonResponse(String json, Set<String> detectedSkills, List<String> detectedProjects) {
        try {
            // Regex parse skills
            Pattern skillPat = Pattern.compile("\"detectedSkills\"\\s*:\\s*\\[([^\\]]*)\\]");
            Matcher m = skillPat.matcher(json);
            if (m.find()) {
                String skillsBlock = m.group(1);
                String[] tokens = skillsBlock.split(",");
                for (String t : tokens) {
                    String clean = t.replace("\"", "").trim();
                    if (!clean.isEmpty()) detectedSkills.add(clean);
                }
            }

            // Regex parse projects
            Pattern projPat = Pattern.compile("\"projectsDetected\"\\s*:\\s*\\[([^\\]]*)\\]");
            Matcher mp = projPat.matcher(json);
            if (mp.find()) {
                String projsBlock = mp.group(1);
                String[] tokens = projsBlock.split(",");
                for (String t : tokens) {
                    String clean = t.replace("\"", "").trim();
                    if (!clean.isEmpty()) detectedProjects.add(clean);
                }
            }
        } catch (Exception ignored) {}
    }

    private String parseCareerInterest(String json, List<String> validRoles) {
        try {
            Pattern p = Pattern.compile("\"careerInterest\"\\s*:\\s*\"([^\"]+)\"");
            Matcher m = p.matcher(json);
            if (m.find()) {
                String interest = m.group(1).trim();
                for (String vr : validRoles) {
                    if (vr.equalsIgnoreCase(interest) || interest.toLowerCase().contains(vr.toLowerCase())) {
                        return vr;
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}
