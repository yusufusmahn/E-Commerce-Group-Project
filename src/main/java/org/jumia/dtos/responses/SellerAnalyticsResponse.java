package org.jumia.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerAnalyticsResponse {
    private long totalProducts;
    private long totalOrders;
}
