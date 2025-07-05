package org.jumia.data.respositories;

import org.jumia.data.models.UserRoleAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleAuditLogRepository extends MongoRepository<UserRoleAuditLog, String> {
}
