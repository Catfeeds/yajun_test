CREATE TABLE WACT_RE_PROC_DEF (
   ID      BIGINT primary key not null identity(0,1),
   DEF_ID_ nvarchar(64) not null,
   CATEGORY_ nvarchar(255),
   NAME_ nvarchar(255),
   KEY_ nvarchar(255) not null,
   VERSION_ int not null,
   DEPLOYMENT_ID_ nvarchar(64),
   RESOURCE_NAME_ nvarchar(4000),
   DGRM_RESOURCE_NAME_ nvarchar(4000),
   DESCRIPTION_ nvarchar(4000),
   SUSPENSION_STATE_ nvarchar(2),
   TENANT_ID_ nvarchar(255) default '',
   HISTORY_URL nvarchar(1023) default '',
   OPT_COUNTER          BIGINT default 0 not null , 
   CREATE_USER          BIGINT,
   CREATE_TIME          datetime,
   UPDATE_USER          BIGINT,
   UPDATE_TIME          datetime
);

CREATE TABLE WACT_RE_MODEL (
   ID               BIGINT  primary key NOT NULL  identity(0,1),
   MODEL_ID         NVARCHAR(64)         NULL,
   OPT_COUNTER      BIGINT default 0 not null , 
   CREATE_USER      BIGINT,
   CREATE_TIME      datetime,
   UPDATE_USER      BIGINT,
   UPDATE_TIME      datetime
);

/*---------流程实例表 ----------*/
CREATE TABLE WACT_RU_EXECUTION (
   ID               BIGINT  primary key NOT NULL identity(0,1),
   EXECUTION_ID     NVARCHAR(64)         NULL,
   OPT_COUNTER      BIGINT default 0 not null , 
   CREATE_USER      BIGINT,
   CREATE_TIME      datetime,
   UPDATE_USER      BIGINT,
   UPDATE_TIME      datetime
);


/*-------代理设置--------*/
CREATE TABLE  WACT_DELEGATE_CONFIG (
  ID                           BIGINT  primary key NOT NULL identity(0,1),
  OWNER_ID              BIGINT,     
  ASSIGNEE_ID               BIGINT,     
  START_TIME                   datetime,
  END_TIME                     datetime,
  PROCESS_DEFINITION_ID        NVARCHAR(64),
  PROCESS_NAME				   NVARCHAR(512) NULL,
  OPT_COUNTER                  BIGINT default 0 not null , 
  CREATE_USER                  BIGINT,
  CREATE_TIME                  datetime,
  UPDATE_USER                  BIGINT,
  UPDATE_TIME                  datetime,
  MARK_FOR_DELETE              CHAR(1) default '0' not null
);

CREATE TABLE WACT_RU_COUNTER_SIGN(
   ID							BIGINT  PRIMARY KEY NOT NULL identity(0,1),
   NODE_ID 						BIGINT NULL,
   SEQUENTIAL 					INT,
   PARTICIPANT 					NVARCHAR(200),
   SIGN_TYPE 						NVARCHAR(128) NULL,
   RATE							INT NULL,
   DEFINITION_ID 		        NVARCHAR(64) NULL,
   TASK_DEFINITION_KEY			NVARCHAR(128) NULL,
   MARK_FOR_DELETE              CHAR(1) default '0' not null,
   OPT_COUNTER      			BIGINT default 0 not null  
);

CREATE TABLE WACT_RU_NODE(
   ID							BIGINT  PRIMARY KEY NOT NULL identity(0,1),
   TASK_DEFINITION_KEY			NVARCHAR(128) NULL,
   TASK_NAME					NVARCHAR(256) NULL,
   TASK_DESC					NVARCHAR(512) NULL,
   DEFINITION_ID 		        NVARCHAR(64) NULL,
   DEFINITION_NAME			    NVARCHAR(512) NULL,
   IS_MULTI_INSTANCE            NVARCHAR(8) DEFAULT 'N',
   ENABLE_RECALL                NVARCHAR(8) default 'N',
   OPT_COUNTER      			BIGINT default 0 not null  
);
CREATE TABLE WACT_RU_NODE_PROPERTY(
   ID							BIGINT  PRIMARY KEY NOT NULL identity(0,1),
   NODE_ID 						BIGINT NULL,
   PROPERTITY_NAME				NVARCHAR(256) NULL,
   EXPRESSION					NVARCHAR(256) NULL,
   VARIABLE						NVARCHAR(256) NULL,
   PROPERTITY_TYPE				NVARCHAR(64) NULL,
   PROPERTITY_KEY				NVARCHAR(256) NULL,
   DEFAULT_EXPRESSION			NVARCHAR(256) NULL,
   DATE_PATTERN					NVARCHAR(128) NULL,
   READABLE						CHAR(10) NULL,
   WRITEABLE					CHAR(10) NULL,
   REQUIRED						CHAR(10) NULL,
   MARK_FOR_DELETE  			CHAR(1) DEFAULT '0' NOT NULL,
   OPT_COUNTER      			BIGINT DEFAULT 0 NOT NULL  
);
/*==============================================================*/
/* Table: 节点超期预警配置 WACT_NODE_DELAY_ALERT_CONF             */
/*==============================================================*/
create table WACT_NODE_DELAY_ALERT_CONF
(
   ID                   BIGINT PRIMARY KEY NOT NULL identity(0,1),
   NODE_ID              BIGINT,
   DELAY_TYPE           NVARCHAR(32),/*'DELAY_DAYS-逾期天数；DELAY_TO_DATE-特定日期'*/
   DELAY_DAYS           int,
   DELAY_TO_DATE        date,
   REMIND_TYPE          NVARCHAR(32),/* comment 'OWNER-任务本人；1_SUPERIOR-抄送部门负责人；ROLE-指定角色'*/
   REMIND_ROLES         NVARCHAR(512),/* comment '多值分号分隔',*/
   EXPRESSION           NVARCHAR(512),
   OPT_COUNTER          BIGINT not null default 0
);
