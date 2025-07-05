package org.jumia.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class DeleteUsersRequest {
    private List<String> emails;
}
