package com.project.npp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.request.GetStatusRequest;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.CustomerService;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private ComplianceLogsService complianceLogsService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController controller;

    private GetStatusRequest getStatusRequest;
    private Customer customer;
    private ComplianceLogs complianceLog;

    @BeforeEach
    void setUp() {
        getStatusRequest = new GetStatusRequest();
        getStatusRequest.setUsername("johndoe");

        customer = new Customer();
        customer.setUsername("johndoe");

        complianceLog = new ComplianceLogs();
        complianceLog.setCheckPassed(true);
        complianceLog.setNotes("All checks passed.");
        complianceLog.setCheckDate(LocalDate.now());
    }

    @Test
    void testGetStatus_Success() throws CustomerNotFoundException, LogNotFoundException {
        when(customerService.getCustomerByUserName(anyString())).thenReturn(customer);
        when(complianceLogsService.getLogByCustomer(customer)).thenReturn(complianceLog);

        ResponseEntity<Map<String, Object>> responseEntity = controller.getStatus(getStatusRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Porting Request is Successful", responseEntity.getBody().get("status"));
        assertEquals("All checks passed.", responseEntity.getBody().get("notes"));
        assertNotNull(responseEntity.getBody().get("lastUpdated"));
    }

    @Test
    void testGetStatus_LogNotFound() throws CustomerNotFoundException, LogNotFoundException {
        when(customerService.getCustomerByUserName(anyString())).thenReturn(customer);
        when(complianceLogsService.getLogByCustomer(customer)).thenThrow(new LogNotFoundException());

        ResponseEntity<Map<String, Object>> responseEntity = controller.getStatus(getStatusRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Log Not Found", responseEntity.getBody().get("status"));
    }

    @Test
    void testGetStatus_CustomerNotFound() throws CustomerNotFoundException {
        when(customerService.getCustomerByUserName(anyString())).thenThrow(new CustomerNotFoundException());

        assertThrows(CustomerNotFoundException.class, () -> {
            controller.getStatus(getStatusRequest);
        });
    }
}
