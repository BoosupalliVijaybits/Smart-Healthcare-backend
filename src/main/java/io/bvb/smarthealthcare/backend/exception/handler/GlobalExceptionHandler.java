package io.bvb.smarthealthcare.backend.exception.handler;

import io.bvb.smarthealthcare.backend.exception.ApplicationException;
import io.bvb.smarthealthcare.backend.exception.NotFoundException;
import io.bvb.smarthealthcare.backend.exception.PermissionDeniedException;
import io.bvb.smarthealthcare.backend.model.ErrorMessage;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorMessage> handleApplicationException(ApplicationException applicationException) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(applicationException.getMessage());
        errorMessage.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(NotFoundException userNotFoundException) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(userNotFoundException.getMessage());
        errorMessage.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ErrorMessage> handlePermissionDeniedException(PermissionDeniedException permissionDeniedException) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(permissionDeniedException.getMessage());
        errorMessage.setStatus(HttpStatus.FORBIDDEN);
        return new ResponseEntity(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorMessage> handleDateTimeParseException(final DateTimeParseException dateTimeParseException) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(dateTimeParseException.getMessage());
        errorMessage.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
