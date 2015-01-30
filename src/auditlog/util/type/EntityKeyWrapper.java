/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util.type;

import java.util.List;

import auditlog.util.key.EntityKey;

public class EntityKeyWrapper {

    private EntityKey entityKey;
    private String[] propertyNames;
    private Object[] updatedValues;
    private String[] existValues;
    private List<String> validProperties;
    private String updatedBy;
    private AuditLogWrapper logWrapper;

    public void setEntityKey(EntityKey entityKey) {
        this.entityKey = entityKey;
    }

    public EntityKey getEntityKey() {
        return entityKey;
    }

    public String[] getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    public Object[] getUpdatedValues() {
        return updatedValues;
    }

    public void setUpdatedValues(Object[] updatedValues) {
        this.updatedValues = updatedValues;
    }

    public String[] getExistValues() {
        return existValues;
    }

    public void setExistValues(String[] existValues) {
        this.existValues = existValues;
    }

    public List<String> getValidProperties() {
        return validProperties;
    }

    public void setValidProperties(List<String> validProperties) {
        this.validProperties = validProperties;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isValidProperty(String property) {
        if (null != validProperties && !validProperties.isEmpty()) {
            return validProperties.contains(property);
        }
        return false;
    }

    public AuditLogWrapper getLogWrapper() {
        return logWrapper;
    }

    public void setLogWrapper(AuditLogWrapper logWrapper) {
        this.logWrapper = logWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityKeyWrapper that = (EntityKeyWrapper) o;

        if (!entityKey.equals(that.entityKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return entityKey.hashCode();
    }
}
