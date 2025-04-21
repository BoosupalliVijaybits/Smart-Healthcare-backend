package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.model.*;
import io.bvb.smarthealthcare.backend.service.DoctorService;
import io.bvb.smarthealthcare.backend.service.PrescriptionService;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;

    public DoctorController(DoctorService doctorService, PrescriptionService prescriptionService) {
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public List<DoctorResponse> listDoctors(@RequestParam(name = "status", required = false) DoctorStatus doctorStatus) {
        return doctorService.listDoctors(doctorStatus);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getDoctor(doctorId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(@RequestParam(name = "search", required = false) String searchString) {
        return ResponseEntity.ok(doctorService.searchDoctorsByClinicNameOrSpecialization(searchString));
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<StringResponse> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok().body(new StringResponse("Doctor deleted successfully"));
    }

    @GetMapping("/todays-appointments")
    public List<Appointment> getTodaysAppointments() {
        return doctorService.getTodaysAppointments(CurrentUserData.getUser().getId());
    }

    @GetMapping("/upcoming-appointments")
    public List<Appointment> getUpcomingAppointments() {
        return doctorService.getUpcomingAppointments(CurrentUserData.getUser().getId());
    }

    @PostMapping("/allocate-timeslots")
    public TimeSlotResponse allocateTimeSlot(@Valid @RequestBody TimeSlotRequest request) {
        return doctorService.allocateTimeSlot(request);
    }

    @GetMapping("/timeslots/{doctorId}/date/{date}")
    public TimeSlotResponse getTimeslotsByDoctorAndDate(@PathVariable Long doctorId, @PathVariable LocalDate date) {
        return doctorService.getTimeSlotsByDoctorIdAndDate(doctorId, date);
    }

    @GetMapping("/timeslots/{doctorId}")
    public TimeSlotResponse getTimeslotsByDoctor(@PathVariable Long doctorId) {
        return doctorService.getTimeSlotsByDoctorId(doctorId);
    }

    @PostMapping("/prescribe")
    public ResponseEntity<StringResponse> prescribeMedication(@Valid @RequestBody PrescriptionsRequest prescriptionsRequest, final HttpSession session) {
        prescriptionService.prescribeMedication(prescriptionsRequest);
        return ResponseEntity.ok(new StringResponse("Prescription added."));
    }
}
