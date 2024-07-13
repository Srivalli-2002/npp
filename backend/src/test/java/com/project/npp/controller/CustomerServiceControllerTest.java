package com.project.npp.controller;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import com.project.npp.entities.Customer;
import com.project.npp.entities.NumberStatus;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.UserEntity;
import com.project.npp.entities.request.CustomerRequest;
import com.project.npp.entities.request.GetCustomerRequest;
import com.project.npp.entities.request.GetPortRequest;
import com.project.npp.entities.request.UpdateCustomerRequest;
import com.project.npp.entities.request.UpdatePortRequest;
import com.project.npp.entities.request.UserPortRequest;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.CustomerService;
import com.project.npp.service.OperatorService;
import com.project.npp.service.PortRequestService;
import com.project.npp.service.UserEntityService;
 
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
 
@ExtendWith(MockitoExtension.class)
public class CustomerServiceControllerTest {
 
    @Mock
    private OperatorService operatorService;
 
    @Mock
    private CustomerService customerService;
 
    @Mock
    private PortRequestService portRequestService;
 
    @Mock
    private ComplianceLogsService complianceLogsService;
 
    @Mock
    private UserEntityService userService;
 
    @InjectMocks
    private CustomerServiceController controller;
    
    private CustomerRequest customerRequest;
    private UpdateCustomerRequest updateCustomerRequest;
    private UserPortRequest userPortRequest;
    private GetCustomerRequest getCustomerRequest;
    private GetPortRequest getPortRequest;
    private UpdatePortRequest updatePortRequest;
    private PortRequest portRequest1;
    private PortRequest portRequest2;
 
    @BeforeEach
    void setUp() {
    	
        customerRequest = new CustomerRequest();
 
        updateCustomerRequest = new UpdateCustomerRequest();
 
        userPortRequest = new UserPortRequest("johndoe", LocalDate.now());
 
        getCustomerRequest = new GetCustomerRequest();
 
        getPortRequest = new GetPortRequest();
 
        updatePortRequest = new UpdatePortRequest();
        
        portRequest1 = new PortRequest();
        portRequest1.setRequestId(1);
        // Set other properties of portRequest1 if needed

        portRequest2 = new PortRequest();
        portRequest2.setRequestId(2);
    }
 
    @Test
    void testAddCustomer() throws OperatorNotFoundException, RoleNotFoundException {
        when(userService.findByUsername(any())).thenReturn(Optional.of(new UserEntity()));
 
        ResponseEntity<Customer> responseEntity = controller.addCustomer(customerRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
 
    @Test
    void testGetCustomer() throws CustomerNotFoundException {
        when(customerService.getCustomerById(any())).thenReturn(new Customer());
 
        ResponseEntity<Customer> responseEntity = controller.getCustomer(getCustomerRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    
    @Test
    void testGetCustomer_CustomerNotFound() throws CustomerNotFoundException {
        when(customerService.getCustomerById(any())).thenThrow(new CustomerNotFoundException());

        assertThrows(CustomerNotFoundException.class, () -> {
            controller.getCustomer(getCustomerRequest);
        });
    }
 
    @Test
    void testUpdateCustomer() throws OperatorNotFoundException, CustomerNotFoundException {
        when(operatorService.getOperatorByOperatorName(any())).thenReturn(new Operator());
        when(customerService.updateCustomer(any())).thenReturn(new Customer());
 
        ResponseEntity<Customer> responseEntity = controller.updateCustomer(updateCustomerRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
    
    @Test
    void testUpdateCustomer_OperatorNotFound() throws OperatorNotFoundException, CustomerNotFoundException {
        when(operatorService.getOperatorByOperatorName(any())).thenThrow(new OperatorNotFoundException());

        assertThrows(OperatorNotFoundException.class, () -> {
            controller.updateCustomer(updateCustomerRequest);
        });
    }
    
    @Test
    void testUpdateCustomer_CustomerNotFound() throws OperatorNotFoundException, CustomerNotFoundException {
        when(operatorService.getOperatorByOperatorName(any())).thenReturn(new Operator());
        when(customerService.updateCustomer(any())).thenThrow(new CustomerNotFoundException());

        assertThrows(CustomerNotFoundException.class, () -> {
            controller.updateCustomer(updateCustomerRequest);
        });
    }
 
    @Test
    void testDeleteCustomer() throws CustomerNotFoundException {
        when(customerService.deleteCustomerById(any())).thenReturn("Customer deleted successfully");
 
        ResponseEntity<String> responseEntity = controller.deleteCustomer(getCustomerRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Customer deleted successfully", responseEntity.getBody());
        // Add more assertions as per your application's behavior
    }
    
    @Test
    void testDeleteCustomer_CustomerNotFound() throws CustomerNotFoundException {
        when(customerService.deleteCustomerById(any())).thenThrow(new CustomerNotFoundException());

        assertThrows(CustomerNotFoundException.class, () -> {
            controller.deleteCustomer(getCustomerRequest);
        });
    }
 
    @Test
    void testGetAllCustomers() throws CustomerNotFoundException {
        List<Customer> mockCustomers = Arrays.asList(new Customer(), new Customer());
        when(customerService.getAllCustomers()).thenReturn(mockCustomers);
 
        ResponseEntity<List<Customer>> responseEntity = controller.getAllCustomer();
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        // Add more assertions as per your application's behavior
    }
    
    @Test
    void testGetAllCustomers_EmptyList() throws CustomerNotFoundException {
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Customer>> responseEntity = controller.getAllCustomer();

        assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody() != null ? responseEntity.getBody().size() : 0);
    }

    @Test
    void testSubmitPortRequest() throws CustomerNotFoundException, PortRequestNotFoundException {
        when(customerService.getCustomerByUserName(any())).thenReturn(new Customer());
        when(portRequestService.addPortRequest(any())).thenReturn(new PortRequest());
 
        ResponseEntity<PortRequest> responseEntity = controller.submitPortRequest(userPortRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions as per your application's behavior
    }
    
    @Test
    void testSubmitPortRequest_CustomerNotFound() throws CustomerNotFoundException, PortRequestNotFoundException {
        when(customerService.getCustomerByUserName(any())).thenThrow(new CustomerNotFoundException());

        assertThrows(CustomerNotFoundException.class, () -> {
            controller.submitPortRequest(userPortRequest);
        });
    }
    
    @Test
    void testGetPortRequest() throws PortRequestNotFoundException {
        when(portRequestService.getPortRequest(any())).thenReturn(new PortRequest());
 
        ResponseEntity<PortRequest> responseEntity = controller.getPortRequest(getPortRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions as per your application's behavior
    }
 
    @Test
    void testGetPortRequest_PortRequestNotFound() throws PortRequestNotFoundException {
        when(portRequestService.getPortRequest(any())).thenThrow(new PortRequestNotFoundException());

        assertThrows(PortRequestNotFoundException.class, () -> {
            controller.getPortRequest(getPortRequest);
        });
    }
    
    @Test
    void testUpdatePortRequest() throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        when(customerService.getCustomerByUserName(any())).thenReturn(new Customer());
        when(portRequestService.updatePortRequest(any())).thenReturn(new PortRequest());
 
        ResponseEntity<PortRequest> responseEntity = controller.updatePortRequest(updatePortRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions as per your application's behavior
    }
    
    @Test
    void testUpdatePortRequest_CustomerNotFound() throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        when(customerService.getCustomerByUserName(any())).thenThrow(new CustomerNotFoundException());

        assertThrows(CustomerNotFoundException.class, () -> {
            controller.updatePortRequest(updatePortRequest);
        });
    }
    
    @Test
    void testUpdatePortRequest_PortRequestNotFound() throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
        when(customerService.getCustomerByUserName(any())).thenReturn(new Customer());
        when(portRequestService.updatePortRequest(any())).thenThrow(new PortRequestNotFoundException());

        assertThrows(PortRequestNotFoundException.class, () -> {
            controller.updatePortRequest(updatePortRequest);
        });
    }
 
    @Test
    void testDeletePortRequest() throws PortRequestNotFoundException {
        when(portRequestService.deletePortRequest(any())).thenReturn("Port request deleted successfully");
 
        ResponseEntity<String> responseEntity = controller.deletePortRequest(getPortRequest);
 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Port request deleted successfully", responseEntity.getBody());
        // Add more assertions as per your application's behavior
    }
    
    @Test
    void testDeletePortRequest_PortRequestNotFound() throws PortRequestNotFoundException {
        when(portRequestService.deletePortRequest(any())).thenThrow(new PortRequestNotFoundException());

        assertThrows(PortRequestNotFoundException.class, () -> {
            controller.deletePortRequest(getPortRequest);
        });
    }

    @Test
    void testGetAllPortRequests() throws PortRequestNotFoundException {
        List<PortRequest> mockPortRequests = Arrays.asList(portRequest1, portRequest2);
        when(portRequestService.getAllPortRequest()).thenReturn(mockPortRequests);

        ResponseEntity<List<PortRequest>> responseEntity = controller.getAllPortRequests();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
        assertEquals(portRequest1, responseEntity.getBody().get(0));
        assertEquals(portRequest2, responseEntity.getBody().get(1));
    }

    @Test
    void testGetAllPortRequests_EmptyList() throws PortRequestNotFoundException {
        when(portRequestService.getAllPortRequest()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PortRequest>> responseEntity = controller.getAllPortRequests();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    void testGetAllPortRequests_PortRequestNotFoundException() throws PortRequestNotFoundException {
        when(portRequestService.getAllPortRequest()).thenThrow(new PortRequestNotFoundException());

        assertThrows(PortRequestNotFoundException.class, () -> {
            controller.getAllPortRequests();
        });
    }
}