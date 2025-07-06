package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class OrderedProduct {

    @DBRef
    private Product product; // optional, for future reference

    private int quantity;

    // Snapshot fields
    private String nameAtPurchase;
    private double priceAtPurchase;
    private String imageUrlAtPurchase;
}
