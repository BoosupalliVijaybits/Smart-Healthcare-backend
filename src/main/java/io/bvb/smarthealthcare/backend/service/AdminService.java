package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.exception.DoctorNotFoundException;
import io.bvb.smarthealthcare.backend.exception.InvalidDataException;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public AdminService(DoctorRepository doctorRepository, NotificationService notificationService, EmailService emailService) {
        this.doctorRepository = doctorRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    public void approveDoctor(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        if (doctorOptional.isEmpty()) {
            LOGGER.error("Doctor not found : {}", doctorId);
            throw new DoctorNotFoundException(doctorId);
        }
        Doctor doctor = doctorOptional.get();
        if (doctor.getStatus() != DoctorStatus.PENDING) {
            LOGGER.error("Doctor status is not PENDING : Id : {}, Status : {}", doctorId, doctor.getStatus());
            throw new InvalidDataException("Doctor is not in pending state");
        }
        doctor.setStatus(DoctorStatus.APPROVED);
        doctorRepository.save(doctor);
        notificationService.sendNotification(doctorId, "doctor.approved", new Object[]{doctor.getFirstName()});
        // Send Email after approval
        emailService.sendDoctorApprovalEmail(doctor.getEmail(), doctor.getFirstName());
        LOGGER.info("Doctor approved successfully : Id : {}, Email : {}", doctorId, doctor.getEmail());
    }

    public void rejectDoctor(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        if (doctorOptional.isEmpty()) {
            LOGGER.error("Doctor not found : {}", doctorId);
            throw new DoctorNotFoundException(doctorId);
        }

        Doctor doctor = doctorOptional.get();
        if (doctor.getStatus() != DoctorStatus.PENDING) {
            LOGGER.error("Doctor status is not PENDING : Id : {}, Status : {}", doctorId, doctor.getStatus());
            throw new InvalidDataException("Doctor is not in pending state");
        }

        emailService.sendRejectionMail(doctor.getEmail(), doctor.getFirstName());
        doctorRepository.delete(doctor);
        LOGGER.info("Doctor rejected successfully. Id : {}, Email : {}", doctorId, doctor.getEmail());
    }
}

