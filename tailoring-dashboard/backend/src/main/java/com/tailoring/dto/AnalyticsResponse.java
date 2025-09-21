package com.tailoring.dto;

import java.math.BigDecimal;
import java.util.List;

public class AnalyticsResponse {

    private BigDecimal totalRevenue;
    private Long totalCustomers;
    private BigDecimal avgBill;
    private List<CustomerResponse> recentTransactions;
    private ChartData chartData;

    // Constructors
    public AnalyticsResponse() {}

    public AnalyticsResponse(BigDecimal totalRevenue, Long totalCustomers,
                             BigDecimal avgBill, List<CustomerResponse> recentTransactions,
                             ChartData chartData) {
        this.totalRevenue = totalRevenue;
        this.totalCustomers = totalCustomers;
        this.avgBill = avgBill;
        this.recentTransactions = recentTransactions;
        this.chartData = chartData;
    }

    // Getters and Setters
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(Long totalCustomers) { this.totalCustomers = totalCustomers; }

    public BigDecimal getAvgBill() { return avgBill; }
    public void setAvgBill(BigDecimal avgBill) { this.avgBill = avgBill; }

    public List<CustomerResponse> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<CustomerResponse> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    public ChartData getChartData() { return chartData; }
    public void setChartData(ChartData chartData) { this.chartData = chartData; }

    // Inner class for chart data
    public static class ChartData {
        private RevenueData revenueData;
        private WorkTypeData workTypeData;

        public ChartData() {}

        public ChartData(RevenueData revenueData, WorkTypeData workTypeData) {
            this.revenueData = revenueData;
            this.workTypeData = workTypeData;
        }

        public RevenueData getRevenueData() { return revenueData; }
        public void setRevenueData(RevenueData revenueData) { this.revenueData = revenueData; }

        public WorkTypeData getWorkTypeData() { return workTypeData; }
        public void setWorkTypeData(WorkTypeData workTypeData) { this.workTypeData = workTypeData; }
    }

    public static class RevenueData {
        private List<String> labels;
        private List<BigDecimal> data;

        public RevenueData() {}

        public RevenueData(List<String> labels, List<BigDecimal> data) {
            this.labels = labels;
            this.data = data;
        }

        public List<String> getLabels() { return labels; }
        public void setLabels(List<String> labels) { this.labels = labels; }

        public List<BigDecimal> getData() { return data; }
        public void setData(List<BigDecimal> data) { this.data = data; }
    }

    public static class WorkTypeData {
        private List<String> labels;
        private List<Long> data;

        public WorkTypeData() {}

        public WorkTypeData(List<String> labels, List<Long> data) {
            this.labels = labels;
            this.data = data;
        }

        public List<String> getLabels() { return labels; }
        public void setLabels(List<String> labels) { this.labels = labels; }

        public List<Long> getData() { return data; }
        public void setData(List<Long> data) { this.data = data; }
    }
}