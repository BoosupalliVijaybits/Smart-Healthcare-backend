package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.MedicationTime;
import io.bvb.smarthealthcare.backend.constant.TimeToTake;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescriptionRequest {
    @NotNull
    private String medicationName;
    @NotNull
    private String dosage;
    @NotEmpty(message = "Medication time list must not be empty")
    private Set<@NotNull(message = "Each medication time must not be null")MedicationTime> medicationTime;
    private TimeToTake timeToTake = TimeToTake.AFTER_FOOD;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;
}