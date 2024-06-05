package com.project.npp.service;

import com.project.npp.entities.PortRequest;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;

public interface PortRequestService {
	
	// Add port request
	public PortRequest addPortRequest(PortRequest portRequest);
	
	// Get port request
	public PortRequest getPortRequest(Integer portRequestId) throws PortRequestNotFoundException;
	
	// Update port request
	public PortRequest updatePortRequest(PortRequest portRequest)throws CustomerNotFoundException,PortRequestNotFoundException;

	// Delete port request
	public String deletePortRequest(Integer portRequestId) throws PortRequestNotFoundException;
}
