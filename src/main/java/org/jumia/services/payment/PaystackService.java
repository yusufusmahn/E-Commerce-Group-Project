package org.jumia.services.payment;

import org.jumia.dtos.requests.InitiatePaymentRequest;
import org.jumia.dtos.responses.InitiatePaymentResponse;

public interface PaystackService {
    InitiatePaymentResponse initiateTransaction(InitiatePaymentRequest request);
    boolean verifyTransaction(String reference);
    boolean isValidWebhook(String payload, String signature);

}
