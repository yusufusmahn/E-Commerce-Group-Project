package org.jumia.controllers.seller;

import org.jumia.dtos.responses.SalesReportResponse;
import org.jumia.services.seller.SellerAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/seller/analytics")
public class SellerAnalyticsController {

    @Autowired
    private SellerAnalyticsService sellerAnalyticsService;

    @GetMapping("/report")
    public SalesReportResponse getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return sellerAnalyticsService.getSalesReport(startDate, endDate);
    }

    @GetMapping("/revenue")
    public double getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return sellerAnalyticsService.calculateTotalRevenue(startDate, endDate);
    }
}
