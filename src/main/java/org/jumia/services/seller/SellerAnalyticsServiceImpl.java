package org.jumia.services.seller;

import org.jumia.data.models.User;
import org.jumia.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Order;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.responses.SalesReportResponse;
import org.jumia.exceptions.AccessDeniedException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.RoleValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerAnalyticsServiceImpl implements SellerAnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public SalesReportResponse getSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Order> orders = orderRepository.findBySellerIdAndCreatedAtBetween(seller.getId(), startDate, endDate);

        double totalRevenue = 0.0;
        int totalOrders = 0;

        for (Order order : orders) {
            totalRevenue += order.getTotalPrice();
            totalOrders++;
        }

        return new SalesReportResponse(totalOrders, totalRevenue);
    }

    @Override
    public double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Order> orders = orderRepository.findBySellerIdAndCreatedAtBetween(seller.getId(), startDate, endDate);

        double total = 0.0;
        for (Order order : orders) {
            total += order.getTotalPrice();
        }

        return total;
    }

//    @Override
//    public double calculateTotalRevenue(String sellerId, LocalDateTime startDate, LocalDateTime endDate) {
//        validateSeller(sellerId);
//
//        double totalRevenue = 0.0;
//        List<Order> orders = orderRepository.findBySellerIdAndCreatedAtBetween(sellerId, startDate, endDate);
//
//        for (Order order : orders) {
//            totalRevenue += order.getTotalPrice();
//        }
//
//        return totalRevenue;
//    }



//    @Override
//    public List<String> getTopProducts(Map<String, Integer> productSalesCount, int limit) {
//        // Convert the map entries to a list
//        List<Map.Entry<String, Integer>> entries = new ArrayList<>(productSalesCount.entrySet());
//
//        // Sort the entries in descending order of sales count
//        entries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
//
//        // Collect the top `limit` product names
//        List<String> topProducts = new ArrayList<>();
//        for (int i = 0; i < Math.min(limit, entries.size()); i++) {
//            topProducts.add(entries.get(i).getKey());
//        }
//
//        return topProducts;
//    }



}
