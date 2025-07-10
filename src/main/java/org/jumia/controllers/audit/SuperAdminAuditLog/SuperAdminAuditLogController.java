package org.jumia.controllers.audit.SuperAdminAuditLog;

import org.jumia.data.models.GeneralAuditLog;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.services.audit.GeneralAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin/audit-logs")
public class SuperAdminAuditLogController {

    @Autowired
    private GeneralAuditLogService auditLogService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @GetMapping
    public List<GeneralAuditLog> getAllAuditLogs() {
        return auditLogService.getAllLogsForSuperAdmin(currentUserProvider.getAuthenticatedUser());
    }
}
