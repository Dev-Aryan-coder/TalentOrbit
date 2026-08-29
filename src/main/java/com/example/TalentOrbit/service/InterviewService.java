package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.InterviewCreateDTO;
import com.example.TalentOrbit.dto.request.InterviewStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.InterviewResponseDTO;
import com.example.TalentOrbit.entity.Application;
import com.example.TalentOrbit.entity.Interview;
import com.example.TalentOrbit.enums.InterviewStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.ApplicationRepository;
import com.example.TalentOrbit.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewService {
    @Autowired private InterviewRepository interviewRepository;
    @Autowired private ApplicationRepository applicationRepository;

    public InterviewResponseDTO scheduleInterview(InterviewCreateDTO req) {
        Application app = applicationRepository.findById(req.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Interview i = new Interview();
        i.setApplication(app);
        i.setScheduledAt(req.getScheduledAt());
        i.setInterviewerName(req.getInterviewerName());
        i.setMeetingLink(req.getMeetingLink());
        i.setStatus(InterviewStatus.SCHEDULED);
        i.setNotes(req.getNotes());
        Interview saved = interviewRepository.save(i);

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

    public List<InterviewResponseDTO> getInterviewsForApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId).stream().map(i -> {
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
        Interview i = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        if (req.getStatus() != null) i.setStatus(req.getStatus());
        if (req.getNotes() != null) i.setNotes(req.getNotes());
        Interview saved = interviewRepository.save(i);

        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(saved.getId());
        dto.setApplicationId(saved.getApplication().getId());
        dto.setStatus(saved.getStatus());
        dto.setNotes(saved.getNotes());
        return dto;
    }
}
