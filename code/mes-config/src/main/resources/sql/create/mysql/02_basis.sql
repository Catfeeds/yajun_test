/*==============================================================*/
/* Table: TS_LANGUAGE                                           */
/*==============================================================*/
create table TS_LANGUAGE
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   CODE                 varchar(255),
   ZH_CN                varchar(255),
   EN_US                varchar(255),
   OPT_COUNTER          BIGINT(5) not null default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_MENU                                           */
/*==============================================================*/
create table TS_MENU
(
   ID                   BIGINT(10) not null,
   PARENT_ID            BIGINT(10),
   MENU_NAME            varchar(128),
   ICON_CLS             varchar(128),
   MARK_FOR_DELETE      char(1) not null default '0',
   OPT_COUNTER          BIGINT(5) not null default 0,
   PAGE_URL             varchar(512),
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_AUDIT_CONFIG                                          */
/*==============================================================*/
create table TS_AUDIT_CONFIG
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   AUDIT_TYPE_ID        varchar(64),
   AUDIT_TYPE_NAME      varchar(60),
   OPERATE_TYPE_ID      varchar(64) not null,
   OPERATE_TYPE_NAME    varchar(60),
   AUDIT_SYSTEM_ID      varchar(32) not null,
   AUDIT_SYSTEM_NAME    varchar(60),
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*==============================================================*/
/* Table: TS_AUDIT_LOG                                             */
/*==============================================================*/
create table TS_AUDIT_LOG
(
   ID         BIGINT(10) not null AUTO_INCREMENT,
   OPERATOR_ID          BIGINT(10),
   OPERATOR_NAME          varchar(32),
   AUDIT_CONFIG_ID		BIGINT(10) ,
   OPERATE_DATE         datetime,
   INDEX_ID             varchar(18),
   AFTER_OPERATE_OBJ    varchar(3999),
   BEFORE_OPERATE_OBJ   varchar(3999),
    primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


/*==============================================================*/
/* Table: TR_DATA_PER_PER_SET                                   */
/*==============================================================*/
create table TR_DATA_PER_PER_SET
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   PERMISSION_ID        BIGINT(10),
   PERMISSION_SET_ID    BIGINT(10),
   DATA_VALUE           varchar(512),
   DATA_TYPE            varchar(128),
   OPT_COUNTER          BIGINT(5) default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_FORM_PER_PER_SET                                   */
/*==============================================================*/
create table TR_FORM_PER_PER_SET
(
   PERMISSION_ID        BIGINT(10),
   PERMISSION_SET_ID    BIGINT(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Creating TR_PERMISSION_URL...
-- Not sure it's really used
create table TR_PERMISSION_URL
(
  ACTION_URL_ID BIGINT(10) not null,
  PERMISSION_ID BIGINT(10) not null
)
;

-- Creating TR_ROLE_PERMISSION...
create table TR_ROLE_PERMISSION
(
   ROLE_ID              BIGINT(10) not null,
   PERMISSION_ID        BIGINT(10) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_ROLE_USER                                          */
/*==============================================================*/
create table TR_ROLE_USER
(
   USER_ID              BIGINT(10) not null,
   ROLE_ID              BIGINT(10) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_USER_DATA_PERMISSION                               */
/*==============================================================*/
create table TR_USER_DATA_PERMISSION
(
   USER_ID              BIGINT(10),
   PERMISSION_SET_ID    BIGINT(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_USER_FORM_PERMISSION                               */
/*==============================================================*/
create table TR_USER_FORM_PERMISSION
(
   USER_ID              BIGINT(10),
   PERMISSION_SET_ID    BIGINT(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_USER_POSITION                                      */
/*==============================================================*/
create table TR_USER_POSITION
(
   USER_ID              BIGINT(10) not null,
   POSITION_ID          BIGINT(10) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_USER_ORGNIZATION                                      */
/*==============================================================*/
create table TR_USER_ORGNIZATION
(
   USER_ID              BIGINT(10) not null,
   ORGNIZATION_ID       BIGINT(10) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_ACTION_URL                                         */
/*==============================================================*/
create table TS_ACTION_URL
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   PERMISSION_CODE      varchar(128),
   REQUEST_PATH         varchar(128),
   URL_DESC             varchar(512),
   OPT_COUNTER          BIGINT(5) not null default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_DATA_PERMISSION                                    */
/*==============================================================*/
create table TS_DATA_PERMISSION
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   PERMISSION_CODE      varchar(128),
   RELATION_CODE        varchar(128),
   DESCRIPTION          varchar(256),
   DATA_URL             varchar(128),
   MODULE               varchar(128),
   OPT_COUNTER          BIGINT(5) default 0,
   PACKAGE_NAME         varchar(256) ,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_DATA_PERMISSION_SET                                */
/*==============================================================*/
create table TS_DATA_PERMISSION_SET
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   DESCRIPTION          varchar(256),
   PERMISSION_SET_CODE  varchar(128),
   OPT_COUNTER          BIGINT(5) default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_EMAIL                                              */
/*==============================================================*/
create table TS_EMAIL
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   SUBJECT              varchar(256),
   MAIL_TO              varchar(2000),
   FILES                varchar(2000),
   CC                   varchar(2000),
   FLAG                 char(1),
   OPT_COUNTER          BIGINT(5) default 0,
   TEXT                 varchar(3999),
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_ENTRY                                              */
/*==============================================================*/
create table TS_DICT_ENTRY
(
   ID                   bigint not null auto_increment,
   TYPE_ID              bigint,
   ENTRY_CODE           varchar(128),
   ENTRY_NAME           nvarchar(128),
   ENTRY_NAME_EN        varchar(256),
   SORT_NO              bigint,
   SYSTEM_DATA           varchar(32),
   MARK_FOR_DELETE      char(1) not null default '0',
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_FORM_PERMISSION                                    */
/*==============================================================*/
create table TS_FORM_PERMISSION
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   PERMISSION_CODE      varchar(128),
   PERMISSION_NAME      varchar(128),
   DESCRIPTION          varchar(256),
   MODULE               varchar(128),
   OPT_COUNTER          BIGINT(5) default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_FORM_PERMISSION_SET                                */
/*==============================================================*/
create table TS_FORM_PERMISSION_SET
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   PERMISSION_SET_NAME  varchar(128),
   DESCRIPTION          varchar(256),
   PERMISSION_SET_CODE  varchar(128),
   OPT_COUNTER          BIGINT(5) default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_JOB                                                */
/*==============================================================*/
create table TS_JOB
(
   ID                   bigint not null auto_increment,
   JOB_NAME             nvarchar(128),
   JOB_TYPE             varchar(512),
   CRON_EXPRESSION      varchar(512),
   RUNTIME_LAST         datetime,
   RUNTIME_NEXT         datetime,
   JOB_STATUS           varchar(32) default 'AWAITING',
   ENABLE_STATUS        varchar(16) default 'ENABLE',
   RUN_TIMES            int default 0,
   RUN_DURATION         bigint,
   SYNC_BEGIN_TIME      datetime,
   SYNC_END_TIME        datetime,
   JOB_MEMO             varchar(512),
   JOB_CLASS            varchar(512),
   JOB_METHOD           varchar(64),
   JOB_OBJECT           varchar(128),
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*==============================================================*/
/* Table: TS_JOB_LOG                                            */
/*==============================================================*/
create table TS_JOB_LOG
(
   ID                   bigint not null auto_increment,
   JOB_ID               bigint,
   EXE_TIME             timestamp,
   EXE_RESULT           varchar(16),
   ERROR_MSG            text,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*==============================================================*/
/* Table: TS_ORGNIZATION                                        */
/*==============================================================*/
create table TS_ORGNIZATION
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   DIMENSION            varchar(100),
   ORGNIZATION_NAME     varchar(50),
   ORGNIZATION_CODE     varchar(50),
   PARENT_ID            BIGINT(10),
   ORGNIZATION_TYPE     varchar(100),
   ORGNIZATION_DESCRIPTION varchar(150),
   LEADER_ID            BIGINT(10),
   SYSTEM_CODE          varchar(512),
   CREATE_USER          BIGINT(10),
   UPDATE_USER          BIGINT(10),
   CREATE_TIME          datetime,
   UPDATE_TIME          datetime,
   OPT_COUNTER          BIGINT(5) not null default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*==============================================================*/
/* Table: TS_PERMISSION                                         */
/*==============================================================*/
create table TS_PERMISSION
(
   ID                   BIGINT(10) not null,
   MENU_ID          BIGINT(10),
   PERMISSION_CODE      varchar(32),
   PERMISSION_NAME      varchar(128),
   IS_MENU_PERMISSION   char(1) default '0',
   OPT_COUNTER          BIGINT(5) not null default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_POSITION                                           */
/*==============================================================*/
create table TS_POSITION
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   POSITION_CODE        varchar(50),
   POSITION_NAME        varchar(50),
   POSITION_DESCRIPTION varchar(150),
   ORGNIZATION_ID       BIGINT(10),
   PARENT_ID            BIGINT(10),
   CREATE_USER          BIGINT(10),
   UPDATE_USER          BIGINT(10),
   CREATE_TIME          datetime,
   UPDATE_TIME          datetime,
   OPT_COUNTER          BIGINT(5) not null default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_ROLE                                               */
/*==============================================================*/
create table TS_ROLE
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   ROLE_CODE            varchar(32),
   SYSTEM_DATA			varchar(32),
   ROLE_NAME            varchar(128),
   ROLE_DESC            varchar(512),
   OPT_COUNTER          BIGINT(5) not null default 0,
   MARK_FOR_DELETE      char(1) not null default '0',
   CREATE_USER          BIGINT(10),
   CREATE_TIME          datetime,
   UPDATE_USER          BIGINT(10),
   UPDATE_TIME          datetime,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_TYPE                                               */
/*==============================================================*/
create table TS_DICT_TYPE
(
   ID                   bigint not null auto_increment,
   TYPE_CODE            varchar(128),
   TYPE_NAME            nvarchar(128),
   TYPE_NAME_EN         varchar(256),
   SYSTEM_DATA          varchar(32),
   MARK_FOR_DELETE      char(1) not null default '0',
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_USER                                               */
/*==============================================================*/
create table TS_USER
(
   ID                   BIGINT(10) not null AUTO_INCREMENT,
   ACCOUNT              varchar(64),
   PASSWORD             varchar(128),
   HISTORY_PWD          varchar(1024),
   STATUS               varchar(32),
   USER_NAME            varchar(128),
   PHONE                varchar(64),
   EMAIL                varchar(128),
   ADDRESS              varchar(128),
   GENDER               char(1),
   PWD_UPDATE_TIME      datetime,
   LAST_LOGIN_DATE      datetime,	
   CREATE_USER          BIGINT(10),
   CREATE_TIME          datetime,
   UPDATE_USER          BIGINT(10),
   UPDATE_TIME          datetime,
   MARK_FOR_DELETE      char(1) not null default '0',
   OPT_COUNTER          BIGINT(5) not null default 0,
   primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for TS_GLOBAL_CONFIGURATION
-- ----------------------------
create table TS_GLOBAL_CONFIGURATION (
   ID                   BIGINT(10) primary key not null  AUTO_INCREMENT,
   SYS_KEY       		varchar(64)                  null,
   NAME               	varchar(128)                 null,
   SYS_VALUE			varchar(128)                  null,
   PROPERTY				varchar(64)                  null,
   REMARK             	varchar(640)                  null,
   SEQ					varchar(20)                  null,
   OPT_COUNTER             BIGINT(5) default 0 not null , 
   CREATE_USER             BIGINT(10),
   CREATE_TIME             datetime,
   UPDATE_USER             BIGINT(10),
   UPDATE_TIME             datetime,
   MARK_FOR_DELETE    char(1) default '0' not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for `ts_serial_num`
-- ----------------------------
CREATE table TS_SERIAL_NUM (
  ID                   BIGINT(10) primary key NOT NULL AUTO_INCREMENT,
  SN_KEY               varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  SN                   BIGINT(20) NOT NULL DEFAULT '0' COMMENT '序列号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_NOTIFICATION                                       */
/*==============================================================*/
CREATE TABLE TS_NOTIFICATION (
  ID                    bigint(10) NOT NULL AUTO_INCREMENT,
  SENDER_ID             bigint(10),
  RECEIVER_ID           bigint(10),
  READ_FLAG             tinyint(4) DEFAULT 0,
  MSG_TYPE              varchar(20),
  MSG_ID                bigint(10),
  PRIMARY KEY (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_MSG_CONTENT                                        */
/*==============================================================*/
CREATE TABLE TS_MSG_CONTENT (
  ID                    bigint(10) NOT NULL AUTO_INCREMENT,
  TITLE                 varchar(200),
  CONTENT               longtext,
  MSG_TYPE              varchar(10),
  OPT_COUNTER           BIGINT(5) default 0 not null , 
  CREATE_USER           BIGINT(10),
  CREATE_TIME           datetime,
  UPDATE_USER           BIGINT(10),
  UPDATE_TIME           datetime,
  PRIMARY KEY (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TS_ATTACHMENT                                 */
/*==============================================================*/
create table TS_ATTACHMENT
(
   ID                   		BIGINT(10) not null AUTO_INCREMENT,
   NAME      					varchar(256),
   URL							varchar(256),
   CREATE_USER    				BIGINT(10),
   CREATE_TIME					datetime,
   UPDATE_USER					BIGINT(10),
   UPDATE_TIME					datetime,
   OPT_COUNTER					BIGINT(5) not null default 0,
   primary key (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*==============================================================*/
/* Table: TR_ROLE_USER                                          */
/*==============================================================*/
create table TR_MSG_ATTACHMENT
(
   MSG_ID              BIGINT(10) not null,
   ATTACHMENT_ID       BIGINT(10) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*==============================================================*/
/* Table: TS_PICTURE                                            */
/*==============================================================*/
create table TS_PICTURE (
   ID                   bigint               not null AUTO_INCREMENT,
   NAME                 nvarchar(128)        null,
   ORG_REL_URL          varchar(512)         null,
   THUMB_REL_URL        varchar(512)         null,
   CREATE_USER          bigint               null,
   CREATE_TIME          datetime             null,
   UPDATE_USER          bigint               null,
   UPDATE_TIME          datetime             null,
   OPT_COUNTER          bigint               not null default 0,
   primary key (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;