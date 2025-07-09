package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class GuestCartItemDTO {
    private String productId;
    private int quantity;
}
