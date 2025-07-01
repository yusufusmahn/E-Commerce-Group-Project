package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class UpdateStockRequest {
    private String productId;
    private int newStock;
}
