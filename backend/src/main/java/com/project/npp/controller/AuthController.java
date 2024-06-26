package com.project.npp.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Operator;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;
import com.project.npp.exceptionmessages.QueryMapper;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

	private static Logger loggers = LogManager.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	UserEntityService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	OperatorService operatorService;

	// API end point for user authentication
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		loggers.info("signin");

		// Authenticate user credentials
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String role = userDetails.getAuthorities().stream().findFirst() // Get the first authority
				.map(item -> item.getAuthority()) // Map it to its authority string
				.orElse(null);

		// Create JWT response with user details and token
		JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), role,
				userDetails.getOperator());
		return ResponseEntity.ok(jwtResponse);
	}

	// API end point for user registration
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest)
			throws OperatorNotFoundException, RoleNotFoundException {

		loggers.info("signin");
		if (userService.existsByUsername(signUpRequest.getUsername())) {
			loggers.error(QueryMapper.USERNAME_TAKEN);
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		// Create new user entity and populate with request data
		UserEntity user = new UserEntity();
		user.setUsername(signUpRequest.getUsername());
		user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));

		// Find user role by name and assign it
		Optional<Role> role = Optional.of(roleService.findRoleByName(ERole.ROLE_DEFAULT).get());
		if (role.isPresent()) {
			user.setRole(role.get());
		}

		// Find operator by ID and assign it to the user
		String operatorName=signUpRequest.getOperatorName();
		Operator operator = operatorService.getOperatorByOperatorName(operatorName);
		user.setOperator(operator);

		// Save the user entity
		userService.addUserEntity(user);
		loggers.info(QueryMapper.USERNAME_REGISTERED);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}
