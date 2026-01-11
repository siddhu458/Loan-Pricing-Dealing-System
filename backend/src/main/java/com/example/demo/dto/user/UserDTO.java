package com.example.demo.dto.user;

import com.example.demo.model.Role;

import lombok.*;

@Data
@Builder
public class UserDTO {

	private String id;
	private String email;
	private Role role;
	private boolean active;
}
