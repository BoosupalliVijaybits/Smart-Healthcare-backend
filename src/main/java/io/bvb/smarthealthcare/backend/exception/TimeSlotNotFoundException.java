package io.bvb.smarthealthcare.backend.exception;

public class TimeSlotNotFoundException extends NotFoundException {
    private static final String TIME_SLOT_NOT_FOUND_EXCEPTION_MESSAGE = "Time slot not found : %s";
    public TimeSlotNotFoundException(String id) {
        super(String.format(TIME_SLOT_NOT_FOUND_EXCEPTION_MESSAGE, id));
    }
}
