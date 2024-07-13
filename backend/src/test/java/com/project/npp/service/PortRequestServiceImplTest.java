package com.project.npp.service;
 
import com.project.npp.entities.*;
import com.project.npp.exceptions.*;
import com.project.npp.repositories.PortRequestRepository;
import com.project.npp.utilities.AirtelVerificationDetailsService;
import com.project.npp.utilities.JioVerificationDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
public class PortRequestServiceImplTest {
 
    @InjectMocks
    private PortRequestServiceImpl portRequestService;
 
    @Mock
    private PortRequestRepository portRequestRepository;
 
    @Mock
    private CustomerService customerService;
 
    @Mock
    private AirtelVerificationDetailsService airtelService;
 
    @Mock
    private JioVerificationDetailsService jioService;
 
    @Mock
    private OperatorService operatorService;
 
    private PortRequest portRequest;
    private Customer customer;
    private Operator operatorJio;
    private Operator operatorAirtel;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
 
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setPhoneNumber(1234567890L);
 
        operatorJio = new Operator();
        operatorJio.setOperatorId(1);
        operatorJio.setOperatorName("jio");
 
        operatorAirtel = new Operator();
        operatorAirtel.setOperatorId(2);
        operatorAirtel.setOperatorName("airtel");
 
        // Set currentOperator for the customer
        customer.setCurrentOperator(operatorJio);
 
        portRequest = new PortRequest();
        portRequest.setRequestId(1);
        portRequest.setCustomer(customer);
        portRequest.setApprovalStatus(Status.PENDING);
        portRequest.setComplianceChecked(false);
    }
 
    @Test
    public void testAddPortRequest() {
        when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);
 
        PortRequest savedPortRequest = portRequestService.addPortRequest(portRequest);
 
        assertNotNull(savedPortRequest);
        assertEquals(Status.PENDING, savedPortRequest.getApprovalStatus());
        verify(portRequestRepository, times(1)).save(portRequest);
    }
 
    @Test
    public void testGetPortRequest() throws PortRequestNotFoundException {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
 
        PortRequest foundPortRequest = portRequestService.getPortRequest(1);
 
        assertNotNull(foundPortRequest);
        assertEquals(1, foundPortRequest.getRequestId());
        verify(portRequestRepository, times(1)).findById(1);
    }
 
    @Test
    public void testGetPortRequestNotFound() {
        when(portRequestRepository.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(PortRequestNotFoundException.class, () -> {
            portRequestService.getPortRequest(1);
        });
        verify(portRequestRepository, times(1)).findById(1);
    }
    
    @Test
    public void testUpdatePortRequestCompleted_Jio() throws Exception {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
        when(customerService.getCustomerById(1)).thenReturn(customer);
        when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);
        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(operatorJio);
        when(operatorService.getOperatorByOperatorName("airtel")).thenReturn(operatorAirtel);

        portRequest.setApprovalStatus(Status.COMPLETED);

        PortRequest updatedPortRequest = portRequestService.updatePortRequest(portRequest);

        assertNotNull(updatedPortRequest);
        assertEquals(Status.COMPLETED, updatedPortRequest.getApprovalStatus());
        assertEquals(LocalDate.now(), updatedPortRequest.getCompletionDate());
        verify(portRequestRepository, times(1)).save(portRequest);
        verify(jioService, times(1)).delete(customer.getPhoneNumber());
        verify(airtelService, never()).delete(anyLong());
    }

    @Test
    public void testUpdatePortRequestCompleted_Airtel() throws Exception {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
        when(customerService.getCustomerById(1)).thenReturn(customer);
        when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);
        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(operatorJio);
        when(operatorService.getOperatorByOperatorName("airtel")).thenReturn(operatorAirtel);

        portRequest.setApprovalStatus(Status.COMPLETED);
        customer.setCurrentOperator(operatorAirtel);

        PortRequest updatedPortRequest = portRequestService.updatePortRequest(portRequest);

        assertNotNull(updatedPortRequest);
        assertEquals(Status.COMPLETED, updatedPortRequest.getApprovalStatus());
        assertEquals(LocalDate.now(), updatedPortRequest.getCompletionDate());
        verify(portRequestRepository, times(1)).save(portRequest);
        verify(airtelService, times(1)).delete(customer.getPhoneNumber());
        verify(jioService, never()).delete(anyLong());
    }

    @Test
    public void testUpdatePortRequestRejected() throws Exception {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
        when(customerService.getCustomerById(1)).thenReturn(customer);
        when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);

        portRequest.setApprovalStatus(Status.REJECTED);

        PortRequest updatedPortRequest = portRequestService.updatePortRequest(portRequest);

        assertNotNull(updatedPortRequest);
        assertEquals(Status.REJECTED, updatedPortRequest.getApprovalStatus());
        assertEquals(LocalDate.now(), updatedPortRequest.getCompletionDate());
        verify(portRequestRepository, times(1)).save(portRequest);
    }

    @Test
    public void testUpdatePortRequestPending() throws Exception {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
        when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);

        portRequest.setApprovalStatus(Status.PENDING);

        PortRequest updatedPortRequest = portRequestService.updatePortRequest(portRequest);

        assertNotNull(updatedPortRequest);
        assertEquals(Status.PENDING, updatedPortRequest.getApprovalStatus());
        assertEquals(null, updatedPortRequest.getCompletionDate());
        verify(portRequestRepository).save(portRequest);
    }

    @Test
    public void testUpdatePortRequestNotFound() {
        when(portRequestRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PortRequestNotFoundException.class, () -> {
            portRequestService.updatePortRequest(portRequest);
        });
        verify(portRequestRepository, never()).save(any(PortRequest.class));
    }

    @Test
    public void testUpdatePortRequestOperatorNotFoundException() throws Exception {
        portRequest.setApprovalStatus(Status.COMPLETED);

        // Set a current operator that is neither Jio nor Airtel
        Operator unknownOperator = new Operator();
        unknownOperator.setOperatorName("unknown");
        customer.setCurrentOperator(unknownOperator);

        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
        when(customerService.getCustomerById(1)).thenReturn(customer);
        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(operatorJio);
        when(operatorService.getOperatorByOperatorName("airtel")).thenReturn(operatorAirtel);

        assertThrows(OperatorNotFoundException.class, () -> {
            portRequestService.updatePortRequest(portRequest);
        });
    }

 
    @Test
    public void testDeletePortRequest() throws PortRequestNotFoundException {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
        doNothing().when(portRequestRepository).deleteById(1);
 
        String result = portRequestService.deletePortRequest(1);
 
        assertEquals("Deleted Successfully!!", result);
        verify(portRequestRepository, times(1)).deleteById(1);
    }
 
    @Test
    public void testDeletePortRequestNotFound() {
        when(portRequestRepository.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(PortRequestNotFoundException.class, () -> {
            portRequestService.deletePortRequest(1);
        });
        verify(portRequestRepository, times(1)).findById(1);
    }
 
    @Test
    public void testGetAllPortRequest() throws PortRequestNotFoundException {
        List<PortRequest> portRequests = new ArrayList<>();
        portRequests.add(portRequest);
 
        when(portRequestRepository.findAll()).thenReturn(portRequests);
 
        List<PortRequest> result = portRequestService.getAllPortRequest();
 
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(portRequestRepository, times(1)).findAll();
    }
 
    @Test
    public void testGetAllPortRequestNotFound() {
        when(portRequestRepository.findAll()).thenReturn(new ArrayList<>());
 
        assertThrows(PortRequestNotFoundException.class, () -> {
            portRequestService.getAllPortRequest();
        });
        verify(portRequestRepository, times(1)).findAll();
    }
}