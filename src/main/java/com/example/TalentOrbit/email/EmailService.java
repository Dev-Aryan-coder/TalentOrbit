package com.example.TalentOrbit.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:yonexffyt14@gmail.com}")
    private String fromEmail;

    public void sendApplicationStatusEmail(String toEmail, String studentName, String postingTitle, String status) {
        String subject = "TalentOrbit: Application Update for " + postingTitle;
        String body = String.format(
            "Dear %s,\n\nYour application status for '%s' has been updated to: %s.\n\n" +
            "Please log in to your TalentOrbit dashboard to view complete ATS progression and details.\n\n" +
            "Best regards,\nTalentOrbit Ecosystem Team",
            studentName, postingTitle, status
        );
        sendEmail(toEmail, subject, body);
    }

    public void sendVerificationDecisionEmail(String toEmail, String userName, String decision, String reason) {
        String subject = "TalentOrbit: KYC Verification Decision (" + decision + ")";
        String body = String.format(
            "Dear %s,\n\nYour TalentOrbit institutional/corporate verification request has been %s.\n\n" +
            "Admin Remarks: %s\n\n" +
            "Best regards,\nTalentOrbit SuperAdmin Governance Team",
            userName, decision, reason != null ? reason : "No additional remarks"
        );
        sendEmail(toEmail, subject, body);
    }

    public void sendRegistrationDecisionEmail(String toEmail, String roleName, String decision, String reason) {
        String subject = "TalentOrbit: Registration Status Update (" + decision + ")";
        String body = String.format(
            "Dear User (%s),\n\nYour TalentOrbit account registration has been reviewed and marked as: %s.\n\n" +
            "Admin Remarks: %s\n\n" +
            "Best regards,\nTalentOrbit SuperAdmin Governance Team",
            roleName, decision, reason != null ? reason : "No additional remarks"
        );
        sendEmail(toEmail, subject, body);
    }

    public void sendInterviewScheduledEmail(String toEmail, String postingTitle, LocalDateTime scheduledAt, String meetingLink) {
        String subject = "TalentOrbit: Technical Interview Scheduled for " + postingTitle;
        String dateStr = scheduledAt != null ? scheduledAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")) : "TBD";
        String body = String.format(
            "Dear Candidate,\n\nYour interview for '%s' has been scheduled.\n\n" +
            "Date & Time: %s\n" +
            "Free Meeting Link (Jitsi Meet): %s\n\n" +
            "Please ensure you join with your video and microphone tested.\n\n" +
            "Best regards,\nTalentOrbit Recruitment Team",
            postingTitle, dateStr, meetingLink
        );
        sendEmail(toEmail, subject, body);
    }

    public void sendTalentInvitationEmail(String toEmail, String recruiterName, String postingTitle, Long postingId) {
        String subject = "TalentOrbit: You have been invited to apply for " + postingTitle;
        String body = String.format(
            "Dear Student,\n\nRecruiter %s has reviewed your verified skill profile in the TalentOrbit Talent Pool and identified you as a great candidate for '%s'.\n\n" +
            "Log in to TalentOrbit to review the job requirements and submit your 1-Click application.\n\n" +
            "Best regards,\nTalentOrbit Talent Sourcing Team",
            recruiterName, postingTitle
        );
        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String toEmail, String subject, String body) {
        if (mailSender == null) {
            System.out.println("[EMAIL SIMULATOR - No SMTP Host Configured]");
            System.out.println("To: " + toEmail + " | Subject: " + subject);
            System.out.println("Body: " + body);
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            System.out.println("Transactional email successfully sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Email dispatch notice: " + e.getMessage());
        }
    }
}
