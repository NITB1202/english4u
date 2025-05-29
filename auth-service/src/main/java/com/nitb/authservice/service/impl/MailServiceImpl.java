package com.nitb.authservice.service.impl;

import com.nitb.authservice.service.MailService;
import com.nitb.common.exceptions.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String email, String verificationCode) {
        String subject = "Verify Your Email Address";

        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                + "<h2>Email Verification</h2>"
                + "<p>Use the verification code below:</p>"
                + "<div style='font-size: 24px; font-weight: bold; color: #2F54EB; margin: 20px 0;'>"
                + verificationCode
                + "</div>"
                + "<p>This code will expire in 5 minutes.</p>"
                + "<p style='margin-top: 40px;'>Regards,<br><strong>English4U</strong></p>"
                + "</div>";

        sendHtmlMail(email, subject, htmlContent);
    }

    @Override
    public void sendAdminAccountEmail(String email, String password) {
        String subject = "Your Admin Account Has Been Created";

        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                + "<h2>Admin Account Created</h2>"
                + "<p>Your administrator account has been successfully created. Below are your login credentials:</p>"
                + "<ul style='font-size: 16px; line-height: 1.6;'>"
                + "<li><strong>Username:</strong> " + email + "</li>"
                + "<li><strong>Password:</strong> " + password + "</li>"
                + "</ul>"
                + "<p>Please log in and change your password as soon as possible for security purposes.</p>"
                + "<p style='margin-top: 40px;'>Regards,<br><strong>English4U</strong></p>"
                + "</div>";

        sendHtmlMail(email, subject, htmlContent);
    }

    @Override
    public void sendUpdateRoleEmail(String email, String oldRole, String newRole) {
        String subject = "Your Role Has Been Updated";

        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>"
                + "<div style='max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; "
                + "box-shadow: 0 2px 8px rgba(0,0,0,0.1);'>"
                + "<h2 style='color: #2F54EB;'>Your Role Has Been Updated</h2>"
                + "<p>Dear <strong>user@example.com</strong>,</p>"
                + "<p>We would like to inform you that your role in <strong>English4U</strong> has been updated.</p>"
                + "<table style='width: 100%; margin-top: 20px; font-size: 16px;'>"
                + "<tr><td style='padding: 8px 0;'><strong>Previous Role:</strong></td>"
                + "<td style='padding: 8px 0;'>"
                + oldRole
                + "</td></tr>"
                + "<tr><td style='padding: 8px 0;'><strong>New Role:</strong></td>"
                + "<td style='padding: 8px 0; color: green;'><strong>"
                + newRole
                + "</strong></td></tr>"
                + "</table>"
                + "<p style='margin-top: 20px;'>If you have any questions regarding this change, please contact support.</p>"
                + "<p style='margin-top: 40px;'>Regards,<br><strong>English4U</strong></p>"
                + "</div></div>";

        sendHtmlMail(email, subject, htmlContent);
    }

    private void sendHtmlMail(String email, String subject, String htmlContent) {
        if(email == null || email.isEmpty()) {
            throw new BusinessException("This user hasn't provided an email yet.");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException("Failed to send email " + email);
        }
    }
}
