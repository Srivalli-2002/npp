package com.project.npp.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.request.GetStatusRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {
	
	private static Logger loggers = LogManager.getLogger(CustomerController.class);
	
	@Autowired
	private ComplianceLogsService complianceLogsService;
	
	@Autowired
	private CustomerService customerService;

	// API end point to track the status of the customer
	@PostMapping("/trackstatus")
	public ResponseEntity<Map<String, Object>> getStatus(@RequestBody GetStatusRequest req)
			throws CustomerNotFoundException {
		Customer customer = customerService.getCustomerByUserName(req.getUsername());
		Map<String, Object> response = new HashMap<>();
		try {
			ComplianceLogs log = complianceLogsService.getLogByCustomer(customer);
			response.put("status",
					log.isCheckPassed() ? "Porting Request is Successful" : "Porting Request is Not Successful");
			response.put("notes", log.getNotes().toString());
			response.put("lastUpdated", log.getCheckDate().toString());
			loggers.info(QueryMapper.TRACK_STATUS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (LogNotFoundException e) {
			response.put("status", "Log Not Found");
			loggers.info(QueryMapper.TRACK_STATUS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

}