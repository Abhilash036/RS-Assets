package com.tailoring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    private Integer age;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "bill_amount")
    private BigDecimal billAmount;

    @Column(name = "work_date")
    private LocalDate workDate;

    @Column(name = "work_type")
    private String workType;

    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = true, updatable = false)  // Changed to nullable = true
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)  // Changed to nullable = true
    private LocalDateTime updatedAt;

    // Default constructor (required by JPA)
    public Customer() {}

    // Constructor with parameters (for your service)
    public Customer(String customerName, Integer age, String phoneNumber,
                    BigDecimal billAmount, LocalDate workDate, String workType, String notes) {
        this.customerName = customerName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.billAmount = billAmount;
        this.workDate = workDate;
        this.workType = workType;
        this.notes = notes;
        // createdAt and updatedAt will be set automatically by @CreationTimestamp and @UpdateTimestamp
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}