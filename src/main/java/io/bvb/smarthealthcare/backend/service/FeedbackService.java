package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Feedback;
import io.bvb.smarthealthcare.backend.model.FeedbackRequest;
import io.bvb.smarthealthcare.backend.model.FeedbackResponse;
import io.bvb.smarthealthcare.backend.repository.FeedbackRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public FeedbackService(FeedbackRepository feedbackRepo, DoctorService doctorService, PatientService patientService) {
        this.feedbackRepo = feedbackRepo;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public String submitFeedback(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setRating(request.getRating());
        feedback.setComments(request.getComments());
        feedback.setDoctor(doctorService.getDoctorById(request.getDoctorId()));
        feedback.setPatient(patientService.getPatientById(CurrentUserData.getUser().getId()));

        feedbackRepo.save(feedback);
        return "Feedback submitted successfully";
    }

    public List<FeedbackResponse> getFeedbacksForDoctor(Long doctorId) {
        return feedbackRepo.findByDoctorIdOrderByRatingDesc(doctorId).stream().map(FeedbackResponse::convertRequestToResponse).toList();
    }
}

