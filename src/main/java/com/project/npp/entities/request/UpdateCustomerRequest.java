package com.project.npp.entities.request;

import com.project.npp.entities.Status;

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
public class UpdateCustomerRequest {
	
	private Integer customerId;
	
	private String name;
	
	private String email;
	
	private Long  phoneNumber;
	
	private Integer currentOperatorId;
	
	private Integer newOperatorId;
	
	private Status status;
}