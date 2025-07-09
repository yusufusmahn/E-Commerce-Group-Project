package org.jumia.controllers.customer;

import jakarta.validation.Valid;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.services.customer.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CartResponse addToCart(@RequestBody @Valid AddToCartRequest request) {
        return cartService.addToCartForCurrentUser(request);
    }

    @PutMapping("/update")
    public CartResponse updateCart(@RequestBody @Valid UpdateCartRequest request) {
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

    @PostMapping("/merge")
    public void mergeGuestCart(@RequestBody List<GuestCartItemDTO> items) {
        cartService.mergeGuestCart(items);
    }

}
