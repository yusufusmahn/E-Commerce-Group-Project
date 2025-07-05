package org.jumia.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters.")
    private String name;

    @Size(max = 100, message = "Store name must be 100 characters or less.")
    private String storeName;

    @Size(max = 100, message = "Contact info must be 100 characters or less.")
    private String contactInfo;

    @Size(max = 250, message = "Description must be 250 characters or less.")
    private String description;
}
