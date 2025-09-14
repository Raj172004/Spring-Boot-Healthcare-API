package com.HelthCare.controller;

import com.HelthCare.dto.PatientDto;
import com.HelthCare.model.Patient;
import com.HelthCare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Only a doctor can view all patients
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping
    public List<PatientDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    // A patient can only view their own details; a doctor can view any patient's details
    @PreAuthorize("hasRole('DOCTOR') or (hasRole('PATIENT') and @userAuthorization.isPatient(#id))")
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        Optional<PatientDto> patientDto = patientService.getPatientById(id);
        return patientDto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // A patient can only update their own details; a doctor can update any patient's details
    @PreAuthorize("hasRole('DOCTOR') or (hasRole('PATIENT') and @userAuthorization.isPatient(#id))")
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        Optional<PatientDto> patientDto = patientService.updatePatient(id, updatedPatient);
        return patientDto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // Only a doctor can delete a patient record
    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}