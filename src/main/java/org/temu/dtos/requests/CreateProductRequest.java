package org.temu.dtos.requests;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private double price;
}
