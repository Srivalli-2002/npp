package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.entities.VerificationDetails;
import com.project.npp.exceptions.*;

import com.project.npp.repositories.ComplianceLogsRepository;
import com.project.npp.service.*;
import com.project.npp.utilities.AirtelVerificationDetailsService;
import com.project.npp.utilities.JioVerificationDetailsService;
import com.project.npp.utilities.VerificationDetailsConversion;

@ExtendWith(MockitoExtension.class)
class ComplianceLogsServiceImplTest {

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

    private ComplianceLogs testLog;
    private Customer testCustomer;
    private PortRequest testPortRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(/* initialize with necessary fields */);
        testPortRequest = new PortRequest(/* initialize with necessary fields */);
        testLog = new ComplianceLogs(/* initialize with necessary fields */);
    }

    @Test
    void testAddLog() throws PortRequestNotFoundException, CustomerNotFoundException {
        // Mocking behavior of repository save method
        when(repo.save(any(ComplianceLogs.class))).thenReturn(testLog);

        // Calling the service method
        ComplianceLogs addedLog = complianceLogsService.addLog(testLog);

        // Assertions
        assertNotNull(addedLog);
        assertEquals(testLog, addedLog);
    }

    @Test
    void testGetLogById() throws LogNotFoundException {
        // Mocking behavior of repository findById method
        when(repo.findById(anyInt())).thenReturn(Optional.of(testLog));

        // Calling the service method
        ComplianceLogs foundLog = complianceLogsService.getLog(1);

        // Assertions
        assertNotNull(foundLog);
        assertEquals(testLog, foundLog);
    }

    @Test
    void testGetLogByIdLogNotFoundException() {
        // Mocking behavior of repository findById method returning empty Optional
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(LogNotFoundException.class, () -> complianceLogsService.getLog(1));
    }

    @Test
    void testUpdateLog() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        // Mocking behavior of dependencies
        when(repo.findById(anyInt())).thenReturn(Optional.of(testLog));
        when(portRequestService.getPortRequest(anyInt())).thenReturn(testPortRequest);
        when(portRequestService.updatePortRequest(any(PortRequest.class))).thenReturn(testPortRequest);

        // Calling the service method
        ComplianceLogs updatedLog = complianceLogsService.UpdateLog(testLog);

        // Assertions
        assertNotNull(updatedLog);
        assertEquals(testLog, updatedLog);
    }

    @Test
    void testUpdateLogLogNotFoundException() {
        // Mocking behavior of repository findById method returning empty Optional
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(LogNotFoundException.class, () -> complianceLogsService.UpdateLog(testLog));
    }

    @Test
    void testGetAllComplianceLogs() throws LogNotFoundException {
        // Mocking behavior of repository findAll method
        List<ComplianceLogs> logs = new ArrayList<>();
        logs.add(testLog);
        when(repo.findAll()).thenReturn(logs);

        // Calling the service method
        List<ComplianceLogs> foundLogs = complianceLogsService.getAllComplianceLogs();

        // Assertions
        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());
        assertEquals(logs.size(), foundLogs.size());
    }

    @Test
    void testGetAllComplianceLogsLogNotFoundException() {
        // Mocking behavior of repository findAll method returning empty list
        when(repo.findAll()).thenReturn(new ArrayList<>());

        // Assertions
        assertThrows(LogNotFoundException.class, () -> complianceLogsService.getAllComplianceLogs());
    }

    @Test
    void testVerifyAndUpdateLog() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, VerificationDetailsNotFoundException, OperatorNotFoundException {
        // Mocking behavior for various service and repository calls
        when(repo.findById(anyInt())).thenReturn(Optional.of(testLog));
        when(operatorService.getOperatorByOperatorName(anyString())).thenReturn(new Operator(/* initialize operator */));
        when(jioVerificationDetailsService.getByPhoneNumber(anyLong())).thenReturn(new JioVerificationDetails(/* initialize verification details */));
        when(airtelVerificationDetailsService.getByPhoneNumber(anyLong())).thenReturn(new AirtelVerificationDetails(/* initialize verification details */));
        when(convert.convertToAirtelVerificationDetails(any())).thenReturn(new AirtelVerificationDetails(/* initialize verification details */));
        when(convert.convertToJioVerificationDetails(any())).thenReturn(new JioVerificationDetails(/* initialize verification details */));
        when(portRequestService.updatePortRequest(any(PortRequest.class))).thenReturn(testPortRequest);

        // Calling the service method
        String result = complianceLogsService.VerifyAndUpdateLog(1);

        // Assertions
        assertNotNull(result);
        // Add specific assertions based on expected behavior from VerifyAndUpdateLog method
    }

    @Test
    void testVerifyAndUpdateLogLogNotFoundException() {
        // Mocking behavior of repository findById method returning empty Optional
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(LogNotFoundException.class, () -> complianceLogsService.VerifyAndUpdateLog(anyInt()));
    }

    @Test
    void testGetLogByCustomer() throws LogNotFoundException {
        // Mocking behavior of repository findByCustomer method
        List<ComplianceLogs> logs = new ArrayList<>();
        logs.add(testLog);
        when(repo.findByCustomer(any())).thenReturn(logs);

        // Calling the service method
        ComplianceLogs foundLog = complianceLogsService.getLogByCustomer(testCustomer);

        // Assertions
        assertNotNull(foundLog);
        assertEquals(testLog, foundLog);
    }

    @Test
    void testGetLogByCustomerLogNotFoundException() {
        // Mocking behavior of repository findByCustomer method returning empty list
        when(repo.findByCustomer(any())).thenReturn(new ArrayList<>());

        // Assertions
        assertThrows(LogNotFoundException.class, () -> complianceLogsService.getLogByCustomer(testCustomer));
    }
}