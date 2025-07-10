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
    private String sellerId;
    private Double totalPrice;
    private OrderStatus status;
    private List<OrderedProduct> products;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private String paymentReference;
    private LocalDateTime paidAt;



}
