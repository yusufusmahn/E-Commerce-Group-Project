package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;

    private String userId;

    @DBRef
    private List<CartItem> items = new ArrayList<>();

    private Double totalAmount;
    private LocalDateTime lastUpdated = LocalDateTime.now();

}
