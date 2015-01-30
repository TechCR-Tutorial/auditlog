/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.listners;


import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

import auditlog.service.AuditLogManager;

public class HibernateAuditLogListener implements PostUpdateEventListener {

    private AuditLogManager auditLogManager;

    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        Object entity = postUpdateEvent.getEntity();
        System.out.println("Entity " + entity.getClass().getName());
        //AuditLog auditLog = AuditLogUtil.createAuditLog(entity);
        //auditLogManager.addAuditLog(auditLog);
    }

    public void setAuditLogManager(AuditLogManager auditLogManager) {
        this.auditLogManager = auditLogManager;
    }
}
