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

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.entities.UserEntity;
import com.project.npp.entities.request.CustomerRequest;
import com.project.npp.entities.request.GetCustomerRequest;
import com.project.npp.entities.request.GetPortRequest;
import com.project.npp.entities.request.UpdateCustomerRequest;
import com.project.npp.entities.request.UpdatePortRequest;
import com.project.npp.entities.request.UserPortRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.CustomerService;
import com.project.npp.service.OperatorService;
import com.project.npp.service.PortRequestService;
import com.project.npp.service.UserEntityService;

@RestController
@RequestMapping("/api/customerservice")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerServiceController {

	private static Logger loggers = LogManager.getLogger(CustomerServiceController.class);

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PortRequestService portRequestService;

	@Autowired
	private ComplianceLogsService complianceLogsService;

	@Autowired
	private UserEntityService userService;

	// API end point to add a new customer
	@PostMapping("/addcustomer")
	public ResponseEntity<Customer> addCustomer(@RequestBody CustomerRequest customerRequest)
			throws OperatorNotFoundException, RoleNotFoundException {
		loggers.info("Add customer");
		Customer customer = new Customer();
		customer.setName(customerRequest.getName());
		customer.setEmail(customerRequest.getEmail());
		customer.setPhoneNumber(customerRequest.getPhoneNumber());
		customer.setUsername(customerRequest.getUsername());
		Optional<UserEntity> user = userService.findByUsername(customerRequest.getUsername());
		Operator currentOperator = user.get().getOperator();
		Operator newOperator = operatorService.getOperatorByOperatorName("prodapt");
		customer.setCurrentOperator(currentOperator);
		customer.setNewOperator(newOperator);
		Customer cust = customerService.addCustomer(customer);
		loggers.info(QueryMapper.ADD_CUSTOMER_SUCCESSFULL);
		return new ResponseEntity<>(cust, HttpStatus.OK);
	}

	// API end point to retrieve a customer by its ID
	@PostMapping("/getcustomer")
	public ResponseEntity<Customer> getCustomer(@RequestBody GetCustomerRequest getCustomerRequest)
			throws CustomerNotFoundException {
		loggers.info("Get customer by ID");
		Customer customer = customerService.getCustomerById(getCustomerRequest.getCustomerId());
		loggers.info(QueryMapper.GET_CUSTOMER_SUCCESSFULL);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	// API end point to update a customer
	@PostMapping("/updatecustomer")
	public ResponseEntity<Customer> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest)
			throws OperatorNotFoundException, CustomerNotFoundException {
		loggers.info("Update customer");
		Customer customer = new Customer();
		customer.setCustomerId(updateCustomerRequest.getCustomerId());
		customer.setName(updateCustomerRequest.getName());
		customer.setUsername(updateCustomerRequest.getUsername());
		customer.setEmail(updateCustomerRequest.getEmail());
		customer.setPhoneNumber(updateCustomerRequest.getPhoneNumber());
		Operator currentOperator = operatorService
				.getOperatorByOperatorName(updateCustomerRequest.getCurrentOperatorName());
		customer.setCurrentOperator(currentOperator);
		customer.setStatus(updateCustomerRequest.getStatus());
		Operator newOperator = operatorService.getOperatorByOperatorName("prodapt");
		customer.setNewOperator(newOperator);
		// Update the customer and retrieve the updated object
		Customer cust = customerService.updateCustomer(customer);
		loggers.info(QueryMapper.UPDATE_CUSTOMER_SUCCESSFULL);
		return new ResponseEntity<>(cust, HttpStatus.OK);
	}

	// API end point to delete a customer by its ID
	@PostMapping("/deletecustomer")
	public ResponseEntity<String> deleteCustomer(@RequestBody GetCustomerRequest getCustomerRequest)
			throws CustomerNotFoundException {
		loggers.info("Delete customer by ID");
		String message = customerService.deleteCustomerById(getCustomerRequest.getCustomerId());
		loggers.info(QueryMapper.DELETE_CUSTOMER_SUCCESSFULL);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// API end point to get all customers
	@GetMapping("/getallcustomers")
	public ResponseEntity<List<Customer>> getAllCustomer() throws CustomerNotFoundException {
	    loggers.info("Get all customers");
	    List<Customer> customers = customerService.getAllCustomers();
	    loggers.info(QueryMapper.GET_CUSTOMER);
	    if (customers.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	// API end point to submit a port request
	@PostMapping("/submitportrequest")
	public ResponseEntity<PortRequest> submitPortRequest(@RequestBody UserPortRequest userPortRequest)
			throws CustomerNotFoundException, PortRequestNotFoundException {
		loggers.info("Submit port request");
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestDate(userPortRequest.getRequestDate());
		Customer customer = customerService.getCustomerByUserName(userPortRequest.getUsername());
		portRequest.setCustomer(customer);
		PortRequest portReq = portRequestService.addPortRequest(portRequest);
		loggers.info(QueryMapper.ADD_PORTREQUEST_SUCCESSFULL);
		ComplianceLogs complianceLogs = new ComplianceLogs();
		complianceLogs.setCustomer(customer);
		complianceLogs.setPortRequest(portReq);
		complianceLogsService.addLog(complianceLogs);
		loggers.info(QueryMapper.ADD_LOG_SUCCESSFULL);
		return new ResponseEntity<>(portReq, HttpStatus.OK);
	}

	// API end point to retrieve a port request by its ID
	@PostMapping("/getportrequest")
	public ResponseEntity<PortRequest> getPortRequest(@RequestBody GetPortRequest getPortRequest)
			throws PortRequestNotFoundException {
		loggers.info("Get port request by ID");
		PortRequest portRequest = portRequestService.getPortRequest(getPortRequest.getRequestId());
		loggers.info(QueryMapper.GET_PORTREQUEST_SUCCESSFULL);
		return new ResponseEntity<>(portRequest, HttpStatus.OK);
	}

	// API end point to update a port request
	@PostMapping("/updateportrequest")
	public ResponseEntity<PortRequest> updatePortRequest(@RequestBody UpdatePortRequest updatePortRequest)
			throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
		loggers.info("Update port request");
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestId(updatePortRequest.getRequestId());
		portRequest.setRequestDate(updatePortRequest.getRequestDate());
		Customer customer = customerService.getCustomerByUserName(updatePortRequest.getUsername());
		portRequest.setComplianceChecked(false);
		portRequest.setCustomer(customer);
		portRequest.setApprovalStatus(Status.PENDING);
		PortRequest portReq = portRequestService.updatePortRequest(portRequest);
		loggers.info(QueryMapper.UPDATE_PORTREQUEST_SUCCESSFULL);
		return new ResponseEntity<>(portReq, HttpStatus.OK);
	}

	// API end point to delete a port request by its ID
	@PostMapping("/deleteportrequest")
	public ResponseEntity<String> deletePortRequest(@RequestBody GetPortRequest getPortRequest)
			throws PortRequestNotFoundException {
		loggers.info("Delete port request by ID");
		String message = portRequestService.deletePortRequest(getPortRequest.getRequestId());
		loggers.info(QueryMapper.DELETE_PORTREQUEST_SUCCESSFULL);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// API end point to get all port requests
	@GetMapping("/getallportrequests")
	public ResponseEntity<List<PortRequest>> getAllPortRequests() throws PortRequestNotFoundException {
		List<PortRequest> portRequests = portRequestService.getAllPortRequest();
		loggers.info(QueryMapper.GET_PORTREQUEST_SUCCESSFULL);
		return new ResponseEntity<>(portRequests, HttpStatus.OK);
	}

}