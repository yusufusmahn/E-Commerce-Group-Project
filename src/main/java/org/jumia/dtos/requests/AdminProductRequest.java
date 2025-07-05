package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class AdminProductRequest {
    private String name;
    private String description;
    private Double price;
}
