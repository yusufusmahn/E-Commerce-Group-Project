package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class InitiatePaymentResponse {
    private String authorizationUrl;
    private String accessCode;
    private String reference;
}
