package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class OrderedProductResponse {
    private String productId;
    private int quantity;

    private String name; // snapshot
    private double price; // snapshot
    private String imageUrl; // snapshot
}
