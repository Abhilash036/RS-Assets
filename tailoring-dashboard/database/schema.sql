-- Create database
CREATE DATABASE IF NOT EXISTS tailoring_db;
USE tailoring_db;

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    age INT,
    phone_number VARCHAR(20) NOT NULL,
    bill_amount DECIMAL(10,2) NOT NULL,
    work_date DATE NOT NULL,
    work_type VARCHAR(100) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_customer_name (customer_name),
    INDEX idx_work_date (work_date),
    INDEX idx_work_type (work_type),
    INDEX idx_created_at (created_at)
);