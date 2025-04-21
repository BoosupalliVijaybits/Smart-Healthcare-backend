package io.bvb.smarthealthcare.backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest extends BasicUserRequest {
    @NotNull(message = "Clinic name is required")
    private String clinicName;
    @NotNull(message = "Clinic address is required")
    private String clinicAddress;
    @NotNull(message = "Specialization is required")
    @Size(message = "Specialization must be at least 2 characters", min = 2)
    private String specialization;
    @NotNull(message = "LicenseNumber is required")
    @Size(message = "LicenseNumber must be between 5 to 10 characters", min = 5, max = 10)
    private String licenseNumber;
    @NotNull(message = "Experience is required")
    private Long experience;
    @NotNull(message = "Qualification is required")
    private String qualification;
}
