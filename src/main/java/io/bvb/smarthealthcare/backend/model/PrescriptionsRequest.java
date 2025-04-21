package io.bvb.smarthealthcare.backend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionsRequest {
    @NotNull
    private String appointmentId;
    @NotNull
    private Long patientId;
    @Valid
    private List<PrescriptionRequest> prescriptions = new ArrayList<>();
}
