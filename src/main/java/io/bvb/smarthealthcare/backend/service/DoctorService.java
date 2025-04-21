package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Feedback;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.DoctorNotFoundException;
import io.bvb.smarthealthcare.backend.exception.InvalidDataException;
import io.bvb.smarthealthcare.backend.model.*;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.FeedbackRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorService.class);
    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final FeedbackRepository feedbackRepository;

    public DoctorService(AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository, final FeedbackRepository feedbackRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public List<DoctorResponse> listDoctors() {
        List<Doctor> doctors = doctorRepository.findAllByDeleted(Boolean.FALSE);
        return convertDoctorsToResponse(doctors);
    }

    private List<DoctorResponse> convertDoctorsToResponse(List<Doctor> doctors) {
        return doctors.stream().map(DoctorResponse::convertDoctorToResponse).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void deleteDoctor(Long id) {
        final Doctor doctor = getDoctorById(id);
        doctor.setDeleted(Boolean.TRUE);
        doctorRepository.save(doctor);
    }

    public DoctorResponse getDoctor(Long id) {
        return DoctorResponse.convertDoctorToResponse(getDoctorById(id));
    }

    public List<Appointment> getTodaysAppointments(Long doctorId) {
        return appointmentRepository.findAppointmentsByDoctorIdAndDate(doctorId, LocalDate.now());
    }

    public List<Appointment> getUpcomingAppointments(Long doctorId) {
        return appointmentRepository.findUpcomingAppointmentsByDoctorIdAndDate(doctorId, LocalDate.now());
    }

    @Transactional
    public TimeSlotResponse allocateTimeSlot(TimeSlotRequest request) {
        final UserResponse userResponse = CurrentUserData.getUser();
        if (LocalDate.now().isAfter(request.getDate())) {
            LOGGER.error("Invalid date format :: {}", request.getDate());
            throw new InvalidDataException("Invalid date format");
        }
        if (LocalDate.now().isEqual(request.getDate()) && LocalTime.now().isAfter(request.getStartTime())) {
            LOGGER.error("Invalid start-time");
            throw new InvalidDataException("Start Time should not be previous :: " + request.getStartTime());
        }
        if (LocalDate.now().isEqual(request.getDate()) && LocalTime.now().isAfter(request.getEndTime())) {
            LOGGER.error("Invalid end-time");
            throw new InvalidDataException("End Time should not be previous :: " + request.getEndTime());
        }
        if (request.getDuration() <= 0 || request.getDuration() > 60) {
            throw new InvalidDataException("Invalid slot duration. Must be between 1 and 60 minutes.");
        }
        Doctor doctor = getDoctorById(userResponse.getId());

        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime current = request.getStartTime();

        while (current.plusMinutes(request.getDuration()).isBefore(request.getEndTime()) || current.plusMinutes(request.getDuration()).equals(request.getEndTime())) {
            if (timeSlotRepository.existsByDoctorIdAndDateAndStartTime(userResponse.getId(), request.getDate(), current)) {
                throw new RuntimeException("Time slot already allocated at " + current);
            }
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDoctor(doctor);
            timeSlot.setDate(request.getDate());
            timeSlot.setStartTime(current);
            timeSlot.setEndTime(current.plusMinutes(request.getDuration()));
            timeSlot.setDuration(request.getDuration());
            timeSlot.setClinicName(request.getClinicName());
            timeSlots.add(timeSlot);
            current = current.plusMinutes(request.getDuration());
        }
        timeSlotRepository.saveAll(timeSlots);
        return getTimeSlotsByDoctorIdAndDate(doctor.getId(), request.getDate());
    }

    public TimeSlotResponse getTimeSlotsByDoctorId(Long doctorId) {
        final Doctor doctor = getDoctorById(doctorId);
        final List<TimeSlot> timeSlots = timeSlotRepository.findByDoctorId(doctorId);
        final TimeSlotResponse timeSlotResponse = new TimeSlotResponse();
        timeSlotResponse.setDoctorId(doctorId);
        timeSlotResponse.setSpecialization(doctor.getSpecialization());
        timeSlotResponse.setFirstName(doctor.getFirstName());
        timeSlotResponse.setLastName(doctor.getLastName());

        timeSlotResponse.setTimeSlots(this.convertTimeSlotsToResponse(timeSlots));
        return timeSlotResponse;
    }

    public TimeSlotResponse getTimeSlotsByDoctorIdAndDate(Long doctorId, LocalDate localDate) {
        final Doctor doctor = getDoctorById(doctorId);
        final List<TimeSlot> timeSlots = timeSlotRepository.findByDoctorIdAndDate(doctorId, localDate);
        final TimeSlotResponse timeSlotResponse = new TimeSlotResponse();
        timeSlotResponse.setDoctorId(doctorId);
        timeSlotResponse.setSpecialization(doctor.getSpecialization());
        timeSlotResponse.setFirstName(doctor.getFirstName());
        timeSlotResponse.setLastName(doctor.getLastName());

        timeSlotResponse.setTimeSlots(this.convertTimeSlotsToResponse(timeSlots));
        return timeSlotResponse;
    }

    private List<io.bvb.smarthealthcare.backend.model.TimeSlot> convertTimeSlotsToResponse(List<TimeSlot> timeSlots) {
        return timeSlots.stream().map(timeSlot -> {
            final io.bvb.smarthealthcare.backend.model.TimeSlot timeSlot1 = new io.bvb.smarthealthcare.backend.model.TimeSlot();
            timeSlot1.setId(timeSlot.getId());
            timeSlot1.setStartTime(timeSlot.getStartTime());
            timeSlot1.setEndTime(timeSlot.getEndTime());
            timeSlot1.setBooked(timeSlot.isBooked());
            timeSlot1.setClinicName(timeSlot.getClinicName());
            timeSlot1.setDate(timeSlot.getDate());
            return timeSlot1;
        }).sorted(Comparator.comparing(io.bvb.smarthealthcare.backend.model.TimeSlot::getDate).thenComparing(io.bvb.smarthealthcare.backend.model.TimeSlot::getStartTime)).collect(Collectors.toList());
    }


    public List<DoctorResponse> searchDoctorsByClinicNameOrSpecialization(String searchString) {
        final List<Doctor> doctors = doctorRepository.findByClinicNameOrSpecializationContainingIgnoreCase(searchString, searchString);
        doctors.sort((d1, d2) -> Double.compare(getAverageRating(d2), getAverageRating(d1)));
        return convertDoctorsToResponse(doctors);
    }

    private double getAverageRating(Doctor doctor) {
        List<Feedback> feedbackList = feedbackRepository.findByDoctorIdOrderByRatingDesc(doctor.getId());
        if (feedbackList.isEmpty()) return 0.0;
        return feedbackList.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
    }

    public List<DoctorResponse> listDoctors(DoctorStatus doctorStatus) {
        if (doctorStatus != null) {
            return convertDoctorsToResponse(doctorRepository.findDoctorsByStatusAndDeleted(doctorStatus, Boolean.FALSE));
        }
        return listDoctors();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findByIdAndDeleted(id, Boolean.FALSE).orElseThrow(() -> new DoctorNotFoundException(id));
    }
}
