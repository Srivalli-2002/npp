package com.project.npp.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.npp.entities.Customer;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.request.CustomerRequest;
import com.project.npp.entities.request.UpdateCustomerRequest;
import com.project.npp.entities.request.UpdatePortRequest;
import com.project.npp.entities.request.UserPortRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.service.CustomerService;
import com.project.npp.service.OperatorService;
import com.project.npp.service.PortRequestService;

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

	// API end point to add a new customer
	@PostMapping("/addcustomer")
	public ResponseEntity<Customer> addCustomer(@RequestBody CustomerRequest customerRequest)
			throws OperatorNotFoundException {
		loggers.info("Add customer");

		// Create a new Customer object and populate it with the request data
		Customer customer = new Customer();
		customer.setName(customerRequest.getName());
		customer.setEmail(customerRequest.getEmail());
		customer.setPhoneNumber(customerRequest.getPhoneNumber());
		Operator currentOperator = operatorService.getOperatorById(customerRequest.getCurrentOperatorId());
		Operator newOperator = operatorService.getOperatorById(customerRequest.getNewOperatorId());
		customer.setCurrentOperator(currentOperator);
		customer.setNewOperator(newOperator);

		// Add the customer and retrieve the persisted object
		Customer cust = customerService.addCustomer(customer);
		loggers.info(QueryMapper.ADD_CUSTOMER_SUCCESSFULL);

		// Return the persisted customer in the response
		return new ResponseEntity<Customer>(cust, HttpStatus.OK);
	}

	// API end point to retrieve a customer by its ID
	@PostMapping("/getcustomer")
	public ResponseEntity<Customer> getCustomer(@RequestParam("customerId") Integer customerId)
			throws CustomerNotFoundException {
		loggers.info("Get customer");

		// Retrieve the customer by its ID
		Customer customer = customerService.getCustomerById(customerId);
		loggers.info(QueryMapper.GET_CUSTOMER_SUCCESSFULL);

		// Return the customer in the response
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	// API end point to update a customer
	@PostMapping("/updatecustomer")
	public ResponseEntity<Customer> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest)
			throws OperatorNotFoundException, CustomerNotFoundException {
		loggers.info("Update customer");

		// Create a new Customer object and populate it with the updated data
		Customer customer = new Customer();
		customer.setCustomerId(updateCustomerRequest.getCustomerId());
		customer.setName(updateCustomerRequest.getName());
		customer.setEmail(updateCustomerRequest.getEmail());
		customer.setPhoneNumber(updateCustomerRequest.getPhoneNumber());
		Operator currentOperator = operatorService.getOperatorById(updateCustomerRequest.getCurrentOperatorId());
		Operator newOperator = operatorService.getOperatorById(updateCustomerRequest.getNewOperatorId());
		customer.setCurrentOperator(currentOperator);
		customer.setNewOperator(newOperator);
		customer.setStatus(updateCustomerRequest.getStatus());

		// Update the customer and retrieve the updated object
		Customer cust = customerService.updateCustomer(customer);
		loggers.info(QueryMapper.UPDATE_CUSTOMER_SUCCESSFULL);

		// Return the updated customer in the response
		return new ResponseEntity<Customer>(cust, HttpStatus.OK);
	}

	// API end point to delete a customer by its ID
	@PostMapping("/deletecustomer")
	public ResponseEntity<String> deleteCustomer(@RequestParam("customerId") Integer customerId)
			throws CustomerNotFoundException {
		loggers.info("Delete customer");

		// Delete the customer and retrieve the deletion message
		String message = customerService.deleteCustomerById(customerId);
		loggers.info(QueryMapper.DELETE_CUSTOMER_SUCCESSFULL);

		// Return the deletion message in the response
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	// API end point to get all customers
	@GetMapping("/getallcustomers")
	public ResponseEntity<List<Customer>> getAllCustomer() throws CustomerNotFoundException {
		loggers.info("Get all customers");
		List<Customer> customers = customerService.getAllCustomers();

		loggers.info(QueryMapper.GET_CUSTOMER);
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	// API end point to submit a port request
	@PostMapping("/submitportrequest")
	public ResponseEntity<PortRequest> submitPortRequest(@RequestBody UserPortRequest userPortRequest)
			throws CustomerNotFoundException {
		loggers.info("Submit port request");

		// Create a new PortRequest object and populate it with the request data
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestDate(userPortRequest.getRequestDate());
		Customer customer = customerService.getCustomerById(userPortRequest.getCustomerId());
		portRequest.setCustomer(customer);

		// Add the port request and retrieve the persisted object
		PortRequest portReq = portRequestService.addPortRequest(portRequest);
		loggers.info(QueryMapper.ADD_PORTREQUEST_SUCCESSFULL);

		// Return the persisted port request in the response
		return new ResponseEntity<PortRequest>(portReq, HttpStatus.OK);
	}

	// API end point to retrieve a port request by its ID
	@PostMapping("/getportrequest")
	public ResponseEntity<PortRequest> getPortRequest(@RequestParam("requestId") Integer requestId)
			throws PortRequestNotFoundException {
		loggers.info("Get port request");

		// Retrieve the port request by its ID
		PortRequest portRequest = portRequestService.getPortRequest(requestId);
		loggers.info(QueryMapper.GET_PORTREQUEST_SUCCESSFULL);

		// Return the port request in the response
		return new ResponseEntity<PortRequest>(portRequest, HttpStatus.OK);
	}

	// API end point to update a port request
	@PostMapping("/updateportrequest")
	public ResponseEntity<PortRequest> updatePortRequest(@RequestBody UpdatePortRequest updatePortRequest)
			throws CustomerNotFoundException, PortRequestNotFoundException {
		loggers.info("Update port request");

		// Create a new PortRequest object and populate it with the updated data
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestId(updatePortRequest.getRequestId());
		portRequest.setRequestDate(updatePortRequest.getRequestDate());
		portRequest.setComplianceChecked(updatePortRequest.getComplianceChecked());
		Customer customer = customerService.getCustomerById(updatePortRequest.getCustomerId());
		portRequest.setCustomer(customer);

		// Update the port request and retrieve the updated object
		PortRequest portReq = portRequestService.updatePortRequest(portRequest);
		loggers.info(QueryMapper.UPDATE_PORTREQUEST_SUCCESSFULL);

		// Return the updated port request in the response
		return new ResponseEntity<PortRequest>(portReq, HttpStatus.OK);
	}

	// API end point to delete a port request by its ID
	@PostMapping("/deleteportrequest")
	public ResponseEntity<String> deletePortRequest(@RequestParam("requestId") Integer requestId)
			throws PortRequestNotFoundException {
		loggers.info("Delete port request");

		// Delete the port request and retrieve the deletion message
		String message = portRequestService.deletePortRequest(requestId);
		loggers.info(QueryMapper.DELETE_PORTREQUEST_SUCCESSFULL);

		// Return the deletion message in the response
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	// API end point to get all port requests
	@GetMapping("/getallportrequests")
	public ResponseEntity<List<PortRequest>> getAllPortRequests() throws PortRequestNotFoundException {
		// Get all the port requests and retrieve the list of port requests
		List<PortRequest> portRequests = portRequestService.getAllPortRequest();
		loggers.info(QueryMapper.GET_PORTREQUEST_SUCCESSFULL);
		// Return all the port requests in the response
		return new ResponseEntity<>(portRequests, HttpStatus.OK);
	}

}