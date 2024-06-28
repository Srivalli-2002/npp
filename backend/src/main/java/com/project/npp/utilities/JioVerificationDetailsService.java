package com.project.npp.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.NumberStatus;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.repositories.JioVerificationDetailsRepository;

@Service
public class JioVerificationDetailsService {

	private static Logger loggers = LogManager.getLogger(JioVerificationDetailsService.class);
	
	@Autowired
	JioVerificationDetailsRepository repo;

	// Method to fetch verification details
	public String fetchVerificationDetails() throws FileNotFoundException, IOException {
		String file = "jio_verification_details.csv";
		var path = Paths.get("src/main/resources/data/", file).toString();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				Long phoneNumber = Long.parseLong(parts[0]);
				Boolean customerIdentityVerified = Boolean.parseBoolean(parts[1]);
				Boolean noOutstandingPayments = Boolean.parseBoolean(parts[2]);
				Integer timeSinceLastPort = Integer.parseInt(parts[3]);
				NumberStatus numberStatus = NumberStatus.valueOf(parts[4]);
				Integer contractualObligationsMet = Integer.parseInt(parts[5]);
				Boolean notificationToCurrentOperator = Boolean.parseBoolean(parts[6]);
				JioVerificationDetails details = new JioVerificationDetails();
				details.setPhoneNumber(phoneNumber);
				details.setCustomerIdentityVerified(customerIdentityVerified);
				details.setNoOutstandingPayments(noOutstandingPayments);
				details.setTimeSinceLastPort(timeSinceLastPort);
				details.setContractualObligationsMet(contractualObligationsMet);
				details.setNotificationToCurrentOperator(notificationToCurrentOperator);
				details.setNumberStatus(numberStatus);
				repo.save(details);
			}
		}
		loggers.info(QueryMapper.VERIFICATION_DETAILS);
		return (QueryMapper.VERIFICATION_DETAILS);
	}

	// Method to get jio verification details by phone number
	public JioVerificationDetails getByPhoneNumber(Long phoneNumber) throws VerificationDetailsNotFoundException {
		Optional<JioVerificationDetails> details = repo.findById(phoneNumber);
		if (details.isPresent()) {
			loggers.info(QueryMapper.VERIFICATION_DETAILS);
			return details.get();
		} else
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException(QueryMapper.NO_VERIFICATION_DETAILS);
	}

	// Method to update jio verification details
	public JioVerificationDetails updateVerificationDetails(JioVerificationDetails details)
			throws VerificationDetailsNotFoundException {
		Optional<JioVerificationDetails> Vdetails = repo.findById(details.getPhoneNumber());
		if (Vdetails.isPresent()) {
			JioVerificationDetails updatedDetails = repo.save(details);
			loggers.info(QueryMapper.VERIFICATION_DETAILS);
			return updatedDetails;
		} else
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException(QueryMapper.NO_VERIFICATION_DETAILS);
	}

	// Method to get all verification details
	public List<JioVerificationDetails> getAll() throws VerificationDetailsNotFoundException {
		List<JioVerificationDetails> details = (List<JioVerificationDetails>) repo.findAll();
		if (!details.isEmpty()) {
			loggers.info(QueryMapper.VERIFICATION_DETAILS);
			return details;
		} else
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException(QueryMapper.NO_VERIFICATION_DETAILS);
	}

}