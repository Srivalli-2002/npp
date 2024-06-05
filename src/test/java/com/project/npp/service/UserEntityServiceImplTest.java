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
import com.project.npp.entities.UserEntity;
import com.project.npp.repositories.UserRepository;

class UserEntityServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateRole_Success() {
        Integer userId = 1;
        Role newRole = new Role(1,ERole.ROLE_SYSTEM_ADMIN);
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setRole(new Role(4,ERole.ROLE_USER));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        String result = userEntityService.updateRole(userId, newRole);

        assertEquals("Role Updated Successfully!!!", result);
        assertEquals(newRole, user.getRole());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateRole_UserNotFound() {
        Integer userId = 1;
        Role newRole = new Role(1, ERole.ROLE_SYSTEM_ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        String result = userEntityService.updateRole(userId, newRole);

        assertEquals("Cannot Update Role !!!", result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(UserEntity.class));
    }

    @Test
    void testAddUserEntity_Success() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity result = userEntityService.addUserEntity(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByUsername_Success() {
        String username = "testuser";
        UserEntity user = new UserEntity();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userEntityService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_NotFound() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userEntityService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testExistsByUsername_True() {
        String username = "testuser";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        Boolean result = userEntityService.existsByUsername(username);

        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void testExistsByUsername_False() {
        String username = "testuser";

        when(userRepository.existsByUsername(username)).thenReturn(false);

        Boolean result = userEntityService.existsByUsername(username);

        assertFalse(result);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void testFindByRole_Success() {
        ERole role = ERole.ROLE_USER;
        UserEntity user = new UserEntity();
        user.setRole(new Role(4, role));

        when(userRepository.findByRole(role)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userEntityService.findByRole(role);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByRole(role);
    }

    @Test
    void testFindByRole_NotFound() {
        ERole role = ERole.ROLE_USER;

        when(userRepository.findByRole(role)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userEntityService.findByRole(role);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByRole(role);
    }

}

