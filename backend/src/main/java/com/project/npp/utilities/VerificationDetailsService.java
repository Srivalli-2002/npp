package com.project.npp.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.npp.entities.NumberStatus;
import com.project.npp.entities.VerificationDetails;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.repositories.VerificationDetailsRepository;
@Service
public class VerificationDetailsService {
	
	@Autowired
	VerificationDetailsRepository repo;

	public String  fetchVerificationDetails() throws FileNotFoundException, IOException{
		String file = "number_porting_compliance.csv";
    	var path = Paths.get("src/main/resources/data/", file).toString()	;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	String[] parts = line.split(",");
            	Long phoneNumber = Long.parseLong(parts[0]);
            	Boolean customerIdentityVerified = Boolean.parseBoolean(parts[1]);
            	Boolean noOutstandingPayments = Boolean.parseBoolean(parts[2]);
            	Integer timeSinceLastPort = Integer.parseInt(parts[3]);
            	NumberStatus numberStatus = NumberStatus.valueOf(parts[4]);
            	Integer contractualObligationsMet= Integer.parseInt(parts[5]);
            	Boolean notificationToCurrentOperator=Boolean.parseBoolean(parts[6]);
                
            	VerificationDetails details = new VerificationDetails();
                
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
        return("Added Verification Details!");
		
	}
	
	public VerificationDetails getByPhoneNumber(Long phoneNumber) throws VerificationDetailsNotFoundException
	{
		Optional<VerificationDetails> details=repo.findById(phoneNumber);
		if(details.isPresent())
		{
			return details.get();
		}
		else throw new VerificationDetailsNotFoundException("No Verification Details Found");
		
	}
	
	public VerificationDetails updateVerificationDetails(VerificationDetails details) throws VerificationDetailsNotFoundException
	{
		Optional<VerificationDetails> Vdetails=repo.findById(details.getPhoneNumber());
		if(Vdetails.isPresent())
		{
			VerificationDetails updatedDetails= repo.save(details);
			return updatedDetails;
		}
		else throw new VerificationDetailsNotFoundException("No Verification Details Found");
		
	}
	
	public List<VerificationDetails> getAll() throws VerificationDetailsNotFoundException
	{
		List<VerificationDetails> details= (List<VerificationDetails>) repo.findAll();
		if(!details.isEmpty())
		{
			return details;
		}
		else throw new VerificationDetailsNotFoundException("No Verification Details Found");
		
	}
	
	
}
