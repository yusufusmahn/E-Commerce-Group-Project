package org.jumia.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required.")
    private String name;

    @NotBlank(message = "Product description is required.")
    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater than zero.")
    private Double price;

    private int quantityAvailable;

    @NotBlank(message = "Category ID is required.")
    private String categoryId; // New


}
