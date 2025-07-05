package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class UpdateSellerDetailsRequest {
    private String contactInfo;
    private String description;
}
