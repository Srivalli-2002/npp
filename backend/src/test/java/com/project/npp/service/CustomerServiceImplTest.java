package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.npp.entities.Customer;
import com.project.npp.entities.Status;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.repositories.CustomerRepository;

class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerServiceImpl customerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddCustomerSuccess() {
		Customer customer = new Customer();
		customer.setStatus(Status.PENDING);

		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		Customer result = customerService.addCustomer(customer);

		assertEquals(Status.PENDING, result.getStatus());
		assertEquals(customer, result);
		verify(customerRepository, times(1)).save(customer);
	}

	@Test
	void testGetCustomerByIdSuccess() throws CustomerNotFoundException {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

		Customer result = customerService.getCustomerById(customerId);

		assertEquals(customer, result);
		verify(customerRepository, times(1)).findById(customerId);
	}

	@Test
	void testGetCustomerByIdNotFound() {
		Integer customerId = 1;

		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.getCustomerById(customerId);
		});

		assertEquals(QueryMapper.CANNOT_GET_CUSTOMER, exception.getMessage());
		verify(customerRepository, times(1)).findById(customerId);
	}

	@Test
	void testUpdateCustomerSuccess() throws CustomerNotFoundException {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		Customer result = customerService.updateCustomer(customer);

		assertEquals(customer, result);
		verify(customerRepository, times(1)).findById(customerId);
		verify(customerRepository, times(1)).save(customer);
	}

	@Test
	void testUpdateCustomerNotFound() {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.updateCustomer(customer);
		});

		assertEquals(QueryMapper.CANNOT_UPDATE_CUSTOMER, exception.getMessage());
		verify(customerRepository, times(1)).findById(customerId);
		verify(customerRepository, times(0)).save(any(Customer.class));
	}

	@Test
	void testDeleteCustomerByIdSuccess() throws CustomerNotFoundException {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

		String result = customerService.deleteCustomerById(customerId);

		assertEquals("Deleted Successfully!!", result);
		verify(customerRepository, times(1)).findById(customerId);
		verify(customerRepository, times(1)).deleteById(customerId);
	}

	@Test
	void testDeleteCustomerByIdNotFound() {
		Integer customerId = 1;

		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.deleteCustomerById(customerId);
		});

		assertEquals(QueryMapper.CANNOT_DELETE_CUSTOMER, exception.getMessage());
		verify(customerRepository, times(1)).findById(customerId);
		verify(customerRepository, times(0)).deleteById(customerId);
	}

	@Test
	void testGetAllCustomersSuccess() throws CustomerNotFoundException {
		List<Customer> mockCustomers = new ArrayList<>();
		mockCustomers.add(new Customer());
		when(customerRepository.findAll()).thenReturn(mockCustomers);

		List<Customer> result = customerService.getAllCustomers();

		assertEquals(mockCustomers, result);
		verify(customerRepository, times(1)).findAll();
	}

}
