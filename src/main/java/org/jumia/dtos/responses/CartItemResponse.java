package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class CartItemResponse {
    private String productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private double totalPrice;
}
