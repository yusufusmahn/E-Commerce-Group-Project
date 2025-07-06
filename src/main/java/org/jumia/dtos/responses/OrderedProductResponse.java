package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class OrderedProductResponse {
    private String productId;
    private int quantity;

    private String name;
    private double price;
    private String imageUrl;
}
