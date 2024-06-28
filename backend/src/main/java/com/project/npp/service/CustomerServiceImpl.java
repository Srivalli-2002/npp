package com.project.npp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.npp.entities.Customer;
import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.entities.Status;
import com.project.npp.entities.UserEntity;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.repositories.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static Logger loggers = LogManager.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepository repo;
	
	@Autowired
	private UserEntityService userEntityService;
	
	@Autowired
	private RoleService roleService;

	// Method to add a new customer
	@Override
	public Customer addCustomer(Customer customer) throws RoleNotFoundException, OperatorNotFoundException {
		customer.setStatus(Status.PENDING);
		Optional<UserEntity> user = userEntityService.findByUsername(customer.getUsername());
		Optional<Role> role = roleService.findRoleByName(ERole.ROLE_USER);
		userEntityService.updateRole(user.get().getUsername(), role.get());
		Customer cust = repo.save(customer);
		loggers.info(QueryMapper.ADD_CUSTOMER);
		return cust;
	}

	// Method to get a customer by ID
	@Override
	public Customer getCustomerById(Integer id) throws CustomerNotFoundException {
		Optional<Customer> cust = repo.findById(id);
		if (cust.isPresent()) {
			loggers.info(QueryMapper.GET_CUSTOMER);
			return cust.get();
		} else
			loggers.error(QueryMapper.CANNOT_GET_CUSTOMER);
		throw new CustomerNotFoundException(QueryMapper.CANNOT_GET_CUSTOMER);
	}

	// Method to update an existing customer
	@Override
	public Customer updateCustomer(Customer customer) throws CustomerNotFoundException {
		Optional<Customer> c = repo.findById(customer.getCustomerId());
		if (c.isPresent()) {
			// Save the updated customer
			Customer cust = repo.save(customer);
			loggers.info(QueryMapper.UPDATE_CUSTOMER);
			return cust;
		} else
			loggers.error(QueryMapper.CANNOT_UPDATE_CUSTOMER);
		throw new CustomerNotFoundException(QueryMapper.CANNOT_UPDATE_CUSTOMER);
	}

	// Method to delete a customer by ID
	@Override
	public String deleteCustomerById(Integer id) throws CustomerNotFoundException {
		Optional<Customer> cust = repo.findById(id);
		if (cust.isPresent()) {
			repo.deleteById(id);
			loggers.info(QueryMapper.DELETE_CUSTOMER);
			return "Deleted Successfully!!";
		} else
			loggers.error(QueryMapper.CANNOT_DELETE_CUSTOMER);
		throw new CustomerNotFoundException(QueryMapper.CANNOT_DELETE_CUSTOMER);
	}

	// Method to get all customers
	@Override
	public List<Customer> getAllCustomers() throws CustomerNotFoundException {
		List<Customer> customers = (List<Customer>) repo.findAll();
		if (!customers.isEmpty()) {
			loggers.info(QueryMapper.GET_CUSTOMER);
			return customers;
		} else
			loggers.error(QueryMapper.CANNOT_GET_CUSTOMER);
		throw new CustomerNotFoundException(QueryMapper.CANNOT_GET_CUSTOMER);
	}

	// Method to get customer by username
	@Override
	public Customer getCustomerByUserName(String username) throws CustomerNotFoundException {
		Optional<Customer> cust = repo.findByUsername(username);
		if (cust.isPresent()) {
			loggers.info(QueryMapper.GET_CUSTOMER);
			return cust.get();
		} else
			loggers.error(QueryMapper.CANNOT_GET_CUSTOMER);
		throw new CustomerNotFoundException(QueryMapper.CANNOT_GET_CUSTOMER);
	}

	// Method to get customer by phone number
	@Override
	public Customer getCustomerByPhoneNumber(Long phoneNumber) throws CustomerNotFoundException {
		Optional<Customer> cust = repo.findByPhoneNumber(phoneNumber);
		if (cust.isPresent()) {
			loggers.info(QueryMapper.GET_CUSTOMER);
			return cust.get();
		} else
			loggers.error(QueryMapper.CANNOT_GET_CUSTOMER);
		throw new CustomerNotFoundException(QueryMapper.CANNOT_GET_CUSTOMER);
	}

}