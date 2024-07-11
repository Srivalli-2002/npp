package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import com.project.npp.entities.ERole;
import com.project.npp.entities.Role;
import com.project.npp.entities.Status;
import com.project.npp.entities.UserEntity;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.RoleNotFoundException;
import com.project.npp.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private UserEntity userEntity;
    private Role role;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setUsername("testuser");
        customer.setPhoneNumber(1234567890L);
        customer.setStatus(Status.PENDING);

        userEntity = new UserEntity();
        userEntity.setUsername("testuser");

        role = new Role();
        role.setName(ERole.ROLE_USER);
    }

    @Test
    public void addCustomer_savesCustomer() throws RoleNotFoundException, OperatorNotFoundException {
        when(userEntityService.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(roleService.findRoleByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.addCustomer(customer);

        assertEquals(customer, result);
        verify(userEntityService).updateRole("testuser", role);
        verify(customerRepository).save(customer);
    }

    @Test
    public void getCustomerById_existingId_returnsCustomer() throws CustomerNotFoundException {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1);

        assertEquals(customer, result);
        verify(customerRepository).findById(1);
    }

    @Test
    public void getCustomerById_nonExistingId_throwsCustomerNotFoundException() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1));
    }

    @Test
    public void updateCustomer_existingCustomer_updatesCustomer() throws CustomerNotFoundException {
        when(customerRepository.findById(customer.getCustomerId())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.updateCustomer(customer);

        assertEquals(customer, result);
        verify(customerRepository).save(customer);
    }

    @Test
    public void updateCustomer_nonExistingCustomer_throwsCustomerNotFoundException() {
        when(customerRepository.findById(customer.getCustomerId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(customer));
    }

    @Test
    public void deleteCustomerById_existingId_deletesCustomer() throws CustomerNotFoundException {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        String result = customerService.deleteCustomerById(1);

        assertEquals("Deleted Successfully!!", result);
        verify(customerRepository).deleteById(1);
    }

    @Test
    public void deleteCustomerById_nonExistingId_throwsCustomerNotFoundException() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(1));
    }

    @Test
    public void getAllCustomers_returnsAllCustomers() throws CustomerNotFoundException {
        List<Customer> allCustomers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(allCustomers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(allCustomers, result);
        verify(customerRepository).findAll();
    }

    @Test
    public void getAllCustomers_noCustomersFound_throwsCustomerNotFoundException() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getAllCustomers());
    }

    @Test
    public void getCustomerByUserName_existingUsername_returnsCustomer() throws CustomerNotFoundException {
        when(customerRepository.findByUsername("testuser")).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByUserName("testuser");

        assertEquals(customer, result);
        verify(customerRepository).findByUsername("testuser");
    }

    @Test
    public void getCustomerByUserName_nonExistingUsername_throwsCustomerNotFoundException() {
        when(customerRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByUserName("testuser"));
    }

    @Test
    public void getCustomerByPhoneNumber_existingPhoneNumber_returnsCustomer() throws CustomerNotFoundException {
        when(customerRepository.findByPhoneNumber(1234567890L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByPhoneNumber(1234567890L);

        assertEquals(customer, result);
        verify(customerRepository).findByPhoneNumber(1234567890L);
    }

    @Test
    public void getCustomerByPhoneNumber_nonExistingPhoneNumber_throwsCustomerNotFoundException() {
        when(customerRepository.findByPhoneNumber(1234567890L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByPhoneNumber(1234567890L));
    }
}