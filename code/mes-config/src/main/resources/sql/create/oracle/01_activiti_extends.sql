CREATE TABLE WACT_RE_PROC_DEF (
   ID                   NUMBER(10) primary key not null,
   DEF_ID_              nvarchar2(64) not null,
   CATEGORY_            nvarchar2(255),
   NAME_                nvarchar2(255),
   KEY_                 nvarchar2(255) not null,
   VERSION_             INTEGER not null,
   DEPLOYMENT_ID_       nvarchar2(64),
   RESOURCE_NAME_       nvarchar2(2000),
   DGRM_RESOURCE_NAME_  nvarchar2(2000),
   DESCRIPTION_         nvarchar2(2000),
   SUSPENSION_STATE_    nvarchar2(2),
   TENANT_ID_           nvarchar2(255) default '',
   HISTORY_URL          nvarchar2(1023) default '',
   OPT_COUNTER          NUMBER(5) default 0 not null , 
   CREATE_USER          NUMBER(10),
   CREATE_TIME          TIMESTAMP,
   UPDATE_USER          NUMBER(10),
   UPDATE_TIME          TIMESTAMP
);

create sequence SEQ_WACT_RE_PROC_DEF_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

CREATE TABLE WACT_RE_MODEL (
   ID               NUMBER(10)  primary key NOT NULL ,
   MODEL_ID         NVARCHAR2(64)         NULL,
   OPT_COUNTER      NUMBER(5) default 0 not null , 
   CREATE_USER      NUMBER(10),
   CREATE_TIME      TIMESTAMP,
   UPDATE_USER      NUMBER(10),
   UPDATE_TIME      TIMESTAMP
);

create sequence SEQ_WACT_RE_MODEL_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

/*---------流程实例表 ----------*/
CREATE TABLE WACT_RU_EXECUTION (
   ID               NUMBER(10)  primary key NOT NULL,
   EXECUTION_ID     NVARCHAR2(64)         NULL,
   OPT_COUNTER      NUMBER(5) default 0 not null , 
   CREATE_USER      NUMBER(10),
   CREATE_TIME      TIMESTAMP,
   UPDATE_USER      NUMBER(10),
   UPDATE_TIME      TIMESTAMP
);

create sequence SEQ_RU_EXECUTION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

/*-------代理设置--------*/
CREATE TABLE  WACT_DELEGATE_CONFIG (
  ID                           NUMBER(10)  primary key NOT NULL,
  OWNER_ID                     NUMBER(10),     
  ASSIGNEE_ID                  NUMBER(10),     
  START_TIME                   TIMESTAMP,
  END_TIME                     TIMESTAMP,
  PROCESS_DEFINITION_ID        NVARCHAR2(64),
  PROCESS_NAME				   NVARCHAR2(512) NULL,
  OPT_COUNTER                  NUMBER(5) default 0 not null , 
  CREATE_USER                  NUMBER(10),
  CREATE_TIME                  TIMESTAMP,
  UPDATE_USER                  NUMBER(10),
  UPDATE_TIME                  TIMESTAMP,
  MARK_FOR_DELETE              CHAR(1) default '0' not null
);

create sequence SEQ_WACT_DELEGATE_CONFIG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

CREATE TABLE WACT_RU_COUNTER_SIGN(
   ID							NUMBER(10)  PRIMARY KEY NOT NULL,
   NODE_ID 						NUMBER(10),
   SEQUENTIAL 					INTEGER,
   PARTICIPANT 					NVARCHAR2(200),
   SIGN_TYPE 					NVARCHAR2(128),
   RATE							INTEGER,
   DEFINITION_ID 		        NVARCHAR2(64),
   TASK_DEFINITION_KEY			NVARCHAR2(128),
   MARK_FOR_DELETE              CHAR(1) default '0' not null,
   OPT_COUNTER      			NUMBER(5) default 0 not null  
);

create sequence SEQ_WACT_RU_COUNTER_SIGN_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

CREATE TABLE WACT_RU_NODE(
   ID							NUMBER(10)  PRIMARY KEY NOT NULL,
   TASK_DEFINITION_KEY			NVARCHAR2(128) NULL,
   TASK_NAME					NVARCHAR2(256) NULL,
   TASK_DESC					NVARCHAR2(512) NULL,
   DEFINITION_ID 		        NVARCHAR2(64) NULL,
   DEFINITION_NAME			    NVARCHAR2(512) NULL,
   IS_MULTI_INSTANCE            NVARCHAR2(8) DEFAULT 'N',
   ENABLE_RECALL                NVARCHAR2(8) default 'N',
   OPT_COUNTER      			NUMBER(5) default 0 not null  
);

create sequence SEQ_WACT_RU_NODE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

CREATE TABLE WACT_RU_NODE_PROPERTY(
   ID							NUMBER(10)  PRIMARY KEY NOT NULL,
   NODE_ID 						NUMBER(10) NULL,
   PROPERTY_NAME				NVARCHAR2(256) NULL,
   EXPRESSION					NVARCHAR2(256) NULL,
   VARIABLE						NVARCHAR2(256) NULL,
   PROPERTY_TYPE				NVARCHAR2(64) NULL,
   PROPERTY_KEY				    NVARCHAR2(256) NULL,
   DEFAULT_EXPRESSION			NVARCHAR2(256) NULL,
   DATE_PATTERN					NVARCHAR2(128) NULL,
   READABLE						CHAR(10) NULL,
   WRITEABLE					CHAR(10) NULL,
   REQUIRED						CHAR(10) NULL,
   MARK_FOR_DELETE  			CHAR(1) DEFAULT '0' NOT NULL,
   OPT_COUNTER      			NUMBER(5) DEFAULT 0 NOT NULL  
);
create sequence SEQ_WACT_RU_NODE_PROPERTY_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;
/*==============================================================*/
/* Table: 节点超期预警配置 WACT_NODE_DELAY_ALERT_CONF             */
/*==============================================================*/
create table WACT_NODE_DELAY_ALERT_CONF
(
   ID                   NUMBER(10) PRIMARY KEY not null,
   NODE_ID              NUMBER(10),
   DELAY_TYPE           nvarchar2(32),
   DELAY_DAYS           INTEGER,
   DELAY_TO_DATE        date,
   REMIND_TYPE          nvarchar2(32),
   REMIND_ROLES         nvarchar2(512),
   EXPRESSION           nvarchar2(512),
   OPT_COUNTER      	NUMBER(5) DEFAULT 0 NOT NULL  
);
create sequence SEQ_WACT_NODE_DELAY_CONF_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;
