package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.InterviewCreateDTO;
import com.example.TalentOrbit.dto.request.InterviewRubricDTO;
import com.example.TalentOrbit.dto.request.InterviewStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.InterviewResponseDTO;
import com.example.TalentOrbit.email.EmailService;
import com.example.TalentOrbit.entity.Application;
import com.example.TalentOrbit.entity.Interview;
import com.example.TalentOrbit.enums.InterviewStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.ApplicationRepository;
import com.example.TalentOrbit.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InterviewService {
    @Autowired private InterviewRepository interviewRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private EmailService emailService;
    @Autowired private BadgeService badgeService;

    public InterviewResponseDTO scheduleInterview(InterviewCreateDTO req) {
        Application app = applicationRepository.findById(req.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        String jitsiLink = "https://meet.jit.si/TalentOrbit-" + app.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);

        Interview interview = new Interview();
        interview.setApplication(app);
        interview.setScheduledAt(req.getScheduledAt());
        interview.setInterviewerName(req.getInterviewerName());
        interview.setMeetingLink(req.getMeetingLink() != null && !req.getMeetingLink().isEmpty() ? req.getMeetingLink() : jitsiLink);
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setNotes(req.getNotes());

        Interview saved = interviewRepository.save(interview);

        emailService.sendInterviewScheduledEmail(
            app.getUser().getEmail(),
            app.getPosting().getTitle(),
            saved.getScheduledAt(),
            saved.getMeetingLink()
        );

        badgeService.checkAndAwardBadges(app.getUser().getId(), "INTERVIEW_SCHEDULED");

        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(saved.getId());
        dto.setApplicationId(app.getId());
        dto.setScheduledAt(saved.getScheduledAt());
        dto.setInterviewerName(saved.getInterviewerName());
        dto.setMeetingLink(saved.getMeetingLink());
        dto.setStatus(saved.getStatus());
        dto.setNotes(saved.getNotes());
        return dto;
    }

    public InterviewResponseDTO submitRubric(Long interviewId, InterviewRubricDTO req) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        interview.setTechnicalScore(req.getTechnicalScore());
        interview.setCommunicationScore(req.getCommunicationScore());
        interview.setCultureFitScore(req.getCultureFitScore());
        interview.setOverallRecommendation(req.getOverallRecommendation());
        if (req.getNotes() != null) interview.setNotes(req.getNotes());
        interview.setStatus(InterviewStatus.COMPLETED);

        Interview saved = interviewRepository.save(interview);

        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(saved.getId());
        dto.setApplicationId(saved.getApplication().getId());
        dto.setScheduledAt(saved.getScheduledAt());
        dto.setInterviewerName(saved.getInterviewerName());
        dto.setMeetingLink(saved.getMeetingLink());
        dto.setStatus(saved.getStatus());
        dto.setNotes(saved.getNotes());
        return dto;
    }

    public List<InterviewResponseDTO> getInterviewsForApplication(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return interviewRepository.findByApplication(app).stream().map(i -> {
            InterviewResponseDTO dto = new InterviewResponseDTO();
            dto.setId(i.getId());
            dto.setApplicationId(i.getApplication().getId());
            dto.setScheduledAt(i.getScheduledAt());
            dto.setInterviewerName(i.getInterviewerName());
            dto.setMeetingLink(i.getMeetingLink());
            dto.setStatus(i.getStatus());
            dto.setNotes(i.getNotes());
            return dto;
        }).collect(Collectors.toList());
    }

    public InterviewResponseDTO updateStatus(Long interviewId, InterviewStatusUpdateDTO req) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        interview.setStatus(req.getStatus());
        Interview saved = interviewRepository.save(interview);

        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(saved.getId());
        dto.setApplicationId(saved.getApplication().getId());
        dto.setScheduledAt(saved.getScheduledAt());
        dto.setInterviewerName(saved.getInterviewerName());
        dto.setMeetingLink(saved.getMeetingLink());
        dto.setStatus(saved.getStatus());
        dto.setNotes(saved.getNotes());
        return dto;
    }
}
