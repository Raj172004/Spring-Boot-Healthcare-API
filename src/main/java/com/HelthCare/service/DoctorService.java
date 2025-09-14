package com.HelthCare.service;

import com.HelthCare.dto.DoctorDto;
import com.HelthCare.model.Doctor;
import com.HelthCare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // You must update this method to return a DTO
    public Optional<DoctorDto> getDoctorById(Long id) {
        return doctorRepository.findById(id).map(this::convertToDto);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> updateDoctor(Long id, Doctor updatedDoctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(id);

        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            doctor.setFirstName(updatedDoctor.getFirstName());
            doctor.setLastName(updatedDoctor.getLastName());
            doctor.setSpecialization(updatedDoctor.getSpecialization());
            doctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
            doctor.setEmail(updatedDoctor.getEmail());
            
            return Optional.of(doctorRepository.save(doctor));
        } else {
            return Optional.empty();
        }
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // New method to convert a Doctor entity to a DoctorDto
    private DoctorDto convertToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setEmail(doctor.getEmail());
        return dto;
    }
}