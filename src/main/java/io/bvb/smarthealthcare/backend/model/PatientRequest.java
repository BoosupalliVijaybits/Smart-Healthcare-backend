package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest extends BasicUserRequest {
    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Marital Status is required")
    private MaritalStatus maritalStatus;

    private String preConditions;

    private String allergies;

    @NotNull(message = "Blood Group is required")
    private String bloodGroup;

    @NotNull(message = "Emergency Number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid emergency phone number")
    private String emergencyNumber;

    @NotNull(message = "Emergency Name is required ")
    private String emergencyName;
}

