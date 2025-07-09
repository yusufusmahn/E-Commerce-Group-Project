package org.jumia.controllers.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jumia.data.models.Order;
import org.jumia.data.models.OrderStatus;
import org.jumia.data.models.User;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.requests.InitiatePaymentRequest;
import org.jumia.dtos.responses.InitiatePaymentResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.services.email.EmailSender;
import org.jumia.services.payment.PaystackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaystackService paystackService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailSender emailSender;


    @Autowired
    private UserRepository userRepository;




    @PostMapping("/initiate")
    public InitiatePaymentResponse initiatePayment(@RequestBody InitiatePaymentRequest request) {
        return paystackService.initiateTransaction(request);
    }


    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("x-paystack-signature") String signature) {
        // Step 1: Verify webhook signature
        boolean isValid = paystackService.isValidWebhook(payload, signature);

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid signature");
        }

        // Step 2: Parse event and get reference
        try {
            JsonNode event = new ObjectMapper().readTree(payload);
            String eventType = event.get("event").asText();

            if ("charge.success".equals(eventType)) {
                String reference = event.get("data").get("reference").asText();

                // Step 3: Confirm payment with Paystack
                boolean verified = paystackService.verifyTransaction(reference);

                if (verified) {
                    Order order = orderRepository.findByPaymentReference(reference)
                            .orElseThrow(() -> new ResourceNotFoundException("Order not found with reference: " + reference));

                    order.setStatus(OrderStatus.PROCESSING);
                    order.setPaidAt(LocalDateTime.now());
                    orderRepository.save(order);

                    User user = userRepository.findById(order.getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found for order"));

                    String subject = "🎉 Payment Successful - Your Order is Confirmed!";
                    String body = """
            Dear %s,<br><br>
            Your payment has been successfully verified and your order is now being processed.<br><br>
            <strong>Order Reference:</strong> %s<br>
            <strong>Total:</strong> ₦%.2f<br><br>
            Thank you for shopping with us!<br>
            - Jumia Team
            """.formatted(user.getName(), reference, order.getTotalPrice());

                    emailSender.send(user.getEmail(), subject, body, true);

                    return ResponseEntity.ok("Payment verified, order updated, and confirmation email sent");
                }
            }
            return ResponseEntity.ok("Event received");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Webhook error");
        }
    }

}
