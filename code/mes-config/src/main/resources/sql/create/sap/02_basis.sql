/*==============================================================*/
/* Table: TS_LANGUAGE                                           */
/*==============================================================*/
create table TS_LANGUAGE
(
   ID                   BIGINT not null,
   CODE                 varchar(255),
   ZH_CN                varchar(255),
   EN_US                varchar(255),
   OPT_COUNTER          BIGINT not null default 0,
   primary key (ID)
) ;

create sequence SEQ_LANGUAGE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;

/*==============================================================*/
/* Table: TS_MENU                                           */
/*==============================================================*/
create table TS_MENU
(
   ID                   BIGINT not null,
   PARENT_ID            BIGINT,
   MENU_NAME            varchar(128),
   ICON_CLS             varchar(128),
   MARK_FOR_DELETE      char(1) not null default '0',
   OPT_COUNTER          BIGINT not null default 0,
   PAGE_URL             varchar(512),
   primary key (ID)
) ;

create sequence SEQ_MENU_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_AUDIT_CONFIG                                          */
/*==============================================================*/
create table TS_AUDIT_CONFIG
(
   ID                   BIGINT not null,
   AUDIT_TYPE_ID        varchar(64),
   AUDIT_TYPE_NAME      varchar(60),
   OPERATE_TYPE_ID      varchar(64) not null,
   OPERATE_TYPE_NAME    varchar(60),
   AUDIT_SYSTEM_ID      varchar(32) not null,
   AUDIT_SYSTEM_NAME    varchar(60),
   primary key (ID)
) ;

create sequence SEQ_AUDIT_CONFIG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_AUDIT_LOG                                             */
/*==============================================================*/
create table TS_AUDIT_LOG
(
   ID         BIGINT not null,
   OPERATOR_ID          BIGINT,
   OPERATOR_NAME        varchar(32),
   AUDIT_CONFIG_ID		BIGINT ,
   OPERATE_DATE         timestamp,
   INDEX_ID             varchar(18),
   AFTER_OPERATE_OBJ    varchar(3999),
   BEFORE_OPERATE_OBJ   varchar(3999),
    primary key (ID)
) ;

create sequence SEQ_AUDIT_LOG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TR_DATA_PER_PER_SET                                   */
/*==============================================================*/
create table TR_DATA_PER_PER_SET
(
   ID                   BIGINT not null,
   PERMISSION_ID        BIGINT,
   PERMISSION_SET_ID    BIGINT,
   DATA_VALUE           varchar(512),
   DATA_TYPE            varchar(128),
   OPT_COUNTER          BIGINT default 0,
   primary key (ID)
) ;

create sequence SEQ_DATA_PER_PER_SET_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TR_FORM_PER_PER_SET                                   */
/*==============================================================*/
create table TR_FORM_PER_PER_SET
(
   PERMISSION_ID        BIGINT,
   PERMISSION_SET_ID    BIGINT
) ;

-- Creating TR_PERMISSION_URL...
-- Not sure it's really used
create table TR_PERMISSION_URL
(
  ACTION_URL_ID BIGINT not null,
  PERMISSION_ID BIGINT not null
)
;

-- Creating TR_ROLE_PERMISSION...
create table TR_ROLE_PERMISSION
(
   ROLE_ID              BIGINT not null,
   PERMISSION_ID        BIGINT not null
) ;

/*==============================================================*/
/* Table: TR_ROLE_USER                                          */
/*==============================================================*/
create table TR_ROLE_USER
(
   USER_ID              BIGINT not null,
   ROLE_ID              BIGINT not null
) ;

/*==============================================================*/
/* Table: TR_USER_DATA_PERMISSION                               */
/*==============================================================*/
create table TR_USER_DATA_PERMISSION
(
   USER_ID              BIGINT,
   PERMISSION_SET_ID    BIGINT
) ;

/*==============================================================*/
/* Table: TR_USER_FORM_PERMISSION                               */
/*==============================================================*/
create table TR_USER_FORM_PERMISSION
(
   USER_ID              BIGINT,
   PERMISSION_SET_ID    BIGINT
) ;

/*==============================================================*/
/* Table: TR_USER_POSITION                                      */
/*==============================================================*/
create table TR_USER_POSITION
(
   USER_ID              BIGINT not null,
   POSITION_ID          BIGINT not null
) ;

/*==============================================================*/
/* Table: TR_USER_ORGNIZATION                                      */
/*==============================================================*/
create table TR_USER_ORGNIZATION
(
   USER_ID              BIGINT not null,
   ORGNIZATION_ID       BIGINT not null
) ;

/*==============================================================*/
/* Table: TS_ACTION_URL                                         */
/*==============================================================*/
create table TS_ACTION_URL
(
   ID                   BIGINT not null,
   PERMISSION_CODE      varchar(128),
   REQUEST_PATH         varchar(128),
   URL_DESC             varchar(512),
   OPT_COUNTER          BIGINT not null default 0,
   primary key (ID)
) ;

create sequence SEQ_ACTION_URL_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_DATA_PERMISSION                                    */
/*==============================================================*/
create table TS_DATA_PERMISSION
(
   ID                   BIGINT not null,
   PERMISSION_CODE      varchar(128),
   RELATION_CODE        varchar(128),
   DESCRIPTION          varchar(256),
   DATA_URL             varchar(128),
   MODULE               varchar(128),
   OPT_COUNTER          BIGINT default 0,
   PACKAGE_NAME         varchar(256) ,
   primary key (ID)
) ;

create sequence SEQ_DATA_PERMISSION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_DATA_PERMISSION_SET                                */
/*==============================================================*/
create table TS_DATA_PERMISSION_SET
(
   ID                   BIGINT not null,
   DESCRIPTION          varchar(256),
   PERMISSION_SET_CODE  varchar(128),
   OPT_COUNTER          BIGINT default 0,
   primary key (ID)
) ;

create sequence SEQ_DATA_PERMISSION_SET_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_EMAIL                                              */
/*==============================================================*/
create table TS_EMAIL
(
   ID                   BIGINT not null,
   SUBJECT              varchar(256),
   MAIL_TO              varchar(2000),
   FILES                varchar(2000),
   CC                   varchar(2000),
   FLAG                 char(1),
   OPT_COUNTER          BIGINT default 0,
   TEXT                 varchar(3999),
   primary key (ID)
) ;

create sequence SEQ_MAIL_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_ENTRY                                              */
/*==============================================================*/
create table TS_DICT_ENTRY
(
   ID                   bigint not null,
   TYPE_ID              bigint,
   ENTRY_CODE           varchar(128),
   ENTRY_NAME           nvarchar(128),
   ENTRY_NAME_EN        varchar(256),
   SORT_NO              bigint,
   SYSTEM_DATA          varchar(32),
   MARK_FOR_DELETE      char(1) not null default '0',
   primary key (ID)
) ;

create sequence SEQ_DICT_ENTRY_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_FORM_PERMISSION                                    */
/*==============================================================*/
create table TS_FORM_PERMISSION
(
   ID                   BIGINT not null,
   PERMISSION_CODE      varchar(128),
   PERMISSION_NAME      varchar(128),
   DESCRIPTION          varchar(256),
   MODULE               varchar(128),
   OPT_COUNTER          BIGINT default 0,
   primary key (ID)
) ;

create sequence SEQ_FORM_PERMISSION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_FORM_PERMISSION_SET                                */
/*==============================================================*/
create table TS_FORM_PERMISSION_SET
(
   ID                   BIGINT not null,
   PERMISSION_SET_NAME  varchar(128),
   DESCRIPTION          varchar(256),
   PERMISSION_SET_CODE  varchar(128),
   OPT_COUNTER          BIGINT default 0,
   primary key (ID)
) ;

create sequence SEQ_FORM_PERMISSION_SET_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_JOB                                                */
/*==============================================================*/
create table TS_JOB
(
   ID                   bigint not null,
   JOB_NAME             nvarchar(128),
   JOB_TYPE             varchar(512),
   CRON_EXPRESSION      varchar(512),
   RUNTIME_LAST         timestamp,
   RUNTIME_NEXT         timestamp,
   JOB_STATUS           varchar(32) default 'AWAITING',
   ENABLE_STATUS        varchar(16) default 'ENABLE',
   RUN_TIMES            int default 0,
   RUN_DURATION         bigint,
   SYNC_BEGIN_TIME      timestamp,
   SYNC_END_TIME        timestamp,
   JOB_MEMO             varchar(512),
   JOB_CLASS            varchar(512),
   JOB_METHOD           varchar(64),
   JOB_OBJECT           varchar(128),
   primary key (ID)
) ;

create sequence SEQ_JOB_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_JOB_LOG                                            */
/*==============================================================*/
create table TS_JOB_LOG
(
   ID                   bigint not null,
   JOB_ID               bigint,
   EXE_TIME             timestamp,
   EXE_RESULT           varchar(16),
   ERROR_MSG            clob,
   primary key (ID)
) ;

create sequence SEQ_JOB_LOG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_JOB_INSTANCE                                       */
/*==============================================================*/
create table TS_JOB_INSTANCE
(
   ID                   BIGINT not null,
   JOB_ID               BIGINT,
   APDAPTER_PARAM       varchar(512),
   NOTES                varchar(30),
   RETRY_TIMES          BIGINT,
   RETRY_INTERVAL       BIGINT,
   STATUS_CODE          varchar(30),
   STATUS_DESC          varchar(512),
   PARAM_SATART_TIME    timestamp,
   PARAM_END_TIME       timestamp,
   EXE_START_TIME       timestamp,
   EXE_END_TIME         timestamp,
   OPT_COUNTER          BIGINT not null default 0,
   CREATE_USER          BIGINT,
   CREATE_TIME          timestamp,
   UPDATE_USER          BIGINT,
   UPDATE_TIME          timestamp,
   primary key (ID)
) ;

/*==============================================================*/
/* Table: TS_ORGNIZATION                                        */
/*==============================================================*/
create table TS_ORGNIZATION
(
   ID                   BIGINT not null,
   DIMENSION            varchar(100),
   ORGNIZATION_NAME     varchar(50),
   ORGNIZATION_CODE     varchar(50),
   PARENT_ID            BIGINT,
   ORGNIZATION_TYPE     varchar(100),
   ORGNIZATION_DESCRIPTION varchar(150),
   LEADER_ID            BIGINT,
   CREATE_USER          BIGINT,
   UPDATE_USER          BIGINT,
   CREATE_TIME          timestamp,
   UPDATE_TIME          timestamp,
   OPT_COUNTER          BIGINT not null default 0,
   primary key (ID)
) ;

create sequence SEQ_ORGNIZATION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_PERMISSION                                         */
/*==============================================================*/
create table TS_PERMISSION
(
   ID                   BIGINT not null,
   MENU_ID              BIGINT,
   PERMISSION_CODE      varchar(32),
   PERMISSION_NAME      varchar(128),
   IS_MENU_PERMISSION   char(1) default '0',
   OPT_COUNTER          BIGINT not null default 0,
   primary key (ID)
) ;

create sequence SEQ_PERMISSION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_POSITION                                           */
/*==============================================================*/
create table TS_POSITION
(
   ID                   BIGINT not null,
   POSITION_CODE        varchar(50),
   POSITION_NAME        varchar(50),
   POSITION_DESCRIPTION varchar(150),
   ORGNIZATION_ID       BIGINT,
   PARENT_ID            BIGINT,
   CREATE_USER          BIGINT,
   UPDATE_USER          BIGINT,
   CREATE_TIME          timestamp,
   UPDATE_TIME          timestamp,
   OPT_COUNTER          BIGINT not null default 0,
   primary key (ID)
) ;

create sequence SEQ_POSITION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_ROLE                                               */
/*==============================================================*/
create table TS_ROLE
(
   ID                   BIGINT not null,
   ROLE_CODE            varchar(32),
   SYSTEM_DATA			varchar(32),
   ROLE_NAME            varchar(128),
   ROLE_DESC            varchar(512),
   OPT_COUNTER          BIGINT not null default 0,
   MARK_FOR_DELETE      char(1) not null default '0',
   CREATE_USER          BIGINT,
   CREATE_TIME          timestamp,
   UPDATE_USER          BIGINT,
   UPDATE_TIME          timestamp,
   primary key (ID)
) ;

create sequence SEQ_ROLE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_TYPE                                               */
/*==============================================================*/
create table TS_DICT_TYPE
(
   ID                   bigint not null,
   TYPE_CODE            varchar(128),
   TYPE_NAME            nvarchar(128),
   TYPE_NAME_EN         varchar(256),
   SYSTEM_DATA          varchar(32),
   MARK_FOR_DELETE      char(1) not null default '0',
   primary key (ID)
) ;

create sequence SEQ_DICT_TYPE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_USER                                               */
/*==============================================================*/
create table TS_USER
(
   ID                   BIGINT not null,
   ACCOUNT              varchar(64),
   PASSWORD             varchar(128),
   STATUS               varchar(32),
   USER_NAME            varchar(128),
   PHONE                varchar(64),
   EMAIL                varchar(128),
   ADDRESS              varchar(128),
   GENDER               char(1),
   LAST_LOGIN_DATE      timestamp,	
   CREATE_USER          BIGINT,
   CREATE_TIME          timestamp,
   UPDATE_USER          BIGINT,
   UPDATE_TIME          timestamp,
   MARK_FOR_DELETE      char(1) not null default '0',
   OPT_COUNTER          BIGINT not null default 0,
   primary key (ID)
) ;

create sequence SEQ_USER_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
-- ----------------------------
-- Table structure for TS_GLOBAL_CONFIGURATION
-- ----------------------------
create table TS_GLOBAL_CONFIGURATION (
   ID                   BIGINT primary key not null ,
   SYS_KEY       		varchar(64)                  null,
   NAME               	varchar(128)                 null,
   SYS_VALUE			varchar(128)                  null,
   PROPERTY				varchar(64)                  null,
   REMARK             	varchar(640)                  null,
   SEQ					varchar(20)                  null,
   OPT_COUNTER             BIGINT default 0 not null , 
   CREATE_USER             BIGINT,
   CREATE_TIME             timestamp,
   UPDATE_USER             BIGINT,
   UPDATE_TIME             timestamp,
   MARK_FOR_DELETE    char(1) default '0' not null
);

create sequence SEQ_GLOBAL_CONFIGURATION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
-- ----------------------------
-- Table structure for `ts_serial_num`
-- ----------------------------
CREATE table TS_SERIAL_NUM (
  ID                   BIGINT primary key NOT NULL,
  SN_KEY               varchar(64) NOT NULL DEFAULT '',
  SN                   BIGINT NOT NULL DEFAULT '0'
) ;

create sequence SEQ_SERIAL_NUM_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_NOTIFICATION                                       */
/*==============================================================*/
CREATE TABLE TS_NOTIFICATION (
  ID                    BIGINT NOT NULL,
  SENDER_ID             BIGINT,
  RECEIVER_ID           BIGINT,
  READ_FLAG             tinyint DEFAULT 0,
  MSG_TYPE              varchar(20),
  MSG_ID                BIGINT,
  PRIMARY KEY (ID)
);
create sequence SEQ_NOTIFICATION_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_MSG_CONTENT                                        */
/*==============================================================*/
CREATE TABLE TS_MSG_CONTENT (
  ID                    BIGINT NOT NULL,
  TITLE                 varchar(200),
  CONTENT               clob,
  MSG_TYPE              varchar(10),
  OPT_COUNTER           BIGINT default 0 not null , 
  CREATE_USER           BIGINT,
  CREATE_TIME           timestamp,
  UPDATE_USER           BIGINT,
  UPDATE_TIME           timestamp,
  PRIMARY KEY (ID)
);
create sequence SEQ_MSG_CONTENT_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TS_ATTACHMENT                                 */
/*==============================================================*/
create table TS_ATTACHMENT
(
   ID                   		BIGINT not null,
   NAME      					varchar(256),
   URL							varchar(256),
   CREATE_USER    				BIGINT,
   CREATE_TIME					timestamp,
   UPDATE_USER					BIGINT,
   UPDATE_TIME					timestamp,
   OPT_COUNTER					BIGINT not null default 0,
   primary key (ID)
);
create sequence SEQ_ATTAHMENT_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;
/*==============================================================*/
/* Table: TR_ROLE_USER                                          */
/*==============================================================*/
create table TR_MSG_ATTACHMENT
(
   MSG_ID              BIGINT not null,
   ATTACHMENT_ID       BIGINT not null
) ;
/*==============================================================*/
/* Table: TS_PICTURE                                            */
/*==============================================================*/
create table TS_PICTURE (
   ID                   bigint               not null,
   NAME                 nvarchar(128)        null,
   ORG_REL_URL          varchar(512)         null,
   THUMB_REL_URL        varchar(512)         null,
   CREATE_USER          bigint               null,
   CREATE_TIME          timestamp             null,
   UPDATE_USER          bigint               null,
   UPDATE_TIME          timestamp             null,
   OPT_COUNTER          bigint               not null default 0,
   primary key (ID)
);
create sequence SEQ_PICTURE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1;