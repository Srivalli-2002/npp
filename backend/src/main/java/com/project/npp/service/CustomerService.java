package com.project.npp.service;

import java.util.List;

import com.project.npp.entities.Customer;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;

public interface CustomerService {

	// Add customer
	public Customer addCustomer(Customer customer) throws RoleNotFoundException, OperatorNotFoundException;

	// Get customer
	public Customer getCustomerById(Integer id) throws CustomerNotFoundException;
	public Customer getCustomerByUserName(String username) throws CustomerNotFoundException;
	public Customer getCustomerByPhoneNumber(Long  phoneNumber) throws CustomerNotFoundException;
	
	// Update customer
	public Customer updateCustomer(Customer customer) throws CustomerNotFoundException;
	
	// Delete customer
	public String deleteCustomerById(Integer id) throws CustomerNotFoundException; 
	
	// Get all customers
	public List<Customer> getAllCustomers() throws CustomerNotFoundException;
}
