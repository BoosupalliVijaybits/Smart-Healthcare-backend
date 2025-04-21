package io.bvb.smarthealthcare.backend.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeSlotResponse {
    List<TimeSlot> timeSlots;
    private Long doctorId;
    private String firstName;
    private String lastName;
    private String specialization;
}
