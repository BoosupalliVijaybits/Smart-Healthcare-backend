package io.bvb.smarthealthcare.backend.exception;

public class DoctorNotApprovedException extends ApplicationException {
    private static final String ERROR_MESSAGE = "Doctor account is pending approval.";

    public DoctorNotApprovedException() {
        super(ERROR_MESSAGE);
    }
}
