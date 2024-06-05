package com.project.npp.service;

import com.project.npp.entities.Operator;
import com.project.npp.exceptions.OperatorNotFoundException;

public interface OperatorService {
	
	// Add operator
	public Operator addOperator(Operator operator);
	
	// Get operator
	public Operator getOperatorById(Integer id) throws OperatorNotFoundException;
	
	// Update operator
	public Operator updateOperator(Operator operator) throws OperatorNotFoundException;
	
	// Delete operator
	public String deleteOperator(Integer id) throws OperatorNotFoundException;
}
