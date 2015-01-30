/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util;

import java.util.List;

import auditlog.domain.AuditLog;

public interface AuditLogCallback {
    public void saveAuditLog(List<AuditLog> auditLogs);
}
