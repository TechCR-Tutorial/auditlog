/*
 *
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Toolkit {

    public static boolean isEmpty(String value) {
        return (value == null || "".equals(value.trim()));
    }

    public static String getValueFromMethod(Object entity, String fieldName) {
        try {
            Method method = getMethod("get", entity, fieldName);
            return method.invoke(entity, new Object[]{}).toString();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "0";
    }

    public static Method getMethod(String prefix, Object entity, String fieldName) {
        String methodName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = null;
        try {
            method = entity.getClass().getMethod(methodName, new Class[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;

    }

    public static String nvl(String value) {
        if (isEmpty(value)) {
            return AuditLogConstant.Symbols.EMPTY_STRING;
        }
        return value.trim();
    }

    public static String nvlToSpace(String value) {
        if (isEmpty(value)) {
            return AuditLogConstant.Symbols.SPACE;
        }
        return value.trim();
    }

    public static String formatDate(Date date) {
        return formatDate(date, AuditLogConstant.DatePattern.DEFAULT_DATE_PATTERN);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static Date parseDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {

        }
        return null;
    }

    public static Date parseDate(String date) {
        return parseDate(date, AuditLogConstant.DatePattern.DEFAULT_DATE_PATTERN);
    }

    public static boolean isDateEqual(Date firstDate, Date secDate, String pattern) {
        if (firstDate == null || secDate == null) {
            return false;
        }
        firstDate = parseDate(formatDate(firstDate, pattern), pattern);
        secDate = parseDate(formatDate(secDate, pattern), pattern);
        if (firstDate.equals(secDate)) {
            return true;
        }
        return false;
    }

    public static boolean isDateEqual(Date firstDate, Date secDate) {
        return isDateEqual(firstDate, secDate, AuditLogConstant.DatePattern.DEFAULT_DATE_PATTERN);
    }

    public static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {

        }
        return new Integer(0);
    }

    public static String intToString(Integer value) {
        if (value == null) {
            return Integer.toString(value);
        }
        return AuditLogConstant.Symbols.EMPTY_STRING;
    }


    public static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ex) {
        }
        return new Double(0.0);
    }

    public static String doubleToString(Double value) {
        if (value != null) {
            return Double.toString(value);
        }
        return AuditLogConstant.Symbols.EMPTY_STRING;
    }


    public static Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
        }
        return new Long(0);
    }

    public static String longToString(Long value) {
        if (value == null) {
            return Long.toString(value);
        }
        return AuditLogConstant.Symbols.EMPTY_STRING;
    }

}
