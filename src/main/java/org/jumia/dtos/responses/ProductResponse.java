package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private String sellerId; // Add sellerId to track ownership
    private Integer quantityAvailable; // Add this field to match Product
    private String imageUrl; // Add this field for the image URL

}
