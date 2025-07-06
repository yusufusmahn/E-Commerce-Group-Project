package org.jumia.dtos.responses;

import lombok.Data;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String userId;
    private Double totalPrice;
    private String orderStatus;
    private List<OrderedProductResponse> products;
    private String createdAt;
}
