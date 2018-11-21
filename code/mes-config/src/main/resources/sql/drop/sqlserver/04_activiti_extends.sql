if object_id('dbo.WACT_RE_PROC_DEF') is not null
begin
  drop table dbo.WACT_RE_PROC_DEF
end;
if object_id('dbo.WACT_RE_MODEL') is not null
begin
  drop table dbo.WACT_RE_MODEL
end;
if object_id('dbo.WACT_RU_EXECUTION') is not null
begin
  drop table dbo.WACT_RU_EXECUTION
end;
if object_id('dbo.WACT_DELEGATE_CONFIG') is not null
begin
  drop table dbo.WACT_DELEGATE_CONFIG
end;
if object_id('dbo.WACT_RU_COUNTER_SIGN') is not null
begin
  drop table dbo.WACT_RU_COUNTER_SIGN
end;
if object_id('dbo.WACT_RU_NODE') is not null
begin
  drop table dbo.WACT_RU_NODE
end;
if object_id('dbo.WACT_RU_NODE_PROPERTY') is not null
begin
  drop table dbo.WACT_RU_NODE_PROPERTY
end;
if object_id('dbo.WACT_NODE_DELAY_ALERT_CONF') is not null
begin
  drop table dbo.WACT_NODE_DELAY_ALERT_CONF
end;