package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.repositories.RoleRepository;

class RoleServiceImplTest {

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private RoleServiceImpl roleService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindRoleByNameSuccess() throws RoleNotFoundException {
		ERole roleName = ERole.ROLE_USER;
		Role role = new Role(4, roleName);

		when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

		Optional<Role> result = roleService.findRoleByName(roleName);

		assertTrue(result.isPresent());
		assertEquals(role, result.get());
		verify(roleRepository, times(1)).findByName(roleName);
	}

	@Test
	void testFindRoleByNameNotFound() throws RoleNotFoundException {
		ERole roleName = ERole.ROLE_USER;

		when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

		Optional<Role> result = roleService.findRoleByName(roleName);

		assertFalse(result.isPresent());
		verify(roleRepository, times(1)).findByName(roleName);
	}

	@Test
	void testFindRoleByIdSuccess() throws RoleNotFoundException {
		Integer roleId = 1;
		Role role = new Role(4, ERole.ROLE_USER);
		role.setId(roleId);

		when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

		Optional<Role> result = roleService.findRoleById(roleId);

		assertTrue(result.isPresent());
		assertEquals(role, result.get());
		verify(roleRepository, times(1)).findById(roleId);
	}

	@Test
	void testFindRoleByIdNotFound() throws RoleNotFoundException {
		Integer roleId = 1;

		when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

		Optional<Role> result = roleService.findRoleById(roleId);

		assertFalse(result.isPresent());
		verify(roleRepository, times(1)).findById(roleId);
	}

}
