package com.project.npp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.npp.entities.ERole;
import com.project.npp.entities.Operator;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;
import com.project.npp.entities.request.GetOperatorRequest;
import com.project.npp.entities.request.OperatorRequest;
import com.project.npp.entities.request.UpdateUserRoleRequest;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.service.OperatorService;
import com.project.npp.service.RoleService;
import com.project.npp.service.UserEntityService;

@ExtendWith(MockitoExtension.class)
public class SystemAdminControllerTest {

	@InjectMocks
	private SystemAdminController systemAdminController;

	@Mock
	private UserEntityService userService;

	@Mock
	private RoleService roleService;

	@Mock
	private OperatorService operatorService;

	private OperatorRequest operatorRequest;
	private Operator operator;

	@BeforeEach
	void setUp() {
		operatorRequest = new OperatorRequest();
		operatorRequest.setOperatorName("Test Operator");
		operatorRequest.setContactInfo("test@example.com");

		operator = new Operator();
		operator.setOperatorId(1);
		operator.setOperatorName("Test Operator");
		operator.setContactInfo("test@example.com");
	}

	@Test
	void testUpdateUserRole() throws RoleNotFoundException {
		Role role = new Role();
		role.setName(ERole.ROLE_SYSTEM_ADMIN);
		when(roleService.findRoleByName(ERole.ROLE_SYSTEM_ADMIN)).thenReturn(Optional.of(role));
		when(userService.updateRole(1, role)).thenReturn("Role updated successfully");

		ResponseEntity<String> response = systemAdminController.updateUserRole(1, ERole.ROLE_SYSTEM_ADMIN);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Role updated successfully", response.getBody());
	}
	
	@Test
	void testUpdateUserRole_Success() throws RoleNotFoundException {
	    Role role = new Role();
	    role.setName(ERole.ROLE_SYSTEM_ADMIN);
	    when(roleService.findRoleByName(ERole.ROLE_SYSTEM_ADMIN)).thenReturn(Optional.of(role));
	    when(userService.updateRole(1, role)).thenReturn("Role updated successfully");

	    UpdateUserRoleRequest request = new UpdateUserRoleRequest();
	    request.setUserId(1);
	    request.setRole(ERole.ROLE_SYSTEM_ADMIN);

	    ResponseEntity<String> response = systemAdminController.updateUserRole(request);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals("Role updated successfully", response.getBody());
	    verify(roleService, times(1)).findRoleByName(ERole.ROLE_SYSTEM_ADMIN);
	    verify(userService, times(1)).updateRole(1, role);
	}

	@Test
	void testUpdateUserRole_InvalidUserId() throws RoleNotFoundException {
	    Role role = new Role();
	    role.setName(ERole.ROLE_SYSTEM_ADMIN);
	    when(roleService.findRoleByName(ERole.ROLE_SYSTEM_ADMIN)).thenReturn(Optional.of(role));
	    when(userService.updateRole(999, role)).thenReturn("User not found");

	    UpdateUserRoleRequest request = new UpdateUserRoleRequest();
	    request.setUserId(999);
	    request.setRole(ERole.ROLE_SYSTEM_ADMIN);

	    ResponseEntity<String> response = systemAdminController.updateUserRole(request);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals("User not found", response.getBody());
	    verify(roleService, times(1)).findRoleByName(ERole.ROLE_SYSTEM_ADMIN);
	    verify(userService, times(1)).updateRole(999, role);
	}

	@Test
	void testAddOperator() {
		when(operatorService.addOperator(any(Operator.class))).thenReturn(operator);

		ResponseEntity<Operator> response = systemAdminController.addOperator(operatorRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(operator, response.getBody());
	}

	@Test
	void testGetOperator() throws OperatorNotFoundException {
		when(operatorService.getOperatorById(1)).thenReturn(operator);

		ResponseEntity<Operator> response = systemAdminController.getOperator(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(operator, response.getBody());
	}

	@Test
	void testGetOperatorNotFound() throws OperatorNotFoundException {
		when(operatorService.getOperatorById(1)).thenThrow(new OperatorNotFoundException("Ope=rator not found"));

		assertThrows(OperatorNotFoundException.class, () -> {
			systemAdminController.getOperator(1);
		});
	}
	
	@Test
	void testGetOperator_Success() throws OperatorNotFoundException {
	    Operator operator = new Operator();
	    operator.setOperatorId(1);
	    operator.setOperatorName("Test Operator");
	    operator.setContactInfo("test@example.com");
	    when(operatorService.getOperatorById(1)).thenReturn(operator);

	    GetOperatorRequest request = new GetOperatorRequest();
	    request.setOperatorId(1);

	    ResponseEntity<Operator> response = systemAdminController.getOperator(request);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(operator, response.getBody());
	    verify(operatorService, times(1)).getOperatorById(1);
	}

	@Test
	void testGetOperator_NotFound() throws OperatorNotFoundException {
	    when(operatorService.getOperatorById(1)).thenThrow(new OperatorNotFoundException("Operator not found"));

	    GetOperatorRequest request = new GetOperatorRequest();
	    request.setOperatorId(1);

	    assertThrows(OperatorNotFoundException.class, () -> {
	        systemAdminController.getOperator(request);
	    });
	    verify(operatorService, times(1)).getOperatorById(1);
	}

	@Test
	void testGetOperator_InvalidOperatorId() throws OperatorNotFoundException {
	    when(operatorService.getOperatorById(999)).thenThrow(new OperatorNotFoundException("Operator not found"));

	    GetOperatorRequest request = new GetOperatorRequest();
	    request.setOperatorId(999);

	    assertThrows(OperatorNotFoundException.class, () -> {
	        systemAdminController.getOperator(request);
	    });
	    verify(operatorService, times(1)).getOperatorById(999);
	}


	@Test
	void testUpdateOperator() throws OperatorNotFoundException {
		when(operatorService.updateOperator(any(Operator.class))).thenReturn(operator);

		ResponseEntity<Operator> response = systemAdminController.updateOperator(operator);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(operator, response.getBody());
	}

	@Test
	void testUpdateOperatorNotFound() throws OperatorNotFoundException {
		when(operatorService.updateOperator(any(Operator.class)))
				.thenThrow(new OperatorNotFoundException("Operator not found"));

		assertThrows(OperatorNotFoundException.class, () -> {
			systemAdminController.updateOperator(operator);
		});
	}

	@Test
	void testDeleteOperator() throws OperatorNotFoundException {
		when(operatorService.deleteOperator(1)).thenReturn("Operator deleted successfully");

		ResponseEntity<String> response = systemAdminController.deleteOperator(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Operator deleted successfully", response.getBody());
	}

	@Test
	void testDeleteOperatorNotFound() throws OperatorNotFoundException {
		when(operatorService.deleteOperator(1)).thenThrow(new OperatorNotFoundException("Operator not found"));

		assertThrows(OperatorNotFoundException.class, () -> {
			systemAdminController.deleteOperator(1);
		});
	}
	
	@Test
	void testDeleteOperator_Success() throws OperatorNotFoundException {
	    when(operatorService.deleteOperator(1)).thenReturn("Operator deleted successfully");

	    GetOperatorRequest request = new GetOperatorRequest();
	    request.setOperatorId(1);

	    ResponseEntity<String> response = systemAdminController.deleteOperator(request);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals("Operator deleted successfully", response.getBody());
	    verify(operatorService, times(1)).deleteOperator(1);
	}

	@Test
	void testDeleteOperator_NotFound() throws OperatorNotFoundException {
	    when(operatorService.deleteOperator(1)).thenThrow(new OperatorNotFoundException("Operator not found"));

	    GetOperatorRequest request = new GetOperatorRequest();
	    request.setOperatorId(1);

	    assertThrows(OperatorNotFoundException.class, () -> {
	        systemAdminController.deleteOperator(request);
	    });
	    verify(operatorService, times(1)).deleteOperator(1);
	}

	@Test
	void testDeleteOperator_InvalidOperatorId() throws OperatorNotFoundException {
	    when(operatorService.deleteOperator(999)).thenThrow(new OperatorNotFoundException("Operator not found"));

	    GetOperatorRequest request = new GetOperatorRequest();
	    request.setOperatorId(999);

	    assertThrows(OperatorNotFoundException.class, () -> {
	        systemAdminController.deleteOperator(request);
	    });
	    verify(operatorService, times(1)).deleteOperator(999);
	}


	@Test
	void testGetAllOperatorsSuccess() throws OperatorNotFoundException {
		List<Operator> mockOperators = new ArrayList<>();
		mockOperators.add(new Operator());

		when(operatorService.getAllOperators()).thenReturn(mockOperators);

		ResponseEntity<List<Operator>> response = systemAdminController.getAllOperators();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockOperators, response.getBody());
		verify(operatorService, times(1)).getAllOperators();
	}

	@Test
	void testGetAllOperatorsEmptyList() throws OperatorNotFoundException {
		List<Operator> mockOperators = Collections.emptyList();

		when(operatorService.getAllOperators()).thenReturn(mockOperators);

		ResponseEntity<List<Operator>> response = systemAdminController.getAllOperators();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockOperators, response.getBody());
		verify(operatorService, times(1)).getAllOperators();
	}

	@Test
	void testGetAllOperatorsNotFound() throws OperatorNotFoundException {
		@SuppressWarnings("unused")
		List<Operator> mockOperators = Collections.emptyList();

		when(operatorService.getAllOperators()).thenThrow(new OperatorNotFoundException("Operators not found"));

		assertThrows(OperatorNotFoundException.class, () -> {
			systemAdminController.getAllOperators();
		});
		verify(operatorService, times(1)).getAllOperators();
	}

	@Test
	void testGetAllUserEntitiesSuccess() {
		List<UserEntity> mockUsers = new ArrayList<>();
		mockUsers.add(new UserEntity());

		when(userService.getAllUserEntities()).thenReturn(mockUsers);

		ResponseEntity<List<UserEntity>> response = systemAdminController.getAllUserEntities();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockUsers, response.getBody());
		verify(userService, times(1)).getAllUserEntities();
	}

	@Test
	void testGetAllUserEntitiesEmptyList() {
		List<UserEntity> mockUsers = Collections.emptyList();

		when(userService.getAllUserEntities()).thenReturn(mockUsers);

		ResponseEntity<List<UserEntity>> response = systemAdminController.getAllUserEntities();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockUsers, response.getBody());
		verify(userService, times(1)).getAllUserEntities();
	}
}
