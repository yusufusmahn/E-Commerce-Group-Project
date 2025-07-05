package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "user_role_audit_logs")
public class UserRoleAuditLog {

    @Id
    private String id;

    private String performedByEmail;   // Who made the change
    private String targetUserEmail;    // Who was modified
    private String action;             // e.g., "PROMOTE_TO_ADMIN", "REVOKE_SELLER"
    private String previousRole;       // e.g., "CUSTOMER"
    private String newRole;            // e.g., "SELLER"
    private LocalDateTime timestamp = LocalDateTime.now();
}
