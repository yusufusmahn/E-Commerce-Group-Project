package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class InitiatePaymentRequest {
    private String email;
    private int amount; // in kobo (e.g., 5000 = ₦50.00)
}
