//package com.project.npp.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import com.project.npp.entities.AirtelVerificationDetails;
//import com.project.npp.entities.ComplianceLogs;
//import com.project.npp.entities.Customer;
//import com.project.npp.entities.JioVerificationDetails;
//import com.project.npp.entities.Operator;
//import com.project.npp.entities.VerificationDetails;
//import com.project.npp.entities.request.GetLogRequest;
//import com.project.npp.entities.request.GetVerificationDetails;
//import com.project.npp.entities.request.GetVerificationDetailsByPhn;
//import com.project.npp.entities.request.UpdateComplianceLogsRequest;
//import com.project.npp.exceptions.CustomerNotFoundException;
//import com.project.npp.exceptions.LogNotFoundException;
//import com.project.npp.exceptions.OperatorNotFoundException;
//import com.project.npp.exceptions.PortRequestNotFoundException;
//import com.project.npp.exceptions.VerificationDetailsNotFoundException;
//import com.project.npp.service.ComplianceLogsService;
//import com.project.npp.service.CustomerService;
//import com.project.npp.service.OperatorService;
//import com.project.npp.utilities.AirtelVerificationDetailsService;
//import com.project.npp.utilities.JioVerificationDetailsService;
//import com.project.npp.utilities.VerificationDetailsConversion;
//
//@ExtendWith(MockitoExtension.class)
//public class ComplianceOfficerControllerTest {
//
//    @InjectMocks
//    private ComplianceOfficerController complianceOfficerController;
//
//    @Mock
//    private ComplianceLogsService complianceLogsService;
//
//    @Mock
//    private JioVerificationDetailsService jioVerificationDetailsService;
//
//    @Mock
//    private AirtelVerificationDetailsService airtelVerificationDetailsService;
//
//    @Mock
//    private OperatorService operatorService;
//
//    @Mock
//    private VerificationDetailsConversion convert;
//
//    @Mock
//    private CustomerService customerService;
//
//    private ComplianceLogs complianceLog;
//    private Customer customer;
//    private Operator operatorJio;
//    private Operator operatorAirtel;
//
//    @BeforeEach
//    void setUp() {
//        customer = new Customer();
//        customer.setPhoneNumber(1234567890L);
//
//        complianceLog = new ComplianceLogs();
//        complianceLog.setCustomer(customer);
//
//        operatorJio = new Operator();
//        operatorJio.setOperatorName("jio");
//
//        operatorAirtel = new Operator();
//        operatorAirtel.setOperatorName("airtel");
//    }
//
//    @Test
//    void testUpdateLog() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException, VerificationDetailsNotFoundException, OperatorNotFoundException {
//        UpdateComplianceLogsRequest request = new UpdateComplianceLogsRequest();
//        request.setLogId(1);
//
//        when(complianceLogsService.VerifyAndUpdateLog(1)).thenReturn("Log updated successfully");
//
//        ResponseEntity<String> response = complianceOfficerController.updateLog(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Log updated successfully", response.getBody());
//    }
//
//    @Test
//    void testGetLog() throws LogNotFoundException {
//        GetLogRequest request = new GetLogRequest();
//        request.setLogId(1);
//
//        when(complianceLogsService.getLog(1)).thenReturn(complianceLog);
//
//        ResponseEntity<ComplianceLogs> response = complianceOfficerController.getLog(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(complianceLog, response.getBody());
//    }
//
//    @Test
//    void testGetAllComplianceLogs() throws LogNotFoundException {
//        List<ComplianceLogs> logs = new ArrayList<>();
//        logs.add(complianceLog);
//
//        when(complianceLogsService.getAllComplianceLogs()).thenReturn(logs);
//
//        ResponseEntity<List<ComplianceLogs>> response = complianceOfficerController.getAllComplianceLogs();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(logs, response.getBody());
//    }
//
//    @Test
//    void testGetVerificationDetailsByLogId_Airtel() throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException {
//        GetVerificationDetails request = new GetVerificationDetails();
//        request.setLogId(1);
//
//        when(complianceLogsService.getLog(1)).thenReturn(complianceLog);
//        when(operatorService.getOperatorByOperatorName("airtel")).thenReturn(operatorAirtel);
//        when(complianceLog.getCustomer().getCurrentOperator()).thenReturn(operatorAirtel);
//
//        AirtelVerificationDetails airtelDetails = new AirtelVerificationDetails();
//        VerificationDetails verificationDetails = new VerificationDetails();
//        when(airtelVerificationDetailsService.getByPhoneNumber(1234567890L)).thenReturn(airtelDetails);
//        when(convert.convertToVerificationDetails(airtelDetails)).thenReturn(verificationDetails);
//
//        ResponseEntity<VerificationDetails> response = complianceOfficerController.getVerificationDetails(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(verificationDetails, response.getBody());
//    }
//
//    @Test
//    void testGetVerificationDetailsByLogId_Jio() throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException {
//        GetVerificationDetails request = new GetVerificationDetails();
//        request.setLogId(1);
//
//        when(complianceLogsService.getLog(1)).thenReturn(complianceLog);
//        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(operatorJio);
//        when(complianceLog.getCustomer().getCurrentOperator()).thenReturn(operatorJio);
//
//        JioVerificationDetails jioDetails = new JioVerificationDetails();
//        VerificationDetails verificationDetails = new VerificationDetails();
//        when(jioVerificationDetailsService.getByPhoneNumber(1234567890L)).thenReturn(jioDetails);
//        when(convert.convertToVerificationDetails(jioDetails)).thenReturn(verificationDetails);
//
//        ResponseEntity<VerificationDetails> response = complianceOfficerController.getVerificationDetails(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(verificationDetails, response.getBody());
//    }
//
//    @Test
//    void testGetVerificationDetailsByPhoneNumber_Airtel() throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException, CustomerNotFoundException {
//        GetVerificationDetailsByPhn request = new GetVerificationDetailsByPhn();
//        request.setPhoneNumber(1234567890L);
//
//        when(customerService.getCustomerByPhoneNumber(1234567890L)).thenReturn(customer);
//        when(operatorService.getOperatorByOperatorName("airtel")).thenReturn(operatorAirtel);
//        when(customer.getCurrentOperator()).thenReturn(operatorAirtel);
//
//        AirtelVerificationDetails airtelDetails = new AirtelVerificationDetails();
//        VerificationDetails verificationDetails = new VerificationDetails();
//        when(airtelVerificationDetailsService.getByPhoneNumber(1234567890L)).thenReturn(airtelDetails);
//        when(convert.convertToVerificationDetails(airtelDetails)).thenReturn(verificationDetails);
//
//        ResponseEntity<VerificationDetails> response = complianceOfficerController.getVerificationDetailsByPhn(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(verificationDetails, response.getBody());
//    }
//
//    @Test
//    void testGetVerificationDetailsByPhoneNumber_Jio() throws VerificationDetailsNotFoundException, LogNotFoundException, OperatorNotFoundException, CustomerNotFoundException {
//        GetVerificationDetailsByPhn request = new GetVerificationDetailsByPhn();
//        request.setPhoneNumber(1234567890L);
//
//        when(customerService.getCustomerByPhoneNumber(1234567890L)).thenReturn(customer);
//        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(operatorJio);
//        when(customer.getCurrentOperator()).thenReturn(operatorJio);
//
//        JioVerificationDetails jioDetails = new JioVerificationDetails();
//        VerificationDetails verificationDetails = new VerificationDetails();
//        when(jioVerificationDetailsService.getByPhoneNumber(1234567890L)).thenReturn(jioDetails);
//        when(convert.convertToVerificationDetails(jioDetails)).thenReturn(verificationDetails);
//
//        ResponseEntity<VerificationDetails> response = complianceOfficerController.getVerificationDetailsByPhn(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(verificationDetails, response.getBody());
//    }
//}
