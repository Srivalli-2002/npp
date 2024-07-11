package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Operator;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private OperatorService operatorService;

    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    private UserEntity userEntity;
    private Role role;
    private Operator operator;

    @BeforeEach
    public void setup() {
        role = new Role();
        role.setRoleId(1);
        role.setName(ERole.ROLE_USER);

        operator = new Operator();
        operator.setOperatorId(1);
        operator.setOperatorName("prodapt");

        userEntity = new UserEntity();
        userEntity.setUserId(1);
        userEntity.setUsername("testuser");
        userEntity.setRole(role);
    }

    @Test
    public void updateRole_existingUser_updatesRole() throws RoleNotFoundException, OperatorNotFoundException {
        Role complianceRole = new Role();
        complianceRole.setName(ERole.ROLE_COMPLIANCE_OFFICER);
        Role customerSupportRole = new Role();
        customerSupportRole.setName(ERole.ROLE_CUSTOMER_SERVICE);

        when(roleService.findRoleByName(ERole.ROLE_COMPLIANCE_OFFICER)).thenReturn(Optional.of(complianceRole));
        when(roleService.findRoleByName(ERole.ROLE_CUSTOMER_SERVICE)).thenReturn(Optional.of(customerSupportRole));
        when(operatorService.getOperatorByOperatorName("prodapt")).thenReturn(operator);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        // Change the role to complianceRole to check if the operator is set correctly
        String result = userEntityService.updateRole("testuser", complianceRole);

        assertEquals("Role Updated Successfully!!!", result);
        verify(userRepository, times(1)).save(userEntity);
        assertEquals(complianceRole, userEntity.getRole());
        assertEquals(operator, userEntity.getOperator());
    }

    @Test
    public void updateRole_nonExistingUser_returnsError() throws RoleNotFoundException, OperatorNotFoundException {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        String result = userEntityService.updateRole("testuser", role);

        assertEquals("Cannot Update Role !!!", result);
    }

    @Test
    public void addUserEntity_savesUser() {
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity result = userEntityService.addUserEntity(userEntity);

        assertEquals(userEntity, result);
        verify(userRepository).save(userEntity);
    }

    @Test
    public void findByUsername_existingUser_returnsUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userEntityService.findByUsername("testuser");

        assertEquals(Optional.of(userEntity), result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void findByUsername_nonExistingUser_returnsEmpty() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Optional<UserEntity> result = userEntityService.findByUsername("testuser");

        assertEquals(Optional.empty(), result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void existsByUsername_existingUser_returnsTrue() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        Boolean result = userEntityService.existsByUsername("testuser");

        assertTrue(result);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    public void existsByUsername_nonExistingUser_returnsFalse() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        Boolean result = userEntityService.existsByUsername("testuser");

        assertFalse(result);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    public void findByRole_existingRole_returnsUser() {
        when(userRepository.findByRole(ERole.ROLE_USER)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userEntityService.findByRole(ERole.ROLE_USER);

        assertEquals(Optional.of(userEntity), result);
        verify(userRepository).findByRole(ERole.ROLE_USER);
    }

    @Test
    public void findByRole_nonExistingRole_returnsEmpty() {
        when(userRepository.findByRole(ERole.ROLE_USER)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userEntityService.findByRole(ERole.ROLE_USER);

        assertEquals(Optional.empty(), result);
        verify(userRepository).findByRole(ERole.ROLE_USER);
    }

    @Test
    public void getAllUserEntities_returnsAllUsers() {
        List<UserEntity> allUsers = Arrays.asList(userEntity);
        when(userRepository.findAll()).thenReturn(allUsers);

        List<UserEntity> result = userEntityService.getAllUserEntities();

        assertEquals(allUsers, result);
        verify(userRepository).findAll();
    }
}
