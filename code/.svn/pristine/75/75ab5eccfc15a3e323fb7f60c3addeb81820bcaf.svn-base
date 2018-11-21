--菜单数据
--钣金原材料
insert into TS_MENU (ID, PARENT_ID, MENU_NAME, ICON_CLS, MARK_FOR_DELETE, OPT_COUNTER, PAGE_URL)
values (8021, 80, 'MENU_METALPLATE_MENAGEMENT', 'th-list', '0', 0, '/metalPlate/listInput.do');

insert into TS_LANGUAGE (ID, CODE, ZH_CN, EN_US, OPT_COUNTER)
values (SEQ_LANGUAGE_ID.NEXTVAL, 'MENU_METALPLATE_MENAGEMENT', '钣金加工信息', 'Metalplate Management', 0);

--钣金设备保养
insert into TS_MENU (ID, PARENT_ID, MENU_NAME, ICON_CLS, MARK_FOR_DELETE, OPT_COUNTER, PAGE_URL)
values (8022, 80, 'MENU_MAINTENANCE_MENAGEMENT', 'th-list', '0', 0, '/maintenance/listInput.do');

insert into TS_LANGUAGE (ID, CODE, ZH_CN, EN_US, OPT_COUNTER)
values (SEQ_LANGUAGE_ID.NEXTVAL, 'MENU_MAINTENANCE_MENAGEMENT', '钣金设备保养', 'Maintenance Management', 0);


--钣金线生产排产
insert into TS_MENU (ID, PARENT_ID, MENU_NAME, ICON_CLS, MARK_FOR_DELETE, OPT_COUNTER, PAGE_URL)
values (8404, 84, 'MENU_PRODUCTION_SCHEDULE', 'th-list', '0', 0, '/mpSourceData/listInput.do');

insert into TS_LANGUAGE (ID, CODE, ZH_CN, EN_US, OPT_COUNTER)
values (SEQ_LANGUAGE_ID.NEXTVAL, 'MENU_PRODUCTION_SCHEDULE', '钣金线生产排产', 'Metalplate Production Schedule', 0);

--钣金线生产记录
insert into TS_MENU (ID, PARENT_ID, MENU_NAME, ICON_CLS, MARK_FOR_DELETE, OPT_COUNTER, PAGE_URL)
values (8405, 84, 'MENU_PRODUCTION_RECORD', 'th-list', '0', 0, '/mpProductionRecord/listInput.do');

insert into TS_LANGUAGE (ID, CODE, ZH_CN, EN_US, OPT_COUNTER)
values (SEQ_LANGUAGE_ID.NEXTVAL, 'MENU_PRODUCTION_RECORD', '钣金线生产记录', 'Metalplate Production Records', 0);

--系统配置钣金冲压线产线编码
INSERT INTO TS_GLOBAL_CONFIGURATION(ID,SYS_KEY,NAME,SYS_VALUE,PROPERTY,REMARK) VALUES(SEQ_GLOBAL_CONFIGURATION_ID.NEXTVAL,'MP_LINE_NO','钣金冲压线产线编码' ,'282','*','钣金冲压线产线编码，需要和系统中维护的产线对应');
--系统配置钣金冲压线左侧加工図面
INSERT INTO TS_GLOBAL_CONFIGURATION(ID,SYS_KEY,NAME,SYS_VALUE,PROPERTY,REMARK) VALUES(SEQ_GLOBAL_CONFIGURATION_ID.NEXTVAL,'MP_LEFT_MACHINING_SURFACE','左侧加工図面' ,'3PR07033-1/3PR07033-2','*','钣金冲压线左侧加工図面,多个/隔开');
--系统配置钣金冲压线右侧加工図面
INSERT INTO TS_GLOBAL_CONFIGURATION(ID,SYS_KEY,NAME,SYS_VALUE,PROPERTY,REMARK) VALUES(SEQ_GLOBAL_CONFIGURATION_ID.NEXTVAL,'MP_RIGHT_MACHINING_SURFACE','右侧加工図面' ,'	3PR07036-1/3PR07036-2','*','钣金冲压线右侧加工図面,多个/隔开');
--每天早上7点30分从APC21获取源列表信息
INSERT INTO ts_job(ID,JOB_NAME, JOB_TYPE, CRON_EXPRESSION, JOB_CLASS, JOB_METHOD, JOB_OBJECT) VALUES (SEQ_JOB_ID.NEXTVAL,'APC21自动获取源数据','APC21_ACQUIRE', '0 30 7 ? * *', 'com.wis.mes.job.APC21SourceDataAcquireJob', 'execue', 'apc21SourceDataAcquireJob');

--钣金设备实时状态
insert into TS_MENU (ID, PARENT_ID, MENU_NAME, ICON_CLS, MARK_FOR_DELETE, OPT_COUNTER, PAGE_URL)
values (8505, 85, 'MENU_MP_EQUIPMENT_RT', 'money', '0', 0, '/mpEquipment/rtListInput.do');

insert into TS_LANGUAGE (ID, CODE, ZH_CN, EN_US, OPT_COUNTER)
values (SEQ_LANGUAGE_ID.NEXTVAL, 'MENU_MP_EQUIPMENT_RT', '钣金设备实时状态', 'Sheet metal equipment real-time status', 0);

--钣金设备历史信息
insert into TS_MENU (ID, PARENT_ID, MENU_NAME, ICON_CLS, MARK_FOR_DELETE, OPT_COUNTER, PAGE_URL)
values (8506, 85, 'MENU_MP_EQUIPMENT_HS', 'money', '0', 0, '/mpEquipment/hsListInput.do');

insert into TS_LANGUAGE (ID, CODE, ZH_CN, EN_US, OPT_COUNTER)
values (SEQ_LANGUAGE_ID.NEXTVAL, 'MENU_MP_EQUIPMENT_HS', '钣金设备历史信息', 'Sheet metal equipment history information', 0);

--PMC看板排产数据，一分钟执行一次
INSERT INTO ts_job(ID,JOB_NAME, JOB_TYPE, CRON_EXPRESSION, JOB_CLASS, JOB_METHOD, JOB_OBJECT) VALUES (SEQ_JOB_ID.NEXTVAL,'PMC看板排产数据更新','PMC_BOARD', '0 0/1 * * * ?', 'com.wis.mes.job.PMCBoardProductionInfoJob', 'execue', 'pmcBoardProductionInfoJob');
--查看区域是否空闲，空闲状态下给派发待产任务，一分钟执行一次
INSERT INTO ts_job(ID,JOB_NAME, JOB_TYPE, CRON_EXPRESSION, JOB_CLASS, JOB_METHOD, JOB_OBJECT) VALUES (SEQ_JOB_ID.NEXTVAL,'钣金线区域空闲监控','MP_REGION_MARK_MONITOR', '0 0/1 * * * ?', 'com.wis.mes.job.MPRegionMarkMonitorJob', 'execue', 'mpregionMarkMonitorJob');
--检查设备是否该保养，每天早上7点30分
INSERT INTO ts_job(ID,JOB_NAME, JOB_TYPE, CRON_EXPRESSION, JOB_CLASS, JOB_METHOD, JOB_OBJECT) VALUES (SEQ_JOB_ID.NEXTVAL,'钣金保养预警通知','MAINTENANCE_WARNING_NOTICE', '0 30 7 ? * *', 'com.wis.mes.job.MaintenanceWarningNoticeJob', 'execue', 'maintenanceWarningNoticeJob');

commit;