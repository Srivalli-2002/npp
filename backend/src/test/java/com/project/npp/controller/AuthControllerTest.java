package com.project.npp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Operator;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.security.jwt.JwtUtils;
import com.project.npp.security.payload.request.LoginRequest;
import com.project.npp.security.payload.request.SignupRequest;
import com.project.npp.security.payload.response.JwtResponse;
import com.project.npp.security.payload.response.MessageResponse;
import com.project.npp.security.service.UserDetailsImpl;
import com.project.npp.security.service.UserDetailsServiceImpl;
import com.project.npp.service.OperatorService;
import com.project.npp.service.RoleService;
import com.project.npp.service.UserEntityService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserEntityService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OperatorService operatorService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private UserEntity userEntity;
    private Role role;
    private Operator operator;
    private Authentication authentication;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("password");
        signupRequest.setOperatorName("testOperator");

        userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPasswordHash("passwordHash");

        role = new Role();
        role.setName(ERole.ROLE_DEFAULT);

        operator = new Operator();
        operator.setOperatorName("testOperator");

        userDetails = new UserDetailsImpl(1, "testuser", "passwordHash", (GrantedAuthority) Collections.emptyList(), operator);

        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @SuppressWarnings("deprecation")
	@Test
    public void testAuthenticateUser_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testToken");

        ResponseEntity<JwtResponse> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("testToken", jwtResponse.getAccessToken());
        assertEquals("testuser", jwtResponse.getUsername());
    }

    @Test
    public void testAuthenticateUser_Failure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> authController.authenticateUser(loginRequest));
    }

    @Test
    public void testRegisterUser_Success() throws OperatorNotFoundException, RoleNotFoundException {
        when(userService.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(roleService.findRoleByName(ERole.ROLE_DEFAULT)).thenReturn(Optional.of(role));
        when(operatorService.getOperatorByOperatorName(signupRequest.getOperatorName())).thenReturn(operator);

        ResponseEntity<MessageResponse> response = authController.registerUser(signupRequest);

        assertEquals(200, response.getStatusCodeValue());
        MessageResponse messageResponse = response.getBody();
        assertNotNull(messageResponse);
        assertEquals("User registered successfully!", messageResponse.getMessage());

        verify(userService, times(1)).addUserEntity(any(UserEntity.class));
    }

    @Test
    public void testRegisterUser_UsernameTaken() throws OperatorNotFoundException, RoleNotFoundException {
        when(userService.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        ResponseEntity<MessageResponse> response = authController.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        MessageResponse messageResponse = response.getBody();
        assertNotNull(messageResponse);
        assertEquals("Error: Username is already taken!", messageResponse.getMessage());

        verify(userService, never()).addUserEntity(any(UserEntity.class));
    }

    @Test
    public void testRegisterUser_RoleNotFound() throws RoleNotFoundException {
        when(userService.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(roleService.findRoleByName(ERole.ROLE_DEFAULT)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> authController.registerUser(signupRequest));
    }

    @Test
    public void testRegisterUser_OperatorNotFound() throws RoleNotFoundException, OperatorNotFoundException {
        when(userService.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(roleService.findRoleByName(ERole.ROLE_DEFAULT)).thenReturn(Optional.of(role));
        when(operatorService.getOperatorByOperatorName(signupRequest.getOperatorName())).thenThrow(OperatorNotFoundException.class);

        assertThrows(OperatorNotFoundException.class, () -> authController.registerUser(signupRequest));
    }
}
