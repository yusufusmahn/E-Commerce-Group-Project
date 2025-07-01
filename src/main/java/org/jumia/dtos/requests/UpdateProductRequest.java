package org.jumia.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateProductRequest {

    @NotBlank(message = "Product name is required.")
    private String name;

    @NotBlank(message = "Product description is required.")
    private String description;

    @Positive(message = "Price must be greater than zero.")
    private Double price;

    private int quantityAvailable;
}
