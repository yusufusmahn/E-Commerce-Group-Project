package org.jumia.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCartRequest {
    private List<CartItemUpdateRequest> items;
}
