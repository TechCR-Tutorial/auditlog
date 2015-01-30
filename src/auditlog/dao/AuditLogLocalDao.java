/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import auditlog.util.AuditLogUtil;

public class AuditLogLocalDao {


    public <T extends Serializable> Object getEntity(Class clazz, T key) {
        Session session = null;
        try {
            SessionFactory sessionFactory = (SessionFactory) AuditLogUtil.getLocalSessionFactoryBean().getObject();
            session = sessionFactory.openSession();
            return session.get(clazz, key);
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }

    public Map<String, Object> getObjectAsMap(String query) {
        Map<String, Object> resultMap = null;
        Session session = null;
        SessionFactory sessionFactory = (SessionFactory) AuditLogUtil.getLocalSessionFactoryBean().getObject();
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            session = sessionFactory.openSession();
            connection = session.connection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            resultMap = getResultMap(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return resultMap;
    }

    private Map<String, Object> getResultMap(ResultSet resultSet) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();
        if (resultSet.next()) {
            for (int i = 1; i <= columnSize; i++) {
                String columnName = metaData.getColumnName(i);
                Object object = resultSet.getObject(i);
                resultMap.put(columnName, object);
            }
        }
        return resultMap;
    }

}
