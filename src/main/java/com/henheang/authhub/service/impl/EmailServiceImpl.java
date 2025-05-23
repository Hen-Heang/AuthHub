package com.henheang.authhub.service.impl;

import com.henheang.authhub.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Override
    public boolean sendPasswordResetEmail(String email, String name, String resetUrl) {
        try {
            log.info("Preparing to send password reset email to: {}", email);

            // Create the email context with variables for the template
            Context context = new Context();
            context.setVariable("name", name != null ? name : "User");
            context.setVariable("resetUrl", resetUrl);

            // Process the email template
            String htmlContent = templateEngine.process("password-reset-email", context);

            // Create and send the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(email);
            helper.setSubject("Reset Your AuthHub Password");
            helper.setText(htmlContent, true); // true indicates HTML content

            // Send the email
            mailSender.send(message);

            log.info("Password reset email sent successfully to: {}", email);
            return true;

        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", email, e.getMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error while sending password reset email to {}: {}", email, e.getMessage(), e);
            return false;
        }
    }
}