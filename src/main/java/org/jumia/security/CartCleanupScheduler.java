package org.jumia.security;

import lombok.extern.slf4j.Slf4j;
import org.jumia.data.models.Cart;
import org.jumia.data.models.CartItem;
import org.jumia.data.respositories.CartItemRepository;
import org.jumia.data.respositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CartCleanupScheduler {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void deleteStaleCarts() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        List<Cart> staleCarts = cartRepository.findByLastUpdatedBefore(cutoff);

        if (!staleCarts.isEmpty()) {
            log.info("Cleaning {} stale carts...", staleCarts.size());
        }

        for (Cart cart : staleCarts) {
            for (CartItem item : cart.getItems()) {
                if (item.getId() != null) {
                    cartItemRepository.deleteById(item.getId());
                }
            }
            cartRepository.delete(cart);
            log.info("✅ Deleted cart for userId: {}", cart.getUserId());
        }
    }

    @Scheduled(cron = "0 30 2 * * *")
    public void cleanInvalidCartItems() {
        List<Cart> allCarts = cartRepository.findAll();
        for (Cart cart : allCarts) {
            List<CartItem> validItems = new ArrayList<>();
            for (CartItem item : cart.getItems()) {
                if (item != null && item.getProduct() != null) {
                    validItems.add(item);
                } else if (item != null && item.getId() != null) {
                    cartItemRepository.deleteById(item.getId());
                    log.info("🗑️ Deleted invalid CartItem in cart for userId: {}", cart.getUserId());
                }
            }

            cart.setItems(validItems);
            cart.setTotalAmount(calculateTotalAmount(cart));
            cart.setLastUpdated(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    private double calculateTotalAmount(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getItems()) {
            if (item == null || item.getProduct() == null) continue;
            total += item.getTotalPrice();
        }
        return total;
    }
}
