package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.exception.InvalidDataException;
import io.bvb.smarthealthcare.backend.model.GetInTouchRequest;
import io.bvb.smarthealthcare.backend.model.StringResponse;
import io.bvb.smarthealthcare.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/get-in-touch")
    public ResponseEntity<StringResponse> getInTouch(@Valid @RequestBody GetInTouchRequest getInTouchRequest) {
        try {
            emailService.sendGetInTouchEmail(getInTouchRequest);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send message to admin :: {}", e.getMessage());
            throw new InvalidDataException("Failed to send message to admin");
        }
        return ResponseEntity.ok(new StringResponse("Thanks for reaching out! We'll get back to you soon."));
    }
}
