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

import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.request.GetVerificationDetailsByPhn;
import com.project.npp.entities.request.UpdateVerificationDetails;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.utilities.JioVerificationDetailsService;

@RestController
@RequestMapping("/api/jiocomplianceofficer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class JioComplianceOfficerController {
	
	private static Logger loggers = LogManager.getLogger(JioComplianceOfficerController.class);
	
	@Autowired
	private JioVerificationDetailsService verificationDetailsService;

	// API end point to add the verification details
	@GetMapping("/addverificationdetails")
	public ResponseEntity<String> addVerificationDetails() throws FileNotFoundException, IOException {
		String message = verificationDetailsService.fetchVerificationDetails();
		loggers.info(QueryMapper.VERIFICATION_DETAILS);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	//API end point to update verification details
	@PostMapping("/updateverificationdetails")
	public ResponseEntity<JioVerificationDetails> updateVerificationDetails(@RequestBody UpdateVerificationDetails details) throws VerificationDetailsNotFoundException {
		JioVerificationDetails vDetails = new JioVerificationDetails();
		vDetails.setContractualObligationsMet(details.getContractualObligationsMet());
		vDetails.setCustomerIdentityVerified(details.getCustomerIdentityVerified());
		vDetails.setNoOutstandingPayments(details.getNoOutstandingPayments());
		vDetails.setNotificationToCurrentOperator(details.getNotificationToCurrentOperator());
		vDetails.setNumberStatus(details.getNumberStatus());
		vDetails.setPhoneNumber(details.getPhoneNumber());
		vDetails.setTimeSinceLastPort(details.getTimeSinceLastPort());
		JioVerificationDetails updatedDetails = verificationDetailsService.updateVerificationDetails(vDetails);
		loggers.info(QueryMapper.UPDATE_VERIFICATION_DETAILS);
		return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
	}

	//API end point to get all verification details
	@GetMapping("/getallverificationdetails")
	public ResponseEntity<List<JioVerificationDetails>> getAllVerificationDetails()
			throws VerificationDetailsNotFoundException {
		List<JioVerificationDetails> details = verificationDetailsService.getAll();
		loggers.info(QueryMapper.GET_VERIFICATION_DETAILS);
		return new ResponseEntity<>(details, HttpStatus.OK);
	}

	//API end point to get all verification details by phone number
	@PostMapping("/getverificationdetailsbyphn")
	public ResponseEntity<JioVerificationDetails> getVerificationDetailsByPhn(@RequestBody GetVerificationDetailsByPhn details) throws VerificationDetailsNotFoundException, LogNotFoundException {
		JioVerificationDetails vDetails = verificationDetailsService.getByPhoneNumber(details.getPhoneNumber());
		loggers.info(QueryMapper.GET_VERIFICATION_DETAILS);
		return new ResponseEntity<>(vDetails, HttpStatus.OK);
	}

}