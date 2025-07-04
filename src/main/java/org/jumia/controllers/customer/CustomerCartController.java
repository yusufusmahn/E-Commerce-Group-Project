package org.jumia.controllers.customer;

import org.jumia.dtos.requests.AddToCartRequest;
import org.jumia.dtos.requests.UpdateCartRequest;
import org.jumia.dtos.responses.CartResponse;
import org.jumia.services.customer.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/cart")
public class CustomerCartController {

    @Autowired
    private CustomerCartService cartService;

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCartForCurrentUser();
    }

    @PostMapping("/add")
    public CartResponse addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCartForCurrentUser(request);
    }

    @PutMapping("/update")
    public CartResponse updateCart(@RequestBody UpdateCartRequest request) {
        return cartService.updateCartForCurrentUser(request);
    }

    @DeleteMapping("/remove/{productId}")
    public void removeFromCart(@PathVariable String productId) {
        cartService.removeFromCartForCurrentUser(productId);
    }

    @DeleteMapping("/clear")
    public void clearCart() {
        cartService.clearCartForCurrentUser();
    }
}
