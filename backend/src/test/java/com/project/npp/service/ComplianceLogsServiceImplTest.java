package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.npp.controller.ComplianceOfficerController;
import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.NumberStatus;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.repositories.ComplianceLogsRepository;
import com.project.npp.utilities.AirtelVerificationDetailsService;
import com.project.npp.utilities.JioVerificationDetailsService;
import com.project.npp.utilities.VerificationDetailsConversion;

@ExtendWith(MockitoExtension.class)
public class ComplianceLogsServiceImplTest {

    @Mock
    private ComplianceLogsRepository repo;

    @Mock
    private PortRequestService portRequestService;

    @Mock
    private AirtelVerificationDetailsService airtelVerificationDetailsService;

    @Mock
    private JioVerificationDetailsService jioVerificationDetailsService;

    @Mock
    private OperatorService operatorService;

    @Mock
    private VerificationDetailsConversion convert;

    @InjectMocks
    private ComplianceLogsServiceImpl complianceLogsService;
    
    @InjectMocks
    private ComplianceOfficerController complicanceOfficerController;
  

    private Customer customer;
    private Operator operatorJio;
    private Operator operatorAirtel;
    private PortRequest portRequest;
    private ComplianceLogs complianceLog;

    @BeforeEach
    void setUp() {
        // Initialize test data
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCurrentOperator(new Operator(1, "jio", "jio"));

        operatorJio = new Operator(1, "jio", "jio");
        operatorAirtel = new Operator(2, "airtel", "airtel");

        portRequest = new PortRequest();
        portRequest.setRequestId(1);
        portRequest.setCustomer(customer);

        complianceLog = new ComplianceLogs();
        complianceLog.setLogId(1);
        complianceLog.setPortRequest(portRequest);
    }

    @Test
    public void testAddLog() throws PortRequestNotFoundException, CustomerNotFoundException {
        when(repo.save(any(ComplianceLogs.class))).thenReturn(complianceLog);

        ComplianceLogs savedLog = complianceLogsService.addLog(complianceLog);

        assertNotNull(savedLog);
        assertEquals(1, savedLog.getLogId());
        verify(repo, times(1)).save(any(ComplianceLogs.class));
    }

    @Test
    public void testGetLogById() throws LogNotFoundException {
        when(repo.findById(1)).thenReturn(Optional.of(complianceLog));

        ComplianceLogs retrievedLog = complianceLogsService.getLog(1);

        assertNotNull(retrievedLog);
        assertEquals(1, retrievedLog.getLogId());
        verify(repo, times(1)).findById(1);
    }

    @Test
    public void testGetLogByIdLogNotFoundException() throws LogNotFoundException {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(LogNotFoundException.class, () -> {
            complianceLogsService.getLog(1);
        });
        verify(repo, times(1)).findById(1);
    }


    @Test
    public void testUpdateLogCheckPassed() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        complianceLog.setCheckPassed(true);
        when(repo.findById(anyInt())).thenReturn(Optional.of(complianceLog));
        when(portRequestService.getPortRequest(anyInt())).thenReturn(portRequest);

        ComplianceLogs updatedLog = complianceLogsService.UpdateLog(complianceLog);

        assertTrue(updatedLog.isCheckPassed());
        
        verify(repo, times(1)).save(any(ComplianceLogs.class));
        verify(portRequestService, times(1)).updatePortRequest(portRequest);
    }


    @Test
    public void testUpdateLogCheckNotPassed() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        complianceLog.setCheckPassed(false);
        when(repo.findById(anyInt())).thenReturn(Optional.of(complianceLog));
        when(portRequestService.getPortRequest(anyInt())).thenReturn(portRequest);

        ComplianceLogs updatedLog = complianceLogsService.UpdateLog(complianceLog);

        assertFalse(updatedLog.isCheckPassed());
        assertNull(updatedLog.getCheckDate());
        verify(repo, times(1)).save(any(ComplianceLogs.class));
        verify(portRequestService, never()).updatePortRequest(any(PortRequest.class));
    }
    
    @Test
    public void testUpdateLogWithNullLog() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        when(repo.findById(anyInt())).thenReturn(Optional.empty()); // Simulate scenario where log is not found

        assertThrows(LogNotFoundException.class, () -> {
            complianceLogsService.UpdateLog(complianceLog);
        });

        verify(portRequestService, never()).getPortRequest(anyInt()); // Ensure portRequestService method not called
        verify(repo, never()).save(any(ComplianceLogs.class)); // Ensure repo.save() not called
        verify(portRequestService, never()).updatePortRequest(any(PortRequest.class)); 
    }
    
    @Test
    public void testVerifyAndUpdateLog_JioSuccess() throws VerificationDetailsNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        Operator op1 = new Operator(1, "jio", "jio@gmail.com");
        Operator op2 = new Operator(2, "airtel", "airtel@gmail.com");
        log.setCustomer(new Customer(1, "JohnDoe", "John Doe","JohnDoe@gmail.com", 1234567890L, op1, op2, Status.PENDING));
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        JioVerificationDetails vDetails = new JioVerificationDetails(1234567890L,true, true,190, NumberStatus.ACTIVE, 10, true);

        try {
            String result = complianceLogsService.verifyAndUpdateLog(logId);
            assertEquals(QueryMapper.LOG_UPDATE_SUCCESSFUL, result);
        } catch (VerificationDetailsNotFoundException | LogNotFoundException | PortRequestNotFoundException | CustomerNotFoundException | OperatorNotFoundException e) {
            e.getMessage();
        }
    }
    
    @Test
    public void testVerifyAndUpdateLog_Jio_CustomerIdentityNotVerified() throws VerificationDetailsNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        Operator op1 = new Operator(1, "jio", "jio@gmail.com");
        Operator op2 = new Operator(2, "airtel", "airtel@gmail.com");
        log.setCustomer(new Customer(1, "JohnDoe", "John Doe", "JohnDoe@gmail.com", 1234567890L, op1, op2, Status.PENDING));
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        
        JioVerificationDetails vDetails = new JioVerificationDetails(1234567890L, false, true, 190, NumberStatus.ACTIVE, 10, true);
        
        try {
            String result = complianceLogsService.verifyAndUpdateLog(logId);
            assertEquals(QueryMapper.CUSTOMER_IDENTITY, result);
            assertFalse(log.isCheckPassed()); 
            assertNotNull(log.getCheckDate()); 
        } catch (VerificationDetailsNotFoundException | LogNotFoundException | PortRequestNotFoundException | CustomerNotFoundException | OperatorNotFoundException e) {
            e.getMessage();
        }
    }
    
    @Test
    public void testVerifyAndUpdateLog_AirtelSuccess() throws VerificationDetailsNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        Operator op1 = new Operator(1, "jio", "jio@gmail.com");
        Operator op2 = new Operator(2, "airtel", "airtel@gmail.com");
        log.setCustomer(new Customer(1, "JohnDoe", "John Doe","JohnDoe@gmail.com", 1234567890L, op1, op2, Status.PENDING));
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        AirtelVerificationDetails vDetails = new AirtelVerificationDetails(1234567890L,true, true,190, NumberStatus.ACTIVE, 10, true);
        
        try {
            String result = complianceLogsService.verifyAndUpdateLog(logId);
            assertEquals(QueryMapper.LOG_UPDATE_SUCCESSFUL, result);
        } catch (VerificationDetailsNotFoundException | LogNotFoundException | PortRequestNotFoundException | CustomerNotFoundException | OperatorNotFoundException e) {
            e.getMessage();
        }
    }
    
    @Test
    public void testVerifyAndUpdateLog_Airtel_NumberStatusNotActive() throws VerificationDetailsNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        Operator op1 = new Operator(1, "jio", "jio@gmail.com");
        Operator op2 = new Operator(2, "airtel", "airtel@gmail.com");
        log.setCustomer(new Customer(1, "JohnDoe", "John Doe", "JohnDoe@gmail.com", 1234567890L, op1, op2, Status.PENDING));
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        
        AirtelVerificationDetails vDetails = new AirtelVerificationDetails(1234567890L, true, true, 190, NumberStatus.DEACTIVATED, 10, true);
        
        try {
            String result = complianceLogsService.verifyAndUpdateLog(logId);
            assertEquals(QueryMapper.NUMBER_STATUS, result);
            assertFalse(log.isCheckPassed()); 
            assertNotNull(log.getCheckDate()); 
        } catch (VerificationDetailsNotFoundException | LogNotFoundException | PortRequestNotFoundException | CustomerNotFoundException | OperatorNotFoundException e) {
            e.getMessage();
        }
    }
    
    @Test
    public void testVerifyAndUpdateLog_LogNotFoundException() throws VerificationDetailsNotFoundException {
        Integer logId = 1;
        when(repo.findById(logId)).thenReturn(Optional.empty()); // Simulate LogNotFoundException
        
        try {
            complianceLogsService.verifyAndUpdateLog(logId);
            fail("Expected LogNotFoundException was not thrown");
        } catch (LogNotFoundException e) {
            assertEquals(QueryMapper.CANNOT_GET_LOG, e.getMessage());
        } catch (Exception e) {
            e.getClass().getSimpleName();
        }
        
        verify(repo, times(1)).findById(logId);
        verifyNoMoreInteractions(repo, portRequestService, operatorService, jioVerificationDetailsService, airtelVerificationDetailsService);
    }
    
    @Test
    public void testVerifyAndUpdateLog_PortRequestNotFoundException() throws VerificationDetailsNotFoundException, LogNotFoundException, CustomerNotFoundException, PortRequestNotFoundException, OperatorNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        log.setLogId(logId);
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        
        try {
            complianceLogsService.verifyAndUpdateLog(logId);
            fail("Expected PortRequestNotFoundException was not thrown");
        } catch (PortRequestNotFoundException e) {
            assertEquals(QueryMapper.CANNOT_GET_PORTREQUEST, e.getMessage());
        } catch (Exception e) {
            e.getClass().getSimpleName();
        }
        
        verify(repo, times(1)).findById(logId);
        verifyNoMoreInteractions(repo, portRequestService, operatorService, jioVerificationDetailsService, airtelVerificationDetailsService);
    }
    
    @Test
    public void testVerifyAndUpdateLog_CustomerNotFoundException() throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        log.setLogId(logId);
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        
        try {
            complianceLogsService.verifyAndUpdateLog(logId);
            fail("Expected CustomerNotFoundException was not thrown");
        } catch (CustomerNotFoundException e) {
            assertEquals(QueryMapper.CANNOT_GET_CUSTOMER, e.getMessage());
        } catch (Exception e) {
            e.getClass().getSimpleName();
        }
        
        verify(repo, times(1)).findById(logId);
        verifyNoMoreInteractions(repo, portRequestService, operatorService, jioVerificationDetailsService, airtelVerificationDetailsService);
    }
    
    @Test
    public void testVerifyAndUpdateLog_OperatorNotFoundException() throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        Operator op1 = new Operator(1, "jio", "jio@gmail.com");
        Operator op2 = new Operator(2, "airtel", "airtel@gmail.com");
        log.setCustomer(new Customer(1, "JohnDoe", "John Doe", "JohnDoe@gmail.com", 1234567890L, op1, op2, Status.PENDING));
        log.setLogId(logId);
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        
        try {
            complianceLogsService.verifyAndUpdateLog(logId);
            fail("Expected OperatorNotFoundException was not thrown");
        } catch (OperatorNotFoundException e) {
            assertEquals(QueryMapper.CANNOT_GET_OPERATOR, e.getMessage());
        } catch (Exception e) {
            e.getClass().getSimpleName();
        }
        
        verify(repo, times(1)).findById(logId);
    }
    
    @Test
    public void testVerifyAndUpdateLog_VerificationDetailsNotFoundException_Jio() throws Exception {
        Integer logId = 1;
        ComplianceLogs log = new ComplianceLogs();
        Operator op1 = new Operator(1, "jio", "jio@gmail.com");
        Operator op2 = new Operator(2, "airtel", "airtel@gmail.com");
        log.setCustomer(new Customer(1, "JohnDoe", "John Doe", "JohnDoe@gmail.com", 1234567890L, op1, op2, Status.PENDING));
        when(repo.findById(logId)).thenReturn(Optional.of(log));
        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(op1);
        when(jioVerificationDetailsService.getByPhoneNumber(anyLong())).thenThrow(new VerificationDetailsNotFoundException("Verification details not found"));
        
        try {
            complianceLogsService.verifyAndUpdateLog(logId);
            fail("Expected VerificationDetailsNotFoundException was not thrown");
        } catch (VerificationDetailsNotFoundException e) {
            assertEquals("Verification details not found", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type thrown: " + e.getClass().getSimpleName());
        }
        
        verify(repo, times(1)).findById(logId);
    }


    @Test
    public void testGetAllComplianceLogs() throws LogNotFoundException {
        List<ComplianceLogs> logs = new ArrayList<>();
        logs.add(complianceLog);
        when(repo.findAll()).thenReturn(logs);

        List<ComplianceLogs> retrievedLogs = complianceLogsService.getAllComplianceLogs();

        assertFalse(retrievedLogs.isEmpty());
        assertEquals(1, retrievedLogs.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    public void testGetAllComplianceLogsEmptyList() {
        List<ComplianceLogs> logs = new ArrayList<>();
        when(repo.findAll()).thenReturn(logs);

        assertThrows(LogNotFoundException.class, () -> {
            complianceLogsService.getAllComplianceLogs();
        });

        verify(repo, times(1)).findAll();
    }
  
    @Test
    public void testGetLogByCustomer() throws LogNotFoundException {
        List<ComplianceLogs> logs = new ArrayList<>();
        logs.add(complianceLog);
        when(repo.findByCustomer(any(Customer.class))).thenReturn(logs);

        ComplianceLogs retrievedLog = complianceLogsService.getLogByCustomer(customer);

        assertNotNull(retrievedLog);
        assertEquals(1, retrievedLog.getLogId());
        verify(repo, times(1)).findByCustomer(customer);
    }

    @Test
    public void testGetLogByCustomerLogNotFoundException() {
        when(repo.findByCustomer(any(Customer.class))).thenReturn(new ArrayList<>());

        assertThrows(LogNotFoundException.class, () -> {
            complianceLogsService.getLogByCustomer(customer);
        });

        verify(repo, times(1)).findByCustomer(customer);
    }
}
