/*
 *
 * ================================================================
 * Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util.key;

public class EntityKey {

    private String entityName;
    private String entityId;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityKey entityKey = (EntityKey) o;

        if (!entityId.equals(entityKey.entityId)) return false;
        if (!entityName.equals(entityKey.entityName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entityName.hashCode();
        result = 31 * result + entityId.hashCode();
        return result;
    }
}
