/*==============================================================*/
/* Table: TS_FORM_PERMISSION_NEW                                */
/*==============================================================*/
create table TS_FORM_PERMISSION_NEW (
   ID                   bigint               not null  IDENTITY(0,1),
   MENU_ID              bigint               null,
   PERMISSION_CODE      varchar(128)         null,
   PERMISSION_NAME      nvarchar(128)        null,
   MODULE               varchar(128)         null,
   primary key (ID)
);
/*==============================================================*/
/* Table: TR_ROLE_FROM_PERMISSION_NEW                           */
/*==============================================================*/
create table TR_ROLE_FROM_PERMISSION_NEW (
   ROLE_ID              bigint               null,
   PERMISSION_ID        bigint               null
);
/*==============================================================*/
/* Table: TS_DATA_PERMISSION                                    */
/*==============================================================*/
create table TS_DATA_PERMISSION_NEW (
   ID                   bigint               not null  IDENTITY(0,1),
   MENU_ID              bigint               null,
   DAO_INTERFACE            varchar(256)         null,
   primary key (ID)
);
/*==============================================================*/
/* Table: TR_ROLE_DATA_PERMISSION_NEW                           */
/*==============================================================*/
create table TR_ROLE_DATA_PERMISSION_NEW (
   ID                   bigint               not null  IDENTITY(0,1),
   ROLE_ID              bigint               null,
   PERMISSION_ID        bigint               null,
   PERMISSION_TYPE      varchar(128)         null,
   SCRIPT               varchar(512)         null,
   primary key (ID)
);