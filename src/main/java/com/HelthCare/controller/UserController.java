package com.HelthCare.controller;

import com.HelthCare.dto.UserRegistrationDto;
import com.HelthCare.model.Doctor;
import com.HelthCare.model.Patient;
import com.HelthCare.model.User;
import com.HelthCare.repository.DoctorRepository;
import com.HelthCare.repository.PatientRepository;
import com.HelthCare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());

        userRepository.save(user);

        if ("PATIENT".equalsIgnoreCase(user.getRole())) {
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setEmail(registrationDto.getEmail());
            // Populate all fields from the DTO
            patient.setFirstName(registrationDto.getFirstName());
            patient.setLastName(registrationDto.getLastName());
            patient.setDateOfBirth(registrationDto.getDateOfBirth());
            patient.setPhoneNumber(registrationDto.getPhoneNumber());
            patientRepository.save(patient);
        } else if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setEmail(registrationDto.getEmail());
            // Populate all fields from the DTO
            doctor.setFirstName(registrationDto.getFirstName());
            doctor.setLastName(registrationDto.getLastName());
            doctor.setSpecialization(registrationDto.getSpecialization());
            doctor.setPhoneNumber(registrationDto.getPhoneNumber());
            doctorRepository.save(doctor);
        }

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
