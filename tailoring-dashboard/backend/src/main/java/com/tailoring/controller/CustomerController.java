package com.tailoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tailoring.entity.Customer;
import com.tailoring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Get all customers
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerRepository.findAll();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create new customer
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Map<String, Object> customerData) {
        try {
            Customer customer = new Customer();
            customer.setCustomerName((String) customerData.get("customerName"));
            customer.setPhoneNumber((String) customerData.get("phoneNumber"));
            customer.setAddress((String) customerData.get("address"));

            // Parse dates
            if (customerData.get("workDate") != null && !customerData.get("workDate").toString().isEmpty()) {
                customer.setWorkDate(LocalDate.parse((String) customerData.get("workDate")));
            }
            if (customerData.get("deliveryDate") != null && !customerData.get("deliveryDate").toString().isEmpty()) {
                customer.setDeliveryDate(LocalDate.parse((String) customerData.get("deliveryDate")));
            }

            customer.setWorkType((String) customerData.get("workType"));
            customer.setGarmentType((String) customerData.get("garmentType"));
            customer.setNotes((String) customerData.get("notes"));

            // Parse bill amount
            if (customerData.get("billAmount") != null) {
                Object billAmountObj = customerData.get("billAmount");
                BigDecimal billAmount;
                if (billAmountObj instanceof Number) {
                    billAmount = BigDecimal.valueOf(((Number) billAmountObj).doubleValue());
                } else {
                    billAmount = new BigDecimal(billAmountObj.toString());
                }
                customer.setBillAmount(billAmount);
            }

            // Convert measurements map to JSON string
            Object measurements = customerData.get("measurements");
            if (measurements != null) {
                String measurementsJson = objectMapper.writeValueAsString(measurements);
                customer.setMeasurements(measurementsJson);
            }

            Customer savedCustomer = customerRepository.save(customer);
            System.out.println("✅ Customer saved: " + savedCustomer.getCustomerName());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Search by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<Customer>> searchByName(@RequestParam String name) {
        try {
            List<Customer> customers = customerRepository.findByCustomerNameContainingIgnoreCase(name);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search by work date
     */
    @GetMapping("/search/date")
    public ResponseEntity<List<Customer>> searchByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Customer> customers = customerRepository.findByWorkDate(date);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search by date range
     */
    @GetMapping("/search/date-range")
    public ResponseEntity<List<Customer>> searchByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Customer> customers = customerRepository.findByWorkDateBetween(startDate, endDate);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get recent transactions
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Customer>> getRecentTransactions() {
        try {
            List<Customer> customers = customerRepository.findTop10ByOrderByCreatedAtDesc();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get analytics stats
     */
    @GetMapping("/analytics/stats")
    public ResponseEntity<Map<String, Object>> getAnalyticsStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> stats = new HashMap<>();

            if (startDate != null && endDate != null) {
                BigDecimal totalRevenue = customerRepository.getTotalRevenueByDateRange(startDate, endDate);
                Long uniqueCustomers = customerRepository.getUniqueCustomerCountByDateRange(startDate, endDate);
                BigDecimal avgBillAmount = customerRepository.getAverageBillAmountByDateRange(startDate, endDate);

                stats.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
                stats.put("uniqueCustomers", uniqueCustomers != null ? uniqueCustomers : 0);
                stats.put("averageBillAmount", avgBillAmount != null ? avgBillAmount : BigDecimal.ZERO);
            } else {
                BigDecimal totalRevenue = customerRepository.getTotalRevenue();
                Long uniqueCustomers = customerRepository.getUniqueCustomerCount();
                BigDecimal avgBillAmount = customerRepository.getAverageBillAmount();

                stats.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
                stats.put("uniqueCustomers", uniqueCustomers != null ? uniqueCustomers : 0);
                stats.put("averageBillAmount", avgBillAmount != null ? avgBillAmount : BigDecimal.ZERO);
            }

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get work type distribution
     */
    @GetMapping("/analytics/work-type-distribution")
    public ResponseEntity<List<Map<String, Object>>> getWorkTypeDistribution(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Object[]> results;

            if (startDate != null && endDate != null) {
                results = customerRepository.getWorkTypeDistributionByDateRange(startDate, endDate);
            } else {
                results = customerRepository.getWorkTypeDistribution();
            }

            List<Map<String, Object>> distribution = results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("workType", result[0]);
                        map.put("count", result[1]);
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(distribution);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get daily revenue
     */
    @GetMapping("/analytics/daily-revenue")
    public ResponseEntity<List<Map<String, Object>>> getDailyRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Object[]> results;

            if (startDate != null && endDate != null) {
                results = customerRepository.getDailyRevenueByDateRange(startDate, endDate);
            } else {
                results = customerRepository.getDailyRevenue();
            }

            List<Map<String, Object>> dailyRevenue = results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", result[0].toString());
                        map.put("revenue", result[1]);
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dailyRevenue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            return customerRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update customer
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Map<String, Object> customerData) {
        try {
            return customerRepository.findById(id)
                    .map(customer -> {
                        customer.setCustomerName((String) customerData.get("customerName"));
                        customer.setPhoneNumber((String) customerData.get("phoneNumber"));
                        customer.setAddress((String) customerData.get("address"));

                        if (customerData.get("workDate") != null && !customerData.get("workDate").toString().isEmpty()) {
                            customer.setWorkDate(LocalDate.parse((String) customerData.get("workDate")));
                        }
                        if (customerData.get("deliveryDate") != null && !customerData.get("deliveryDate").toString().isEmpty()) {
                            customer.setDeliveryDate(LocalDate.parse((String) customerData.get("deliveryDate")));
                        }

                        customer.setWorkType((String) customerData.get("workType"));
                        customer.setGarmentType((String) customerData.get("garmentType"));
                        customer.setNotes((String) customerData.get("notes"));

                        if (customerData.get("billAmount") != null) {
                            Object billAmountObj = customerData.get("billAmount");
                            BigDecimal billAmount;
                            if (billAmountObj instanceof Number) {
                                billAmount = BigDecimal.valueOf(((Number) billAmountObj).doubleValue());
                            } else {
                                billAmount = new BigDecimal(billAmountObj.toString());
                            }
                            customer.setBillAmount(billAmount);
                        }

                        try {
                            Object measurements = customerData.get("measurements");
                            if (measurements != null) {
                                String measurementsJson = objectMapper.writeValueAsString(measurements);
                                customer.setMeasurements(measurementsJson);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Customer updatedCustomer = customerRepository.save(customer);
                        System.out.println("✅ Customer updated: " + updatedCustomer.getCustomerName());
                        return ResponseEntity.ok(updatedCustomer);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Delete customer
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            return customerRepository.findById(id)
                    .map(customer -> {
                        customerRepository.delete(customer);
                        System.out.println("🗑️ Customer deleted: " + customer.getCustomerName());
                        return ResponseEntity.ok().<Void>build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

