package com.HelthCare.service.security;

import com.HelthCare.model.Appointment;
import com.HelthCare.model.Patient;
import com.HelthCare.model.User;
import com.HelthCare.model.Doctor;
import com.HelthCare.repository.AppointmentRepository;
import com.HelthCare.repository.PatientRepository;
import com.HelthCare.repository.UserRepository;
import com.HelthCare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("userAuthorization")
public class UserAuthorization {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public boolean isPatientAppointmentOwner(Long appointmentId) {
        String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> user = userRepository.findByUsername(currentUsername);

        if (user.isEmpty()) {
            return false;
        }

        Optional<Patient> patient = patientRepository.findById(user.get().getId());

        if (patient.isEmpty()) {
            return false;
        }

        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        return appointment.isPresent() &&
               appointment.get().getPatient() != null &&
               appointment.get().getPatient().getPatientId().equals(patient.get().getPatientId());
    }

    public boolean isPatient(Long patientId) {
        String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> user = userRepository.findByUsername(currentUsername);
        if (user.isEmpty()) {
            return false;
        }
        Optional<Patient> patient = patientRepository.findById(patientId);
        return patient.isPresent() && patient.get().getUser().getId().equals(user.get().getId());
    }

    // Replace the existing isDoctor method with this corrected version
    public boolean isDoctor(Long doctorId) {
        try {
            String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            Optional<User> userOptional = userRepository.findByUsername(currentUsername);
    
            if (userOptional.isEmpty()) {
                System.out.println("isDoctor check failed: User not found for username " + currentUsername);
                return false;
            }
            
            Long authenticatedUserId = userOptional.get().getId();
            System.out.println("Authenticated User ID: " + authenticatedUserId);
    
            // Find the doctor by the ID from the URL
            Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
    
            if (doctorOptional.isEmpty()) {
                System.out.println("isDoctor check failed: Doctor not found for ID " + doctorId);
                return false;
            }
    
            // Compare the IDs
            Long doctorUserId = doctorOptional.get().getUser().getId();
            System.out.println("Doctor User ID from Path: " + doctorUserId);
    
            return doctorUserId.equals(authenticatedUserId);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during isDoctor check: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}