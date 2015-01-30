/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import auditlog.conf.LocalSpringContext;
import auditlog.domain.AuditLog;
import auditlog.service.AuditLogManager;
import auditlog.util.annotate.AuditLogField;
import auditlog.util.annotate.AuditLogger;
import auditlog.util.key.EntityKey;
import auditlog.util.type.AuditLogWrapper;
import auditlog.util.type.EntityKeyWrapper;

public class AuditLogUtil {

    public static void createAuditLog(Object entity, List<AuditLog> logs, String action) {

        AuditLogWrapper wrapper = new AuditLogWrapper();
        boolean continuePerform = configureEntity(entity, wrapper);
        List<String> excludes = wrapper.getExcludes();
        String updatedByField = wrapper.getUpdatedByField();

        if (continuePerform) {
            AuditLog auditLog = new AuditLog();
            PersistentClass persistentClass = getPersistentClass(entity.getClass().getName());
            String keyField = persistentClass.getIdentifierProperty().getName();
            String keyValue = Toolkit.getValueFromMethod(entity, keyField);

            auditLog.setEntityName(persistentClass.getTable().getName());
            auditLog.setEntityId(keyValue);

            if (!Toolkit.isEmpty(updatedByField)) {
                auditLog.setUserId(Toolkit.getValueFromMethod(entity, updatedByField));
            } else {
                auditLog.setUserId(getUserId(entity));
            }
            auditLog.setModifiedDate(new Date());
            //auditLog.setDetails(entity.toString());
            auditLog.setAction(action);
            logs.add(auditLog);
        }
    }

    public static void createAuditLog(EntityKeyWrapper entityWrapper, List<AuditLog> logs, String action) {
        AuditLog auditLog = new AuditLog();

        auditLog.setEntityName(entityWrapper.getEntityKey().getEntityName());
        auditLog.setEntityId(entityWrapper.getEntityKey().getEntityId());
        auditLog.setAction(action);
        auditLog.setUserId(entityWrapper.getUpdatedBy());
        auditLog.setModifiedDate(new Date());

        String[] properties = entityWrapper.getPropertyNames();
        Object[] updatedValues = entityWrapper.getUpdatedValues();
        String[] existsValues = entityWrapper.getExistValues();
        AuditLogWrapper logWrapper = entityWrapper.getLogWrapper();

        StringBuilder diff = new StringBuilder();
        for (int i = 0; i < properties.length; i++) {
            Object updated = updatedValues[i];
            String exists = existsValues[i];
            String property = properties[i];
            if (entityWrapper.isValidProperty(property)) {

                String displayName = logWrapper.getDisplayName(property);
                displayName = Toolkit.isEmpty(displayName) ? property : displayName;

                if (null != updated && !Toolkit.isEmpty(updated.toString())) {

                    if (null == exists) {
                        diff.append(getModifiedString(displayName, "", updated.toString()));
                    } else if (updated instanceof Date) {
                        Date existDate = Toolkit.parseDate(exists, AuditLogConstant.APP_DATE_PATTERN);
                        if (!Toolkit.isDateEqual(existDate, (Date) updated, AuditLogConstant.DatePattern.DATE_PATTERN)) {
                            diff.append(getModifiedString(displayName, Toolkit.formatDate(existDate),
                                    Toolkit.formatDate((Date) updated)));
                        }
                    } else if (updated instanceof Double) {
                        Double updatedValue = (Double) updated;
                        Double existValue = Toolkit.parseDouble(exists);
                        if (!updatedValue.equals(existValue)) {
                            diff.append(getModifiedString(displayName, Toolkit.doubleToString(existValue),
                                    Toolkit.doubleToString(updatedValue)));
                        }
                    } else if (updated instanceof Long) {
                        Long updatedValue = (Long) updated;
                        Long existValue = Toolkit.parseLong(exists);
                        if (!updatedValue.equals(existValue)) {
                            diff.append(getModifiedString(displayName, Toolkit.longToString(existValue),
                                    Toolkit.longToString(updatedValue)));
                        }
                    } else {
                        String updatedValue = updated.toString();
                        if (!updatedValue.equalsIgnoreCase(exists)) {
                            diff.append(getModifiedString(displayName, exists, updatedValue));
                        }
                    }
                } else {
                    //System.out.println(getModifiedString(property, exists, "null"));
                    if (!Toolkit.isEmpty(exists)) {
                        diff.append(getModifiedString(displayName, exists, ""));
                    }
                }
            }
        }
        //System.out.println("Diff ...... " + diff.toString());
        auditLog.setDetails(diff.toString());
        logs.add(auditLog);

    }

    private static String getModifiedString(String property, String exists, String updated) {
        return "" + property + "[" + Toolkit.nvlToSpace(exists) + "->" + Toolkit.nvlToSpace(updated) + "] | ";
    }

    public static EntityKeyWrapper getEntityKeyContainer(Object entity, String[] propertyNames,
                                                         AuditLogManager auditLogManager) {

        AuditLogWrapper wrapper = new AuditLogWrapper();
        boolean continuePerform = configureEntity(entity, wrapper);
        List<String> excludes = wrapper.getExcludes();
        String updatedByField = wrapper.getUpdatedByField();

        EntityKeyWrapper entityKeyContainer = null;
        if (continuePerform) {

            entityKeyContainer = new EntityKeyWrapper();
            entityKeyContainer.setLogWrapper(wrapper);
            PersistentClass persistentClass = getPersistentClass(entity.getClass().getName());
            Property keyProperty = persistentClass.getIdentifierProperty();
            Column keyColumn = (Column) keyProperty.getColumnIterator().next();
            String keyColumnName = keyColumn.getName();
            String keyField = persistentClass.getIdentifierProperty().getName();
            String keyValue = Toolkit.getValueFromMethod(entity, keyField);

            EntityKey entityKey = new EntityKey();
            entityKey.setEntityId(keyValue);
            entityKey.setEntityName(persistentClass.getTable().getName());
            entityKeyContainer.setEntityKey(entityKey);

            StringBuilder query = new StringBuilder();
            query.append("select * from ").append(persistentClass.getTable().getName()).append(" ");
            query.append("where ").append(keyColumnName).append(" = ");
            query.append(keyValue);

            Map<String, Object> resultMap = auditLogManager.getResultMap(query.toString());


            String[] existsValues = new String[propertyNames.length];
            List<String> validProperties = new ArrayList<String>();

            for (int i = 0; i < propertyNames.length; i++) {
                String propertyName = propertyNames[i];
                String objectValue = null;
                try {
                    if (!excludes.contains(propertyName)) {
                        Property property = persistentClass.getProperty(propertyName);
                        //System.out.println("Pro....... " + propertyName);
                        Column column = (Column) property.getColumnIterator().next();
                        if (resultMap.containsKey(column.getName())) {
                            Object object = resultMap.get(column.getName());
                            if (null != object) {
                                if (object instanceof Date) {
                                    objectValue = Toolkit.formatDate((Date) object, AuditLogConstant.APP_DATE_PATTERN);
                                } else {
                                    objectValue = object.toString();
                                }
                            }
                            validProperties.add(propertyName);
                        }
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                existsValues[i] = objectValue;
            }

            entityKeyContainer.setPropertyNames(propertyNames);
            entityKeyContainer.setExistValues(existsValues);
            entityKeyContainer.setValidProperties(validProperties);
            if (!Toolkit.isEmpty(updatedByField)) {
                entityKeyContainer.setUpdatedBy(Toolkit.getValueFromMethod(entity, updatedByField));
            } else {
                entityKeyContainer.setUpdatedBy(getUserId(entity));
            }
        }
        return entityKeyContainer;
    }

    private static boolean configureEntity(Object entity, AuditLogWrapper wrapper) {
        if (entity.getClass().isAnnotationPresent(AuditLogger.class)) {
            AuditLogger auditLogger = entity.getClass().getAnnotation(AuditLogger.class);
            if (auditLogger.auditable() == AuditLogger.DualStatus.YES) {
                wrapper.setUpdatedByField(auditLogger.updatedByField());
                if (null != auditLogger.excludes()) {
                    for (int i = 0; i < auditLogger.excludes().length; i++) {
                        wrapper.setExcludeProperty(auditLogger.excludes()[i]);
                    }
                }
            } else {
                return false;
            }
            performEntityFields(entity.getClass(), wrapper);
        }
        return true;
    }

    private static void performEntityFields(Class clazz, AuditLogWrapper wrapper) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AuditLogField.class)) {
                AuditLogField logField = field.getAnnotation(AuditLogField.class);
                if (logField.exclude() == AuditLogField.DualStatus.YES) {
                    wrapper.setExcludeProperty(field.getName());
                } else {
                    String displayName = logField.displayName();
                    if (!Toolkit.isEmpty(displayName)) {
                        wrapper.putDisplayProperty(field.getName(), displayName);
                    }
                }
                if (AuditLogField.DualStatus.YES == logField.updatedBy()) {
                    wrapper.setUpdatedByField(field.getName());
                }

            }
        }
        if (clazz.getSuperclass() != null) {
            performEntityFields(clazz.getSuperclass(), wrapper);
        }
    }

    public static String getUserId(Object entity) {
        String userId = "";
        try {
            if (AuditLogConstant.APPLICATION_TYPE == AuditLogConstant.ApplicationType.WEB) {
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                userId = (String) attr.getRequest().getSession(true).getAttribute(AuditLogConstant.USER_ID_PARAM);
            } else {
                userId = Toolkit.getValueFromMethod(entity, AuditLogConstant.USER_ID_PARAM);
                //System.out.println("User ....... " + userId);
            }
        } catch (IllegalStateException e) {
            userId = "System";
        }
        return userId;
    }

    public static Object getSpringBean(String id) {
        return LocalSpringContext.getBean(id);
    }

    public static PersistentClass getPersistentClass(String className) {
        LocalSessionFactoryBean factory = getLocalSessionFactoryBean();
        return factory.getConfiguration().getClassMapping(className);
    }

    public static LocalSessionFactoryBean getLocalSessionFactoryBean() {
        return (LocalSessionFactoryBean) getSpringBean("&localHibernateSession");
    }


}
