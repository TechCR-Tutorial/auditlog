/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util;

public class AuditLogConstant {

    public static String USER_ID_PARAM = "";
    public static Integer APPLICATION_TYPE = 0;
    public static String APP_DATE_PATTERN = "yyyy-MM-dd hh:mm.ss";

    public static class Actions {
        public static final String ADD = "ADD";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
    }

    public static class ApplicationType {
        public static Integer WEB = 1;
        public static Integer STD_FROM_ENTITY = 2;
    }

    public static class DefaultsValue {
        public static String USER_ID_PARAM = "user_id";
    }

    public static class DatePattern {
        public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd hh:mm.ss";
        public static String DATE_PATTERN = "yyyy-MM-dd";
    }

    public static class Symbols {
        public static String EMPTY_STRING = "";
        public static String SPACE = "";
    }
}
