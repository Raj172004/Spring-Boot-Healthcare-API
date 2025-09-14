package com.HelthCare.service;

import com.HelthCare.dto.AppointmentDto;
import com.HelthCare.dto.AppointmentRequestDto;
import com.HelthCare.dto.PatientDto;
import com.HelthCare.dto.DoctorDto;
import com.HelthCare.model.Appointment;
import com.HelthCare.model.Doctor;
import com.HelthCare.model.Patient;
import com.HelthCare.repository.AppointmentRepository;
import com.HelthCare.repository.DoctorRepository;
import com.HelthCare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired 
    private NotificationService notificationService;

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<AppointmentDto> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(this::convertToDto);
    }
    
    // **FULL UPDATED METHOD**
    public AppointmentDto createAppointment(AppointmentRequestDto requestDto, Long userId) {
        Optional<Patient> patient = patientRepository.findById(userId);
        Optional<Doctor> doctor = doctorRepository.findById(requestDto.getDoctorId());

        if (patient.isPresent() && doctor.isPresent()) {
            // Step 1: Validation for overlapping appointments
            boolean appointmentExists = appointmentRepository.findByDoctorAndAppointmentDateAndAppointmentTime(
                doctor.get(),
                requestDto.getAppointmentDate(),
                requestDto.getAppointmentTime()
            ).isPresent();

            if (appointmentExists) {
                throw new IllegalArgumentException("An appointment for this doctor is already scheduled at the same date and time.");
            }
            
            // Step 2: Create and save the new appointment
            Appointment appointment = new Appointment();
            appointment.setAppointmentDate(requestDto.getAppointmentDate());
            appointment.setAppointmentTime(requestDto.getAppointmentTime());
            appointment.setReason(requestDto.getReason());
            appointment.setPatient(patient.get());
            appointment.setDoctor(doctor.get());

            Appointment savedAppointment = appointmentRepository.save(appointment);

            // Step 3: Send confirmation email
            String emailBody = String.format("Dear %s %s, your appointment with Dr. %s has been confirmed for %s at %s. Reason: %s",
                patient.get().getFirstName(),
                patient.get().getLastName(),
                doctor.get().getLastName(),
                savedAppointment.getAppointmentDate(),
                savedAppointment.getAppointmentTime(),
                savedAppointment.getReason()
            );
            notificationService.sendAppointmentConfirmation(patient.get().getEmail(), "Appointment Confirmed", emailBody);

            return convertToDto(savedAppointment);
        } else {
            throw new IllegalArgumentException("Patient or Doctor not found.");
        }
    }
    
    // Corrected update method
    public Optional<AppointmentDto> updateAppointment(Long id, AppointmentRequestDto requestDto) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        
        if (existingAppointment.isPresent()) {
            Appointment appointment = existingAppointment.get();
            appointment.setAppointmentDate(requestDto.getAppointmentDate());
            appointment.setAppointmentTime(requestDto.getAppointmentTime());
            appointment.setReason(requestDto.getReason());
            
            // Only allow changing the doctor, not the patient owner
            Optional<Doctor> doctor = doctorRepository.findById(requestDto.getDoctorId());
            
            if (doctor.isPresent()) {
                appointment.setDoctor(doctor.get());
                return Optional.of(convertToDto(appointmentRepository.save(appointment)));
            } else {
                throw new IllegalArgumentException("Doctor ID not found.");
            }
        } else {
            return Optional.empty();
        }
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
    
    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setReason(appointment.getReason());
        
        // Convert Patient entity to its DTO
        Patient patient = appointment.getPatient();
        if (patient != null) {
            PatientDto patientDto = new PatientDto();
            patientDto.setPatientId(patient.getPatientId());
            patientDto.setFirstName(patient.getFirstName());
            patientDto.setLastName(patient.getLastName());
            patientDto.setDateOfBirth(patient.getDateOfBirth());
            patientDto.setPhoneNumber(patient.getPhoneNumber());
            patientDto.setEmail(patient.getEmail());
            dto.setPatient(patientDto);
        }

        // Convert Doctor entity to its DTO
        Doctor doctor = appointment.getDoctor();
        if (doctor != null) {
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setDoctorId(doctor.getDoctorId());
            doctorDto.setFirstName(doctor.getFirstName());
            doctorDto.setLastName(doctor.getLastName());
            doctorDto.setSpecialization(doctor.getSpecialization());
            doctorDto.setPhoneNumber(doctor.getPhoneNumber());
            doctorDto.setEmail(doctor.getEmail());
            dto.setDoctor(doctorDto);
        }
        
        return dto;
    }
}