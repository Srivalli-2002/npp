package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.npp.entities.Customer;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.repositories.PortRequestRepository;

class PortRequestServiceImplTest {

	@Mock
	private PortRequestRepository portRequestRepository;

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private PortRequestServiceImpl portRequestService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddPortRequestSuccess() {
		PortRequest portRequest = new PortRequest();
		portRequest.setComplianceChecked(false);
		portRequest.setApprovalStatus(Status.PENDING);
		portRequest.setCompletionDate(null);

		when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);

		PortRequest result = portRequestService.addPortRequest(portRequest);

		assertEquals(false, result.getComplianceChecked());
		assertEquals(Status.PENDING, result.getApprovalStatus());
		assertNull(result.getCompletionDate());
		assertEquals(portRequest, result);
		verify(portRequestRepository, times(1)).save(portRequest);
	}

	@Test
	void testGetPortRequestSuccess() throws PortRequestNotFoundException {
		Integer portRequestId = 1;
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestId(portRequestId);

		when(portRequestRepository.findById(portRequestId)).thenReturn(Optional.of(portRequest));

		PortRequest result = portRequestService.getPortRequest(portRequestId);

		assertEquals(portRequest, result);
		verify(portRequestRepository, times(1)).findById(portRequestId);
	}

	@Test
	void testGetPortRequestNotFound() {
		Integer portRequestId = 1;

		when(portRequestRepository.findById(portRequestId)).thenReturn(Optional.empty());

		PortRequestNotFoundException exception = assertThrows(PortRequestNotFoundException.class, () -> {
			portRequestService.getPortRequest(portRequestId);
		});

		assertEquals(QueryMapper.CANNOT_GET_PORTREQUEST, exception.getMessage());
		verify(portRequestRepository, times(1)).findById(portRequestId);
	}

	@Test
	void testUpdatePortRequestSuccess() throws CustomerNotFoundException, PortRequestNotFoundException {
		Integer portRequestId = 1;
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestId(portRequestId);
		portRequest.setComplianceChecked(true);

		Customer customer = new Customer();
		customer.setCustomerId(1);
		portRequest.setCustomer(customer);

		when(portRequestRepository.findById(portRequestId)).thenReturn(Optional.of(portRequest));
		when(customerService.getCustomerById(customer.getCustomerId())).thenReturn(customer);
		when(customerService.updateCustomer(any(Customer.class))).thenReturn(customer);
		when(portRequestRepository.save(any(PortRequest.class))).thenReturn(portRequest);

		PortRequest result = portRequestService.updatePortRequest(portRequest);

		assertEquals(Status.COMPLETED, result.getApprovalStatus());
		assertNotNull(result.getCompletionDate());
		verify(portRequestRepository, times(1)).findById(portRequestId);
		verify(customerService, times(1)).getCustomerById(customer.getCustomerId());
		verify(customerService, times(1)).updateCustomer(customer);
		verify(portRequestRepository, times(1)).save(portRequest);
	}

	@Test
	void testUpdatePortRequestNotFound() {
		Integer portRequestId = 1;
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestId(portRequestId);

		when(portRequestRepository.findById(portRequestId)).thenReturn(Optional.empty());

		PortRequestNotFoundException exception = assertThrows(PortRequestNotFoundException.class, () -> {
			portRequestService.updatePortRequest(portRequest);
		});

		assertEquals(QueryMapper.CANNOT_UPDATE_PORTREQUEST, exception.getMessage());
		verify(portRequestRepository, times(1)).findById(portRequestId);
		verify(portRequestRepository, times(0)).save(any(PortRequest.class));
	}

	@Test
	void testDeletePortRequestSuccess() throws PortRequestNotFoundException {
		Integer portRequestId = 1;
		PortRequest portRequest = new PortRequest();
		portRequest.setRequestId(portRequestId);

		when(portRequestRepository.findById(portRequestId)).thenReturn(Optional.of(portRequest));

		String result = portRequestService.deletePortRequest(portRequestId);

		assertEquals("Deleted Successfully!!", result);
		verify(portRequestRepository, times(1)).findById(portRequestId);
		verify(portRequestRepository, times(1)).deleteById(portRequestId);
	}

	@Test
	void testDeletePortRequestNotFound() {
		Integer portRequestId = 1;

		when(portRequestRepository.findById(portRequestId)).thenReturn(Optional.empty());

		PortRequestNotFoundException exception = assertThrows(PortRequestNotFoundException.class, () -> {
			portRequestService.deletePortRequest(portRequestId);
		});

		assertEquals(QueryMapper.CANNOT_DELETE_PORTREQUEST, exception.getMessage());
		verify(portRequestRepository, times(1)).findById(portRequestId);
		verify(portRequestRepository, times(0)).deleteById(portRequestId);
	}

	@Test
	void testGetAllPortRequestSuccess() throws PortRequestNotFoundException {
		List<PortRequest> mockPortRequests = new ArrayList<>();
		mockPortRequests.add(new PortRequest());
		when(portRequestRepository.findAll()).thenReturn(mockPortRequests);

		List<PortRequest> result = portRequestService.getAllPortRequest();

		assertEquals(mockPortRequests, result);
		verify(portRequestRepository, times(1)).findAll();
	}

	@Test
	void testGetAllPortRequestNotFound() {
		List<PortRequest> mockPortRequests = Collections.emptyList();
		when(portRequestRepository.findAll()).thenReturn(mockPortRequests);

		PortRequestNotFoundException exception = assertThrows(PortRequestNotFoundException.class, () -> {
			portRequestService.getAllPortRequest();
		});

		assertNotEquals(QueryMapper.CANNOT_GET_PORTREQUEST, exception.getMessage());
		verify(portRequestRepository, times(1)).findAll();
	}

}
