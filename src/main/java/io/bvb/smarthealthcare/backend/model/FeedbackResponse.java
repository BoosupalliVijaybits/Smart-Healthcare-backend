package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private String id;
    private int rating;
    private String comments;
    private LocalDate date;
    private PatientResponse patient;

    public static FeedbackResponse convertRequestToResponse(final Feedback feedback) {
        final FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setId(feedback.getId());
        feedbackResponse.setDate(feedback.getDate());
        feedbackResponse.setComments(feedback.getComments());
        feedbackResponse.setRating(feedback.getRating());
        feedbackResponse.setPatient(PatientResponse.convertPatientToPatientResponse(feedback.getPatient()));
        return feedbackResponse;
    }
}
