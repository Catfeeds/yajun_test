---- Creating TS_LANGUAGE...
create table TS_LANGUAGE
(
  ID          NUMBER(10) PRIMARY KEY not null,
  CODE        VARCHAR2(48),
  ZH_CN       NVARCHAR2(128),
  EN_US       VARCHAR2(128),
  OPT_COUNTER NUMBER(5) default 0 not null
);

create sequence SEQ_LANGUAGE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;
  
-- Creating TS_MENU...
create table TS_MENU
(
  ID              NUMBER(10) PRIMARY KEY not null,
  PARENT_ID       NUMBER(10),
  MENU_NAME       VARCHAR2(128),
  ICON_CLS        VARCHAR2(128),
  MARK_FOR_DELETE CHAR(1) default '0' not null,
  OPT_COUNTER     NUMBER(5) default 0 not null,
  PAGE_URL        VARCHAR2(512)
);

create sequence SEQ_MENU_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

create table TS_AUDIT_CONFIG
(
  ID                NUMBER(10) PRIMARY KEY not null,
  AUDIT_TYPE_ID     VARCHAR2(64),
  AUDIT_TYPE_NAME   VARCHAR2(60),
  OPERATE_TYPE_ID   VARCHAR2(64) not null,
  OPERATE_TYPE_NAME VARCHAR2(100),
  AUDIT_SYSTEM_ID   VARCHAR2(32) not null,
  AUDIT_SYSTEM_NAME VARCHAR2(60)
);

create sequence SEQ_AUDIT_CONFIG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating AUDIT_LOG...
create table TS_AUDIT_LOG
(
  ID       NUMBER(10) PRIMARY KEY not null,
  OPERATOR_ID        NUMBER(10),
  OPERATOR_NAME      VARCHAR2(32),
  AUDIT_CONFIG_ID    NUMBER(10),
  OPERATE_DATE       DATE not null,
  INDEX_ID           VARCHAR2(18),
  AFTER_OPERATE_OBJ  VARCHAR2(3999),
  BEFORE_OPERATE_OBJ VARCHAR2(3999)
);

create sequence SEQ_AUDIT_LOG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TR_DATA_PER_PER_SET...
create table TR_DATA_PER_PER_SET
(
  ID                NUMBER(10) PRIMARY KEY not null,
  PERMISSION_ID     NUMBER(10),
  PERMISSION_SET_ID NUMBER(10),
  DATA_VALUE        VARCHAR2(512),
  DATA_TYPE         VARCHAR2(128),
  OPT_COUNTER       NUMBER(5) default 0
);

create sequence SEQ_DATA_PER_PER_SET_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TR_FORM_PER_PER_SET...
create table TR_FORM_PER_PER_SET
(
  PERMISSION_ID     NUMBER(10),
  PERMISSION_SET_ID NUMBER(10)
)
;

-- Creating TR_PERMISSION_URL...
-- Not sure it's really used
create table TR_PERMISSION_URL
(
  ACTION_URL_ID NUMBER(10) not null,
  PERMISSION_ID NUMBER(10) not null
)
;

-- Creating TR_ROLE_PERMISSION...
create table TR_ROLE_PERMISSION
(
  ROLE_ID       NUMBER(10) not null,
  PERMISSION_ID NUMBER(10) not null
);

-- Creating TR_ROLE_USER...
create table TR_ROLE_USER
(
  USER_ID NUMBER(10) not null,
  ROLE_ID NUMBER(10) not null
);

-- Creating TR_USER_DATA_PERMISSION...
create table TR_USER_DATA_PERMISSION
(
  USER_ID           NUMBER(10),
  PERMISSION_SET_ID NUMBER(10)
);

-- Creating TR_USER_FORM_PERMISSION...
create table TR_USER_FORM_PERMISSION
(
  USER_ID           NUMBER(10),
  PERMISSION_SET_ID NUMBER(10)
);

-- Creating TR_USER_POSITION...
create table TR_USER_POSITION
(
  USER_ID     NUMBER(10) not null,
  POSITION_ID NUMBER(10) not null
);

-- Createing TR_USER_OGRNIZATION
create table TR_USER_ORGNIZATION
(
	USER_ID         NUMBER(10) not null,
	ORGNIZATION_ID  NUMBER(10) not null
);

-- Creating TS_ACTION_URL...
create table TS_ACTION_URL
(
  ID           NUMBER(10) PRIMARY KEY not null,
  PERMISSION_CODE      varchar(128),
  REQUEST_PATH VARCHAR2(128),
  URL_DESC     VARCHAR2(512),
  OPT_COUNTER  NUMBER(5) default 0 not null
);

create sequence SEQ_ACTION_URL_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_DATA_PERMISSION...
create table TS_DATA_PERMISSION
(
  ID              NUMBER(10) PRIMARY KEY not null,
  PERMISSION_CODE VARCHAR2(128),
  RELATION_CODE   VARCHAR2(128),
  DESCRIPTION     VARCHAR2(256),
  DATA_URL        VARCHAR2(128),
  MODULE          VARCHAR2(128),
  OPT_COUNTER     NUMBER(5) default 0,
  PACKAGE_NAME    VARCHAR2(256)
);

create sequence SEQ_DATA_PERMISSION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_DATA_PERMISSION_SET...
create table TS_DATA_PERMISSION_SET
(
  ID                  NUMBER(10) PRIMARY KEY not null,
  DESCRIPTION         VARCHAR2(256),
  PERMISSION_SET_CODE VARCHAR2(128),
  OPT_COUNTER         NUMBER(5) default 0
);

create sequence SEQ_DATA_PERMISSION_SET_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_EMAIL...
create table TS_EMAIL
(
  ID          NUMBER(10) PRIMARY KEY not null,
  SUBJECT     VARCHAR2(256),
  MAIL_TO     VARCHAR2(2000),
  FILES       VARCHAR2(2000),
  CC          VARCHAR2(2000),
  FLAG        CHAR(1),
  OPT_COUNTER NUMBER(5) default 0,
  TEXT        CLOB
);

create sequence SEQ_EMAIL_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_DICT_ENTRY...
create table TS_DICT_ENTRY
(
  ID            NUMBER(12) PRIMARY KEY not null,
  TYPE_ID       NUMBER(10),
  ENTRY_CODE    VARCHAR2(32),
  ENTRY_NAME    NVARCHAR2(128),
  ENTRY_NAME_EN VARCHAR2(256),
  SORT_NO       NUMBER(10),
  SYSTEM_DATA   varchar2(32),
  OPT_COUNTER   NUMBER(5) default 0 not null
);

create sequence SEQ_DICT_ENTRY_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_FORM_PERMISSION...
create table TS_FORM_PERMISSION
(
  ID              NUMBER(10) PRIMARY KEY not null,
  PERMISSION_CODE VARCHAR2(128),
  PERMISSION_NAME VARCHAR2(128),
  DESCRIPTION     VARCHAR2(256),
  MODULE          VARCHAR2(128),
  OPT_COUNTER     NUMBER(5) default 0
);

create sequence SEQ_FORM_PERMISSION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;


-- Creating TS_FORM_PERMISSION_SET...
create table TS_FORM_PERMISSION_SET
(
  ID                  NUMBER(10) PRIMARY KEY not null,
  PERMISSION_SET_NAME VARCHAR2(128),
  DESCRIPTION         VARCHAR2(256),
  PERMISSION_SET_CODE VARCHAR2(128),
  OPT_COUNTER         NUMBER(5) default 0
);

create sequence SEQ_FORM_PERMISSION_SET_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;


-- Creating TS_JOB...
create table TS_JOB
(
  ID                NUMBER(10) PRIMARY KEY not null,
  JOB_NAME          NVARCHAR2(128),
  JOB_TYPE          NVARCHAR2(512),
  CRON_EXPRESSION   VARCHAR2(512),
  RUNTIME_LAST      TIMESTAMP,
  RUNTIME_NEXT      TIMESTAMP,
  JOB_STATUS        VARCHAR2(32) default 'AWAITING',
  ENABLE_STATUS     VARCHAR2(16) default 'ENABLE',
  RUN_TIMES         INTEGER DEFAULT 0,
  RUN_DURATION      NUMBER,
  SYNC_BEGIN_TIME   TIMESTAMP,
  SYNC_END_TIME     TIMESTAMP,
  JOB_MEMO          NVARCHAR2(512),
  JOB_CLASS         VARCHAR2(512),
  JOB_METHOD        VARCHAR2(64),
  JOB_OBJECT        VARCHAR2(128)
);

create sequence SEQ_JOB_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_JOB_LOG...
create table TS_JOB_LOG
(
   ID                   NUMBER(10) PRIMARY KEY not null,
   JOB_ID               NUMBER(10),
   EXE_TIME             TIMESTAMP,
   EXE_RESULT           varchar2(16),
   ERROR_MSG            CLOB
);

create sequence SEQ_JOB_LOG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_ORGNIZATION...
create table TS_ORGNIZATION
(
  ID                      NUMBER(10) PRIMARY KEY not null,
  DIMENSION               VARCHAR2(100),
  ORGNIZATION_NAME        NVARCHAR2(50),
  ORGNIZATION_CODE        VARCHAR2(50),
  PARENT_ID               NUMBER(10),
  ORGNIZATION_TYPE        VARCHAR2(100),
  ORGNIZATION_DESCRIPTION NVARCHAR2(150),
  LEADER_ID               NUMBER(10),
  SYSTEM_CODE             VARCHAR2(512),
  CREATE_USER             NUMBER(10),
  UPDATE_USER             NUMBER(10),
  CREATE_TIME             TIMESTAMP(6),
  UPDATE_TIME             TIMESTAMP(6),
  OPT_COUNTER             NUMBER(5) default 0 not null
);

create sequence SEQ_ORGNIZATION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_PERMISSION...
create table TS_PERMISSION
(
  ID                 NUMBER(10) PRIMARY KEY not null,
  MENU_ID            NUMBER(10),
  PERMISSION_CODE    VARCHAR2(32),
  PERMISSION_NAME    VARCHAR2(128),
  IS_MENU_PERMISSION CHAR(1) default '0',
  OPT_COUNTER        NUMBER(5) default 0 not null
);

create sequence SEQ_PERMISSION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_POSITION...
create table TS_POSITION
(
  ID                   NUMBER(10) PRIMARY KEY not null,
  POSITION_CODE        VARCHAR2(50),
  POSITION_NAME        NVARCHAR2(50),
  POSITION_DESCRIPTION NVARCHAR2(150),
  ORGNIZATION_ID       NUMBER(10),
  PARENT_ID            NUMBER(10),
  CREATE_USER          NUMBER(10),
  UPDATE_USER          NUMBER(10),
  CREATE_TIME          TIMESTAMP,
  UPDATE_TIME          TIMESTAMP,
  OPT_COUNTER          NUMBER(5) default 0 not null
);

create sequence SEQ_POSITION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_ROLE...
create table TS_ROLE
(
  ID              NUMBER(10) PRIMARY KEY not null,
  ROLE_CODE       VARCHAR2(32),
  SYSTEM_DATA	  VARCHAR2(32),
  ROLE_NAME       NVARCHAR2(128),
  ROLE_DESC       NVARCHAR2(512),
  OPT_COUNTER     NUMBER(5) default 0 not null,
  MARK_FOR_DELETE CHAR(1) default '0' not null,
  CREATE_USER     NUMBER(10),
  CREATE_TIME     TIMESTAMP(6),
  UPDATE_USER     NUMBER(10),
  UPDATE_TIME     TIMESTAMP(6)
);

create sequence SEQ_ROLE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_TYPE...
create table TS_DICT_TYPE
(
  ID              NUMBER(10) PRIMARY KEY not null,
  TYPE_CODE       VARCHAR2(32),
  TYPE_NAME       NVARCHAR2(128),
  TYPE_NAME_EN    NVARCHAR2(256),
  SYSTEM_DATA     VARCHAR2(32),
  MARK_FOR_DELETE char(1) default '0' not null
);

create sequence SEQ_DICT_TYPE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Creating TS_USER...
create table TS_USER
(
  ID              NUMBER(10) PRIMARY KEY not null,
  ACCOUNT         VARCHAR2(64),
  PASSWORD        VARCHAR2(128),
  HISTORY_PWD     VARCHAR2(1024),
  STATUS          VARCHAR2(32),
  USER_CODE       VARCHAR2(32),
  USER_NAME       NVARCHAR2(128),
  PHONE           VARCHAR2(64),
  EMAIL           VARCHAR2(128),
  ADDRESS         NVARCHAR2(128),
  GENDER          VARCHAR2(32),
  USER_TYPE       CHAR(1),
  PWD_UPDATE_TIME TIMESTAMP,
  LAST_LOGIN_DATE TIMESTAMP,	
  CREATE_USER     NUMBER(10),
  CREATE_TIME     TIMESTAMP,
  UPDATE_USER     NUMBER(10),
  UPDATE_TIME     TIMESTAMP,
  MARK_FOR_DELETE CHAR(1) default '0' not null,
  OPT_COUNTER     NUMBER(5) default 0 not null
);

create sequence SEQ_USER_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- 系统配置
create table TS_GLOBAL_CONFIGURATION (
   ID                   NUMBER(10) primary key not null,
   SYS_KEY       		VARCHAR2(64),
   NAME               	NVARCHAR2(128),
   SYS_VALUE			VARCHAR2(128),
   PROPERTY				VARCHAR2(64),
   REMARK             	VARCHAR2(640),
   SEQ					VARCHAR2(20),
   OPT_COUNTER             NUMBER(5) default 0 not null , 
   CREATE_USER             NUMBER(10),
   CREATE_TIME             TIMESTAMP(6),
   UPDATE_USER             NUMBER(10),
   UPDATE_TIME             TIMESTAMP(6),
   MARK_FOR_DELETE    CHAR(1) default '0' not null
);

create sequence SEQ_GLOBAL_CONFIGURATION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;
 

--CREATINT TS_SERIAL_NUM...
create table TS_SERIAL_NUM (
  ID          NUMBER(10) PRIMARY KEY not null,
  SN_KEY      VARCHAR2(64) default '' not null ,
  SN          NUMBER(20)  default 0 not null
);

create sequence SEQ_SERIAL_NUM_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

-- Done.

  
--CREATE TS_NOTIFICATION                                       
CREATE TABLE TS_NOTIFICATION (
  ID                    NUMBER(10) PRIMARY KEY NOT NULL,
  SENDER_ID             NUMBER(10),
  RECEIVER_ID           NUMBER(10),
  READ_FLAG             NUMBER(2) DEFAULT 0,
  MSG_TYPE              VARCHAR2(10),
  MSG_ID                NUMBER(10)
);

create sequence SEQ_NOTIFICATION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

--CREATE TS_NOTIFICATION 
CREATE TABLE TS_MSG_CONTENT (
  ID                    NUMBER(10) PRIMARY KEY NOT NULL,
  TITLE                 VARCHAR2(200),
  CONTENT               clob,
  MSG_TYPE           VARCHAR2(10),
  OPT_COUNTER           NUMBER(5) default 0 not null , 
  CREATE_USER           NUMBER(10),
  CREATE_TIME           TIMESTAMP(6),
  UPDATE_USER           NUMBER(10),
  UPDATE_TIME           TIMESTAMP(6)
);  
  
create sequence SEQ_MSG_CONTENT_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

--CREATE TS_ATTACHMENT 
create table TS_ATTACHMENT
(
   ID                   		NUMBER(10) PRIMARY KEY not null,
   NAME      					VARCHAR2(256),
   URL							VARCHAR2(256),
   CREATE_USER    				NUMBER(10),
   CREATE_TIME					TIMESTAMP,
   UPDATE_USER					NUMBER(10),
   UPDATE_TIME					TIMESTAMP,
   OPT_COUNTER					NUMBER(5) default 0 not null
);

create sequence SEQ_ATTAHMENT_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

--CREATE TR_MSG_ATTACHMENT 
create table TR_MSG_ATTACHMENT
(
   MSG_ID              NUMBER(10) not null,
   ATTACHMENT_ID       NUMBER(10) not null
);

--CREATE TS_PICTURE 
create table TS_PICTURE (
   ID                   NUMBER(10) PRIMARY KEY not null,
   NAME                 nvarchar2(128),
   ORG_REL_URL          varchar2(512),
   THUMB_REL_URL        varchar2(512),
   CREATE_USER          NUMBER(10),
   CREATE_TIME          TIMESTAMP ,
   UPDATE_USER          NUMBER(10),
   UPDATE_TIME          TIMESTAMP,
   OPT_COUNTER          NUMBER(5) default 0 not null
);

create sequence SEQ_PICTURE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;





