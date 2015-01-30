/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AuditLogField {

    public enum DualStatus {
        YES, NO
    }

    DualStatus exclude() default DualStatus.NO;
    String displayName() default "";
    DualStatus updatedBy() default DualStatus.NO;
}
