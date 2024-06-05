package com.project.npp.service;

import java.util.Optional;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;

public interface UserEntityService {

	// Add user
	public UserEntity addUserEntity(UserEntity user);

	// Update role
	public String updateRole(Integer userId, Role role);

	// Find by username
	public Optional<UserEntity> findByUsername(String username);

	// Exists by username
	public Boolean existsByUsername(String username);

	// Find by role
	public Optional<UserEntity> findByRole(ERole role);
}
