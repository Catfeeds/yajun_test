 INSERT INTO ts_language VALUES(SEQ_LANGUAGE_ID.NEXTVAL,'UNIT_EXCEPTION_MENU','钣金-设备异常分析年报','unit exception',0);
 INSERT INTO ts_menu VALUES('8725',87,'UNIT_EXCEPTION_MENU','bar-chart',0,0,'/metalPlateReport/unitExceptionReport.do');
 INSERT INTO ts_permission VALUES('872501','8725','UnitExcePtionView','设备异常分析年报查看',1,0);
 
 
 
CREATE TABLE tm_bad_parts(
  id NUMBER(10) PRIMARY KEY not null,
  NO varchar2(30),
  NAME varchar2(100),
  REMARKS  varchar2(150),
  TM_LINE_ID NUMBER(10),
  CREATE_USER NUMBER(10),
  CREATE_TIME Date ,
  UPDATE_USER NUMBER(10),
  UPDATE_TIME Date,
  OPT_COUNTER NUMBER(5) 
);

create sequence SEQ_BAD_PARTS_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

INSERT INTO ts_language VALUES(seq_language_id.nextval,'MENU_BAD_PARTS','部品不良','Bad parts ',0);
INSERT INTO ts_menu VALUES('9719',80,'MENU_BAD_PARTS','money',0,0,'/badParts/listInput.do');
INSERT INTO ts_permission VALUES('971901','9719','BadPartsView','部品不良查看',1,0);
INSERT INTO ts_permission VALUES('971902','9719','BadPartsAdd','部品不良新增',0,0);
INSERT INTO ts_permission VALUES('971903','9719','BadPartsUpdate','部品不良编辑',0,0);
INSERT INTO ts_permission VALUES('971904','9719','BadPartsDelete','部品不良删除',0,0);


INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9719','BAD_PARTS_NO','编号','BAD_PARTS');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9719','BAD_PARTS_NAME','描述','BAD_PARTS');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9719','BAD_PARTS_REMARKS','备注','BAD_PARTS');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9719','BAD_PARTS_ILNE','产线','BAD_PARTS');

alter table TB_MP_DEFECTIVE_RECORD add TM_BAD_PARTS_IDS varchar2(30);

alter table TM_SHEET_METAL_MATERIAL add APC_PLANNED_DEMAND number(10);



#机台管理
CREATE TABLE tm_board_manage(
  id number(10) PRIMARY KEY not null,
  TM_EQUIPMENT_IDS varchar2(30),
  TM_EQUIPMENT_NAMES varchar2(100),
  STEP_NUMBER number(10),
  REGION_MARK varchar2(10),
  PLC_NO varchar2(30),
  REMARKS varchar2(150)
);

create sequence SEQ_BOARD_MANAGE_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

INSERT INTO ts_language VALUES(seq_language_id.nextval,'MENU_BOARD_MANAGE','区域管理','board manage',0);
INSERT INTO ts_menu VALUES('9720',80,'MENU_BOARD_MANAGE','money',0,0,'/boardManage/listInput.do');
INSERT INTO ts_permission VALUES('972001','9720','BoardManageView','区域管理查看',1,0);
INSERT INTO ts_permission VALUES('972002','9720','BoardManageAdd','区域管理新增',0,0);
INSERT INTO ts_permission VALUES('972003','9720','BoardManageUpdate','区域管理编辑',0,0);
INSERT INTO ts_permission VALUES('972004','9720','BoardManageDelete','区域管理删除',0,0);


INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9720','BOARD_MANAGE_NAME','机台','BOARD_MANAGE');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9720','BOARD_MANAGE_STEP_NUMBER','工程数','BOARD_MANAGE');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9720','BOARD_MANAGE_REGION_MARK','区域标识','BOARD_MANAGE');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9720','BOARD_MANAGE_PLC_NO','PLC编码','BOARD_MANAGE');
INSERT INTO ts_form_permission_new VALUES(seq_form_permission_new_id.nextval,'9720','BOARD_MANAGE_REMARKS','备注','BOARD_MANAGE');

----生产报表查看
INSERT INTO ts_language VALUES(seq_language_id.nextval,'PRODUCT_MRG_REPORT_TABLE_MENU','生成管理报表','PRODUCT MRG ',0);
INSERT INTO ts_menu VALUES('8726',87,'PRODUCT_MRG_REPORT_TABLE_MENU','bar-chart',0,0,'/productMrgReport/productMrgReport.do');
INSERT INTO ts_permission VALUES('872601','8726','ProductMrgView','生产报表查看',1,0);

--钣金故障明细报表
INSERT INTO ts_language VALUES(seq_language_id.nextval,'FAULETD_DETAILS','钣金故障明细报表','FAULETD_DETAILS_REPORT ',0);
INSERT INTO ts_menu VALUES('8727',87,'FAULETD_DETAILS','bar-chart',0,0,'/metalplateFauletdReport/fauletdReport.do');
INSERT INTO ts_permission VALUES('872602','8727','fauletdReportView','钣金故障明细报表查看',1,0);
--钣金年报表
INSERT INTO ts_language VALUES(seq_language_id.nextval,'YEAR_REPORT','钣金年报表','YEAR_REPORTS ',0);
INSERT INTO ts_menu VALUES('8728',87,'YEAR_REPORT','bar-chart',0,0,'/metalplateYearReport/yearReport.do');
INSERT INTO ts_permission VALUES('872603','8728','yearReportView','钣金年报表查看',1,0);

---排产记录管理
INSERT INTO ts_language VALUES(seq_language_id.nextval,'SCHEDUL_LOG','排产记录管理','SchedulLog',0);
INSERT INTO ts_menu VALUES('8408',84,'SCHEDUL_LOG','th-list',0,0,'/schedulingRecorLog/listInput.do');
INSERT INTO ts_permission VALUES('840801','8408','schedulLogView','排产记录管理查看',1,0);

insert into TS_FORM_PERMISSION_NEW (ID, MENU_ID, PERMISSION_CODE, PERMISSION_NAME, MODULE) values (SEQ_FORM_PERMISSION_NEW_ID.NEXTVAL, 8408, 'LOG_TASK_TIME', '计划需求日期', 'SCHEDUL_LOG');
insert into TS_FORM_PERMISSION_NEW (ID, MENU_ID, PERMISSION_CODE, PERMISSION_NAME, MODULE) values (SEQ_FORM_PERMISSION_NEW_ID.NEXTVAL, 8408, 'LOG_MODEL', '钣金加工用图号', 'SCHEDUL_LOG');
insert into TS_FORM_PERMISSION_NEW (ID, MENU_ID, PERMISSION_CODE, PERMISSION_NAME, MODULE) values (SEQ_FORM_PERMISSION_NEW_ID.NEXTVAL, 8408, 'LOG_STATUS', '状态', 'SCHEDUL_LOG');
insert into TS_FORM_PERMISSION_NEW (ID, MENU_ID, PERMISSION_CODE, PERMISSION_NAME, MODULE) values (SEQ_FORM_PERMISSION_NEW_ID.NEXTVAL, 8408, 'LOG_CREATE_TIME', '创建日期', 'SCHEDUL_LOG');
insert into TS_FORM_PERMISSION_NEW (ID, MENU_ID, PERMISSION_CODE, PERMISSION_NAME, MODULE) values (SEQ_FORM_PERMISSION_NEW_ID.NEXTVAL, 8408, 'LOG_CREATE_USER', '创建人', 'SCHEDUL_LOG');


INSERT INTO ts_dict_type  VALUES(SEQ_DICT_TYPE_ID.nextval,'BJ_BAD_STATUS','钣金生产记录状态','bj bad stauts',NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'BJ_BAD_STATUS'),'0','生产完成','bj bad status 0',1,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'BJ_BAD_STATUS'),'1','不良待确认','bj bad status 1',2,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'BJ_BAD_STATUS'),'2','已追加排产','bj bad status 2',3,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'BJ_BAD_STATUS'),'3','不良确认','bj bad status 3',4,NULL,0);

--钣金原材料新增机台名称
alter table tm_sheet_metal_material add MACHINE_TAG_NAMES varchar2(100);


----排产记录管理
CREATE TABLE TB_SCHEDULING_RECOR_LOG(
  id number(10) PRIMARY KEY not null,
  MACHINING_SURFACE varchar2(30),
  TASK_TIME varchar2(30),
  STATUS varchar2(30),
  DISPATCH_NUMBER number(10),
  FINISH_NUMBER number(10),
  SYN_POSITION varchar2(30),
  CREATE_USER number(10),
  CREATE_TIME date
);
COMMENT ON TABLE TB_SCHEDULING_RECOR_LOG IS '排产记录日志表';
COMMENT ON COLUMN TB_SCHEDULING_RECOR_LOG.MACHINING_SURFACE IS '钣金图号';
COMMENT ON COLUMN TB_SCHEDULING_RECOR_LOG.TASK_TIME IS '计划需求时间';
COMMENT ON COLUMN TB_SCHEDULING_RECOR_LOG.STATUS IS '状态';
COMMENT ON COLUMN TB_SCHEDULING_RECOR_LOG.DISPATCH_NUMBER IS '派工数';
COMMENT ON COLUMN TB_SCHEDULING_RECOR_LOG.FINISH_NUMBER IS '成品数';
COMMENT ON COLUMN TB_SCHEDULING_RECOR_LOG.SYN_POSITION IS '生产顺位';


create sequence SEQ_SCHEDULING_RECOR_LOG_ID
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
cache 20;

INSERT INTO ts_permission VALUES('840409','8404','ProductionScheduleDeblocking','解锁',0,0);
INSERT INTO ts_permission VALUES('840410','8404','ProductionScheduleLocking','锁定',0,0);
INSERT INTO ts_permission VALUES('840411','8404','ProductionScheduleMandatoryEnd','强制结束',0,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'SM_PRODUCTION_STATE'),'5','锁定','locking',6,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'SM_PRODUCTION_STATE'),'6','取消派工','locking',6,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'SM_PRODUCTION_STATE'),'7','解锁','unlocking',6,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'SM_PRODUCTION_STATE'),'8','强制结束','end',6,NULL,0);



create or replace trigger TB_MP_SCHEDUL_TRIGGER
  before insert or update
   on TB_MP_SCHEDUL
  for each row
declare
    --钣金线生产记录
    userId number(10);--操作用户
    model varchar2(30);---钣金图号
    schedulDate varchar2(30);---计划时间
    status varchar2(30);---状态
    isInsert number(1);--是否新增日志
    dispatchNumber number(10);--派工数
    finishNumber number(10);--成品数
    synPosition varchar(30);--生产顺位
    --pragma autonomous_transaction;
begin
    model := :new.model;
    schedulDate := :new.schedul_date;
    status := to_char(:new.status);
    dispatchNumber := :new.dispatch_number;
    finishNumber := :new.finish_number;
    synPosition := :new.syn_position;
    isInsert := 0; 
    if inserting then 
       --insert 时处理
       userId := :new.create_user;
    elsif updating then
       --update时处理
        if :new.status = :old.status then 
          isInsert := 1;
        end if;
        userId := :new.update_user;
    elsif deleting then
          model := :old.model;
          schedulDate := :old.schedul_date;
          status := 8;
          dispatchNumber := :old.dispatch_number;
          finishNumber := :old.finish_number;
          synPosition := :old.syn_position;
          userId := :old.update_user;
    end if;
    ---elsif deleting then
     --delete时处理
       --status := '6';
       --model := :old.model;
       --schedulDate := :old.schedul_date;
       --dispatchNumber := :old.dispatch_number;
       --finishNumber := :old.finish_number;
       --synPosition := :old.syn_position;
    --end if;
    if isInsert = 0 then
     insert into TB_SCHEDULING_RECOR_LOG(id,MACHINING_SURFACE,TASK_TIME,CREATE_TIME,CREATE_USER,STATUS,DISPATCH_NUMBER,FINISH_NUMBER,SYN_POSITION) 
     values(SEQ_SCHEDULING_RECOR_LOG_ID.Nextval,model,schedulDate,sysdate,userId,status,dispatchNumber,finishNumber,synPosition);
    end if;
end TB_MP_SCHEDUL_TRIGGER;





