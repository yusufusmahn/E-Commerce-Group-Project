package org.temu.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String id;
    private String userId;
    private Double totalPrice;
    private String orderStatus;
    private List<String> products;
    private String createdAt;
}
