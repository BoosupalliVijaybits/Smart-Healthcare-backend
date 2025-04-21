package io.bvb.smarthealthcare.backend.exception;

public class TimeSlotOccupiedException extends ApplicationException {
    private static final String  ERROR_MESSAGE = "This time slot is already booked.";
    public TimeSlotOccupiedException() {
        super(ERROR_MESSAGE);
    }
}
