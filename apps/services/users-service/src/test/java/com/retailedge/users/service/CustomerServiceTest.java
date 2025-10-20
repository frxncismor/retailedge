package com.retailedge.users.service;

import com.retailedge.users.dto.CustomerRequest;
import com.retailedge.users.dto.CustomerResponse;
import com.retailedge.users.entity.Customer;
import com.retailedge.users.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private CustomerService customerService;
    
    private Customer testCustomer;
    private CustomerRequest testCustomerRequest;
    private CustomerResponse expectedResponse;
    
    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setPhoneNumber("+1234567890");
        testCustomer.setIsActive(true);
        testCustomer.setCreatedAt(LocalDateTime.now());
        testCustomer.setUpdatedAt(LocalDateTime.now());
        
        testCustomerRequest = new CustomerRequest();
        testCustomerRequest.setFirstName("John");
        testCustomerRequest.setLastName("Doe");
        testCustomerRequest.setEmail("john.doe@example.com");
        testCustomerRequest.setPhoneNumber("+1234567890");
        testCustomerRequest.setIsActive(true);
        
        expectedResponse = new CustomerResponse(
                1L, "John", "Doe", "john.doe@example.com",
                "+1234567890", null, true, LocalDateTime.now(), LocalDateTime.now()
        );
    }
    
    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findAll()).thenReturn(customers);
        
        // When
        List<CustomerResponse> result = customerService.getAllCustomers();
        
        // Then
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(customerRepository).findAll();
    }
    
    @Test
    void getCustomerById_WhenCustomerExists_ShouldReturnCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        
        // When
        Optional<CustomerResponse> result = customerService.getCustomerById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(customerRepository).findById(1L);
    }
    
    @Test
    void getCustomerByEmail_WhenCustomerExists_ShouldReturnCustomer() {
        // Given
        when(customerRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testCustomer));
        
        // When
        Optional<CustomerResponse> result = customerService.getCustomerByEmail("john.doe@example.com");
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("john.doe@example.com", result.get().getEmail());
        verify(customerRepository).findByEmail("john.doe@example.com");
    }
    
    @Test
    void getActiveCustomers_ShouldReturnActiveCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByIsActiveTrue()).thenReturn(customers);
        
        // When
        List<CustomerResponse> result = customerService.getActiveCustomers();
        
        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
        verify(customerRepository).findByIsActiveTrue();
    }
    
    @Test
    void createCustomer_WhenValidRequest_ShouldCreateCustomer() {
        // Given
        when(customerRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        
        // When
        CustomerResponse result = customerService.createCustomer(testCustomerRequest);
        
        // Then
        assertEquals("John", result.getFirstName());
        verify(customerRepository).existsByEmail("john.doe@example.com");
        verify(customerRepository).save(any(Customer.class));
    }
    
    @Test
    void createCustomer_WhenEmailExists_ShouldThrowException() {
        // Given
        when(customerRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(testCustomerRequest);
        });
        verify(customerRepository).existsByEmail("john.doe@example.com");
        verify(customerRepository, never()).save(any(Customer.class));
    }
    
    @Test
    void updateCustomer_WhenCustomerExists_ShouldUpdateCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.existsByEmailAndIdNot("john.doe@example.com", 1L)).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        
        // When
        Optional<CustomerResponse> result = customerService.updateCustomer(1L, testCustomerRequest);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(customerRepository).findById(1L);
        verify(customerRepository).existsByEmailAndIdNot("john.doe@example.com", 1L);
        verify(customerRepository).save(any(Customer.class));
    }
    
    @Test
    void deleteCustomer_WhenCustomerExists_ShouldDeleteCustomer() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(true);
        
        // When
        boolean result = customerService.deleteCustomer(1L);
        
        // Then
        assertTrue(result);
        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
    }
    
    @Test
    void deleteCustomer_WhenCustomerNotExists_ShouldReturnFalse() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(false);
        
        // When
        boolean result = customerService.deleteCustomer(1L);
        
        // Then
        assertFalse(result);
        verify(customerRepository).existsById(1L);
        verify(customerRepository, never()).deleteById(anyLong());
    }
}
