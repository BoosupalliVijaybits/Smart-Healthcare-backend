package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByDeleted(Boolean isDeleted);
    Optional<Doctor> findByIdAndDeleted(Long id, Boolean isDeleted);
    boolean existsByLicenseNumber(String licenseNumber);

    List<Doctor> findByClinicNameOrSpecializationContainingIgnoreCase(String clinicName, String specialization);

    List<Doctor> findDoctorsByStatusAndDeleted(DoctorStatus status, Boolean isDeleted);
}
