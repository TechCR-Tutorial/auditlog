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
@Target(ElementType.TYPE)
public @interface AuditLogger {

    public enum DualStatus {
        YES, NO
    }

    DualStatus auditable() default DualStatus.YES;

    String updatedByField() default "";

    String[] excludes() default "";

}
