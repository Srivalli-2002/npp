package com.project.npp.controller;

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
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.CustomerService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {
	
	@Autowired
	private ComplianceLogsService complianceLogsService;
	
	@Autowired
	private CustomerService customerService;

	@PostMapping("/trackstatus")
	public ResponseEntity<Map<String, Object>> getStatus(@RequestBody GetStatusRequest req) throws CustomerNotFoundException {
	    Customer customer = customerService.getCustomerByUserName(req.getUsername());
	    Map<String, Object> response = new HashMap<>();
	    try {
	        ComplianceLogs log = complianceLogsService.getLogByCustomer(customer);
	        response.put("status", log.isCheckPassed() ? "Porting Request is Successful" : "Porting Request is Not Successful");
	        response.put("notes", log.getNotes().toString());
	        response.put("lastUpdated", log.getCheckDate().toString());
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (LogNotFoundException e) {
	        response.put("status", "Log Not Found");
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	}


}
