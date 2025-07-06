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

    private String performedByEmail;
    private String targetUserEmail;
    private String action;
    private String previousRole;
    private String newRole;
    private LocalDateTime timestamp = LocalDateTime.now();
}
