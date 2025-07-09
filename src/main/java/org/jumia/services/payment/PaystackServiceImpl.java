package org.jumia.services.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jumia.dtos.requests.InitiatePaymentRequest;
import org.jumia.dtos.responses.InitiatePaymentResponse;
import org.jumia.services.payment.PaystackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaystackServiceImpl implements PaystackService {

    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public InitiatePaymentResponse initiateTransaction(InitiatePaymentRequest request) {
        String url = baseUrl + "/transaction/initialize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<InitiatePaymentRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.path("data");

            InitiatePaymentResponse paymentResponse = new InitiatePaymentResponse();
            paymentResponse.setAuthorizationUrl(data.get("authorization_url").asText());
            paymentResponse.setAccessCode(data.get("access_code").asText());
            paymentResponse.setReference(data.get("reference").asText());

            return paymentResponse;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Paystack response", e);
        }
    }

    @Override
    public boolean verifyTransaction(String reference) {
        String url = baseUrl + "/transaction/verify/" + reference;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.path("data");
            String status = data.get("status").asText();

            return "success".equalsIgnoreCase(status);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isValidWebhook(String payload, String signature) {
        try {
            String mySignature = hmacSha512(payload, secretKey);
            return mySignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSha512(String data, String key) throws Exception {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        sha512Hmac.init(secretKeySpec);
        byte[] macData = sha512Hmac.doFinal(data.getBytes());
        StringBuilder result = new StringBuilder();
        for (byte b : macData) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
