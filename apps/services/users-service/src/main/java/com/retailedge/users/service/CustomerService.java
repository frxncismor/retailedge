package com.retailedge.users.service;

import com.retailedge.users.dto.CustomerRequest;
import com.retailedge.users.dto.CustomerResponse;
import com.retailedge.users.entity.Customer;
import com.retailedge.users.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    public Optional<CustomerResponse> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    public Optional<CustomerResponse> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::convertToResponse);
    }
    
    public List<CustomerResponse> getActiveCustomers() {
        return customerRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<CustomerResponse> searchCustomersByFirstName(String firstName) {
        return customerRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<CustomerResponse> searchCustomersByLastName(String lastName) {
        return customerRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<CustomerResponse> searchCustomers(String firstName, String lastName, String email, Boolean isActive) {
        return customerRepository.findByFilters(firstName, lastName, email, isActive).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public CustomerResponse createCustomer(CustomerRequest request) {
        // Check if customer with same email already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email '" + request.getEmail() + "' already exists");
        }
        
        Customer customer = convertToEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }
    
    public Optional<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Check if another customer with same email exists
                    if (customerRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                        throw new IllegalArgumentException("Customer with email '" + request.getEmail() + "' already exists");
                    }
                    
                    existingCustomer.setFirstName(request.getFirstName());
                    existingCustomer.setLastName(request.getLastName());
                    existingCustomer.setEmail(request.getEmail());
                    existingCustomer.setPhoneNumber(request.getPhoneNumber());
                    existingCustomer.setDateOfBirth(request.getDateOfBirth());
                    existingCustomer.setIsActive(request.getIsActive());
                    
                    Customer updatedCustomer = customerRepository.save(existingCustomer);
                    return convertToResponse(updatedCustomer);
                });
    }
    
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }
    
    private Customer convertToEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setIsActive(request.getIsActive());
        return customer;
    }
    
    private CustomerResponse convertToResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getDateOfBirth(),
                customer.getIsActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
