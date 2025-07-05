package org.jumia.dtos.responses;

import lombok.Data;
import java.util.List;

@Data
public class CartResponse {
    private List<CartItemResponse> items;
    private double totalAmount;
}
