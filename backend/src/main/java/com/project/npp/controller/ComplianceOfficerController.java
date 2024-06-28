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
import org.springframework.web.bind.annotation.RestController;

import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.Operator;
import com.project.npp.entities.VerificationDetails;
import com.project.npp.entities.request.GetLogRequest;
import com.project.npp.entities.request.GetVerificationDetails;
import com.project.npp.entities.request.GetVerificationDetailsByPhn;
import com.project.npp.entities.request.UpdateComplianceLogsRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.CustomerService;
import com.project.npp.service.OperatorService;
import com.project.npp.utilities.AirtelVerificationDetailsService;
import com.project.npp.utilities.JioVerificationDetailsService;
import com.project.npp.utilities.VerificationDetailsConversion;

@RestController
@RequestMapping("/api/complianceofficer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ComplianceOfficerController {

	private static Logger loggers = LogManager.getLogger(ComplianceOfficerController.class);

	@Autowired
	private ComplianceLogsService complianceLogsService;

	@Autowired
	private JioVerificationDetailsService jioVerificationDetailsService;
	
	@Autowired
	private AirtelVerificationDetailsService airtelVerificationDetailsService;

	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private VerificationDetailsConversion convert;
	
	@Autowired
	private CustomerService customerService;

	// API end point to retrieve a compliance log by its ID
	@PostMapping("/updatelog")
	public ResponseEntity<String> updateLog(@RequestBody UpdateComplianceLogsRequest req)
			throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException,
			VerificationDetailsNotFoundException, OperatorNotFoundException {
		loggers.info("Update log");
		String message = complianceLogsService.VerifyAndUpdateLog(req.getLogId());
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	// API end point to retrieve a compliance log by its ID
	@PostMapping("/getlog")
	public ResponseEntity<ComplianceLogs> getLog(@RequestBody GetLogRequest getLogRequest) throws LogNotFoundException {
		loggers.info("Get log");
		ComplianceLogs complianceLog = complianceLogsService.getLog(getLogRequest.getLogId());
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);
		return new ResponseEntity<ComplianceLogs>(complianceLog, HttpStatus.OK);
	}

	// API end point to get all compliance logs
	@GetMapping("/getalllogs")
	public ResponseEntity<List<ComplianceLogs>> getAllComplianceLogs() throws LogNotFoundException {
		List<ComplianceLogs> complianceLogs = complianceLogsService.getAllComplianceLogs();
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);
		return new ResponseEntity<List<ComplianceLogs>>(complianceLogs, HttpStatus.OK);

	}

	// API end point to get all verification details by log id
	@SuppressWarnings("static-access")
	@PostMapping("/getverificationdetailsbylogid")
	public ResponseEntity<VerificationDetails> getVerificationDetails(@RequestBody GetVerificationDetails details)
			throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException {
		ComplianceLogs complianceLog = complianceLogsService.getLog(details.getLogId());
		Long phoneNumber = complianceLog.getCustomer().getPhoneNumber();
		Operator operatorJio = operatorService.getOperatorByOperatorName("jio");
		Operator operatorAirtel = operatorService.getOperatorByOperatorName("airtel");
		if (complianceLog.getCustomer().getCurrentOperator().equals(operatorAirtel)) {
			AirtelVerificationDetails vDetails = airtelVerificationDetailsService.getByPhoneNumber(phoneNumber);
			VerificationDetails result = convert.convertToVerificationDetails(vDetails);
			loggers.info(QueryMapper.GET_VERIFICATION_DETAILS);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		if (complianceLog.getCustomer().getCurrentOperator().equals(operatorJio)) {
			JioVerificationDetails vDetails = jioVerificationDetailsService.getByPhoneNumber(phoneNumber);
			VerificationDetails result = convert.convertToVerificationDetails(vDetails);
			loggers.info(QueryMapper.GET_VERIFICATION_DETAILS);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else
			return null;
	}

	// API end point to get all verification details by phone number
	@SuppressWarnings("static-access")
	@PostMapping("/getverificationdetailsbyphn")
	public ResponseEntity<VerificationDetails> getVerificationDetailsByPhn(@RequestBody GetVerificationDetailsByPhn details) throws VerificationDetailsNotFoundException,LogNotFoundException, OperatorNotFoundException, CustomerNotFoundException {
		Customer customer = customerService.getCustomerByPhoneNumber(details.getPhoneNumber());
		Operator operatorJio = operatorService.getOperatorByOperatorName("jio");
		Operator operatorAirtel = operatorService.getOperatorByOperatorName("airtel");
		if (customer.getCurrentOperator().equals(operatorAirtel)) {
			AirtelVerificationDetails vDetails = airtelVerificationDetailsService
					.getByPhoneNumber(details.getPhoneNumber());
			VerificationDetails result = convert.convertToVerificationDetails(vDetails);
			loggers.info(QueryMapper.GET_VERIFICATION_DETAILS);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		if (customer.getCurrentOperator().equals(operatorJio)) {
			JioVerificationDetails vDetails = jioVerificationDetailsService.getByPhoneNumber(details.getPhoneNumber());
			VerificationDetails result = convert.convertToVerificationDetails(vDetails);
			loggers.info(QueryMapper.GET_VERIFICATION_DETAILS);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else
			return null;
	}

}