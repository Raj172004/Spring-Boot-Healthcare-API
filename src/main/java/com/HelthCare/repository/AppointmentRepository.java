package com.HelthCare.repository;

import com.HelthCare.model.Appointment;
import com.HelthCare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByDoctorAndAppointmentDateAndAppointmentTime(
            Doctor doctor, LocalDate appointmentDate, LocalTime appointmentTime);
    List<Appointment> findByDoctorDoctorId(Long doctorId);
    List<Appointment> findByAppointmentDate(LocalDate date);
}