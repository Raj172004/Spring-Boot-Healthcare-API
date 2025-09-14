package com.HelthCare.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDto {
    private Long appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
    private PatientDto patient;
    private DoctorDto doctor;

    public Long getAppointmentId() { return this.appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public LocalDate getAppointmentDate() { return this.appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getAppointmentTime() { return this.appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getReason() { return this.reason; }
    public void setReason(String reason) { this.reason = reason; }

    // Corrected setters to accept DTO objects
    public PatientDto getPatient() { return this.patient; }
    public void setPatient(PatientDto patient) { this.patient = patient; }
    public DoctorDto getDoctor() { return this.doctor; }
    public void setDoctor(DoctorDto doctor) { this.doctor = doctor; }
}