package com.tailoring.dto;

import com.tailoring.entity.Customer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerResponse {

    private Long id;
    private String customerName;
    private Integer age;
    private String phoneNumber;
    private BigDecimal billAmount;
    private LocalDate workDate;
    private String workType;
    private String notes;
    private LocalDateTime createdAt;

    // Constructors
    public CustomerResponse() {}

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.customerName = customer.getCustomerName();
        this.age = customer.getAge();
        this.phoneNumber = customer.getPhoneNumber();
        this.billAmount = customer.getBillAmount();
        this.workDate = customer.getWorkDate();
        this.workType = customer.getWorkType();
        this.notes = customer.getNotes();
        this.createdAt = customer.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public BigDecimal getBillAmount() { return billAmount; }
    public void setBillAmount(BigDecimal billAmount) { this.billAmount = billAmount; }

    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }

    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}