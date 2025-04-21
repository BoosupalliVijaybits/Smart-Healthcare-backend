package io.bvb.smarthealthcare.backend.exception;

public class InvalidLoginUserTypeException extends ApplicationException {
    private static final String DEFAULT_MESSAGE = "Invalid Login User Type : %s";

    public InvalidLoginUserTypeException(String loginUserType) {
        super(String.format(DEFAULT_MESSAGE, loginUserType));
    }
}
