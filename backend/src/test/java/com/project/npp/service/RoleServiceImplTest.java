package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.repositories.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    public void setup() {
        role = new Role();
        role.setRoleId(7);
        role.setName(ERole.ROLE_DEFAULT);
    }

    @Test
    public void findRoleByName_existingRole_returnsRole() throws RoleNotFoundException {
        when(roleRepository.findByName(ERole.ROLE_DEFAULT)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleService.findRoleByName(ERole.ROLE_DEFAULT);

        assertEquals(Optional.of(role), foundRole);
        verify(roleRepository).findByName(ERole.ROLE_DEFAULT);
    }

    @Test
    public void findRoleByName_nonExistingRole_throwsRoleNotFoundException() {
        when(roleRepository.findByName(ERole.ROLE_DEFAULT)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.findRoleByName(ERole.ROLE_DEFAULT));
    }

    @Test
    public void findRoleById_existingRole_returnsRole() throws RoleNotFoundException {
        when(roleRepository.findById(7)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleService.findRoleById(7);

        assertEquals(Optional.of(role), foundRole);
        verify(roleRepository).findById(7);
    }

    @Test
    public void findRoleById_nonExistingRole_throwsRoleNotFoundException() {
        when(roleRepository.findById(7)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.findRoleById(7));
    }
}