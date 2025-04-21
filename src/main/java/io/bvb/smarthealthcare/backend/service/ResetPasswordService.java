package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.PasswordResetToken;
import io.bvb.smarthealthcare.backend.repository.PasswordResetTokenRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResetPasswordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordService.class);
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void generateResetToken(String email, String firstName) {
        // Generate Token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

        // Save token
        PasswordResetToken resetToken = new PasswordResetToken(email, token, expiryDate);
        tokenRepository.save(resetToken);

        // Generate Reset Link
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        // Send Email
        try {
            emailService.sendResetPasswordEmail(email, firstName, resetLink);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send reset password email", e);
            throw new RuntimeException(e);
        }
    }
}

