package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.constant.PrescriptionStatus;
import io.bvb.smarthealthcare.backend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByDoctorId(Long doctorId);

    List<Prescription> findByEndDateBeforeAndStatus(LocalDate date, PrescriptionStatus status);
}

