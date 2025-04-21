package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.model.*;
import io.bvb.smarthealthcare.backend.service.PatientService;
import io.bvb.smarthealthcare.backend.service.PrescriptionService;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/patients")
public class PatientController {
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;

    public PatientController(PatientService patientService, final PrescriptionService prescriptionService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public List<PatientResponse> listPatients() {
        return patientService.listPatients();
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<StringResponse> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok().body(new StringResponse("Patient deleted successfully"));
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(patientService.bookAppointment(request));
    }

    @DeleteMapping("/cancel-appointment/{appointmentId}")
    public StringResponse cancelAppointment(@PathVariable String appointmentId) {
        return new StringResponse(patientService.cancelAppointment(appointmentId));
    }

    @GetMapping("/upcoming-appointments")
    public List<Appointment> getUpcomingAppointments() {
        return patientService.getUpcomingAppointments();
    }

    @GetMapping("/prescriptions")
    public List<PrescriptionResponse> getPrescriptions() {
        return prescriptionService.getPrescriptionsByPatient(CurrentUserData.getUser().getId());
    }
}