package com.project.npp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Operator;
import com.project.npp.entities.Role;
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

class AuthControllerTest {

	private AuthController authController;

	private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;
	private UserDetailsServiceImpl userDetailsService;
	private UserEntityService userService;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;
	private OperatorService operatorService;

	@BeforeEach
	void setUp() {
		authenticationManager = mock(AuthenticationManager.class);
		jwtUtils = mock(JwtUtils.class);
		userDetailsService = mock(UserDetailsServiceImpl.class);
		userService = mock(UserEntityService.class);
		roleService = mock(RoleService.class);
		passwordEncoder = mock(PasswordEncoder.class);
		operatorService = mock(OperatorService.class);

		authController = new AuthController();
		authController.authenticationManager = authenticationManager;
		authController.jwtUtils = jwtUtils;
		authController.userDetailsService = userDetailsService;
		authController.userService = userService;
		authController.roleService = roleService;
		authController.passwordEncoder = passwordEncoder;
		authController.operatorService = operatorService;
	}

	@Test
	void testAuthenticateUser() {
		// Mocking
		Authentication authentication = mock(Authentication.class);
		UserDetailsImpl userDetails = new UserDetailsImpl(1, "test_user", "password", mock(GrantedAuthority.class),
				new Operator());
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("test_token");

		// Test
		LoginRequest loginRequest = new LoginRequest("test_user", "password");
		ResponseEntity<JwtResponse> response = authController.authenticateUser(loginRequest);

		// Assertion
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("test_token", response.getBody().getAccessToken());
		assertEquals(1, response.getBody().getId());
		assertEquals("test_user", response.getBody().getUsername());
	}

	@Test
	void testRegisterUser() throws OperatorNotFoundException, RoleNotFoundException {
		// Mocking
		SignupRequest signupRequest = new SignupRequest("new_user", "password", 1);
		Role role = new Role();
		role.setId(4);
		when(userService.existsByUsername("new_user")).thenReturn(false);
		when(roleService.findRoleByName(ERole.ROLE_USER)).thenReturn(java.util.Optional.of(role));
		when(operatorService.getOperatorById(1)).thenReturn(new Operator());

		// Test
		ResponseEntity<MessageResponse> response = authController.registerUser(signupRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("User registered successfully!", response.getBody().getMessage());
	}

	@Test
	void testRegisterUserExistingUsername() throws OperatorNotFoundException, RoleNotFoundException {
		// Mocking
		SignupRequest signupRequest = new SignupRequest("existing_user", "password", 1);
		when(userService.existsByUsername("existing_user")).thenReturn(true);

		// Test
		ResponseEntity<MessageResponse> response = authController.registerUser(signupRequest);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Error: Username is already taken!", response.getBody().getMessage());
	}
}
