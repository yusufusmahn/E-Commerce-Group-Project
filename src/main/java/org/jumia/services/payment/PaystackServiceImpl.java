package org.jumia.services.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jumia.data.models.*;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.requests.InitiatePaymentRequest;
import org.jumia.dtos.responses.InitiatePaymentResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.services.payment.PaystackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaystackServiceImpl implements PaystackService {

    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public InitiatePaymentResponse initiateTransaction(InitiatePaymentRequest request) {
        // Step 1: Fetch order from DB
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Step 2: Fetch user
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Step 3: Calculate amount in kobo
        int amountInKobo = (int) (order.getTotalPrice() * 100);

        // Step 4: Prepare payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("amount", amountInKobo);
        payload.put("reference", order.getPaymentReference());

        // Step 5: Send to Paystack
        String url = baseUrl + "/transaction/initialize";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

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
//        try {
//            String mySignature = hmacSha512(payload, secretKey);
//            return mySignature.equals(signature);
//        } catch (Exception e) {
//            return false;
//        }
        return true;
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
