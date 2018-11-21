 
----mes~~dakin
CREATE DATABASE LINK daikindblink
CONNECT TO MANU IDENTIFIED BY '密码'
USING '(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL = TCP)(HOST = 182.168.1.248)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = orcl)))'; 


CREATE  SYNONYM COOLRST_TBL  FOR  COOLRST_TBL@R5_MANU_DBLINK;
CREATE  SYNONYM DIS_ZH_TBL  FOR  DIS_ZH_TBL@R5_MANU_DBLINK;
CREATE  SYNONYM LOG  FOR  LOG@R5_MANU_DBLINK;
CREATE  SYNONYM OILRST_TBL  FOR  OILRST_TBL@R5_MANU_DBLINK;
CREATE  SYNONYM OIL_TBL  FOR  OIL_TBL@R5_MANU_DBLINK;
CREATE  SYNONYM PMSHISTORY  FOR  PMSHISTORY@R5_MANU_DBLINK;
CREATE  SYNONYM R5_RFID_LOG  FOR  R5_RFID_LOG@R5_MANU_DBLINK;
CREATE  SYNONYM RESISTANCETEST  FOR  RESISTANCETEST@R5_MANU_DBLINK;
CREATE  SYNONYM VEC_SCAN_LOG  FOR  VEC_SCAN_LOG@R5_MANU_DBLINK;
CREATE  SYNONYM VTS_RST  FOR  VTS_RST@R5_MANU_DBLINK;
CREATE  SYNONYM ZZJC_DATE FOR ZZJC_DATE@R5_MANU_DBLINK;
CREATE  SYNONYM GSVX_GM_MV FOR GSVX_GM_MV@R5_MANU_DBLINK;
CREATE  SYNONYM DK_PRODUCT_PARAMETERS FOR DK_PRODUCT_PARAMETERS@R5_MANU_DBLINK; 


insert into ts_global_configuration (ID, SYS_KEY, NAME, SYS_VALUE, PROPERTY, REMARK, SEQ, OPT_COUNTER, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, MARK_FOR_DELETE, TYPE)
values (seq_global_configuration_id.nextval, 'APC21_FILE_PATH', 'APC21计划需求导入文件地址', '\\\\192.168.90.4\\共享文件\\KUKURI_PROCA.csv', '*', '文件存放地址', null, 0, null, null, 1, null, '0', null);

CREATE TABLE  DK_PRODUCT_PARAMETERS(
  --id NUMBER(10) PRIMARY KEY not null,
  SEBANGO varchar2(10),
  KIBAN varchar2(20),
  EQUIPMENTTYPE varchar2(50),
  EQUIPMENTNO varchar2(50),
  PARAMS varchar2(50),
  PARAMSVALUE varchar2(50),
  CREATE_TIME date
) ;     

comment on column DK_PRODUCT_PARAMETERS.SEBANGO is '背番号';
comment on column DK_PRODUCT_PARAMETERS.KIBAN is '机号';
comment on column DK_PRODUCT_PARAMETERS.EQUIPMENTTYPE is '设备';
comment on column DK_PRODUCT_PARAMETERS.EQUIPMENTNO is '设备号';
comment on column DK_PRODUCT_PARAMETERS.PARAMS is '参数';
comment on column DK_PRODUCT_PARAMETERS.PARAMSVALUE is '参数值';

create or replace trigger TB_PRODUCT_TRACING_TRIGGER
  after  update
  on TB_PRODUCT_TRACING
  for each row
declare

    machineName varchar2(10);--机号
    backNumber varchar2(10);--背番号
    updateTime date;
    id number;
    pragma autonomous_transaction;
begin
  machineName := :new.MACHINE_NAME;
  backNumber := :new.BACK_NUMBER;
  updateTime := :new.UPDATE_TIME;
  begin
    if (machineName is not null ) and (backNumber is not null )  and (updateTime is not null) then
      insert into DK_PRODUCT_PARAMETERS select * from (
             ----气密绑定数据
          select sebango, kiban,'PMSHISTORY'as equipmentType ,EQUIPMENTNUM as equipmentNo, params, paramsValue
              from (select * from PMSHISTORY where EQUIPMENTNUM in(1,2,3,4) and RESULT in ('OK','NG') and sebango = backNumber and kiban = machineName )  
                   unpivot(paramsValue for params in(PRESSURE,PERIOD,SETUPTIME,SETUPPRESSURE,SETUPRETURNVAL,RESULT,WORKINGTIME,UPDATETIME))
          union all
          ----真空绑定数据    
          select sebango,kiban,'VTS_RST'as equipmentType,equipmentNo,params,paramsValue
              from(select sebango,kiban,vtno as equipmentNo,to_char(VACUUM1) VACUUM1,to_char(VACUUM2) VACUUM2,to_char(VACUUM3) VACUUM3,to_char(VACUUM4)VACUUM4,
                  to_char(VACUUM5)VACUUM5,to_char(VACUUM6)VACUUM6,to_char(VACUUM7)VACUUM7,ELAPSEDTIME,STABILIZATION,RESULT,SETDATE,GAPRESULT from VTS_RST t where VTNO in(1,2,3,4,5,6,7)  and sebango = backNumber and kiban = machineName)
                    unpivot(paramsValue for params in(VACUUM1,VACUUM2,VACUUM3,VACUUM4,VACUUM5,VACUUM6,VACUUM7,ELAPSEDTIME,STABILIZATION,RESULT,SETDATE,GAPRESULT))
          union all
          ----冷媒绑定数据             
          select sebango,kiban,'COOLRST_TBL'as equipmentType,toruno as equipmentNo,params,paramsValue  from
                 (select sebango, kiban,toruno,to_char(TORUNUM)TORUNUM,to_char(ZUJANUM)ZUJANUM,to_char(TORUCOU)TORUCOU,to_char(BEFORE_CZ)BEFORE_CZ,to_char(AFTER_CZ)AFTER_CZ,
                   to_char(BALANCE)BALANCE,to_char(PORT)PORT,CZTEST_FLG,TYPE,PRDYMDEND,PRDYMDSTD,PRDTIME,ERRORCODE,TORU,SBID from coolrst_tbl s where s.toruno in(1,2) and sebango = backNumber and kiban = machineName)
                    unpivot(paramsValue for params in(SBID,TORUNUM,ZUJANUM,TORUCOU,TORU,ERRORCODE,PRDTIME,PRDYMDSTD,PRDYMDEND,TYPE,BEFORE_CZ,AFTER_CZ,BALANCE,CZTEST_FLG,PORT))
          union all          
          ----绝缘耐压绑定数据             
          select  sebango,kiban,'RESISTANCETEST'as equipmentType,'' equipmentNo,params,paramsValue from RESISTANCETEST 
                    unpivot(paramsValue for params in(INSULATION1,RESISTANCE1,EARTH,RESULT,TESTTIME,RESISTANCETEST,INSULATIONTEST,INSULATION2,INSULATION3,RESISTANCE2,RESISTANCE3))
                   where 1=1 and  sebango = backNumber and kiban = machineName
          union all  
          ----最终检查绑定数据              
          select bfid sebango,jh kiban,'ZZJC_DATE'as equipmentType,'' equipmentNo,params,paramsValue from ZZJC_DATE
                  unpivot(paramsValue for params in(TIMEID,PRDYMD,RFID,WARRANTY,PQC,MP,RESISTANCE,LABELL,COOL1,COOL2,OIL,VTS,WYZ,HE,DYQM,CZK,GYQM,QMHS,PICKING,MPSB,BZP,BLCZ,DYKQ,DZJP,NYZ,ZZYW,PRESULT,RESULTT,TIMEE,ERR_ID,NXBQ))
                   where 1=1 and  bfid = backNumber and jh = machineName 
          union all  
          ----油充填绑定数据         
          select sebango,kiban,'OILRST_TBL'as equipmentType,'' equipmentNo,params,paramsValue  from
                  (select sebango,kiban,to_char(TORUNUM)TORUNUM,to_char(ZUJANUM)ZUJANUM,to_char(TORUCOU)TORUCOU,TORURST,ERRORCODE,PRDTIME,PRDYMDSTD,PRDYMDEND  from  OILRST_TBL where 1=1 and sebango = backNumber and kiban = machineName)
                    unpivot(paramsValue for params in(TORUNUM,ZUJANUM,TORUCOU,TORURST,ERRORCODE,PRDTIME,PRDYMDSTD,PRDYMDEND)) 
                    
          union all          
          ----CCD识别数据绑定              
           select sebngo sebango,kiban,'VEC_SCAN_LOG'as equipmentType,'' equipmentNo,params,paramsValue from
                  (select sebngo,kiban,to_char(ID)ID,BARCODE,OTHER,to_char(HEIGHT)HEIGHT,LOGO,MNAME,MTYPE,KIBAN_0,RESULT_MSG,to_char(ERR_CODE)ERR_CODE,CREATE_TIME,NX_HTTP,JXMP_HTTP,to_char(NX_ERR_CODE)NX_ERR_CODE from VEC_SCAN_LOG where 1=1 and sebngo = backNumber and kiban = machineName)
                   unpivot(paramsValue for params in(ID,BARCODE,OTHER,HEIGHT,LOGO,MNAME,MTYPE,KIBAN_0,RESULT_MSG,ERR_CODE,CREATE_TIME,NX_HTTP,JXMP_HTTP,NX_ERR_CODE))
           union all
           ----捆包标签数据绑定     
          select sebango,kiban,'LOG'as equipmentType,'' equipmentNo,params,paramsValue from LOG 
               unpivot(paramsValue for params in(PRINTDATE)）         
               where 1=1 and sebango = backNumber and kiban = machineName;   
      );
    end if;
 end;
end TB_PRODUCT_TRACING_TRIGGER;





create or replace trigger DK_PMSHISTORY_TRIGGER
  after  insert
  on  PMSHISTORY
  for each row
declare
 ----气密绑定数据
 --pragma autonomous_transaction;
begin
  begin
    if :new.RESULT = 'OK' OR :new.RESULT = 'NG' then
       insert into DK_PRODUCT_PARAMETERS  select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
          select SEBANGO as sebango,KIBAN as kiban,'PMSHISTORY'as equipmentType ,EQUIPMENTNUM as equipmentNo, params, paramsValue  from (
             select
             :new.SEBANGO AS SEBANGO,
             :new.KIBAN AS KIBAN,
             :new.PRESSURE AS PRESSURE,
             :new.PERIOD AS PERIOD,
             :new.SETUPTIME AS SETUPTIME,
             :new.SETUPPRESSURE AS SETUPPRESSURE,
             :new.SETUPRETURNVAL AS SETUPRETURNVAL,
             :new.RESULT AS RESULT,
             :new.WORKINGTIME AS WORKINGTIME,
             :new.EQUIPMENTNUM AS EQUIPMENTNUM,
             :new.UPDATETIME AS UPDATETIME
             from dual
           )
           unpivot(paramsValue for params in(PRESSURE,PERIOD,SETUPTIME,SETUPPRESSURE,SETUPRETURNVAL,RESULT,WORKINGTIME,UPDATETIME))
           where EQUIPMENTNUM in(1,2,3,4)
      );
   end if;
  end;
end DK_PMSHISTORY_TRIGGER;


create or replace trigger DK_OILRST_TBL_TRIGGER
  after  insert
  on  OILRST_TBL
  for each row
declare
--油充填绑定数据
 --pragma autonomous_transaction;
begin
  begin
   insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'OILRST_TBL' as equipmentType,'' as equipmentNo ,params,paramsValue from (
        select
          :new.sebango as sebango,
          :new.kiban as kiban,
          to_char(:new.TORUNUM) as TORUNUM,
          to_char(:new.ZUJANUM) as ZUJANUM,
          to_char(:new.TORUCOU) as TORUCOU,
          :new.TORURST as TORURST,
          :new.ERRORCODE as ERRORCODE,
          :new.PRDTIME as PRDTIME,
          :new.PRDYMDSTD as PRDYMDSTD,
          :new.PRDYMDEND as PRDYMDEND
        from dual
      )
      unpivot(paramsValue for params in(TORUNUM,ZUJANUM,TORUCOU,TORURST,ERRORCODE,PRDTIME,PRDYMDSTD,PRDYMDEND))
    );
  end;
end DK_OILRST_TBL_TRIGGER;


create or replace trigger DK_COOLRST_TBL_TRIGGER
  after  insert
  on  COOLRST_TBL
  for each row
declare
--冷媒绑定数据
 --pragma autonomous_transaction;
begin
  begin
    insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'COOLRST_TBL' as equipmentType,toruno as equipmentNo,params,paramsValue  from (
        select
          :new.sebango as sebango,
          :new.kiban as kiban,
          :new.toruno as toruno,
          to_char(:new.TORUNUM) as TORUNUM,
          to_char(:new.ZUJANUM) as ZUJANUM,
          to_char(:new.TORUCOU) as TORUCOU,
          to_char(:new.BEFORE_CZ) as BEFORE_CZ,
          to_char(:new.AFTER_CZ) as AFTER_CZ,
          to_char(:new.BALANCE) as BALANCE,
          to_char(:new.PORT) as PORT,
          :new.CZTEST_FLG as CZTEST_FLG,
          :new.TYPE as TYPE,
          :new.PRDYMDEND as PRDYMDEND,
          :new.PRDYMDSTD as PRDYMDSTD,
          :new.PRDTIME as PRDTIME,
          :new.ERRORCODE as ERRORCODE,
          :new.TORU as TORU,
          :new.SBID as SBID
        from dual
      )
      unpivot(paramsValue for params in(SBID,TORUNUM,ZUJANUM,TORUCOU,TORU,ERRORCODE,PRDTIME,PRDYMDSTD,PRDYMDEND,TYPE,BEFORE_CZ,AFTER_CZ,BALANCE,CZTEST_FLG,PORT))
   );
  end;
end DK_COOLRST_TBL_TRIGGER;


create or replace trigger DK_VTS_RST_TRIGGER
  after  insert
  on  VTS_RST
  for each row
declare
--真空绑定数据
 --pragma autonomous_transaction;
begin
  begin
    insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'VTS_RST' as equipmentType,equipmentNo,params,paramsValue  from (
        select
          :new.sebango as sebango,
          :new.kiban as kiban,
          :new.vtno as equipmentNo,
          to_char(:new.VACUUM1) as VACUUM1,
          to_char(:new.VACUUM2) as VACUUM2,
          to_char(:new.VACUUM3) as VACUUM3,
          to_char(:new.VACUUM4) as VACUUM4,
          to_char(:new.VACUUM5) as VACUUM5,
          to_char(:new.VACUUM6) as VACUUM6,
          to_char(:new.VACUUM7) as VACUUM7,
          :new.ELAPSEDTIME as ELAPSEDTIME,
          :new.STABILIZATION as STABILIZATION,
          :new.RESULT as RESULT,
          :new.SETDATE as SETDATE,
          :new.GAPRESULT as GAPRESULT
        from dual
      )
      unpivot(paramsValue for params in(VACUUM1,VACUUM2,VACUUM3,VACUUM4,VACUUM5,VACUUM6,VACUUM7,ELAPSEDTIME,STABILIZATION,RESULT,SETDATE,GAPRESULT))
      where equipmentNo in(1,2,3,4,5,6,7)
    );
  end;
end DK_VTS_RST_TRIGGER;

create or replace trigger DK_RESISTANCETEST_TRIGGER
  after  insert
  on  RESISTANCETEST
  for each row
declare
--绝缘耐压绑定数据  
 --pragma autonomous_transaction;
begin
  begin
    insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'RESISTANCETEST' as equipmentType,equipmentNo,params,paramsValue  from (
        select
          :new.sebango as sebango,
          :new.kiban as kiban,
          '' equipmentNo,
          :new.INSULATION1 as INSULATION1,
      :new.RESISTANCE1 as RESISTANCE1,
      :new.EARTH as EARTH,
      :new.RESULT as RESULT,
      :new.TESTTIME as TESTTIME,
      :new.RESISTANCETEST as RESISTANCETEST,
      :new.INSULATIONTEST as INSULATIONTEST,
      :new.INSULATION2 as INSULATION2,
      :new.INSULATION3 as INSULATION3,
      :new.RESISTANCE2 as RESISTANCE2,
      :new.RESISTANCE3 as RESISTANCE3
        from dual
      )
     unpivot(paramsValue for params in(INSULATION1,RESISTANCE1,EARTH,RESULT,TESTTIME,RESISTANCETEST,INSULATIONTEST,INSULATION2,INSULATION3,RESISTANCE2,RESISTANCE3))
    );
  end;
end DK_RESISTANCETEST_TRIGGER;


create or replace trigger DK_ZZJC_DATE_TRIGGER
  after  insert
  on  ZZJC_DATE
  for each row
declare
--最终检查绑定数据 
 --pragma autonomous_transaction;
begin
  begin
    insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'ZZJC_DATE' as equipmentType,equipmentNo,params,paramsValue  from (
        select
          :new.bfid as sebango,
          :new.jh as kiban,
          '' equipmentNo,
          :new.TIMEID as TIMEID,
      :new.PRDYMD as PRDYMD,
      :new.RFID as RFID,
      :new.WARRANTY as WARRANTY,
      :new.PQC as PQC,
      :new.MP as MP,
      :new.RESISTANCE as RESISTANCE,
      :new.LABELL as LABELL,
      :new.COOL1 as COOL1,
      :new.COOL2 as COOL2,
      :new.OIL as OIL,
      :new.VTS as VTS,
      :new.WYZ as WYZ,
      :new.HE as HE,
      :new.DYQM as DYQM,
      :new.CZK as CZK,
      :new.GYQM as GYQM,
      :new.QMHS as QMHS,
      :new.PICKING as PICKING,
      :new.MPSB as MPSB,
      :new.BZP as BZP,
      :new.BLCZ as BLCZ,
      :new.DYKQ as DYKQ,
      :new.DZJP as DZJP,
      :new.NYZ as NYZ,
      :new.ZZYW as ZZYW,
      :new.PRESULT as PRESULT,
      :new.RESULTT as RESULTT,
      :new.TIMEE as TIMEE,
      :new.ERR_ID as ERR_ID,
      :new.NXBQ as NXBQ
        from dual
      )
     unpivot(paramsValue for params in(TIMEID,PRDYMD,RFID,WARRANTY,PQC,MP,RESISTANCE,LABELL,COOL1,COOL2,OIL,VTS,WYZ,HE,DYQM,CZK,GYQM,QMHS,PICKING,MPSB,BZP,BLCZ,DYKQ,DZJP,NYZ,ZZYW,PRESULT,RESULTT,TIMEE,ERR_ID,NXBQ))
    );
  end;
end DK_ZZJC_DATE_TRIGGER;



create or replace trigger DK_VEC_SCAN_LOG_TRIGGER
  after  insert
  on  VEC_SCAN_LOG
  for each row
declare
--CCD识别数据绑定
 --pragma autonomous_transaction;
begin
  begin
    insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'VEC_SCAN_LOG' as equipmentType,equipmentNo,params,paramsValue  from (
        select
          :new.sebngo as sebango,
          :new.kiban as kiban,
          to_char(:new.ID) as ID,
          '' as equipmentNo,
          :new.BARCODE as BARCODE,
          :new.OTHER as OTHER,
          to_char(:new.HEIGHT) as HEIGHT,
          :new.LOGO as LOGO,
          :new.MNAME as MNAME,
          :new.MTYPE as MTYPE,
          :new.KIBAN_0 as KIBAN_0,
          :new.RESULT_MSG as RESULT_MSG,
          to_char(:new.ERR_CODE) as ERR_CODE,
          :new.CREATE_TIME as CREATE_TIME,
          :new.NX_HTTP as NX_HTTP,
          :new.JXMP_HTTP as JXMP_HTTP,
          to_char(:new.NX_ERR_CODE) as NX_ERR_CODE
        from dual
      )
     unpivot(paramsValue for params in(ID,BARCODE,OTHER,HEIGHT,LOGO,MNAME,MTYPE,KIBAN_0,RESULT_MSG,ERR_CODE,CREATE_TIME,NX_HTTP,JXMP_HTTP,NX_ERR_CODE))
    );
  end;
end DK_VEC_SCAN_LOG_TRIGGER;

create or replace trigger DK_LOG_TRIGGER
  after  insert
  on  LOG
  for each row
declare
--捆包标签数据绑定
 --pragma autonomous_transaction;
begin
  begin
    insert into DK_PRODUCT_PARAMETERS select sebango,kiban,equipmentType,equipmentNo,params,paramsValue,sysdate from (
      select sebango,kiban,'LOG' as equipmentType,equipmentNo,params,paramsValue  from (
        select
          :new.sebango as sebango,
          :new.kiban as kiban,
          '' as equipmentNo,
         :new.PRINTDATE as PRINTDATE
        from dual
      )
      unpivot(paramsValue for params in(PRINTDATE))
    );
  end;
end DK_LOG_TRIGGER;


INSERT INTO ts_language VALUES(SEQ_LANGUAGE_ID.NEXTVAL,'PHONE_MANUAL_TRIGGER','NG出入口手动触发',' Manual trigger ',0);
INSERT INTO ts_menu VALUES('20005',200,'PHONE_MANUAL_TRIGGER','money',0,0,'../fault-machine/manual-trigger.html');
INSERT INTO ts_permission VALUES('2000501','20005','ManualTriggerView','NG出入口手动触发查看',1,0);

alter table  tb_quality_tracing add MARK_FOR_DELETE     CHAR(1) default '0' not null;

INSERT INTO ts_dict_type  VALUES(SEQ_DICT_TYPE_ID.nextval,'WHETHER_DELETE','是否删除','whether to delete',NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'WHETHER_DELETE'),'0','否','whether to delete YES',1,NULL,0);
INSERT INTO ts_dict_entry VALUES(SEQ_DICT_ENTRY_ID.nextval,(select id from ts_dict_type where type_code = 'WHETHER_DELETE'),'1','是','whether to delete NO',2,NULL,0);


insert into ts_global_configuration (ID, SYS_KEY, NAME, SYS_VALUE, PROPERTY, REMARK, SEQ, OPT_COUNTER, CREATE_USER, CREATE_TIME, UPDATE_USER, UPDATE_TIME, MARK_FOR_DELETE, TYPE)
values (seq_global_configuration_id.nextval, 'APP_TIME_OUT', 'APP超时时间', '10', '*', 'APP超时时间(分钟)', null, 0, null, null, 1, null, '0', null);