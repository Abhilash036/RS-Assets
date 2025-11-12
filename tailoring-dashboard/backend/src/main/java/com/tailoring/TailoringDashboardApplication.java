package com.tailoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TailoringDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(TailoringDashboardApplication.class, args);
        System.out.println("🚀 Tailoring Dashboard Backend is running on http://localhost:8080");
    }
}