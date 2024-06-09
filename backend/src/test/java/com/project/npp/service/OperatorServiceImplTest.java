package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.project.npp.entities.Operator;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.repositories.OperatorRepository;

@SpringBootTest
public class OperatorServiceImplTest {

	@MockBean
	private OperatorRepository operatorRepository;

	@Autowired
	private OperatorService operatorService;

	@Test
	public void testAddOperator() {
		Operator operator = new Operator();
		operator.setOperatorId(1);
		operator.setOperatorName("Test Operator");

		when(operatorRepository.save(operator)).thenReturn(operator);

		Operator savedOperator = operatorService.addOperator(operator);

		assertEquals(operator, savedOperator);
	}

	@Test
	public void testGetOperatorById() throws OperatorNotFoundException {
		Operator operator = new Operator();
		operator.setOperatorId(1);
		operator.setOperatorName("Test Operator");

		when(operatorRepository.findById(1)).thenReturn(Optional.of(operator));

		Operator retrievedOperator = operatorService.getOperatorById(1);

		assertEquals(operator, retrievedOperator);
	}

	@Test
	public void testGetOperatorByIdNotFound() {
		when(operatorRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(OperatorNotFoundException.class, () -> {
			operatorService.getOperatorById(1);
		});
	}

	@Test
	public void testUpdateOperator() throws OperatorNotFoundException {
		Operator operator = new Operator();
		operator.setOperatorId(1);
		operator.setOperatorName("Updated Operator");

		when(operatorRepository.findById(1)).thenReturn(Optional.of(operator));
		when(operatorRepository.save(operator)).thenReturn(operator);

		Operator updatedOperator = operatorService.updateOperator(operator);

		assertEquals(operator, updatedOperator);
	}

	@Test
	public void testUpdateOperatorNotFound() {
		Operator operator = new Operator();
		operator.setOperatorId(1);
		operator.setOperatorName("Updated Operator");

		when(operatorRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(OperatorNotFoundException.class, () -> {
			operatorService.updateOperator(operator);
		});
	}

	@Test
	public void testDeleteOperator() throws OperatorNotFoundException {
		Operator operator = new Operator();
		operator.setOperatorId(1);
		operator.setOperatorName("Test Operator");

		when(operatorRepository.findById(1)).thenReturn(Optional.of(operator));

		String result = operatorService.deleteOperator(1);

		assertEquals("Deleted Successfully!!", result);
	}

	@Test
	public void testDeleteOperatorNotFound() {
		when(operatorRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(OperatorNotFoundException.class, () -> {
			operatorService.deleteOperator(1);
		});
	}

	@Test
	public void testGetAllOperators() throws OperatorNotFoundException {
		Operator operator1 = new Operator();
		operator1.setOperatorId(1);
		operator1.setOperatorName("Operator One");

		Operator operator2 = new Operator();
		operator2.setOperatorId(2);
		operator2.setOperatorName("Operator Two");

		List<Operator> operators = Arrays.asList(operator1, operator2);

		when(operatorRepository.findAll()).thenReturn(operators);

		List<Operator> retrievedOperators = operatorService.getAllOperators();

		assertEquals(operators, retrievedOperators);
	}

	@Test
	public void testGetAllOperatorsNotFound() {
		when(operatorRepository.findAll()).thenReturn(Collections.emptyList());

		assertThrows(OperatorNotFoundException.class, () -> {
			operatorService.getAllOperators();
		});
	}
}
