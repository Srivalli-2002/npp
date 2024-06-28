package com.project.npp.service;

import java.util.Optional;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.exceptions.RoleNotFoundException;

public interface RoleService {
	
	// Find role by name
	public Optional<Role> findRoleByName(ERole eRole) throws RoleNotFoundException;

	// Find role by id
	public Optional<Role> findRoleById(Integer id) throws RoleNotFoundException;
}