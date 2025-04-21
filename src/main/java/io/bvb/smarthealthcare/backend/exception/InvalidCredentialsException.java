package io.bvb.smarthealthcare.backend.exception;

public class InvalidCredentialsException extends ApplicationException {
    private static final String ERROR_MESSAGE = "Invalid credentials";
    public InvalidCredentialsException() {
        super(ERROR_MESSAGE);
    }
}
