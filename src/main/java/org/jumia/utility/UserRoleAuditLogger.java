package org.jumia.utility;

import org.jumia.data.models.UserRoleAuditLog;
import org.jumia.data.respositories.UserRoleAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleAuditLogger {

    @Autowired
    private UserRoleAuditLogRepository auditLogRepository;

    public void log(String performedByEmail, String targetUserEmail, String action, String previousRole, String newRole) {
        UserRoleAuditLog log = new UserRoleAuditLog();
        log.setPerformedByEmail(performedByEmail);
        log.setTargetUserEmail(targetUserEmail);
        log.setAction(action);
        log.setPreviousRole(previousRole);
        log.setNewRole(newRole);
        auditLogRepository.save(log);
    }
}
