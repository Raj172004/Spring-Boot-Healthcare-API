package com.HelthCare.controller;

import com.HelthCare.dto.DoctorDto;
import com.HelthCare.model.Doctor;
import com.HelthCare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // A dependency for UserAuthorization is needed
    // @Autowired
    // private UserAuthorization userAuthorization;
    
    // Only a patient can view all doctors to book an appointment
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // Both a patient and a doctor (for themselves) can view a single doctor's details
    @PreAuthorize("hasRole('PATIENT') or (hasRole('DOCTOR') and @userAuthorization.isDoctor(#id))")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        Optional<DoctorDto> doctorDto = doctorService.getDoctorById(id);
        return doctorDto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // The rest of the methods remain the same as they don't return the entity directly.
    @PreAuthorize("hasRole('DOCTOR') and @userAuthorization.isDoctor(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        Optional<Doctor> doctor = doctorService.updateDoctor(id, updatedDoctor);
        return doctor.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Only a doctor can delete a doctor record
    @PreAuthorize("hasRole('DOCTOR') and @userAuthorization.isDoctor(#id)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}