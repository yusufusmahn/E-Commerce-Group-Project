package org.temu.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;

    private String userId;
    private List<String> productIds;
    private Double totalAmount;

}
