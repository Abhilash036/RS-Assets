package com.tailoring.repository;

import com.tailoring.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Search customers by name (case-insensitive)
    List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);

    // Find customers by date range
    List<Customer> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);

    // Find customers by work date
    List<Customer> findByWorkDate(LocalDate workDate);

    // Find customers after a specific date
    List<Customer> findByWorkDateAfter(LocalDate date);

    // Get recent transactions (limit)
    List<Customer> findTop10ByOrderByCreatedAtDesc();

    // Custom queries for analytics
    @Query("SELECT SUM(c.billAmount) FROM Customer c")
    BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(DISTINCT c.customerName) FROM Customer c")
    Long getUniqueCustomerCount();

    @Query("SELECT AVG(c.billAmount) FROM Customer c")
    BigDecimal getAverageBillAmount();

    // Analytics by date range
    @Query("SELECT SUM(c.billAmount) FROM Customer c WHERE c.workDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByDateRange(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT c.customerName) FROM Customer c WHERE c.workDate BETWEEN :startDate AND :endDate")
    Long getUniqueCustomerCountByDateRange(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(c.billAmount) FROM Customer c WHERE c.workDate BETWEEN :startDate AND :endDate")
    BigDecimal getAverageBillAmountByDateRange(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    // Work type distribution
    @Query("SELECT c.workType, COUNT(c) FROM Customer c GROUP BY c.workType")
    List<Object[]> getWorkTypeDistribution();

    @Query("SELECT c.workType, COUNT(c) FROM Customer c WHERE c.workDate BETWEEN :startDate AND :endDate GROUP BY c.workType")
    List<Object[]> getWorkTypeDistributionByDateRange(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    // Daily revenue for charts
    @Query("SELECT c.workDate, SUM(c.billAmount) FROM Customer c GROUP BY c.workDate ORDER BY c.workDate")
    List<Object[]> getDailyRevenue();

    @Query("SELECT c.workDate, SUM(c.billAmount) FROM Customer c WHERE c.workDate BETWEEN :startDate AND :endDate GROUP BY c.workDate ORDER BY c.workDate")
    List<Object[]> getDailyRevenueByDateRange(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}