package io.bvb.smarthealthcare.backend.exception;

public class PermissionDeniedException extends ApplicationException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
