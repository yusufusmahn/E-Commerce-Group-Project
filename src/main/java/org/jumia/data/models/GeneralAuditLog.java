package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "audit_logs")
public class GeneralAuditLog {

    @Id
    private String id;

    private String actorId;
    private Role actorType;            // Use Role enum instead of String
    private String action;
    private String resource;
    private String resourceId;
    private String message;
    private LocalDateTime timestamp;
}
