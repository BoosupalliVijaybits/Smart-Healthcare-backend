package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.model.FeedbackRequest;
import io.bvb.smarthealthcare.backend.model.FeedbackResponse;
import io.bvb.smarthealthcare.backend.model.StringResponse;
import io.bvb.smarthealthcare.backend.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/submit")
    public ResponseEntity<StringResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(new StringResponse(feedbackService.submitFeedback(request)));
    }

    @GetMapping("/doctor/{doctorId}")
    public List<FeedbackResponse> getDoctorFeedbacks(@PathVariable Long doctorId) {
        return feedbackService.getFeedbacksForDoctor(doctorId);
    }
}

