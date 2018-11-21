/*==============================================================*/
/* Table: TS_FORM_PERMISSION_NEW                                */
/*==============================================================*/
create table TS_FORM_PERMISSION_NEW (
   ID                   NUMBER(10) PRIMARY KEY not null,
   MENU_ID              NUMBER(10),
   PERMISSION_CODE      varchar2(128),
   PERMISSION_NAME      nvarchar2(128),
   MODULE               varchar2(128)
);

create sequence SEQ_FORM_PERMISSION_NEW_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

/*==============================================================*/
/* Table: TR_ROLE_FROM_PERMISSION_NEW                           */
/*==============================================================*/
create table TR_ROLE_FROM_PERMISSION_NEW (
   ROLE_ID              NUMBER(10),
   PERMISSION_ID        NUMBER(10)
);
/*==============================================================*/
/* Table: TS_DATA_PERMISSION                                    */
/*==============================================================*/
create table TS_DATA_PERMISSION_NEW (
   ID                   NUMBER(10) PRIMARY KEY not null,
   MENU_ID              NUMBER(10),
   DAO_INTERFACE        varchar2(256)
);

create sequence SEQ_DATA_PERMISSION_NEW_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;
/*==============================================================*/
/* Table: TR_ROLE_DATA_PERMISSION_NEW                           */
/*==============================================================*/
create table TR_ROLE_DATA_PERMISSION_NEW (
   ID                   NUMBER(10) PRIMARY KEY not null,
   ROLE_ID              NUMBER(10),
   PERMISSION_ID        NUMBER(10),
   PERMISSION_TYPE      varchar2(128)         null,
   SCRIPT               varchar2(512)         null
);

create sequence SEQ_ROLE_DATA_PERM_NEW_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;