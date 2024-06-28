package com.project.npp.utilities;

import org.springframework.stereotype.Service;

import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.VerificationDetails;

@Service
public class VerificationDetailsConversion {
	
	// Convert Verification details to Airtel verification details
	public static AirtelVerificationDetails convertToAirtelVerificationDetails(
			VerificationDetails verificationDetails) {
		AirtelVerificationDetails airtelDetails = new AirtelVerificationDetails();
		airtelDetails.setPhoneNumber(verificationDetails.getPhoneNumber());
		airtelDetails.setCustomerIdentityVerified(verificationDetails.getCustomerIdentityVerified());
		airtelDetails.setNoOutstandingPayments(verificationDetails.getNoOutstandingPayments());
		airtelDetails.setTimeSinceLastPort(verificationDetails.getTimeSinceLastPort());
		airtelDetails.setNumberStatus(verificationDetails.getNumberStatus());
		airtelDetails.setContractualObligationsMet(verificationDetails.getContractualObligationsMet());
		airtelDetails.setNotificationToCurrentOperator(verificationDetails.getNotificationToCurrentOperator());
		return airtelDetails;
	}

	// Convert Airtel verification details to Verification details
	public static VerificationDetails convertToVerificationDetails(AirtelVerificationDetails airtelDetails) {
		VerificationDetails verificationDetails = new VerificationDetails();
		verificationDetails.setPhoneNumber(airtelDetails.getPhoneNumber());
		verificationDetails.setCustomerIdentityVerified(airtelDetails.getCustomerIdentityVerified());
		verificationDetails.setNoOutstandingPayments(airtelDetails.getNoOutstandingPayments());
		verificationDetails.setTimeSinceLastPort(airtelDetails.getTimeSinceLastPort());
		verificationDetails.setNumberStatus(airtelDetails.getNumberStatus());
		verificationDetails.setContractualObligationsMet(airtelDetails.getContractualObligationsMet());
		verificationDetails.setNotificationToCurrentOperator(airtelDetails.getNotificationToCurrentOperator());
		return verificationDetails;
	}

	// Convert Jio verification details to Verification details
	public static VerificationDetails convertToVerificationDetails(JioVerificationDetails jioDetails) {
		VerificationDetails verificationDetails = new VerificationDetails();
		verificationDetails.setPhoneNumber(jioDetails.getPhoneNumber());
		verificationDetails.setCustomerIdentityVerified(jioDetails.getCustomerIdentityVerified());
		verificationDetails.setNoOutstandingPayments(jioDetails.getNoOutstandingPayments());
		verificationDetails.setTimeSinceLastPort(jioDetails.getTimeSinceLastPort());
		verificationDetails.setNumberStatus(jioDetails.getNumberStatus());
		verificationDetails.setContractualObligationsMet(jioDetails.getContractualObligationsMet());
		verificationDetails.setNotificationToCurrentOperator(jioDetails.getNotificationToCurrentOperator());
		return verificationDetails;
	}

	// Convert Verification details to Jio verification details
	public static JioVerificationDetails convertToJioVerificationDetails(VerificationDetails verificationDetails) {
		JioVerificationDetails jioDetails = new JioVerificationDetails();
		jioDetails.setPhoneNumber(verificationDetails.getPhoneNumber());
		jioDetails.setCustomerIdentityVerified(verificationDetails.getCustomerIdentityVerified());
		jioDetails.setNoOutstandingPayments(verificationDetails.getNoOutstandingPayments());
		jioDetails.setTimeSinceLastPort(verificationDetails.getTimeSinceLastPort());
		jioDetails.setNumberStatus(verificationDetails.getNumberStatus());
		jioDetails.setContractualObligationsMet(verificationDetails.getContractualObligationsMet());
		jioDetails.setNotificationToCurrentOperator(verificationDetails.getNotificationToCurrentOperator());
		return jioDetails;
	}

}