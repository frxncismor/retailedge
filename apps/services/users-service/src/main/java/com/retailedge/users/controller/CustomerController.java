package com.retailedge.users.controller;

import com.retailedge.users.dto.CustomerRequest;
import com.retailedge.users.dto.CustomerResponse;
import com.retailedge.users.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management API")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved customers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/paged")
    @Operation(summary = "Get all customers with pagination", description = "Retrieve a paginated list of all customers")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(Pageable pageable) {
        Page<CustomerResponse> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        Optional<CustomerResponse> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email", description = "Retrieve a specific customer by email")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(
            @Parameter(description = "Customer email") @PathVariable String email) {
        Optional<CustomerResponse> customer = customerService.getCustomerByEmail(email);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active customers", description = "Retrieve all active customers")
    public ResponseEntity<List<CustomerResponse>> getActiveCustomers() {
        List<CustomerResponse> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/search/firstname")
    @Operation(summary = "Search customers by first name", description = "Search customers by first name")
    public ResponseEntity<List<CustomerResponse>> searchCustomersByFirstName(
            @Parameter(description = "First name to search") @RequestParam String firstName) {
        List<CustomerResponse> customers = customerService.searchCustomersByFirstName(firstName);
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/search/lastname")
    @Operation(summary = "Search customers by last name", description = "Search customers by last name")
    public ResponseEntity<List<CustomerResponse>> searchCustomersByLastName(
            @Parameter(description = "Last name to search") @RequestParam String lastName) {
        List<CustomerResponse> customers = customerService.searchCustomersByLastName(lastName);
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Search customers by multiple criteria")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(
            @Parameter(description = "First name filter") @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name filter") @RequestParam(required = false) String lastName,
            @Parameter(description = "Email filter") @RequestParam(required = false) String email,
            @Parameter(description = "Active status filter") @RequestParam(required = false) Boolean isActive) {
        List<CustomerResponse> customers = customerService.searchCustomers(firstName, lastName, email, isActive);
        return ResponseEntity.ok(customers);
    }
    
    @PostMapping
    @Operation(summary = "Create new customer", description = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Customer with same email already exists")
    })
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        try {
            CustomerResponse customer = customerService.createCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "Customer with same email already exists")
    })
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        try {
            Optional<CustomerResponse> customer = customerService.updateCustomer(id, request);
            return customer.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Delete a customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
