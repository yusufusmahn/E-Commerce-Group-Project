package org.jumia.services.audit;

import org.jumia.data.models.GeneralAuditLog;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;

import java.util.List;

public interface GeneralAuditLogService {
    void log(String actorId, Role actorType, String action, String resource, String resourceId, String message);
    List<GeneralAuditLog> getAllLogsForSuperAdmin(User superAdmin);



}
