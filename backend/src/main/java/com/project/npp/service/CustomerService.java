package com.project.npp.service;

import java.util.List;

import com.project.npp.entities.Customer;
import com.project.npp.exceptions.CustomerNotFoundException;

public interface CustomerService {

	// Add customer
	public Customer addCustomer(Customer customer);

	// Get customer
	public Customer getCustomerById(Integer id) throws CustomerNotFoundException;

	// Update customer
	public Customer updateCustomer(Customer customer) throws CustomerNotFoundException;
	
	// Delete customer
	public String deleteCustomerById(Integer id) throws CustomerNotFoundException; 
	
	// Get all customers
	public List<Customer> getAllCustomers() throws CustomerNotFoundException;
}
