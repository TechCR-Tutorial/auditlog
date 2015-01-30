/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.util.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import auditlog.util.Toolkit;

public class AuditLogWrapper {

    String updatedByField;
    List<String> excludes = new ArrayList<String>();
    Map<String, String> displayMap = new HashMap<String, String>();

    public String getUpdatedByField() {
        return updatedByField;
    }

    public void setUpdatedByField(String updatedByField) {
        this.updatedByField = updatedByField;
    }

    public void setExcludeProperty(String property) {
        if (!Toolkit.isEmpty(property) && !excludes.contains(property)) {
            excludes.add(property);
        }
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public Map<String, String> getDisplayMap() {
        return displayMap;
    }

    public void putDisplayProperty(String fieldName, String displayName) {
        displayMap.put(fieldName, displayName);
    }

    public String getDisplayName(String fieldName) throws NullPointerException{
            return displayMap.get(fieldName);
    }
}
