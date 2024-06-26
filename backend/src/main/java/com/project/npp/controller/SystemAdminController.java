package com.project.npp.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.npp.entities.Operator;
import com.project.npp.entities.Role;
import com.project.npp.entities.UserEntity;
import com.project.npp.entities.request.GetOperatorRequest;
import com.project.npp.entities.request.OperatorRequest;
import com.project.npp.entities.request.UpdateUserRoleRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.service.OperatorService;
import com.project.npp.service.RoleService;
import com.project.npp.service.UserEntityService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemAdminController {

	private static Logger loggers = LogManager.getLogger(SystemAdminController.class);

	@Autowired
	private UserEntityService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private OperatorService operatorService;

	// API end point to update user role
	@PostMapping("/updateuserrole")
	public ResponseEntity<String> updateUserRole(@RequestBody UpdateUserRoleRequest updateUserRoleRequest)
			throws RoleNotFoundException, OperatorNotFoundException {

		// Find the role by name
		Optional<Role> r = roleService.findRoleByName(updateUserRoleRequest.getRole());

		// Update the user role
		String message = userService.updateRole(updateUserRoleRequest.getUsername(), r.get());
		loggers.info(QueryMapper.ROLE_UPDATE_SUCCESSFULL);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// API end point to get all users
	@GetMapping("/getallusers")
	public ResponseEntity<List<UserEntity>> getAllUserEntities() {
		loggers.info("Get all Users");

		// Get list of all users
		List<UserEntity> allUsers = userService.getAllUserEntities();
		loggers.info(QueryMapper.GET_USER);
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	// ***************************************************************************************************

	// API end point to add a new operator
	@PostMapping("/addoperator")
	public ResponseEntity<Operator> addOperator(@RequestBody OperatorRequest operatorRequest) {
		loggers.info("Add operator");

		// Create a new Operator object and populate it with the request data
		Operator operator = new Operator();
		operator.setOperatorName(operatorRequest.getOperatorName());
		operator.setContactInfo(operatorRequest.getContactInfo());

		// Add the operator and retrieve the persisted object
		Operator op = operatorService.addOperator(operator);
		loggers.info(QueryMapper.ADD_OPERATOR_SUCCESSFULL);
		return new ResponseEntity<>(op, HttpStatus.OK);
	}

	// API end point to retrieve an operator by its ID
	@PostMapping("/getoperator")
	public ResponseEntity<Operator> getOperator(@RequestBody GetOperatorRequest getOperatorRequest)
			throws OperatorNotFoundException {
		loggers.info("Get operatorby ID");

		// Retrieve the operator by its ID
		Operator operator = operatorService.getOperatorById(getOperatorRequest.getOperatorId());
		loggers.info(QueryMapper.GET_OPERATOR_SUCCESSFULL);
		return new ResponseEntity<>(operator, HttpStatus.OK);
	}

	// API end point to update an operator
	@PostMapping("/updateoperator")
	public ResponseEntity<Operator> updateOperator(@RequestBody Operator operator) throws OperatorNotFoundException {
		loggers.info("Update operator");

		// Update the operator and retrieve the updated object
		Operator op = operatorService.updateOperator(operator);
		loggers.info(QueryMapper.UPDATE_OPERATOR_SUCCESSFULL);
		return new ResponseEntity<>(op, HttpStatus.OK);
	}

	// API end point to delete an operator by its ID
	@PostMapping("/deleteoperator")
	public ResponseEntity<String> deleteOperator(@RequestBody GetOperatorRequest getOperatorRequest)
			throws OperatorNotFoundException {
		loggers.info("Delete operator by ID");

		// Delete the operator and retrieve the deletion message
		String message = operatorService.deleteOperator(getOperatorRequest.getOperatorId());
		loggers.info(QueryMapper.DELETE_OPERATOR_SUCCESSFULL);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// API end point to get all operators
	@GetMapping("/getalloperators")
	public ResponseEntity<List<Operator>> getAllOperators() throws OperatorNotFoundException {
		loggers.info("Get all Operators");

		// Get list of all operators
		List<Operator> allOperators = operatorService.getAllOperators();
		loggers.info(QueryMapper.GET_OPERATOR);
		return new ResponseEntity<>(allOperators, HttpStatus.OK);
	}

}
