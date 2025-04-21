package io.bvb.smarthealthcare.backend.exception;

public class DoctorNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "Doctor not found : %s";
    public DoctorNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
