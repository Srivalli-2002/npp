package com.project.npp.entities.request;

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
public class CustomerRequest {
	
	private String name;
	
	private String username;
	
	private String email; 
	
	private Long  phoneNumber; 
	
}
