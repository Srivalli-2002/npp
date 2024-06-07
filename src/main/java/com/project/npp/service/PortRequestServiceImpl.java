package com.project.npp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.npp.entities.Customer;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.repositories.PortRequestRepository;

@Service
public class PortRequestServiceImpl implements PortRequestService {
	
	private static Logger loggers = LogManager.getLogger(PortRequestServiceImpl.class);

	@Autowired
	private PortRequestRepository repo;
	
	@Autowired
	private CustomerService customerService;
	
	// Method to add a new port request
	@Override
	public PortRequest addPortRequest(PortRequest portRequest) {
		
		// Set initial status and flags
		portRequest.setComplianceChecked(false);
		portRequest.setApprovalStatus(Status.PENDING);
		portRequest.setCompletionDate(null);
		PortRequest portReq= repo.save(portRequest);
		loggers.info(QueryMapper.ADD_PORTREQUEST);
		return portReq;
	}

	// Method to get a port request by ID
	@Override
	public PortRequest getPortRequest(Integer portRequestId)throws PortRequestNotFoundException {
		Optional<PortRequest> portReq=repo.findById(portRequestId);
		if(portReq.isPresent())
		{
			loggers.info(QueryMapper.GET_PORTREQUEST);
			return portReq.get();
		}
		else
			loggers.error(QueryMapper.CANNOT_GET_PORTREQUEST);
			throw new  PortRequestNotFoundException(QueryMapper.CANNOT_GET_PORTREQUEST);
	}

	// Method to update a port request
	@Override
	public PortRequest updatePortRequest(PortRequest portRequest) throws CustomerNotFoundException, PortRequestNotFoundException {
		Optional<PortRequest> p=repo.findById(portRequest.getRequestId());
		if(p.isPresent())
		{
		   if(portRequest.getComplianceChecked())
		   {
			// If compliance is checked, update status and completion date
			portRequest.setApprovalStatus(Status.COMPLETED);
			portRequest.setCompletionDate(LocalDate.now());
			
			// Update customer status
			Customer customer= customerService.getCustomerById(portRequest.getCustomer().getCustomerId());
			customer.setStatus(Status.COMPLETED);
			customerService.updateCustomer(customer);
			PortRequest portReq= repo.save(portRequest);
			loggers.info(QueryMapper.UPDATE_PORTREQUEST);
			return portReq;
		}
		else 
		{
			// If compliance is not checked, reset status and completion date
			portRequest.setApprovalStatus(Status.PENDING);
		    portRequest.setCompletionDate(null);
		    PortRequest portReq= repo.save(portRequest);
		    loggers.info(QueryMapper.CANNOT_UPDATE_PORTREQUEST);
			return portReq;
		}
		
		}
		else 
			loggers.error(QueryMapper.CANNOT_UPDATE_PORTREQUEST);
			throw new  PortRequestNotFoundException(QueryMapper.CANNOT_UPDATE_PORTREQUEST);
	}

	// Method to delete a port request by ID
	@Override
	public String deletePortRequest(Integer portRequestId) throws PortRequestNotFoundException {
		Optional<PortRequest> portReq=repo.findById(portRequestId);
		if(portReq.isPresent())
		{
			repo.deleteById(portRequestId);
			loggers.info(QueryMapper.DELETE_PORTREQUEST);
			return "Deleted Successfully!!";
		}
		else
			loggers.error(QueryMapper.CANNOT_DELETE_PORTREQUEST);
			throw new  PortRequestNotFoundException(QueryMapper.CANNOT_DELETE_PORTREQUEST);
	}

	// Method to get all port requests
	@Override
	public List<PortRequest> getAllPortRequest() throws PortRequestNotFoundException {
		List<PortRequest> portRequests= (List<PortRequest>) repo.findAll();
		if(!portRequests.isEmpty()){
			loggers.info(QueryMapper.GET_PORTREQUEST);
			return portRequests;
		}
		else 
			loggers.error(QueryMapper.CANNOT_GET_PORTREQUEST);
			throw new PortRequestNotFoundException(QueryMapper.GET_PORTREQUEST);
	}

}