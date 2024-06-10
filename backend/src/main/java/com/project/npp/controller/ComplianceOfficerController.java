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

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.request.ComplianceLogsRequest;
import com.project.npp.entities.request.GetLogRequest;
import com.project.npp.entities.request.UpdateComplianceLogsRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.PortRequestService;

@RestController
@RequestMapping("/api/complianceofficer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ComplianceOfficerController {

	private static Logger loggers = LogManager.getLogger(ComplianceOfficerController.class);

	@Autowired
	private PortRequestService portRequestService;

	@Autowired
	private ComplianceLogsService complianceLogsService;

	// API end point to add a compliance log
	@PostMapping("/addlog")
	public ResponseEntity<ComplianceLogs> addLog(@RequestBody ComplianceLogsRequest complianceLogsRequest)
			throws CustomerNotFoundException, PortRequestNotFoundException {
		loggers.info("Add log");

		// Create a new ComplianceLogs object and populate it with the request data
		ComplianceLogs complianceLogs = new ComplianceLogs();
		complianceLogs.setCheckPassed(complianceLogsRequest.isCheckPassed());
		complianceLogs.setNotes(complianceLogsRequest.getNotes());
		complianceLogs.setCheckDate(complianceLogsRequest.getCheckDate());

		// Retrieve the PortRequest object associated with the request and set it in the
		// ComplianceLogs object
		PortRequest portRequest = portRequestService.getPortRequest(complianceLogsRequest.getPortRequestId());

		// Add the compliance log and retrieve the persisted object
		complianceLogs.setPortRequest(portRequest);
		ComplianceLogs complianceLog = complianceLogsService.addLog(complianceLogs);
		loggers.info(QueryMapper.ADD_LOG_SUCCESSFULL);

		// Return the persisted compliance log in the response
		return new ResponseEntity<ComplianceLogs>(complianceLog, HttpStatus.OK);
	}

	// API end point to retrieve a compliance log by its ID
	@PostMapping("/getlogbyid")
	public ResponseEntity<ComplianceLogs> getLog(@RequestParam("logId") Integer logId) throws LogNotFoundException {
		loggers.info("Get log");

		// Retrieve the compliance log by its ID
		ComplianceLogs complianceLog = complianceLogsService.getLog(logId);
		loggers.info(QueryMapper.GET_LOG_SUCCESSFULL);

		// Return the compliance log in the response
		return new ResponseEntity<ComplianceLogs>(complianceLog, HttpStatus.OK);
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

	// API end point to update a compliance log
	@PostMapping("/updatelog")
	public ResponseEntity<ComplianceLogs> updateLog(
			@RequestBody UpdateComplianceLogsRequest updateComplianceLogsRequest)
			throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException {
		loggers.info("Update log");

		// Create a new ComplianceLogs object and populate it with the updated data
		ComplianceLogs complianceLogs = new ComplianceLogs();
		complianceLogs.setLogId(updateComplianceLogsRequest.getLogId());
		complianceLogs.setCheckPassed(updateComplianceLogsRequest.isCheckPassed());
		complianceLogs.setNotes(updateComplianceLogsRequest.getNotes());
		complianceLogs.setCheckDate(updateComplianceLogsRequest.getCheckDate());

		// Retrieve the PortRequest object associated with the request and set it in the
		// ComplianceLogs object
		PortRequest portRequest = portRequestService.getPortRequest(updateComplianceLogsRequest.getPortRequestId());
		complianceLogs.setPortRequest(portRequest);

		// Update the compliance log and retrieve the updated object
		ComplianceLogs complianceLog = complianceLogsService.UpdateLog(complianceLogs);
		loggers.info(QueryMapper.UPDATE_LOG_SUCCESSFULL);

		// Return the updated compliance log in the response
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

}
