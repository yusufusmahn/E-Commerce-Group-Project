package org.jumia.services.seller;

import org.jumia.dtos.responses.SalesReportResponse;

import java.time.LocalDateTime;

public interface SellerAnalyticsService {
    SalesReportResponse getSalesReport(LocalDateTime startDate, LocalDateTime endDate);
    double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);
//    List<String> getTopSellingProducts(String sellerId, int limit);
}


