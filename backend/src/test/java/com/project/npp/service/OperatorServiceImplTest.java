package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.npp.entities.Operator;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.repositories.OperatorRepository;

@ExtendWith(MockitoExtension.class)
public class OperatorServiceImplTest {

    @Mock
    private OperatorRepository operatorRepository;

    @InjectMocks
    private OperatorServiceImpl operatorService;

    private Operator operator;

    @BeforeEach
    public void setup() {
        operator = new Operator();
        operator.setOperatorId(1);
        operator.setOperatorName("testOperator");
    }

    @Test
    public void addOperator_savesOperator() {
        when(operatorRepository.save(operator)).thenReturn(operator);

        Operator result = operatorService.addOperator(operator);

        assertEquals(operator, result);
        verify(operatorRepository).save(operator);
    }

    @Test
    public void getOperatorById_existingId_returnsOperator() throws OperatorNotFoundException {
        when(operatorRepository.findById(1)).thenReturn(Optional.of(operator));

        Operator result = operatorService.getOperatorById(1);

        assertEquals(operator, result);
        verify(operatorRepository).findById(1);
    }

    @Test
    public void getOperatorById_nonExistingId_throwsOperatorNotFoundException() {
        when(operatorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> operatorService.getOperatorById(1));
    }

    @Test
    public void updateOperator_existingOperator_updatesOperator() throws OperatorNotFoundException {
        when(operatorRepository.findById(operator.getOperatorId())).thenReturn(Optional.of(operator));
        when(operatorRepository.save(operator)).thenReturn(operator);

        Operator result = operatorService.updateOperator(operator);

        assertEquals(operator, result);
        verify(operatorRepository).save(operator);
    }

    @Test
    public void updateOperator_nonExistingOperator_throwsOperatorNotFoundException() {
        when(operatorRepository.findById(operator.getOperatorId())).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> operatorService.updateOperator(operator));
    }

    @Test
    public void deleteOperator_existingId_deletesOperator() throws OperatorNotFoundException {
        when(operatorRepository.findById(1)).thenReturn(Optional.of(operator));

        String result = operatorService.deleteOperator(1);

        assertEquals("Deleted Successfully!!", result);
        verify(operatorRepository).deleteById(1);
    }

    @Test
    public void deleteOperator_nonExistingId_throwsOperatorNotFoundException() {
        when(operatorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> operatorService.deleteOperator(1));
    }

    @Test
    public void getAllOperators_returnsAllOperators() throws OperatorNotFoundException {
        List<Operator> allOperators = Arrays.asList(operator);
        when(operatorRepository.findAll()).thenReturn(allOperators);

        List<Operator> result = operatorService.getAllOperators();

        assertEquals(allOperators, result);
        verify(operatorRepository).findAll();
    }

    @Test
    public void getAllOperators_noOperatorsFound_throwsOperatorNotFoundException() {
        when(operatorRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(OperatorNotFoundException.class, () -> operatorService.getAllOperators());
    }

    @Test
    public void getOperatorByOperatorName_existingName_returnsOperator() throws OperatorNotFoundException {
        when(operatorRepository.findByOperatorName("testOperator")).thenReturn(Optional.of(operator));

        Operator result = operatorService.getOperatorByOperatorName("testOperator");

        assertEquals(operator, result);
        verify(operatorRepository).findByOperatorName("testOperator");
    }

    @Test
    public void getOperatorByOperatorName_nonExistingName_throwsOperatorNotFoundException() {
        when(operatorRepository.findByOperatorName("testOperator")).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> operatorService.getOperatorByOperatorName("testOperator"));
    }
}