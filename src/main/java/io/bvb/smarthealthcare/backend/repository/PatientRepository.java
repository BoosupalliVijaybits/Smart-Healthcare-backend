package io.bvb.smarthealthcare.backend.repository;


import io.bvb.smarthealthcare.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    List<Patient> findAllByDeleted(boolean isDeleted);
    Optional<Patient> findByIdAndDeleted(Long patientId, Boolean isDeleted);
}