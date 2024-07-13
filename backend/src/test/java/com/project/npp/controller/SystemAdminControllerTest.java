package com.project.npp.controller;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
 
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
 
	@Mock
	private UserEntityService userService;
 
	@Mock
	private RoleService roleService;
 
	@Mock
	private OperatorService operatorService;
 
	@InjectMocks
	private SystemAdminController controller;
 
	private UpdateUserRoleRequest updateUserRoleRequest;
	private OperatorRequest operatorRequest;
	private GetOperatorRequest getOperatorRequest;
 
	@BeforeEach
	void setUp() {
		updateUserRoleRequest = new UpdateUserRoleRequest("username", ERole.ROLE_SYSTEM_ADMIN);
		operatorRequest = new OperatorRequest("Operator 1", "Contact Info");
		getOperatorRequest = new GetOperatorRequest();
	}
 
	// Test case for updateUserRole endpoint
	@Test
	void testUpdateUserRole() throws RoleNotFoundException, OperatorNotFoundException {
		Role role = new Role(1, ERole.ROLE_SYSTEM_ADMIN);
		when(roleService.findRoleByName(any())).thenReturn(Optional.of(role));
		when(userService.updateRole(any(), any())).thenReturn("Role updated successfully");
 
		ResponseEntity<String> response = controller.updateUserRole(updateUserRoleRequest);
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Role updated successfully", response.getBody());
	}
	// Negative test case for updateUserRole endpoint - OperatorNotFoundException
    @Test
    void testUpdateUserRole_OperatorNotFound() throws RoleNotFoundException, OperatorNotFoundException {
        when(roleService.findRoleByName(any())).thenReturn(Optional.of(new Role(1, ERole.ROLE_SYSTEM_ADMIN)));
        when(userService.updateRole(any(), any())).thenThrow(new OperatorNotFoundException("Operator not found"));
 
        OperatorNotFoundException exception = assertThrows(OperatorNotFoundException.class, () -> {
            controller.updateUserRole(updateUserRoleRequest);
        });
 
        assertEquals("Operator not found", exception.getMessage());
    }
 
	// Test case for getAllUserEntities endpoint
	@Test
	void testGetAllUserEntities() {
		List<UserEntity> users = Collections.singletonList(new UserEntity());
		when(userService.getAllUserEntities()).thenReturn(users);
 
		ResponseEntity<List<UserEntity>> response = controller.getAllUserEntities();
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(users, response.getBody());
	}
 
	// Test case for addOperator endpoint
	@Test
	void testAddOperator() {
		Operator operator = new Operator(1, "Operator 1", "Contact Info");
		when(operatorService.addOperator(any())).thenReturn(operator);
 
		ResponseEntity<Operator> response = controller.addOperator(operatorRequest);
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Operator 1", response.getBody().getOperatorName());
		assertEquals("Contact Info", response.getBody().getContactInfo());
	}
 
	// Test case for getOperator endpoint
	@Test
	void testGetOperator() throws OperatorNotFoundException {
		Operator operator = new Operator(1, "Operator 1", "Contact Info");
		when(operatorService.getOperatorById(any())).thenReturn(operator);
 
		ResponseEntity<Operator> response = controller.getOperator(getOperatorRequest);
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Operator 1", response.getBody().getOperatorName());
		assertEquals("Contact Info", response.getBody().getContactInfo());
	}
	// Negative test case for getOperator endpoint - OperatorNotFoundException
    @Test
    void testGetOperator_OperatorNotFound() throws OperatorNotFoundException {
        when(operatorService.getOperatorById(any())).thenThrow(new OperatorNotFoundException("Operator not found"));
 
        OperatorNotFoundException exception = assertThrows(OperatorNotFoundException.class, () -> {
            controller.getOperator(getOperatorRequest);
        });
 
        assertEquals("Operator not found", exception.getMessage());
    }
 
	// Test case for updateOperator endpoint
	@Test
	void testUpdateOperator() throws OperatorNotFoundException {
		Operator operator = new Operator(1, "Operator 1", "Contact Info");
		when(operatorService.updateOperator(any())).thenReturn(operator);
 
		ResponseEntity<Operator> response = controller.updateOperator(operator);
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Operator 1", response.getBody().getOperatorName());
		assertEquals("Contact Info", response.getBody().getContactInfo());
	}
	// Negative test case for updateOperator endpoint - OperatorNotFoundException
    @Test
    void testUpdateOperator_OperatorNotFound() throws OperatorNotFoundException {
        when(operatorService.updateOperator(any())).thenThrow(new OperatorNotFoundException("Operator not found"));
 
        Operator operator = new Operator(1, "Operator 1", "Contact Info");
 
        OperatorNotFoundException exception = assertThrows(OperatorNotFoundException.class, () -> {
            controller.updateOperator(operator);
        });
 
        assertEquals("Operator not found", exception.getMessage());
    }
 
	// Test case for deleteOperator endpoint
	@Test
	void testDeleteOperator() throws OperatorNotFoundException {
		when(operatorService.deleteOperator(any())).thenReturn("Operator deleted successfully");
 
		ResponseEntity<String> response = controller.deleteOperator(getOperatorRequest);
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Operator deleted successfully", response.getBody());
	}
	// Negative test case for deleteOperator endpoint - OperatorNotFoundException
    @Test
    void testDeleteOperator_OperatorNotFound() throws OperatorNotFoundException {
        when(operatorService.deleteOperator(any())).thenThrow(new OperatorNotFoundException("Operator not found"));
 
        OperatorNotFoundException exception = assertThrows(OperatorNotFoundException.class, () -> {
            controller.deleteOperator(getOperatorRequest);
        });
 
        assertEquals("Operator not found", exception.getMessage());
    }
 
	// Test case for getAllOperators endpoint
	@Test
	void testGetAllOperators() throws OperatorNotFoundException {
		List<Operator> operators = Collections.singletonList(new Operator(1, "Operator 1", "Contact Info"));
		when(operatorService.getAllOperators()).thenReturn(operators);
 
		ResponseEntity<List<Operator>> response = controller.getAllOperators();
 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(operators, response.getBody());
	}
}