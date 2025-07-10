package org.jumia.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectProductRequest {
    @NotBlank(message = "Rejection reason is required")
    private String reason;
}
