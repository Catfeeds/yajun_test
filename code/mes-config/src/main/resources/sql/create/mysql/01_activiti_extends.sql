CREATE TABLE WACT_RE_PROC_DEF (
   ID                   BIGINT(10) primary key not null AUTO_INCREMENT,
   DEF_ID_ varchar(64) not null,
   CATEGORY_ varchar(255),
   NAME_ varchar(255),
   KEY_ varchar(255) not null,
   VERSION_ integer not null,
   DEPLOYMENT_ID_ varchar(64),
   RESOURCE_NAME_ varchar(4000),
   DGRM_RESOURCE_NAME_ varchar(4000),
   DESCRIPTION_ varchar(4000),
   SUSPENSION_STATE_ varchar(2),
   TENANT_ID_ varchar(255) default '',
   HISTORY_URL varchar(1023) default '',
   OPT_COUNTER          BIGINT(5) default 0 not null , 
   CREATE_USER          BIGINT(10),
   CREATE_TIME          TIMESTAMP,
   UPDATE_USER          BIGINT(10),
   UPDATE_TIME          TIMESTAMP
);

CREATE TABLE WACT_RE_MODEL (
   ID               BIGINT(10)  primary key NOT NULL  AUTO_INCREMENT,
   MODEL_ID         VARCHAR(64)         NULL,
   OPT_COUNTER      BIGINT(5) default 0 not null , 
   CREATE_USER      BIGINT(10),
   CREATE_TIME      TIMESTAMP,
   UPDATE_USER      BIGINT(10),
   UPDATE_TIME      TIMESTAMP
);

/*---------流程实例表 ----------*/
CREATE TABLE WACT_RU_EXECUTION (
   ID               BIGINT(10)  primary key NOT NULL AUTO_INCREMENT,
   EXECUTION_ID     VARCHAR(64)         NULL,
   OPT_COUNTER      BIGINT(5) default 0 not null , 
   CREATE_USER      BIGINT(10),
   CREATE_TIME      TIMESTAMP,
   UPDATE_USER      BIGINT(10),
   UPDATE_TIME      TIMESTAMP
);


/*-------代理设置--------*/
CREATE TABLE  WACT_DELEGATE_CONFIG (
  ID                           BIGINT(10)  primary key NOT NULL AUTO_INCREMENT,
  OWNER_ID              BIGINT(10),     
  ASSIGNEE_ID               BIGINT(10),     
  START_TIME                   TIMESTAMP,
  END_TIME                     TIMESTAMP,
  PROCESS_DEFINITION_ID        VARCHAR(64),
  PROCESS_NAME				   VARCHAR(512) NULL,
  OPT_COUNTER                  BIGINT(5) default 0 not null , 
  CREATE_USER                  BIGINT(10),
  CREATE_TIME                  TIMESTAMP,
  UPDATE_USER                  BIGINT(10),
  UPDATE_TIME                  TIMESTAMP,
  MARK_FOR_DELETE              CHAR(1) default '0' not null
);

CREATE TABLE WACT_RU_COUNTER_SIGN(
   ID							BIGINT(10)  PRIMARY KEY NOT NULL AUTO_INCREMENT,
   NODE_ID 						BIGINT(10) NULL,
   SEQUENTIAL 					INT,
   PARTICIPANT 					VARCHAR(200),
   SIGN_TYPE 						VARCHAR(128) NULL,
   RATE							INT NULL,
   DEFINITION_ID 		        VARCHAR(64) NULL,
   TASK_DEFINITION_KEY			VARCHAR(128) NULL,
   MARK_FOR_DELETE              CHAR(1) default '0' not null,
   OPT_COUNTER      			BIGINT(5) default 0 not null  
);

CREATE TABLE WACT_RU_NODE(
   ID							BIGINT(10)  PRIMARY KEY NOT NULL AUTO_INCREMENT,
   TASK_DEFINITION_KEY			VARCHAR(128) NULL,
   TASK_NAME					VARCHAR(256) NULL,
   TASK_DESC					VARCHAR(512) NULL,
   DEFINITION_ID 		        VARCHAR(64) NULL,
   DEFINITION_NAME			    VARCHAR(512) NULL,
   IS_MULTI_INSTANCE            VARCHAR(8) DEFAULT 'N',
   ENABLE_RECALL                VARCHAR(8) default 'N',
   OPT_COUNTER      			BIGINT(5) default 0 not null  
);
CREATE TABLE WACT_RU_NODE_PROPERTY(
   ID							BIGINT(10)  PRIMARY KEY NOT NULL AUTO_INCREMENT,
   NODE_ID 						BIGINT(10) NULL,
   PROPERTITY_NAME				VARCHAR(256) NULL,
   EXPRESSION					VARCHAR(256) NULL,
   VARIABLE						VARCHAR(256) NULL,
   PROPERTITY_TYPE				VARCHAR(64) NULL,
   PROPERTITY_KEY				VARCHAR(256) NULL,
   DEFAULT_EXPRESSION			VARCHAR(256) NULL,
   DATE_PATTERN					VARCHAR(128) NULL,
   READABLE						CHAR(10) NULL,
   WRITEABLE					CHAR(10) NULL,
   REQUIRED						CHAR(10) NULL,
   MARK_FOR_DELETE  			CHAR(1) DEFAULT '0' NOT NULL,
   OPT_COUNTER      			BIGINT(5) DEFAULT 0 NOT NULL  
);
/*==============================================================*/
/* Table: 节点超期预警配置 WACT_NODE_DELAY_ALERT_CONF             */
/*==============================================================*/
create table WACT_NODE_DELAY_ALERT_CONF
(
   ID                   bigint(10) PRIMARY KEY NOT NULL AUTO_INCREMENT,
   NODE_ID              bigint(10),
   DELAY_TYPE           varchar(32) comment 'DELAY_DAYS-逾期天数；DELAY_TO_DATE-特定日期',
   DELAY_DAYS           int,
   DELAY_TO_DATE        date,
   REMIND_TYPE          varchar(32) comment 'OWNER-任务本人；1_SUPERIOR-抄送部门负责人；ROLE-指定角色',
   REMIND_ROLES         varchar(512) comment '多值分号分隔',
   EXPRESSION           varchar(512),
   OPT_COUNTER          bigint(5) not null default 0
);
