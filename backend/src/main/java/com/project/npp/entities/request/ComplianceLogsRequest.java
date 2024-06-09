package com.project.npp.entities.request;

import java.time.LocalDate;
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
public class ComplianceLogsRequest {
	
	private Integer portRequestId;
	
	private boolean checkPassed;
	
	private String notes;
	
	private LocalDate checkDate;
}
