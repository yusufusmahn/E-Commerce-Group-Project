package org.temu.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required.")
    private String name;

    @NotBlank(message = "Product description is required.")
    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than zero.")
    private Double price;
}
