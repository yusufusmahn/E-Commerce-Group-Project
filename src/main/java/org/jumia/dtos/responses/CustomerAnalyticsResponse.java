package org.jumia.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerAnalyticsResponse {
    private double totalSpent;
    private int totalOrders;
}
