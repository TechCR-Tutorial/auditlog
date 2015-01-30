/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.PersistentClass;

import auditlog.dao.AuditLogDao;
import auditlog.dao.AuditLogLocalDao;
import auditlog.domain.AuditLog;
import auditlog.util.AuditLogUtil;
import auditlog.util.Toolkit;

public class AuditLogManager {
    private AuditLogDao auditLogDao;
    private AuditLogLocalDao auditLogLocalDao;

    void addAuditLog(AuditLog auditLog) {
        auditLogDao.addAuditLog(auditLog);
    }

    <T extends Serializable> Object getObject(Class clazz, T key) {
        return auditLogLocalDao.getEntity(clazz, key);
    }

    public <T extends Serializable> List<AuditLog> fetchAuditLogs(AuditLog auditLog) {
        List<AuditLog> list = auditLogDao.getAuditLogs(auditLog);
        for (AuditLog log : list) {
            System.out.println(log.getModifiedDate().toString());
        }
        return list;
    }

    public <T extends Serializable> List<AuditLog> getAuditLogs(String entityName, String keyValue) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName(entityName);
        auditLog.setEntityId(keyValue);
        return fetchAuditLogs(auditLog);
    }

    public <T extends Serializable> List<AuditLog> getAuditLogs(Object entity) {
        PersistentClass persistentClass = AuditLogUtil.getPersistentClass(entity.getClass().getName());
        String keyField = persistentClass.getIdentifierProperty().getName();
        String keyValue = Toolkit.getValueFromMethod(entity, keyField);
        return getAuditLogs(persistentClass.getTable().getName(), keyValue);
    }

    public Map<String, Object> getResultMap(String query) {
        return auditLogLocalDao.getObjectAsMap(query);
    }

    public void setAuditLogDao(AuditLogDao auditLogDao) {
        this.auditLogDao = auditLogDao;
    }

    public void setAuditLogLocalDao(AuditLogLocalDao auditLogLocalDao) {
        this.auditLogLocalDao = auditLogLocalDao;
    }
}