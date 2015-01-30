/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package auditlog.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import auditlog.domain.AuditLog;

public class AuditLogDao {

    private JdbcTemplate jdbcTemplate;

    public void addAuditLog(AuditLog auditLog) {
        String insertSql = "insert into AUDIT_LOG (LOG_ID, ENTITY_ID, ENTITY_NAME, ACTION, USER_ID, MODIFIED_DATE, DETAILS) " +
                        "values (SEQ_AUDIT_LOG.nextval, ?, ?, ?, ?, ?, ?) ";
        jdbcTemplate.update(insertSql, new Object[] {auditLog.getEntityId(), auditLog.getEntityName(), auditLog.getAction(),
                                                     auditLog.getUserId(), auditLog.getModifiedDate(), auditLog.getDetails()});
    }

    public List<AuditLog> getAuditLogs(AuditLog auditLog) {
        StringBuilder fetchSql = new StringBuilder();
        fetchSql.append("select * from AUDIT_LOG ")
                .append("where ENTITY_NAME = '").append(auditLog.getEntityName()).append("' ")
                .append("and ENTITY_ID = '").append(auditLog.getEntityId()).append("' ")
                .append("order by LOG_ID");

        return jdbcTemplate.query(fetchSql.toString(), new AuditLogRowMapper());
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class AuditLogRowMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            AuditLog auditLog = new AuditLog();
            auditLog.setLogId(resultSet.getLong("LOG_ID"));
            auditLog.setEntityId(resultSet.getString("ENTITY_ID"));
            auditLog.setEntityName(resultSet.getString("ENTITY_NAME"));
            auditLog.setAction(resultSet.getString("ACTION"));
            auditLog.setUserId(resultSet.getString("USER_ID"));
            auditLog.setModifiedDate(resultSet.getTimestamp("MODIFIED_DATE"));
            auditLog.setDetails(resultSet.getString("DETAILS"));
            return auditLog;
        }
    }
}