package com.project.npp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.npp.repositories.ComplianceLogsRepository;
import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.PortRequest;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptionmessages.QueryMapper;

@Service
public class ComplianceLogsServiceImpl implements ComplianceLogsService {
	
	private static Logger loggers = LogManager.getLogger(ComplianceLogsServiceImpl.class);

	@Autowired
	private ComplianceLogsRepository repo; 
	
	@Autowired
	private PortRequestService portRequestService;
	
	// Method to add a compliance log
	@Override
	public ComplianceLogs addLog(ComplianceLogs complianceLogs) throws PortRequestNotFoundException, CustomerNotFoundException {
		
		// Check if the compliance check is passed
		if(complianceLogs.isCheckPassed())
		{
			PortRequest portRequest= portRequestService.getPortRequest(complianceLogs.getPortRequest().getRequestId());
			portRequest.setComplianceChecked(true);
			PortRequest portReq= portRequestService.updatePortRequest(portRequest);
			complianceLogs.setPortRequest(portReq);
			ComplianceLogs complianceLog=repo.save(complianceLogs);
			loggers.info(QueryMapper.LOG_CHECK_SUCCESSFULL);
			return complianceLog;
		}
		else {
			PortRequest portRequest= portRequestService.getPortRequest(complianceLogs.getPortRequest().getRequestId());
			complianceLogs.setPortRequest(portRequest);
			ComplianceLogs complianceLog=repo.save(complianceLogs);
			loggers.error(QueryMapper.LOG_CHECK_UNSUCCESSFULL);
			return complianceLog;
		}
	}

	// Method to get a compliance log by its ID
	@Override
	public ComplianceLogs getLog(Integer LogId) throws LogNotFoundException{
		Optional<ComplianceLogs> complianceLog=repo.findById(LogId);
		loggers.info(QueryMapper.GET_LOG);
		if(complianceLog.isPresent()) return complianceLog.get();
		else 
			loggers.error(QueryMapper.CANNOT_GET_LOG);
			throw new LogNotFoundException(QueryMapper.CANNOT_GET_LOG);
	}

	// Method to update a compliance log
	@Override
	public ComplianceLogs UpdateLog(ComplianceLogs complianceLogs) throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException {
		Optional<ComplianceLogs> complianceLog=repo.findById(complianceLogs.getLogId());
		if(complianceLog.isPresent()) 
			{
			
			if(complianceLogs.isCheckPassed())
			{
				PortRequest portRequest= portRequestService.getPortRequest(complianceLogs.getPortRequest().getRequestId());
				portRequest.setComplianceChecked(true);
				PortRequest portReq= portRequestService.updatePortRequest(portRequest);
				complianceLogs.setPortRequest(portReq);
			}
			else {
				PortRequest portRequest= portRequestService.getPortRequest(complianceLogs.getPortRequest().getRequestId());
				complianceLogs.setPortRequest(portRequest);
			}
			
			 repo.save(complianceLogs);
			 loggers.info(QueryMapper.UPDATE_LOG);
			 return complianceLog.get();
			}
		else 
		{
			loggers.error(QueryMapper.CANNOT_UPDATE_LOG);
			throw new  LogNotFoundException(QueryMapper.CANNOT_UPDATE_LOG);
		}
	}

	// Method to get all logs
	@Override
	public List<ComplianceLogs> getAllComplianceLogs() throws LogNotFoundException {
		List<ComplianceLogs> complianceLogs = (List<ComplianceLogs>) repo.findAll();
		if(!complianceLogs.isEmpty()) {
			loggers.info(QueryMapper.GET_LOG);
			return complianceLogs;
		}
		else 
		{
			loggers.error(QueryMapper.CANNOT_GET_LOG);
			throw new LogNotFoundException(QueryMapper.CANNOT_GET_LOG);
		}
	}

}
