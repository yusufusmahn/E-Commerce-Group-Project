package org.temu.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
public class Cart {
    @Id
    private String id;

    private String userId;
    private List<String> productIds;
    private Double totalAmount;

}
