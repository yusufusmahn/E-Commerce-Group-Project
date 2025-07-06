package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class SalesReportResponse {
    private int totalOrders;
    private double totalRevenue;

    public SalesReportResponse(int totalOrders, double totalRevenue) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }
}
