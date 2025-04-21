package io.bvb.smarthealthcare.backend.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationResponse {
    private String id;
    private Long userId;
    private String message;
    private LocalDateTime date;
    private boolean isRead = false;
}
