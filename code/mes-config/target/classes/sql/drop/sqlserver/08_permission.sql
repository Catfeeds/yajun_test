if object_id('dbo.TS_FORM_PERMISSION_NEW') is not null
begin
  drop table dbo.TS_FORM_PERMISSION_NEW
end;
if object_id('dbo.TR_ROLE_FROM_PERMISSION_NEW') is not null
begin
  drop table dbo.TR_ROLE_FROM_PERMISSION_NEW
end;
if object_id('dbo.TS_DATA_PERMISSION_NEW') is not null
begin
  drop table dbo.TS_DATA_PERMISSION_NEW
end;
if object_id('dbo.TR_ROLE_DATA_PERMISSION_NEW') is not null
begin
  drop table dbo.TR_ROLE_DATA_PERMISSION_NEW
end;