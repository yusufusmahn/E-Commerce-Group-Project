package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private String sellerId;
    private Integer quantityAvailable;
    private String imageUrl;

}
