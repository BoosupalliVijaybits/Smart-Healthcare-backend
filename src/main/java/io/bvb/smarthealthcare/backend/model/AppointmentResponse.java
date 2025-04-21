package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private String id;

    public static AppointmentResponse convertEntityToResponse(final Appointment appointment) {
        return new AppointmentResponse(appointment.getId());
    }
}
