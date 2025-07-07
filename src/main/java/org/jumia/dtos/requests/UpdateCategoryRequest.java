package org.jumia.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

    @NotBlank(message = "Category name is required.")
    private String name;

    private String description;
}
