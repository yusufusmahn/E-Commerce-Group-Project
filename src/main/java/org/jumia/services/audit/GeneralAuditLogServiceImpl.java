package org.jumia.services.audit;

import org.jumia.data.models.GeneralAuditLog;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.data.respositories.GeneralAuditLogRepository;
import org.jumia.security.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeneralAuditLogServiceImpl implements GeneralAuditLogService {

    @Autowired
    private GeneralAuditLogRepository auditLogRepository;

    @Override
    public void log(String actorId, Role actorType, String action, String resource, String resourceId, String message) {
        GeneralAuditLog log = new GeneralAuditLog();
        log.setActorId(actorId);
        log.setActorType(actorType);
        log.setAction(action);
        log.setResource(resource);
        log.setResourceId(resourceId);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    @Override
    public List<GeneralAuditLog> getAllLogsForSuperAdmin(User superAdmin) {
        RoleValidator.validateSuperAdmin(superAdmin);
        return auditLogRepository.findAll(); // later, add pagination if needed
    }


}
