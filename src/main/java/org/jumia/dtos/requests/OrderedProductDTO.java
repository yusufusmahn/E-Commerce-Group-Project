package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class OrderedProductDTO {
    private String productId;
    private int quantity;
}
