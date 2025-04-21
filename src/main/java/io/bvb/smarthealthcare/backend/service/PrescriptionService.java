package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.AppointmentStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.Prescription;
import io.bvb.smarthealthcare.backend.exception.AppointmentNotFoundException;
import io.bvb.smarthealthcare.backend.model.PrescriptionResponse;
import io.bvb.smarthealthcare.backend.model.PrescriptionsRequest;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.PrescriptionRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final NotificationService notificationService;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, DoctorService doctorService, PatientService patientService, NotificationService notificationService, final AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.notificationService = notificationService;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void prescribeMedication(final PrescriptionsRequest prescriptionsRequest) {
        final Appointment appointment = appointmentRepository.findById(prescriptionsRequest.getAppointmentId()).orElseThrow(() -> new AppointmentNotFoundException(prescriptionsRequest.getAppointmentId()));
        final Doctor doctor = doctorService.getDoctorById(CurrentUserData.getUser().getId());
        final Patient patient = patientService.getPatientById(prescriptionsRequest.getPatientId());
        prescriptionsRequest.getPrescriptions().forEach(pr -> {
            final Prescription prescription = new Prescription();
            prescription.setDoctor(doctor);
            prescription.setPatient(patient);
            prescription.setMedicationName(pr.getMedicationName());
            prescription.setMedicationTime(pr.getMedicationTime());
            prescription.setDosage(pr.getDosage());
            prescription.setTimeToTake(pr.getTimeToTake());
            prescription.setStartDate(pr.getStartDate());
            prescription.setEndDate(pr.getEndDate());
            prescription.setAppointment(appointment);
            prescriptionRepository.save(prescription);
        });
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.saveAndFlush(appointment);
        notificationService.sendNotification(prescriptionsRequest.getPatientId(), "doctor.patient.prescription.added", new Object[]{patient.getFirstName(), doctor.getFirstName()});
        notificationService.sendNotification(prescriptionsRequest.getPatientId(), "appointment.completed", new Object[]{patient.getFirstName(), doctor.getFirstName(), appointment.getTimeSlot().getClinicName(), appointment.getTimeSlot().getDate(), appointment.getTimeSlot().getStartTime()});
    }

    public List<PrescriptionResponse> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream().map(PrescriptionResponse::convertEntityToResponse).toList();
    }

    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
}

