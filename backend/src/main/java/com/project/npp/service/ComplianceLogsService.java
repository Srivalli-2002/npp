package com.project.npp.service;

import java.util.List;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;

public interface ComplianceLogsService {
	
	// Add log
	public ComplianceLogs addLog(ComplianceLogs complianceLogs) throws PortRequestNotFoundException, CustomerNotFoundException;
	
	// Get log
	public ComplianceLogs getLog(Integer LogId) throws LogNotFoundException;
	public ComplianceLogs getLogByCustomer(Customer customer) throws LogNotFoundException;
	//public ComplianceLogs getLogByCustomer(Customer customer) throws LogNotFoundException;
	
	// Update log
	public ComplianceLogs UpdateLog(ComplianceLogs complianceLogs) throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException;
	
	// Get all logs
	public List<ComplianceLogs> getAllComplianceLogs() throws LogNotFoundException;
	
	public String VerifyAndUpdateLog(Integer logId) throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, VerificationDetailsNotFoundException ;
}

