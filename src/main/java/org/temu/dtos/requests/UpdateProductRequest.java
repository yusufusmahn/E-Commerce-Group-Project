package org.temu.dtos.requests;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private double price;
}
