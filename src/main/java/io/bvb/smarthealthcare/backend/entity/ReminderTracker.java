package io.bvb.smarthealthcare.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reminder_tracker")
public class ReminderTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String appointmentId;

    private LocalDateTime reminderSentAt;
}

