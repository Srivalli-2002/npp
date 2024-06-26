package com.project.npp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "number_porting_compliance ")
public class VerificationDetails {
	
	@Id
	private Long phoneNumber ;
	
	private Boolean customerIdentityVerified;
	
	private Boolean noOutstandingPayments;
	
	private Integer timeSinceLastPort;
	
	private NumberStatus numberStatus;
	
	private Integer contractualObligationsMet;
	
	private Boolean notificationToCurrentOperator;
	
	

}
