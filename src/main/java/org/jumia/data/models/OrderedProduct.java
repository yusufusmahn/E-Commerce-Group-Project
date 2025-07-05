package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class OrderedProduct {
    @DBRef
    private Product product;

    private int quantity;
}
