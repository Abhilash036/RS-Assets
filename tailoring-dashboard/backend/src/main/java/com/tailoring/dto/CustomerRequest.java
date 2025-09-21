package com.tailoring.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Bill amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Bill amount must be greater than 0")
    private BigDecimal billAmount;

    @NotNull(message = "Work date is required")
    private LocalDate workDate;

    @NotBlank(message = "Work type is required")
    private String workType;

    private String notes;

    // Constructors
    public CustomerRequest() {}

    public CustomerRequest(String customerName, Integer age, String phoneNumber,
                           BigDecimal billAmount, LocalDate workDate, String workType, String notes) {
        this.customerName = customerName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.billAmount = billAmount;
        this.workDate = workDate;
        this.workType = workType;
        this.notes = notes;
    }

    // Getters and Setters
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
}