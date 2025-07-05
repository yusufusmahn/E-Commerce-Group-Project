package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cart_items")
public class CartItem {
    @Id
    private String id;

    @DBRef
    private Product product;

    private int quantity;

    private double totalPrice;
}
