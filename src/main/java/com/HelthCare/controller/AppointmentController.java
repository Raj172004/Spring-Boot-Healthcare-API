package com.HelthCare.controller;

import com.HelthCare.dto.AppointmentDto;
import com.HelthCare.dto.AppointmentRequestDto;
import com.HelthCare.model.User;
import com.HelthCare.repository.UserRepository;
import com.HelthCare.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

    // Added to resolve the ClassCastException
    @Autowired
    private UserRepository userRepository;

	@PreAuthorize("hasRole('DOCTOR')")
	@GetMapping
	public List<AppointmentDto> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}

	@PreAuthorize("hasRole('DOCTOR') or (hasRole('PATIENT') and @userAuthorization.isPatientAppointmentOwner(#id))")
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
		Optional<AppointmentDto> appointmentDto = appointmentService.getAppointmentById(id);
		return appointmentDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// This method is now updated
	@PreAuthorize("hasRole('PATIENT')")
	@PostMapping
	public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentRequestDto requestDto) {
	    try {
	        // Get the authenticated user's username
	        String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	        
	        // Use the username to find the User entity from the database
	        User user = userRepository.findByUsername(currentUsername)
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));

	        // Use the authenticated user's ID as the patientId
	        AppointmentDto newAppointment = appointmentService.createAppointment(requestDto, user.getId());
	        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().build();
	    }
	}

	@PreAuthorize("hasRole('DOCTOR') or (hasRole('PATIENT') and @userAuthorization.isPatientAppointmentOwner(#id))")
	@PutMapping("/{id}")
	public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id,
			@RequestBody AppointmentRequestDto requestDto) {
		try {
			Optional<AppointmentDto> appointmentDto = appointmentService.updateAppointment(id, requestDto);
			return appointmentDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PreAuthorize("hasRole('DOCTOR') or (hasRole('PATIENT') and @userAuthorization.isPatientAppointmentOwner(#id))")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAppointment(@PathVariable Long id) {
		appointmentService.deleteAppointment(id);
	}
}