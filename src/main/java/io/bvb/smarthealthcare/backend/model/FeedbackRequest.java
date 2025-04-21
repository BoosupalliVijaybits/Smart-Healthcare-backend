package io.bvb.smarthealthcare.backend.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
    @NotNull
    private Long doctorId;
    @Min(1)
    @Max(5)
    private int rating;
    private String comments;
}

