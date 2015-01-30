/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import auditlog.domain.AuditLog;
import auditlog.util.AuditLogConstant;
import auditlog.util.AuditLogUtil;
import auditlog.util.type.EntityKeyWrapper;

public class EntityLogInterceptor extends EmptyInterceptor {

    private List<Object> inserts = new ArrayList<Object>();
    private List<EntityKeyWrapper> updates = new ArrayList<EntityKeyWrapper>();
    private List<Object> deletes = new ArrayList<Object>();
    private AuditLogManager auditLogManager;

    /**
     * Called when save an object, the object is not save into database yet.
     */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        inserts.add(entity);
        return super.onSave(entity, id, state, propertyNames, types);
    }

    /**
     * Called when update an object, the object is not update into database yet.
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {

        EntityKeyWrapper container = AuditLogUtil.getEntityKeyContainer(entity, propertyNames, auditLogManager);
        if (null != container) {
            container.setUpdatedValues(currentState);
            updates.add(container);
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    /**
     * Called when you delete an object, the object is not delete into database yet.
     */
    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        deletes.add(entity);
        super.onDelete(entity, id, state, propertyNames, types);
    }

    /**
     * Called after the saved, updated or deleted objects are committed to database.
     */
    @Override
    public void postFlush(Iterator entities) {

        try {
            List<AuditLog> auditLogs = new ArrayList<AuditLog>();
            for (Object entity : inserts) {
                AuditLogUtil.createAuditLog(entity, auditLogs, AuditLogConstant.Actions.ADD);
            }
            for (Object entity : deletes) {
                AuditLogUtil.createAuditLog(entity, auditLogs, AuditLogConstant.Actions.DELETE);
            }
            for (EntityKeyWrapper entity : updates) {
                AuditLogUtil.createAuditLog(entity, auditLogs, AuditLogConstant.Actions.UPDATE);
            }
            if (!auditLogs.isEmpty()) {
                for (AuditLog auditLog : auditLogs) {
                    try {
                        auditLogManager.addAuditLog(auditLog);
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            }
            reset();
        } catch (Exception e) {
        }

    }

    private void reset() {
        inserts = new ArrayList<Object>();
        updates = new ArrayList<EntityKeyWrapper>();
        deletes = new ArrayList<Object>();
    }

    public void setAuditLogManager(AuditLogManager auditLogManager) {
        this.auditLogManager = auditLogManager;
    }

    public void setUserField(String userField) {
        AuditLogConstant.USER_ID_PARAM = userField;
    }
}
