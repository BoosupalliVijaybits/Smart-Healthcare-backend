package io.bvb.smarthealthcare.backend.exception;

public class NotificationNotFoundException extends NotFoundException {
    private static final String ERROR_MESSAGE = "Notification is not found : %s";
    public NotificationNotFoundException(String notificationId) {
        super(String.format(ERROR_MESSAGE, notificationId));
    }
}
