package com.project.npp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.npp.entities.Operator;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.repositories.OperatorRepository;

@Service
public class OperatorServiceImpl implements OperatorService {

	private static Logger loggers = LogManager.getLogger(OperatorServiceImpl.class);

	@Autowired
	private OperatorRepository repo;

	// Method to add a new operator
	@Override
	public Operator addOperator(Operator operator) {
		loggers.info(QueryMapper.ADD_OPERATOR);
		return repo.save(operator);
	}

	// Method to get an operator by ID
	@Override
	public Operator getOperatorById(Integer id) throws OperatorNotFoundException {
		Optional<Operator> operator = repo.findById(id);
		if (operator.isPresent()) {
			loggers.info(QueryMapper.GET_OPERATOR);
			return operator.get();
		} else
			loggers.error(QueryMapper.CANNOT_GET_OPERATOR);
		throw new OperatorNotFoundException(QueryMapper.CANNOT_GET_OPERATOR);
	}

	// Method to update an existing operator
	@Override
	public Operator updateOperator(Operator operator) throws OperatorNotFoundException {
		Optional<Operator> op = repo.findById(operator.getOperatorId());
		if (op.isPresent()) {
			loggers.info(QueryMapper.UPDATE_CUSTOMER);
			return repo.save(operator);
		} else
			loggers.error(QueryMapper.CANNOT_UPDATE_CUSTOMER);
		throw new OperatorNotFoundException(QueryMapper.CANNOT_UPDATE_OPERATOR);
	}

	// Method to delete an operator by ID
	@Override
	public String deleteOperator(Integer id) throws OperatorNotFoundException {
		Optional<Operator> operator = repo.findById(id);
		if (operator.isPresent()) {
			repo.deleteById(id);
			loggers.info(QueryMapper.DELETE_CUSTOMER);
			return "Deleted Successfully!!";
		} else
			loggers.error(QueryMapper.CANNOT_DELETE_CUSTOMER);
		throw new OperatorNotFoundException(QueryMapper.CANNOT_DELETE_OPERATOR);
	}

	// Method to get all operators
	@Override
	public List<Operator> getAllOperators() throws OperatorNotFoundException {
		List<Operator> allOperators = (List<Operator>) repo.findAll();
		if (!allOperators.isEmpty()) {
			loggers.info(QueryMapper.GET_OPERATOR);
			return allOperators;
		} else
			loggers.error(QueryMapper.CANNOT_GET_OPERATOR);
		throw new OperatorNotFoundException(QueryMapper.CANNOT_GET_OPERATOR);
	}

	@Override
	public Operator getOperatorByOperatorName(String operatorName) throws OperatorNotFoundException {
		Optional<Operator> operator = repo.findByOperatorName(operatorName);
		if (operator.isPresent()) {
			loggers.info(QueryMapper.GET_OPERATOR);
			return operator.get();
		} else
			loggers.error(QueryMapper.CANNOT_GET_OPERATOR);
		throw new OperatorNotFoundException(QueryMapper.CANNOT_GET_OPERATOR);
	}

}