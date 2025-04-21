package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
    List<TimeSlot> findByDoctorId(Long doctorId);
    boolean existsByDoctorIdAndDateAndStartTime(Long doctorId, LocalDate date, LocalTime startTime);
    List<TimeSlot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    Optional<TimeSlot> findByDoctorIdAndDateAndStartTime(Long doctorId, LocalDate date, LocalTime startTime);
}