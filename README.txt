--READ ME FOR AUDIT LOG LIBRARY. --

This library for save changes made to entities through your application.
This is developed using hibernate interceptors. So Hibernate is main requirement for apply this jar.
Also this is valid to table those use number as primary key.

Create AUDIT_LOG table.

  CREATE TABLE "RATES_3"."AUDIT_LOG" 
   (	"LOG_ID" NUMBER NOT NULL ENABLE, 
	"ENTITY_ID" VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	"ENTITY_NAME" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"ACTION" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"USER_ID" VARCHAR2(20 BYTE) NOT NULL ENABLE, 	
	"MODIFIED_DATE" DATE NOT NULL ENABLE,
  "DETAILS" VARCHAR2(4000 BYTE), 
	 CONSTRAINT "AUDIT_LOG_PK" PRIMARY KEY ("LOG_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 163840 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "RATES_3_SMALL_DATA"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 163840 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "RATES_3_SMALL_DATA" ;


Also Create SEQ_AUDIT_LOG sequence.



Also its better if your application use spring.
If not you have to set all dependencies manually.

Define properties related to data base under below keys.
database.driverClassName    = data base drivaer class.
database.url                = url for data base.
database.username           = user name for data base.
database.password           = password for data base.

Also org.springframework.orm.hibernate3.LocalSessionFactoryBean bean under id localHibernateSession.
If you already have configure this use alias.

    <alias name="appHibernateSessionFactory" alias="localHibernateSession" />


Create Application Audit Log Interceptor and set auditLogInterceptor as parent.
    Ex :
    <bean id="appEntityInterceptor" class="com.log.AppEntityInterceptor" parent="auditLogInterceptor" >

    public class AppEntityInterceptor extends EntityLogInterceptor {

    }

Import Audit Log Spring configuration file.
    <import resource="classpath:auditlog/conf/auditlog-conf.xml" />



Annotate AuditLogger
Normally AuditLogger library believe that all entity changes should logged.
So if some entity need to exclude from logging use this annotation.

    @AuditLogger(auditable = AuditLogger.DualStatus.NO)

Also can exclude some fields by using this anotation.
    Ex :
    excludes = {"attribute1", "attribute2"})

@AuditLogField
can exclude a field using this annotation.
  Ex : @AuditLogField(exclude = AuditLogField.DualStatus.YES)

Also can give meaning full name to save in data base.
  Ex : suppose field name is fName and its need to save as FIRST NAME. then use
       @AuditLogField(displayName = "FIRST NAME")

Find Updated User _______________

This library support few ways to get updates user.

1. You can tell library to some specific field to get updated user via annotation.
        @AuditLogger(updatedByField = "updatedBy")
        Should have setter method for updatedBy which return String.

2. If its web Application Library can get logged user automatically. You have do below configuration.
        To say session attribute for user id. You have to put user id to session.
        <context-param>
           <param-name>userIdParam</param-name>
           <param-value>user_id</param-value>
        </context-param>

        And implement listner.
        <listener>
            <listener-class>auditlog.conf.AuditLogHttpListener</listener-class>
        </listener>

3. If its web application can give entity attribute for get user id.
   Then library will access all entities and invoke get method using reflection.
   You have configure as below.
       <listener>
           <listener-class>auditlog.conf.AuditLogHttpListener</listener-class>
       </listener>

        Configure User Id Attribute.
        <context-param>
           <param-name>userIdParam</param-name>
           <param-value>updatedBy</param-value>
        </context-param>

        Configure User Id Attribute.
        <context-param>
           <param-name>appType</param-name>
           <param-value>2</param-value>
        </context-param>

4. Set user parameter on entity creation.
   But You have to 100% sure you have set updated user of entity all time.
        <bean id="appEntityInterceptor" class="xx.xx.XXXEntityInterceptor"
                parent="auditLogInterceptor" >
            <property name="userField" value="updateUser"/>
        </bean>