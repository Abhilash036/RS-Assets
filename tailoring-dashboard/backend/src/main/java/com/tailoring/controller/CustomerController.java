package com.tailoring.controller;

import com.tailoring.dto.*;
import com.tailoring.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Add new customer
    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(@Valid @RequestBody CustomerRequest request) {
        try {
            CustomerResponse response = customerService.addCustomer(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search customers by name
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String name) {
        try {
            List<CustomerResponse> customers = customerService.searchCustomers(name);
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get analytics data
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate) {
        try {
            AnalyticsResponse analytics;
            if (period != null || fromDate != null || toDate != null) {
                analytics = customerService.getAnalytics(period, fromDate, toDate);
            } else {
                analytics = customerService.getAnalytics();
            }
            return new ResponseEntity<>(analytics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        try {
            List<CustomerResponse> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        try {
            CustomerResponse customer = customerService.getCustomerById(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}