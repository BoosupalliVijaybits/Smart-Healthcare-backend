package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.AppointmentNotFoundException;
import io.bvb.smarthealthcare.backend.exception.PatientNotFoundException;
import io.bvb.smarthealthcare.backend.exception.TimeSlotNotFoundException;
import io.bvb.smarthealthcare.backend.exception.TimeSlotOccupiedException;
import io.bvb.smarthealthcare.backend.model.AppointmentRequest;
import io.bvb.smarthealthcare.backend.model.AppointmentResponse;
import io.bvb.smarthealthcare.backend.model.PatientResponse;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);
    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public PatientService(AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository, PatientRepository patientRepository, EmailService emailService, NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.patientRepository = patientRepository;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    public List<PatientResponse> listPatients() {
        return PatientResponse.convertPatientsToPatientResponses(patientRepository.findAllByDeleted(Boolean.FALSE));
    }

    public PatientResponse getPatient(Long id) {
        return PatientResponse.convertPatientToPatientResponse(getPatientById(id));
    }

    @Transactional
    public void deletePatient(Long id) {
        final Patient patient = getPatientById(id);
        patient.setDeleted(Boolean.TRUE);
        patientRepository.save(patient);
    }

    public AppointmentResponse bookAppointment(final AppointmentRequest appointmentRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(appointmentRequest.getTimeSlotId()).orElseThrow(() -> {
            LOGGER.error("Time slot not found : {}", appointmentRequest.getTimeSlotId());
            return new TimeSlotNotFoundException(appointmentRequest.getTimeSlotId());
        });
        if (timeSlot.isBooked()) {
            LOGGER.error("Timeslot is already booked :: TimeSlot Id : {}", appointmentRequest.getTimeSlotId());
            throw new TimeSlotOccupiedException();
        }
        timeSlot.setBooked(true);
        timeSlotRepository.save(timeSlot);

        Appointment appointment = new Appointment();
        appointment.setPatient(patientRepository.findById(CurrentUserData.getUser().getId()).orElseThrow(() -> {
            LOGGER.error("Patient not found : {}", CurrentUserData.getUser().getId());
            return new PatientNotFoundException(CurrentUserData.getUser().getId());
        }));
        appointment.setTimeSlot(timeSlot);

        final Appointment savedAppointment = appointmentRepository.save(appointment);
        try {
            final Patient patient = appointment.getPatient();
            final Doctor doctor = appointment.getTimeSlot().getDoctor();
            notificationService.sendNotification(appointment.getPatient().getId(), "appointment.booked", new Object[]{patient.getFirstName(), doctor.getFirstName(), timeSlot.getClinicName(), timeSlot.getDate(), timeSlot.getStartTime(), timeSlot.getEndTime()});
            emailService.sendAppointmentConfirmationmail(appointment.getPatient(), savedAppointment);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
        return new AppointmentResponse(savedAppointment.getId());
    }

    public String cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlot.setBooked(false);
        timeSlotRepository.saveAndFlush(timeSlot);
        appointment.setCancelled(true);
        appointmentRepository.saveAndFlush(appointment);
        try {
            final Patient patient = appointment.getPatient();
            final Doctor doctor = appointment.getTimeSlot().getDoctor();
            notificationService.sendNotification(patient.getId(), "appointment.cancelled", new Object[]{patient.getFirstName(), doctor.getFirstName(), doctor.getClinicName(), timeSlot.getDate(), timeSlot.getStartTime()});
            emailService.sendCancelledByPatientEmails(appointment.getPatient(), appointment.getTimeSlot().getDoctor(), appointment);
        } catch (MessagingException e) {
            throw new RuntimeException("fail to send mail");
        }
        return "Appointment canceled successfully.";
    }

    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments(CurrentUserData.getUser().getId(), LocalDate.now());
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findByIdAndDeleted(id, Boolean.FALSE).orElseThrow(() -> new PatientNotFoundException(id));
    }
}