package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.npp.entities.Customer;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.repositories.PortRequestRepository;
import com.project.npp.utilities.AirtelVerificationDetailsService;
import com.project.npp.utilities.JioVerificationDetailsService;

@ExtendWith(MockitoExtension.class)
public class PortRequestServiceImplTest {

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

    @InjectMocks
    private PortRequestServiceImpl portRequestService;

    private PortRequest portRequest;
    private Customer customer;
    private Operator operator;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setUsername("testuser");
        customer.setPhoneNumber(1234567890L);
        customer.setStatus(Status.PENDING);

        operator = new Operator();
        operator.setOperatorId(1);
        operator.setOperatorName("jio");

        portRequest = new PortRequest();
        portRequest.setRequestId(1);
        portRequest.setCustomer(customer);
        portRequest.setApprovalStatus(Status.PENDING);
    }

    @Test
    public void addPortRequest_savesPortRequest() {
        when(portRequestRepository.save(portRequest)).thenReturn(portRequest);

        PortRequest result = portRequestService.addPortRequest(portRequest);

        assertEquals(portRequest, result);
        verify(portRequestRepository).save(portRequest);
    }

    @Test
    public void getPortRequest_existingId_returnsPortRequest() throws PortRequestNotFoundException {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));

        PortRequest result = portRequestService.getPortRequest(1);

        assertEquals(portRequest, result);
        verify(portRequestRepository).findById(1);
    }

    @Test
    public void getPortRequest_nonExistingId_throwsPortRequestNotFoundException() {
        when(portRequestRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PortRequestNotFoundException.class, () -> portRequestService.getPortRequest(1));
    }

//    @Test
//    public void updatePortRequest_existingPortRequest_updatesPortRequest() throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException, OperatorNotFoundException, VerificationDetailsNotFoundException {
//        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));
//        when(customerService.getCustomerById(1)).thenReturn(customer);
//        when(portRequestRepository.save(portRequest)).thenReturn(portRequest);
//        when(operatorService.getOperatorByOperatorName("jio")).thenReturn(operator);
//
//        portRequest.setApprovalStatus(Status.COMPLETED);
//
//        PortRequest result = portRequestService.updatePortRequest(portRequest);
//
//        assertEquals(portRequest, result);
//        verify(customerService).updateCustomer(customer);
//        verify(portRequestRepository).save(portRequest);
//        verify(jioService).delete(1234567890L);
//    }

    @Test
    public void updatePortRequest_nonExistingPortRequest_throwsPortRequestNotFoundException() {
        when(portRequestRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PortRequestNotFoundException.class, () -> portRequestService.updatePortRequest(portRequest));
    }

    @Test
    public void deletePortRequest_existingId_deletesPortRequest() throws PortRequestNotFoundException {
        when(portRequestRepository.findById(1)).thenReturn(Optional.of(portRequest));

        String result = portRequestService.deletePortRequest(1);

        assertEquals("Deleted Successfully!!", result);
        verify(portRequestRepository).deleteById(1);
    }

    @Test
    public void deletePortRequest_nonExistingId_throwsPortRequestNotFoundException() {
        when(portRequestRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PortRequestNotFoundException.class, () -> portRequestService.deletePortRequest(1));
    }

    @Test
    public void getAllPortRequest_returnsAllPortRequests() throws PortRequestNotFoundException {
        List<PortRequest> allPortRequests = Arrays.asList(portRequest);
        when(portRequestRepository.findAll()).thenReturn(allPortRequests);

        List<PortRequest> result = portRequestService.getAllPortRequest();

        assertEquals(allPortRequests, result);
        verify(portRequestRepository).findAll();
    }

    @Test
    public void getAllPortRequest_noPortRequestsFound_throwsPortRequestNotFoundException() {
        when(portRequestRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(PortRequestNotFoundException.class, () -> portRequestService.getAllPortRequest());
    }
}