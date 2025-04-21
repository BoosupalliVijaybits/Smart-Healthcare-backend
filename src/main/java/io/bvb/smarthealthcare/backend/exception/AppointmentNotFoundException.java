package io.bvb.smarthealthcare.backend.exception;

public class AppointmentNotFoundException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Appointment not found : %s";
    public AppointmentNotFoundException(String appointmentId) {
        super(String.format(ERROR_MESSAGE, appointmentId));
    }
}
