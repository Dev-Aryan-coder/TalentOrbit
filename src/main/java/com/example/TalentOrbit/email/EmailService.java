package com.example.TalentOrbit.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendApplicationStatusEmail(String toEmail, String studentName, String postingTitle, String status) {
        if (mailSender == null) return;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("TalentOrbit: Application Status Updated for " + postingTitle);
            message.setText("Dear " + studentName + ",\n\nYour application status for '" + postingTitle + "' has been updated to: " + status + ".\n\nBest regards,\nTalentOrbit Placement & Immersion Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendRegistrationDecisionEmail(String toEmail, String role, String status, String reason) {
        if (mailSender == null) return;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("TalentOrbit: Account Verification Status (" + status + ")");
            message.setText("Hello,\n\nYour TalentOrbit account registration for role [" + role + "] is now: " + status + ".\nNotes: " + (reason != null ? reason : "Welcome to the verified TalentOrbit platform!") + "\n\nBest regards,\nSuperAdmin Governance Cell");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendNewMatchEmail(String toEmail, String name, String postingTitle, Integer matchPercentage) {
        if (mailSender == null) return;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("TalentOrbit: High Match Found (" + matchPercentage + "%): " + postingTitle);
            message.setText("Hi " + name + ",\n\nA new opportunity matching your verified skill profile with " + matchPercentage + "% affinity has been posted: " + postingTitle + ".\n\nLog in to TalentOrbit to review and 1-Click apply!\n\nBest regards,\nTalentOrbit AI Sourcing Engine");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
