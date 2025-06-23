package org.temu.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class UpdateOrderRequest {
    private Double totalPrice;
    private String orderStatus;
    private List<String> products;
}
