package io.bvb.smarthealthcare.backend.exception;

public abstract class NotFoundException extends ApplicationException {
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
