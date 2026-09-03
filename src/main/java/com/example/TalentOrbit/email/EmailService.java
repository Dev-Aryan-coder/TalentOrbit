package com.example.TalentOrbit.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void sendPasswordChangedEmail(String toEmail, String userName, String role, Long userId) {
        String subject = "Security Alert: TalentOrbit Account Password Changed";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

        String plainTextBody = String.format(
            "Dear %s,\n\n" +
            "This is an official security confirmation that the password for your TalentOrbit account was successfully changed.\n\n" +
            "=== ACCOUNT SECURITY DETAILS ===\n" +
            "• Account Email: %s\n" +
            "• Account ID: #TO-%s\n" +
            "• Assigned Role: %s\n" +
            "• Timestamp: %s (IST)\n" +
            "• Security Protocol: SHA-256 Digest Synchronized in MySQL\n\n" +
            "If you performed this update, no further action is required.\n\n" +
            "SECURITY WARNING: If you did not make this change, your account may be compromised. Please contact TalentOrbit Security Governance immediately at support@talentorbit.gov.in.\n\n" +
            "Best regards,\nTalentOrbit Security & Identity Governance Team",
            userName != null && !userName.trim().isEmpty() ? userName : "User",
            toEmail,
            userId != null ? userId : "N/A",
            role != null ? role : "Member",
            dateStr
        );

        String htmlBody = String.format(
            "<div style=\"font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; max-width: 600px; margin: 0 auto; background: #ffffff; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden;\">" +
            "  <div style=\"background: #041638; padding: 24px; text-align: center; color: #ffffff;\">" +
            "    <h1 style=\"margin: 0; font-size: 22px; font-weight: 700; letter-spacing: 0.5px;\">TalentOrbit</h1>" +
            "    <p style=\"margin: 4px 0 0 0; font-size: 12px; color: #94a3b8; text-transform: uppercase; letter-spacing: 1px;\">Security & Identity Intelligence</p>" +
            "  </div>" +
            "  <div style=\"padding: 32px 28px; color: #1e293b;\">" +
            "    <h2 style=\"font-size: 18px; color: #0f172a; margin: 0 0 12px 0;\">Password Successfully Changed</h2>" +
            "    <p style=\"font-size: 14px; line-height: 1.6; color: #475569; margin: 0 0 20px 0;\">" +
            "      Dear <strong>%s</strong>,<br><br>" +
            "      Your TalentOrbit account password was successfully updated and cryptographically synchronized with the central database." +
            "    </p>" +
            "    <div style=\"background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 18px; margin-bottom: 24px;\">" +
            "      <h3 style=\"margin: 0 0 12px 0; font-size: 12px; text-transform: uppercase; letter-spacing: 0.5px; color: #64748b;\">Account Security Audit Record</h3>" +
            "      <table style=\"width: 100%%; font-size: 13px; border-collapse: collapse;\">" +
            "        <tr><td style=\"padding: 6px 0; color: #64748b; width: 40%%;\">Account Email:</td><td style=\"padding: 6px 0; font-weight: 600; color: #0f172a;\">%s</td></tr>" +
            "        <tr><td style=\"padding: 6px 0; color: #64748b;\">Account ID:</td><td style=\"padding: 6px 0; font-weight: 600; color: #0f172a;\">#TO-%s</td></tr>" +
            "        <tr><td style=\"padding: 6px 0; color: #64748b;\">Assigned Role:</td><td style=\"padding: 6px 0; font-weight: 600; color: #0055ff;\">%s</td></tr>" +
            "        <tr><td style=\"padding: 6px 0; color: #64748b;\">Timestamp:</td><td style=\"padding: 6px 0; font-weight: 600; color: #0f172a;\">%s (IST)</td></tr>" +
            "        <tr><td style=\"padding: 6px 0; color: #64748b;\">Security Status:</td><td style=\"padding: 6px 0; font-weight: 600; color: #059669;\">SHA-256 Digest Synchronized</td></tr>" +
            "      </table>" +
            "    </div>" +
            "    <div style=\"background: #fef2f2; border: 1px solid #fecaca; border-radius: 8px; padding: 14px; margin-bottom: 24px;\">" +
            "      <p style=\"margin: 0; font-size: 12.5px; color: #991b1b; line-height: 1.5;\">" +
            "        <strong>Security Warning:</strong> If you did NOT initiate this password change, someone may have accessed your credentials. Please reset your password immediately or contact our security governance desk at support@talentorbit.gov.in." +
            "      </p>" +
            "    </div>" +
            "    <div style=\"text-align: center; margin-top: 24px;\">" +
            "      <a href=\"http://localhost:5173/login\" style=\"display: inline-block; background: #0055ff; color: #ffffff; text-decoration: none; padding: 12px 28px; border-radius: 8px; font-size: 14px; font-weight: 600;\">Log In to TalentOrbit</a>" +
            "    </div>" +
            "  </div>" +
            "  <div style=\"background: #f8fafc; border-top: 1px solid #e2e8f0; padding: 16px; text-align: center; font-size: 11.5px; color: #94a3b8;\">" +
            "    TalentOrbit National Academic & Industry Collaboration Registry • Automated Security Notification" +
            "  </div>" +
            "</div>",
            userName != null && !userName.trim().isEmpty() ? userName : "User",
            toEmail,
            userId != null ? userId : "N/A",
            role != null ? role : "Member",
            dateStr
        );

        sendHtmlEmail(toEmail, subject, htmlBody, plainTextBody);
    }

    public void sendPasswordChangedEmail(String toEmail, String userName) {
        sendPasswordChangedEmail(toEmail, userName, "Member", 0L);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlBody, String plainTextFallback) {
        if (mailSender == null) {
            System.out.println("[EMAIL SIMULATOR - No SMTP Host Configured]");
            System.out.println("To: " + toEmail + " | Subject: " + subject);
            System.out.println("Body: " + plainTextFallback);
            return;
        }
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(plainTextFallback, htmlBody);
            mailSender.send(mimeMessage);
            System.out.println("Rich HTML security alert email successfully sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Falling back to plain text email due to: " + e.getMessage());
            sendEmail(toEmail, subject, plainTextFallback);
        }
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