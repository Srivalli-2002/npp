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

import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.NumberStatus;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.repositories.AirtelVerificationDetailsRepository;

@Service
public class AirtelVerificationDetailsService {
	
	private static Logger loggers = LogManager.getLogger(AirtelVerificationDetailsService.class);
	
	@Autowired
	AirtelVerificationDetailsRepository repo;

	// Method to fetch verification details
	public String fetchVerificationDetails() throws FileNotFoundException, IOException {
		String file = "airtel_verification_details.csv";
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
				AirtelVerificationDetails details = new AirtelVerificationDetails();
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

	// Method to get airtel verification details by phone number
	public AirtelVerificationDetails getByPhoneNumber(Long phoneNumber) throws VerificationDetailsNotFoundException {
		Optional<AirtelVerificationDetails> details = repo.findById(phoneNumber);
		if (details.isPresent()) {
			loggers.info(QueryMapper.VERIFICATION_DETAILS);
			return details.get();
		}
		else
		{
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException(QueryMapper.NO_VERIFICATION_DETAILS);
		}
	}

	// Method to update airtel verification details
	public AirtelVerificationDetails updateVerificationDetails(AirtelVerificationDetails details)
			throws VerificationDetailsNotFoundException {
		Optional<AirtelVerificationDetails> Vdetails = repo.findById(details.getPhoneNumber());
		if (Vdetails.isPresent()) {
			AirtelVerificationDetails updatedDetails = repo.save(details);
			loggers.info(QueryMapper.VERIFICATION_DETAILS);
			return updatedDetails;
		} else
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException(QueryMapper.NO_VERIFICATION_DETAILS);
	}

	// Method to get all airtel verification details
	public List<AirtelVerificationDetails> getAll() throws VerificationDetailsNotFoundException {
		List<AirtelVerificationDetails> details = (List<AirtelVerificationDetails>) repo.findAll();
		if (!details.isEmpty()) {
			loggers.info(QueryMapper.VERIFICATION_DETAILS);
			return details;
		} else
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException(QueryMapper.NO_VERIFICATION_DETAILS);
	}
	
	public String delete(Long phoneNumber) throws VerificationDetailsNotFoundException
	{
		Optional<AirtelVerificationDetails> Vdetails = repo.findById(phoneNumber);
		if (Vdetails.isPresent()) {
			repo.deleteById(phoneNumber);
			return "Deleted Successfull";
		} else
		{
			loggers.error(QueryMapper.NO_VERIFICATION_DETAILS);
			throw new VerificationDetailsNotFoundException("Airtel Verification Details Not Found");
		}
	}

}