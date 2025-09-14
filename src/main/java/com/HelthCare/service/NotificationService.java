package com.HelthCare.service;

import com.HelthCare.model.Appointment;
import com.HelthCare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void sendAppointmentConfirmation(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com"); // Use your configured email
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
    public void sendDailyReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Appointment> upcomingAppointments = appointmentRepository.findByAppointmentDate(tomorrow);

        for (Appointment appt : upcomingAppointments) {
            String emailBody = String.format("Reminder: You have an appointment tomorrow, %s, at %s with Dr. %s.",
                appt.getAppointmentDate(),
                appt.getAppointmentTime(),
                appt.getDoctor().getLastName()
            );
            sendAppointmentConfirmation(appt.getPatient().getEmail(), "Appointment Reminder", emailBody);
        }
    }
}