package com.project.npp.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.VerificationDetails;
import com.project.npp.entities.request.GetLogRequest;
import com.project.npp.entities.request.GetVerificationDetails;
import com.project.npp.entities.request.GetVerificationDetailsByPhn;
import com.project.npp.entities.request.UpdateComplianceLogsRequest;
import com.project.npp.entities.request.UpdateVerificationDetails;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.utilities.VerificationDetailsService;

@RestController
@RequestMapping("/api/complianceofficer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ComplianceOfficerController {

	private static Logger loggers = LogManager.getLogger(ComplianceOfficerController.class);

	@Autowired
	private ComplianceLogsService complianceLogsService;

	@Autowired
	private VerificationDetailsService verificationDetailsService;

	// API end point to retrieve a compliance log by its ID
	@PostMapping("/updatelog")
	public ResponseEntity<String> updateLog(@RequestBody UpdateComplianceLogsRequest req) throws LogNotFoundException,
			PortRequestNotFoundException, CustomerNotFoundException, VerificationDetailsNotFoundException {
		loggers.info("Update log");

		// Update the compliance log by its ID
		String message = complianceLogsService.VerifyAndUpdateLog(req.getLogId());
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);

		// Return the compliance log in the response
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

	// API end point to retrieve a compliance log by its ID
	@PostMapping("/getlog")
	public ResponseEntity<ComplianceLogs> getLog(@RequestBody GetLogRequest getLogRequest) throws LogNotFoundException {
		loggers.info("Get log");

		// Retrieve the compliance log by its ID
		ComplianceLogs complianceLog = complianceLogsService.getLog(getLogRequest.getLogId());
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);

		// Return the compliance log in the response
		return new ResponseEntity<ComplianceLogs>(complianceLog, HttpStatus.OK);
	}

	// API end point to get all compliance logs
	@GetMapping("/getalllogs")
	public ResponseEntity<List<ComplianceLogs>> getAllComplianceLogs() throws LogNotFoundException {

		// Retrive list of compliance logs
		List<ComplianceLogs> complianceLogs = complianceLogsService.getAllComplianceLogs();
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);

		// Return the list of compliance logs in the response
		return new ResponseEntity<List<ComplianceLogs>>(complianceLogs, HttpStatus.OK);

	}
	// *************************************************************************************************

	@GetMapping("/addverificationdetails")
	public ResponseEntity<String> addVerificationDetails() throws FileNotFoundException, IOException {
		String message = verificationDetailsService.fetchVerificationDetails();
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping("/updateverificationdetails")
	public ResponseEntity<VerificationDetails> updateVerificationDetails(@RequestBody UpdateVerificationDetails details)
			throws VerificationDetailsNotFoundException {
		VerificationDetails vDetails = new VerificationDetails();
		vDetails.setContractualObligationsMet(details.getContractualObligationsMet());
		vDetails.setCustomerIdentityVerified(details.getCustomerIdentityVerified());
		vDetails.setNoOutstandingPayments(details.getNoOutstandingPayments());
		vDetails.setNotificationToCurrentOperator(details.getNotificationToCurrentOperator());
		vDetails.setNumberStatus(details.getNumberStatus());
		vDetails.setPhoneNumber(details.getPhoneNumber());
		vDetails.setTimeSinceLastPort(details.getTimeSinceLastPort());
		VerificationDetails updatedDetails = verificationDetailsService.updateVerificationDetails(vDetails);
		return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
	}

	@PostMapping("/getverificationdetailsbylogid")
	public ResponseEntity<VerificationDetails> getVerificationDetails(@RequestBody GetVerificationDetails details)
			throws VerificationDetailsNotFoundException, LogNotFoundException {
		ComplianceLogs complianceLog = complianceLogsService.getLog(details.getLogId());
		Long phoneNumber= complianceLog.getCustomer().getPhoneNumber();
		VerificationDetails vDetails = verificationDetailsService.getByPhoneNumber(phoneNumber);
		return new ResponseEntity<>(vDetails, HttpStatus.OK);
	}
	
	@GetMapping("/getallverificationdetails")
	public ResponseEntity<List<VerificationDetails>> getAllVerificationDetails() throws VerificationDetailsNotFoundException
	{
		List<VerificationDetails> details= verificationDetailsService.getAll();
		return new ResponseEntity<>(details, HttpStatus.OK);
	}
	@PostMapping("/getverificationdetailsbyphn")
	public ResponseEntity<VerificationDetails> getVerificationDetailsByPhn(@RequestBody GetVerificationDetailsByPhn details)
			throws VerificationDetailsNotFoundException, LogNotFoundException {
		VerificationDetails vDetails = verificationDetailsService.getByPhoneNumber(details.getPhoneNumber());
		return new ResponseEntity<>(vDetails, HttpStatus.OK);
	}
}
