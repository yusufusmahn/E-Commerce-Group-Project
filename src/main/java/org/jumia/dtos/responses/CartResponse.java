package org.jumia.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private String id;
    private String userId;
    private List<String> productIds;

    private String userEmail; // Add this field

}
