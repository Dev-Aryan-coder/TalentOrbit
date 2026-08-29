package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.ChatMessageRequestDTO;
import com.example.TalentOrbit.dto.response.ChatMessageHistoryDTO;
import com.example.TalentOrbit.dto.response.ChatMessageResponseDTO;
import com.example.TalentOrbit.dto.response.ChatSessionResponseDTO;
import com.example.TalentOrbit.entity.ChatMessage;
import com.example.TalentOrbit.entity.ChatSession;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.Sender;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.ChatMessageRepository;
import com.example.TalentOrbit.repository.ChatSessionRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    @Autowired private ChatSessionRepository chatSessionRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private RestTemplate restTemplate;

    @Value("${groq.api.key:}")
    private String groqApiKey;

    @Value("${groq.model:qwen/qwen3.8-27b}")
    private String groqModel;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    private static final String TALENT_ORBIT_SYSTEM_PROMPT = 
        "You are the official AI Assistant for TalentOrbit (SIH Problem Statement #26044), an intelligent Academia-Industry collaboration platform.\n\n" +
        "PLATFORM ARCHITECTURE & ROLES:\n" +
        "1. Student Portal: Skill assessments (MCQ & adaptive grading), Explainable Matching (shows matched vs missing skills), Target Milestone Roadmap, Gamification Badges, and Digital Verified Portfolio.\n" +
        "2. Industry / Recruiter Portal: Post weighted opportunities, Live Candidate Availability Previews, Talent Pool Search, ATS Funnel, Technical Interview Scheduling, and Post-Internship Mentor Verification Feedback Loop (which updates student skill profiles upon completion).\n" +
        "3. Academician / Faculty Portal: Dedicated streams for AICTE FDPs, Industry Sabbaticals, Joint Research Grants, Corporate Consultancies, and Tech Bootcamps with research tag matching.\n" +
        "4. Institution / TPO Portal: Demand vs Supply Skill Deficit Heatmaps (e.g. Docker -52%), 1-Click Remedial Bootcamps, and NIRF/NAAC/AICTE compliant reporting.\n" +
        "5. SuperAdmin God Mode: AISHE and MCA CIN KYC approvals, content moderation, and cryptographic audit logging.\n\n" +
        "BEHAVIOR GUIDELINES:\n" +
        "- Provide concise, authoritative, and helpful answers.\n" +
        "- Support text queries and multimodal image analysis (resumes, charts, code screenshots, certificates).\n" +
        "- Tailor recommendations to the user's specific role and verified skills.";

    public ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ChatSession session;
        if (req.getSessionId() != null) {
            session = chatSessionRepository.findById(req.getSessionId())
                    .orElseGet(() -> createNewSession(user, req.getMessage()));
        } else {
            session = createNewSession(user, req.getMessage());
        }

        // 1. Save USER message
        ChatMessage userMsg = new ChatMessage(session, Sender.USER, req.getMessage(), req.getImageUrl());
        chatMessageRepository.save(userMsg);

        // 2. Load last 10 messages for context
        List<ChatMessage> history = chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(session.getId());
        int historySize = history.size();
        List<ChatMessage> recentHistory = history.subList(Math.max(0, historySize - 10), historySize);

        // 3. Build dynamic user context
        StringBuilder userContext = new StringBuilder();
        userContext.append("\nCURRENT USER CONTEXT:\n");
        userContext.append("- Role: ").append(user.getRole()).append("\n");
        userContext.append("- Email: ").append(user.getEmail()).append("\n");

        if (user.getRole() == Role.STUDENT) {
            List<StudentSkill> skills = studentSkillRepository.findByUserId(user.getId());
            String skillList = skills.stream()
                    .map(s -> s.getSkill().getName() + " (" + s.getProficiency() + (s.getIsVerified() ? ", Verified" : "") + ")")
                    .collect(Collectors.joining(", "));
            userContext.append("- Verified Skill Genome: ").append(skillList.isEmpty() ? "None assessed yet" : skillList).append("\n");
        }

        // 4. Call Groq Qwen API with vision support
        String assistantReply = callGroqApi(TALENT_ORBIT_SYSTEM_PROMPT + userContext.toString(), recentHistory, req.getMessage(), req.getImageUrl());

        // 5. Save ASSISTANT message
        ChatMessage assistantMsg = new ChatMessage(session, Sender.ASSISTANT, assistantReply, null);
        chatMessageRepository.save(assistantMsg);

        return new ChatMessageResponseDTO(session.getId(), assistantReply, assistantMsg.getCreatedAt());
    }

    private ChatSession createNewSession(User user, String firstMessage) {
        String title = firstMessage != null && firstMessage.length() > 30 
                ? firstMessage.substring(0, 30) + "..." 
                : (firstMessage != null ? firstMessage : "New Conversation");
        ChatSession session = new ChatSession(user, title);
        return chatSessionRepository.save(session);
    }

    public List<ChatSessionResponseDTO> getUserSessions(Long userId) {
        return chatSessionRepository.findByUserIdOrderByStartedAtDesc(userId).stream()
                .map(s -> new ChatSessionResponseDTO(s.getId(), s.getTitle(), s.getStartedAt()))
                .collect(Collectors.toList());
    }

    public List<ChatMessageHistoryDTO> getSessionMessages(Long sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(m -> new ChatMessageHistoryDTO(m.getId(), m.getSender(), m.getContent(), m.getImageUrl(), m.getCreatedAt()))
                .collect(Collectors.toList());
    }

    private String callGroqApi(String fullSystemPrompt, List<ChatMessage> history, String newMessage, String imageUrl) {
        if (groqApiKey == null || groqApiKey.trim().isEmpty() || groqApiKey.startsWith("gsk_default")) {
            return "Welcome to TalentOrbit! I am your AI Career and Platform Assistant. " +
                   "Our system connects Students, Industry, Academicians, and TPOs with verified skill assessments, " +
                   "explainable AI matching, and target milestone roadmaps. How can I assist you with your career or recruitment workflow today?";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey.trim());

            List<Map<String, Object>> messagesPayload = new ArrayList<>();

            // System Message
            Map<String, Object> sysMsg = new HashMap<>();
            sysMsg.put("role", "system");
            sysMsg.put("content", fullSystemPrompt);
            messagesPayload.add(sysMsg);

            // History
            for (ChatMessage m : history) {
                Map<String, Object> hMsg = new HashMap<>();
                hMsg.put("role", m.getSender() == Sender.USER ? "user" : "assistant");
                hMsg.put("content", m.getContent());
                messagesPayload.add(hMsg);
            }

            // Current New User Message (supports text and vision multimodal format)
            Map<String, Object> currentMsg = new HashMap<>();
            currentMsg.put("role", "user");

            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                List<Map<String, Object>> contentParts = new ArrayList<>();
                Map<String, Object> textPart = new HashMap<>();
                textPart.put("type", "text");
                textPart.put("text", newMessage);
                contentParts.add(textPart);

                Map<String, Object> imgPart = new HashMap<>();
                imgPart.put("type", "image_url");
                Map<String, String> urlMap = new HashMap<>();
                urlMap.put("url", imageUrl.trim());
                imgPart.put("image_url", urlMap);
                contentParts.add(imgPart);

                currentMsg.put("content", contentParts);
            } else {
                currentMsg.put("content", newMessage);
            }
            messagesPayload.add(currentMsg);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", groqModel);
            requestBody.put("messages", messagesPayload);
            requestBody.put("temperature", 0.5);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(groqApiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List choices = (List) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map msg = (Map) choice.get("message");
                    return (String) msg.get("content");
                }
            }
        } catch (Exception e) {
            System.err.println("Groq Chatbot API call error: " + e.getMessage());
            return "I am currently running in offline fallback mode. TalentOrbit is actively tracking your verified skills, opportunities, and applications. Please ask any specific platform question!";
        }

        return "I'm ready to help with your TalentOrbit journey. What would you like to explore next?";
    }
}
