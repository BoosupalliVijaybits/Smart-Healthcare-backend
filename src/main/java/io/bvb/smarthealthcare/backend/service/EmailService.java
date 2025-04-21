package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.model.GetInTouchRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String mailFromAddress;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEMail(String to, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", name);
        // Load and process the HTML template
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("welcome_email", context);

        helper.setTo(to);
        helper.setSubject("Welcome to our Smart Health Care Service");
        helper.setText(htmlContent, true); // true to enable HTML content
        helper.setFrom(mailFromAddress);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String username, String resetlink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("resetLink", resetlink);
        // Load and process the HTML template
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("reset_password_email", context);

        helper.setTo(to);
        helper.setSubject("Reset Your Password");
        helper.setText(htmlContent, true); // Enable HTML content
        helper.setFrom(mailFromAddress);
        mailSender.send(message);
    }


    public void sendDoctorApprovalEmail(String toEmail, String name) {
        Context context = new Context();
        context.setVariable("name", name);

        String body = templateEngine.process("approval-template.html", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(toEmail);
            helper.setSubject("Doctor Registration Approved");
            helper.setFrom(mailFromAddress);
            helper.setText(body, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendRejectionMail(String toEmail, String username) {
        Context context = new Context();
        context.setVariable("username", username);
        String htmlContent = templateEngine.process("doctor_rejection_mail.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Doctor Registration Rejected");
            helper.setText(htmlContent, true);
            helper.setFrom(mailFromAddress);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendAppointmentConfirmationmail(User patient, Appointment appointment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariable("patientName", patient.getFirstName());
        context.setVariable("doctorName", appointment.getTimeSlot().getDoctor().getFirstName());
        context.setVariable("appointmentDate", appointment.getTimeSlot().getDate().toString());
        context.setVariable("appointmentTime", appointment.getTimeSlot().getStartTime().toString());

        String htmlContent = templateEngine.process("Appointments/appointment-confirmation.html", context);

        helper.setTo(patient.getEmail());
        helper.setSubject("Appointment Confirmation");
        helper.setText(htmlContent, true);
        helper.setFrom(mailFromAddress);

        mailSender.send(message);
    }

    public void sendCancelledByPatientEmails(Patient patient, Doctor doctor, Appointment appointment) throws MessagingException {
        // Patient Email
        MimeMessage patientMsg = mailSender.createMimeMessage();
        MimeMessageHelper patientHelper = new MimeMessageHelper(patientMsg, true);

        Context patientContext = new Context();
        patientContext.setVariable("patientName", appointment.getPatient().getFirstName());
        patientContext.setVariable("doctorName", appointment.getTimeSlot().getDoctor().getFirstName());
        patientContext.setVariable("appointmentDate", appointment.getTimeSlot().getDate().toString());
        patientContext.setVariable("appointmentTime", appointment.getTimeSlot().getStartTime().toString());

        String patientHtml = templateEngine.process("Appointments/cancelled-by-patient-patient.html", patientContext);

        patientHelper.setTo(patient.getEmail());
        patientHelper.setSubject("Your Appointment Has Been Cancelled");
        patientHelper.setText(patientHtml, true);
        patientHelper.setFrom(mailFromAddress);

        mailSender.send(patientMsg);

        // Doctor Email
        MimeMessage doctorMsg = mailSender.createMimeMessage();
        MimeMessageHelper doctorHelper = new MimeMessageHelper(doctorMsg, true);

        Context doctorContext = new Context();
        doctorContext.setVariable("doctorName", appointment.getTimeSlot().getDoctor().getFirstName());
        doctorContext.setVariable("patientName", appointment.getPatient().getFirstName());
        doctorContext.setVariable("appointmentDate", appointment.getTimeSlot().getDate().toString());
        doctorContext.setVariable("appointmentTime", appointment.getTimeSlot().getStartTime().toString());

        String doctorHtml = templateEngine.process("Appointments/cancelled-by-patient-doctor.html", doctorContext);

        doctorHelper.setTo(doctor.getEmail());
        doctorHelper.setSubject("Appointment Cancelled by Patient");
        doctorHelper.setText(doctorHtml, true);
        doctorHelper.setFrom(mailFromAddress);

        mailSender.send(doctorMsg);
    }

    public void sendGetInTouchEmail(final GetInTouchRequest getInTouchRequest) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // Load and process the HTML template
        Context context = new Context();
        context.setVariable("name", getInTouchRequest.getName());
        context.setVariable("email", getInTouchRequest.getEmail());
        context.setVariable("mobile", getInTouchRequest.getMobile());
        context.setVariable("subject", getInTouchRequest.getSubject());
        context.setVariable("message", getInTouchRequest.getMessage());
        String htmlContent = templateEngine.process("get-in-touch-email-template", context);

        helper.setTo(mailFromAddress);
        helper.setSubject(getInTouchRequest.getSubject());
        helper.setText(htmlContent, true); // true to enable HTML content
        helper.setFrom(mailFromAddress);

        mailSender.send(message);
    }

    public void sendAppointmentNotification(final String subject, final Map<String, String> data) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // Load and process the HTML template
        Context context = new Context();
        context.setVariable("patientName", data.get("patientName"));
        context.setVariable("appointmentTime", data.get("appointmentTime"));
        context.setVariable("doctorName", data.get("doctorName"));
        context.setVariable("clinicName", data.get("clinicName"));
        String htmlContent = templateEngine.process("appointment_notification", context);

        helper.setTo(mailFromAddress);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true to enable HTML content
        helper.setFrom(mailFromAddress);
        mailSender.send(message);
    }
}


