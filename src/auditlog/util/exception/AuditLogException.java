/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util.exception;

public class AuditLogException extends RuntimeException {

    String errorDesc;

    public AuditLogException() {
        super();
    }

    public AuditLogException(String errorDesc) {
        super(errorDesc);
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
