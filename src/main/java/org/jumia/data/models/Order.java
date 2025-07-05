package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private String sellerId;     // Add this field for seller-specific orders
    private Double totalPrice;
    private OrderStatus status;
    private List<OrderedProduct> products;

    private LocalDateTime createdAt;


}
