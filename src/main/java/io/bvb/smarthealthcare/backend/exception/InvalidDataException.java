package io.bvb.smarthealthcare.backend.exception;

public class InvalidDataException extends ApplicationException {
    private static final String ERROR_MESSAGE = "Invalid data : %s";
    public InvalidDataException(String message) {
        super(message.isEmpty() ? String.format(ERROR_MESSAGE, message) : message);
    }
}
