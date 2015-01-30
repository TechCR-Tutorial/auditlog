/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.conf;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import auditlog.util.AuditLogConstant;
import auditlog.util.Toolkit;

public class AuditLogHttpListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        String applicationType = httpSessionEvent.getSession().getServletContext().getInitParameter("appType");
        if (Toolkit.isEmpty(applicationType)) {
            AuditLogConstant.APPLICATION_TYPE = AuditLogConstant.ApplicationType.WEB;
        } else {
            AuditLogConstant.APPLICATION_TYPE = Toolkit.parseInt(applicationType);
        }
        String userIdParam = httpSessionEvent.getSession().getServletContext().getInitParameter("userIdParam");
        if (Toolkit.isEmpty(userIdParam)) {
            userIdParam = AuditLogConstant.DefaultsValue.USER_ID_PARAM;
        }
        AuditLogConstant.USER_ID_PARAM = userIdParam;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
