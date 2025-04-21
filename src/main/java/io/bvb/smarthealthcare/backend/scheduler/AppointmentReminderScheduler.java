package io.bvb.smarthealthcare.backend.scheduler;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.ReminderTracker;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.ReminderTrackerRepository;
import io.bvb.smarthealthcare.backend.service.EmailService;
import io.bvb.smarthealthcare.backend.service.NotificationService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentReminderScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentReminderScheduler.class);
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ReminderTrackerRepository reminderTrackerRepository;

    @Scheduled(cron = "0 */15 * * * *") // every 15 mins
    public void sendReminders() {
        LOGGER.info("*******AppointmentReminderScheduler scheduler is running*******");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTimeStart = now.plusHours(2);
        LocalDateTime targetTimeEnd = targetTimeStart.plusMinutes(15);

        List<Appointment> appointments = appointmentRepository.findUpcomingAppointments(targetTimeStart.toLocalDate(), targetTimeStart.toLocalTime(), targetTimeEnd.toLocalTime());

        for (Appointment appt : appointments) {
            if (appt.isCancelled()) continue;

            // Avoid duplicate reminders using a tracker table
            if (reminderTrackerRepository.existsByAppointmentId(appt.getId())) continue;

            TimeSlot timeSlot = appt.getTimeSlot();
            String formattedTime = timeSlot.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
            String message = String.format("📅 Reminder: Your appointment is at %s with Dr. %s at %s.", formattedTime, timeSlot.getDoctor().getFirstName(), timeSlot.getClinicName());
            Map<String, String> data = new HashMap<>();
            data.put("patientName", appt.getPatient().getFirstName());
            data.put("appointmentTime", String.valueOf(appt.getTimeSlot().getStartTime()));
            data.put("doctorName", appt.getTimeSlot().getDoctor().getFirstName());
            data.put("clinicName", appt.getTimeSlot().getClinicName());
            try {
                emailService.sendAppointmentNotification(message, data);
            } catch (MessagingException e) {
                LOGGER.error("Error in processing : {}", e.getMessage());
            }

            // Save reminder sent record
            ReminderTracker tracker = new ReminderTracker();
            tracker.setAppointmentId(appt.getId());
            tracker.setReminderSentAt(LocalDateTime.now());
            reminderTrackerRepository.save(tracker);
            notificationService.sendNotification(appt.getPatient().getId(), "appointment.reminder", new Object[]{appt.getPatient().getFirstName(), appt.getTimeSlot().getDoctor().getFirstName(), appt.getTimeSlot().getClinicName(), appt.getTimeSlot().getDate(), appt.getTimeSlot().getStartTime(), appt.getTimeSlot().getEndTime()});
            notificationService.sendNotification(appt.getTimeSlot().getDoctor().getId(), "doctor.appointment.reminder", new Object[]{appt.getTimeSlot().getDoctor().getFirstName(),appt.getPatient().getFirstName(), appt.getTimeSlot().getClinicName(), appt.getTimeSlot().getDate(), appt.getTimeSlot().getStartTime(), appt.getTimeSlot().getEndTime()});
        }
        LOGGER.info("*******AppointmentReminderScheduler scheduler is running completed*******");
    }
}

