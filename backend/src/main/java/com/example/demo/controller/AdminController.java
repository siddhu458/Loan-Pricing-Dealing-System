package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.dto.user.UserCreateDTO;
import com.example.demo.dto.user.UserResponseDTO;
import com.example.demo.dto.user.AuditTrailDTO;
import com.example.demo.model.User;
import com.example.demo.services.UserService;
import com.example.demo.services.AuditService;
import com.example.demo.model.Audit;
import com.example.demo.model.AuditAction;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final UserService userService;
	private final AuditService auditService;
	
	public AdminController(UserService userService, AuditService auditService) {
		this.userService = userService;
		this.auditService = auditService;
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<UserResponseDTO>> listUsers() {
		List<User> users = userService.findAll();
		List<UserResponseDTO> userDTOs = users.stream()
			.map(this::convertToUserResponseDTO)
			.collect(Collectors.toList());
		return ResponseEntity.ok(userDTOs);
	}
	
	@PostMapping("/users")
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
		userCreateDTO.validate();
		
		User user = new User();
		user.setEmail(userCreateDTO.getEmail());
		user.setPassword(userCreateDTO.getPassword());
		user.setRole(userCreateDTO.getRole());
		
		User createdUser = userService.create(user);
		
		// Log user creation
		auditService.logAudit(
			createdUser.getId(),
			"admin@system.com",
			AuditAction.CREATED,
			"User created by admin"
		);
		
		return ResponseEntity.ok(convertToUserResponseDTO(createdUser));
	}
	
	@PutMapping("/users/{id}/status")
	public ResponseEntity<UserResponseDTO> updateStatus(
			@PathVariable String id,
			@RequestParam boolean active
			) {
		User user = userService.updateStatus(id, active);
		
		// Log status update
		auditService.logAudit(
			id,
			"admin@system.com", // This should come from authenticated user
			AuditAction.UPDATED,
			"User status updated to " + active
		);
		
		return ResponseEntity.ok(convertToUserResponseDTO(user));
	}
	
	@GetMapping("/audit-trail")
	public ResponseEntity<List<AuditTrailDTO>> getAuditTrail() {
		List<Audit> audits = auditService.getAllAuditLogs();
		List<AuditTrailDTO> auditDTOs = audits.stream()
			.map(this::convertToAuditTrailDTO)
			.collect(Collectors.toList());
		return ResponseEntity.ok(auditDTOs);
	}
	
	@GetMapping("/audit-trail/{loanId}")
	public ResponseEntity<List<AuditTrailDTO>> getAuditTrailForLoan(@PathVariable String loanId) {
		List<Audit> audits = auditService.getAuditLogsForLoan(loanId);
		List<AuditTrailDTO> auditDTOs = audits.stream()
			.map(this::convertToAuditTrailDTO)
			.collect(Collectors.toList());
		return ResponseEntity.ok(auditDTOs);
	}
	
	private UserResponseDTO convertToUserResponseDTO(User user) {
		return UserResponseDTO.builder()
			.id(user.getId())
			.email(user.getEmail())
			.role(user.getRole())
			.active(user.isActive())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.build();
	}
	
	private AuditTrailDTO convertToAuditTrailDTO(Audit audit) {
		return AuditTrailDTO.builder()
			.id(audit.getId())
			.loanId(audit.getLoanId())
			.userEmail(audit.getBy())
			.action(audit.getAction().toString())
			.comments(audit.getComments())
			.timestamp(audit.getTimestamp())
			.build();
	}
}
