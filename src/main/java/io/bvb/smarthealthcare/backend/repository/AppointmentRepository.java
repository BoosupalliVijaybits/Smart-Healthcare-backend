package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    @Query("SELECT appointment FROM Appointment appointment, TimeSlot timeslot WHERE appointment.timeSlot.id = timeslot.id AND timeslot.doctor.id = :doctorId AND timeslot.date = :date")
    List<Appointment> findAppointmentsByDoctorIdAndDate(@Param("doctorId") Long doctorId, LocalDate date);

    @Query("SELECT appointment FROM Appointment appointment, TimeSlot timeslot WHERE appointment.timeSlot.id = timeslot.id AND timeslot.doctor.id = :doctorId AND timeslot.date >= :date")
    List<Appointment> findUpcomingAppointmentsByDoctorIdAndDate(@Param("doctorId") Long doctorId, LocalDate date);

    @Query("SELECT appointment FROM Appointment appointment, TimeSlot timeslot WHERE appointment.timeSlot.id = timeslot.id and timeslot.doctor.id = :doctorId AND appointment.patient.id = :patientId")
    Optional<Appointment> findByDoctorIdAndPatientId(Long doctorId, Long patientId);

    @Query("SELECT appointment FROM Appointment appointment, Patient patient, TimeSlot timeslot WHERE appointment.patient.id = patient.id AND timeslot.date >= :date AND patient.id = :patientId AND appointment.isCancelled = FALSE")
    List<Appointment> findUpcomingAppointments(@Param("patientId") Long patientId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE " +
            "a.timeSlot.date = :date " +
            "AND a.timeSlot.startTime BETWEEN :start AND :end")
    List<Appointment> findUpcomingAppointments(
            @Param("date") LocalDate date,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end
    );
}
