package com.tailoring.service;

import com.tailoring.dto.*;
import com.tailoring.entity.Customer;
import com.tailoring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Add new customer
    public CustomerResponse addCustomer(CustomerRequest request) {
        Customer customer = new Customer(
                request.getCustomerName(),
                request.getAge(),
                request.getPhoneNumber(),
                request.getBillAmount(),
                request.getWorkDate(),
                request.getWorkType(),
                request.getNotes()
        );

        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponse(savedCustomer);
    }

    // Search customers by name
    public List<CustomerResponse> searchCustomers(String name) {
        List<Customer> customers = customerRepository.findByCustomerNameContainingIgnoreCase(name);
        return customers.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    // Get analytics data
    public AnalyticsResponse getAnalytics() {
        BigDecimal totalRevenue = customerRepository.getTotalRevenue();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        Long totalCustomers = customerRepository.getUniqueCustomerCount();
        if (totalCustomers == null) totalCustomers = 0L;

        BigDecimal avgBill = customerRepository.getAverageBillAmount();
        if (avgBill == null) avgBill = BigDecimal.ZERO;

        List<Customer> recentTransactions = customerRepository.findTop10ByOrderByCreatedAtDesc();
        List<CustomerResponse> recentTransactionResponses = recentTransactions.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());

        AnalyticsResponse.ChartData chartData = generateChartData(null, null);

        return new AnalyticsResponse(totalRevenue, totalCustomers, avgBill,
                recentTransactionResponses, chartData);
    }

    // Get analytics data with filters
    public AnalyticsResponse getAnalytics(String period, LocalDate fromDate, LocalDate toDate) {
        LocalDate startDate = fromDate;
        LocalDate endDate = toDate;

        // Calculate date range based on period
        if (period != null && !period.equals("custom")) {
            LocalDate today = LocalDate.now();
            switch (period) {
                case "today":
                    startDate = today;
                    endDate = today;
                    break;
                case "week":
                    startDate = today.minusDays(7);
                    endDate = today;
                    break;
                case "month":
                    startDate = today.minusDays(30);
                    endDate = today;
                    break;
                case "6months":
                    startDate = today.minusDays(180);
                    endDate = today;
                    break;
            }
        }

        BigDecimal totalRevenue;
        Long totalCustomers;
        BigDecimal avgBill;

        if (startDate != null && endDate != null) {
            totalRevenue = customerRepository.getTotalRevenueByDateRange(startDate, endDate);
            totalCustomers = customerRepository.getUniqueCustomerCountByDateRange(startDate, endDate);
            avgBill = customerRepository.getAverageBillAmountByDateRange(startDate, endDate);
        } else {
            totalRevenue = customerRepository.getTotalRevenue();
            totalCustomers = customerRepository.getUniqueCustomerCount();
            avgBill = customerRepository.getAverageBillAmount();
        }

        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (totalCustomers == null) totalCustomers = 0L;
        if (avgBill == null) avgBill = BigDecimal.ZERO;

        // Get filtered recent transactions
        List<Customer> recentTransactions;
        if (startDate != null && endDate != null) {
            recentTransactions = customerRepository.findByWorkDateBetween(startDate, endDate)
                    .stream()
                    .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            recentTransactions = customerRepository.findTop10ByOrderByCreatedAtDesc();
        }

        List<CustomerResponse> recentTransactionResponses = recentTransactions.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());

        AnalyticsResponse.ChartData chartData = generateChartData(startDate, endDate);

        return new AnalyticsResponse(totalRevenue, totalCustomers, avgBill,
                recentTransactionResponses, chartData);
    }

    // Generate chart data
    private AnalyticsResponse.ChartData generateChartData(LocalDate startDate, LocalDate endDate) {
        // Revenue chart data
        List<Object[]> dailyRevenueData;
        if (startDate != null && endDate != null) {
            dailyRevenueData = customerRepository.getDailyRevenueByDateRange(startDate, endDate);
        } else {
            dailyRevenueData = customerRepository.getDailyRevenue();
        }

        List<String> revenueLabels = new ArrayList<>();
        List<BigDecimal> revenueValues = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        for (Object[] row : dailyRevenueData) {
            LocalDate date = (LocalDate) row[0];
            BigDecimal amount = (BigDecimal) row[1];

            revenueLabels.add(date.format(formatter));
            revenueValues.add(amount);
        }

        AnalyticsResponse.RevenueData revenueData = new AnalyticsResponse.RevenueData(revenueLabels, revenueValues);

        // Work type chart data
        List<Object[]> workTypeData;
        if (startDate != null && endDate != null) {
            workTypeData = customerRepository.getWorkTypeDistributionByDateRange(startDate, endDate);
        } else {
            workTypeData = customerRepository.getWorkTypeDistribution();
        }

        List<String> workTypeLabels = new ArrayList<>();
        List<Long> workTypeCounts = new ArrayList<>();

        for (Object[] row : workTypeData) {
            String workType = (String) row[0];
            Long count = (Long) row[1];

            workTypeLabels.add(workType);
            workTypeCounts.add(count);
        }

        AnalyticsResponse.WorkTypeData workTypeChartData = new AnalyticsResponse.WorkTypeData(workTypeLabels, workTypeCounts);

        return new AnalyticsResponse.ChartData(revenueData, workTypeChartData);
    }

    // Get all customers
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    // Get customer by ID
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return new CustomerResponse(customer);
    }
}