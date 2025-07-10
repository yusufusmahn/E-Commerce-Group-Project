package org.jumia.data.respositories;

import org.jumia.data.models.GeneralAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralAuditLogRepository extends MongoRepository<GeneralAuditLog, String> {


}
