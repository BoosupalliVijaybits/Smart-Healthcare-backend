package io.bvb.smarthealthcare.backend.entity;

import io.bvb.smarthealthcare.backend.constant.MedicationTime;
import io.bvb.smarthealthcare.backend.constant.PrescriptionStatus;
import io.bvb.smarthealthcare.backend.constant.TimeToTake;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    private String medicationName;
    private String dosage;
    private Set<MedicationTime> medicationTime;  // Morning, Afternoon, Night
    @Enumerated(EnumType.STRING)
    private TimeToTake timeToTake;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.IN_PROGRESS;
}

