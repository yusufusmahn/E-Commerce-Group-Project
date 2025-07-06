package org.jumia.controllers.Admin;

import org.jumia.dtos.responses.CartResponse;
import org.jumia.services.admin.AdminCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/carts")
public class AdminCartController {

    @Autowired
    private AdminCartService adminCartService;

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        List<CartResponse> carts = adminCartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCartForUser(@PathVariable String userId) {
        adminCartService.clearCartForUser(userId);
        return ResponseEntity.ok("Cart cleared for user ID: " + userId);
    }

    @DeleteMapping("/remove-product/{productId}")
    public ResponseEntity<?> removeProductFromAllCarts(@PathVariable String productId) {
        adminCartService.removeProductFromAllCarts(productId);
        return ResponseEntity.ok("Product removed from all carts: " + productId);
    }
}
