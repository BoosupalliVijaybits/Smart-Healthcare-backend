package io.bvb.smarthealthcare.backend.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeSlot {
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String clinicName;
    private LocalDate date;
    private boolean isBooked;
}
