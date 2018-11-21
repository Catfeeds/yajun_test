CREATE OR REPLACE TRIGGER TMP_MAINTENANCE_INFO_TRIGGER
  after insert 
  on TMP_MAINTENANCE_INFO_TEMP 
  for each row
declare
   isExist number;-- 是否存在
   dm_id number(10); --设备id
   dm_value number(10);--设备运行总时间 
   dm_last_time TIMESTAMP(6); --数据采集时间
   diff_value number; --单次运行时间
   now_date TIMESTAMP;--当前系统时间
   old_status number(1);
   now_status number;
   status_1 varchar(16);
   status_2 varchar(16);
   status_3 varchar(16);
   maintenancecode  varchar(64);   
   pragma autonomous_transaction;
begin
     now_date := SYSDATE;
     status_1 :=GET_ALLCODE_BY_BIN(num_to_bin(:NEW.STATUS_1),16);
     status_2 :=GET_ALLCODE_BY_BIN(num_to_bin(:NEW.STATUS_2),16);
     status_3 :=GET_ALLCODE_BY_BIN(num_to_bin(:NEW.STATUS_3),16);
       FOR a IN 1..3 loop
        FOR b IN 1..16 loop
           isExist := 0;
           dm_value := 0;
           if a=1 then 
             now_status := substr(status_1,b,1);
             maintenancecode :=:NEW.MATERIAL_CODE ||'A'||GET_BINCODE_BY_INDEX(b,16);
           elsif a=2 then
             now_status := substr(status_2,b,1);
             maintenancecode :=:NEW.MATERIAL_CODE ||'B'||GET_BINCODE_BY_INDEX(b,16);
           else
             now_status := substr(status_3,b,1);
             maintenancecode :=:NEW.MATERIAL_CODE ||'C'||GET_BINCODE_BY_INDEX(b,16);
           end if;
           begin
              select t.id,NVL(t.Current_Value,0),t.last_collect_tiime,t.status 
              into dm_id,dm_value,dm_last_time,old_status from TM_DEVICE_MAINTENANCE t 
              where t.mark_for_delete=0 and t.maintenance_code = maintenancecode  and rownum = 1;
              exception when no_data_found then 
                  isExist:=1;
            end;
            if isExist=0 and  old_status!=now_status then
                if now_status=1 then
                  begin
                     update TM_DEVICE_MAINTENANCE set LAST_COLLECT_TIIME = now_date ,STATUS=now_status where id = dm_id;
                  end;
                 else
                   begin
                     diff_value := EXTRACT (Minute FROM(now_date - dm_last_time));
                     dm_value := dm_value + diff_value;
                     update TM_DEVICE_MAINTENANCE set LAST_COLLECT_TIIME = now_date,CURRENT_VALUE=dm_value,STATUS= now_status where id = dm_id;
                   end;
                 end if;
               end if;
           end loop;
        end loop;
      commit;  
end TMP_MAINTENANCE_INFO_TRIGGER;


#######################################################################################
CREATE OR REPLACE TRIGGER TMP_PRODUCTION_TRIGGER
  after insert
  on TMP_PRODUCTION_TEMP
  for each row
declare
   isExist number;
   recordIsExist number;
   nowStatus number;
   schedulId number;
   relationId number;
   workTimeId number;
   regionMark VARCHAR2(64);
   startDate TIMESTAMP(6);
   issuedDate TIMESTAMP(6);
   periodTakeTime NUMBER(10);
   placeTime NUMBER(10);
   alarmNumber NUMBER(10);
   workPeople VARCHAR2(512);
   moulds VARCHAR2(256);
   mcode  VARCHAR2(64);
   finishNumber number;
   pragma autonomous_transaction;
begin
  isExist := 0;
  begin
    select t.id,t.status,t.work_time_id,t.start_time,t.region_mark,t.issued_time,nvl(t.relation_id,0),t.moulds
    into schedulId,nowStatus,workTimeId,startDate,regionMark,issuedDate,relationId,moulds
    from tb_mp_schedul t
    where t.mark_for_delete=0 and t.batch_number=:NEW.BATCH_NUMBER;
    exception when no_data_found then
        isExist:=1;
  end;
  if isExist=0 then
     finishNumber:=:NEW.Finish_Number;
     if :NEW.STATUS=2 and nowStatus!=3 then --已接受，根据批次号修改排产列表状态为3已下发
        update tb_mp_schedul set status=3,production_time=0,issued_time=sysdate,update_time =sysdate where id=schedulId;
     elsif :NEW.STATUS=4 then --生产中，更新当前生产时间及当前完成产量
        if nowStatus!=1 then
           update tb_mp_schedul set status=1,start_time=sysdate,update_time =sysdate,production_time=:NEW.PRODUCTION_TIME,finish_number=finishNumber where id=schedulId;
        else
           update tb_mp_schedul set status=1,update_time =sysdate,production_time=:NEW.PRODUCTION_TIME,finish_number=finishNumber where id=schedulId;
        end if;
     elsif :NEW.STATUS=5 then --生产完成，更新排产列表状态2已完成，插入生产记录数据CT/冲速/停止角度获取
         recordIsExist:=1;
         
         begin
	       -- start 生产完成后重新计算库存  计划生产量 - 计划需求量+剩余库存
           SELECT T.APC_TOTAL_NUMBER, --计划需求量
             T2.INVENTORY_NUMBER, --剩余库存
             T.PLANNED_PRODUCTION --计划生产量
             into apcTotalNumber,inventoryNumber,plannedProduction
            FROM TB_MP_SOURCE_DATA T
            left join TM_SHEET_METAL_MATERIAL T2
              on T.MATERIAL_ID = T2.ID
            where T.status < 2 and T.MARK_FOR_DELETE = 0  and rownum=1 and T.MODEL=mould ORDER BY T.ID DESC;
           
           UPDATE TM_SHEET_METAL_MATERIAL T SET T.INVENTORY_NUMBER=plannedProduction-apcTotalNumber+inventoryNumber WHERE T.MARK_FOR_DELETE = 0 and T.MACHINING_SURFACE = mould;
           
           if plannedProduction=0 and apcTotalNumber=0 then
             
              UPDATE TM_SHEET_METAL_MATERIAL T SET T.INVENTORY_NUMBER=dispatchNumber-consumeApcTotal+inventoryNumber WHERE T.MARK_FOR_DELETE = 0 and T.MACHINING_SURFACE = mould;
             
           end if;
          end;
         --end
	         
         
         begin
            --查询记录是否存在，存在不做处理
            select count(*) into recordIsExist from tb_mp_production_record t where t.mark_for_delete=0 and t.batch_number=:NEW.BATCH_NUMBER;
            exception when no_data_found then
                recordIsExist:=0;
         end;
         if recordIsExist=0 then
             begin
                --查询当前工作人员
                select listagg(t.no||'_'||t.name_cn,',') within group (order by t.id) into workPeople from TM_EMPLOYEE_NO t
                left join TM_CLASS_MANAGER_DETAIL d on d.tm_employee_no_id=t.id
                left join tm_worktime w on w.tm_class_manager_id=d.tm_class_manager_id
                where w.id=workTimeId;
                --查询该订单开始时间到结束的报警次数 故障编码:0000000000000100
                select count(*) INTO alarmNumber from TMP_EQUIPMENT_STATUS t
                left join tm_equipment e on e.id=t.tm_equipment_id
                where e.LOCATION=regionMark and t.create_time>=issuedDate AND t.running_state='0000000000000100';
                --查询该订单的换模时间 换模编码:0001000000000000
                select nvl(max(t.duration),0) into periodTakeTime from TMP_EQUIPMENT_STATUS t
                left join tm_equipment e on e.id=t.tm_equipment_id
                where e.LOCATION=regionMark
                and t.create_time>(select max(record.create_time) from tb_mp_production_record record where record.region_mark=regionMark)
                AND t.running_state='0001000000000000';
                --查询该订单的换料时间 换料编码:0000000010000000
                select nvl(sum(t.duration),0) into placeTime from TMP_EQUIPMENT_STATUS t
                left join tm_equipment e on e.id=t.tm_equipment_id
                where e.LOCATION=regionMark
                and t.create_time>(select max(record.create_time) from tb_mp_production_record record where record.region_mark=regionMark)
                AND t.running_state='0000000010000000';
             end;
            insert into tb_mp_production_record
            (id,create_time,region_mark,status,velocity,cycle_time,stop_angle,start_time,end_time,
            schedul_id,production_date,model,batch_number,work_schedule,press,moulds,planned_cycles,defective_number,inventory_consumption,create_user,
            period_take_time,place_time,alarm_number,work_people,production_time,practical_cycles
            )
            (select SEQ_MP_PRODUCTION_RECORD_ID.NEXTVAL,sysdate,region_mark,'0',:new.velocity,:new.cycle_time,:new.stop_angle,to_char(startDate, 'HH24:MI:SS'),to_char(sysdate, 'HH24:MI:SS'),
            s.id,s.schedul_date,s.model,s.batch_number,s.work_schedule,s.press,s.moulds,s.dispatch_number,0,0,s.create_user,periodTakeTime,placeTime,
            alarmNumber,workPeople,:NEW.PRODUCTION_TIME,finishNumber from tb_mp_schedul s where s.id = schedulId
            );
            if relationId!=0 then -- 生成关联数据的生产记录
              insert into tb_mp_production_record
              (id,create_time,region_mark,status,velocity,cycle_time,stop_angle,start_time,end_time,
              schedul_id,production_date,model,batch_number,work_schedule,press,moulds,planned_cycles,defective_number,inventory_consumption,create_user,
              period_take_time,place_time,alarm_number,work_people,production_time,practical_cycles
              )
              (select SEQ_MP_PRODUCTION_RECORD_ID.NEXTVAL,sysdate,region_mark,'0',:new.velocity,:new.cycle_time,:new.stop_angle,to_char(startDate, 'HH24:MI:SS'),to_char(sysdate, 'HH24:MI:SS'),
              s.id,s.schedul_date,s.model,s.batch_number,s.work_schedule,s.press,s.moulds,s.dispatch_number,0,0,s.create_user,periodTakeTime,placeTime,
              alarmNumber,workPeople,:NEW.PRODUCTION_TIME,finishNumber from tb_mp_schedul s where s.id = relationId
              );
             end if;
           update tb_mp_schedul set status=2,update_time =sysdate,production_time=:NEW.PRODUCTION_TIME,finish_number=finishNumber where id=schedulId;
           if relationId!=0 then -- 修改关联排产数据
             update tb_mp_schedul set status=2,update_time =sysdate,production_time=:NEW.PRODUCTION_TIME,finish_number=finishNumber where id=relationId;
           end if;

           -- 生产完成后模具的弹簧和刀口的设备保养数据增加使用次数（实际生产数量）
           begin
             for mould in (select * from table(strsplit(moulds,','))) loop
               mcode := mould.COLUMN_VALUE || 'A0000000000000001';
               update TM_DEVICE_MAINTENANCE
               set LAST_COLLECT_TIIME = sysdate,CURRENT_VALUE=NVL(Current_Value,0)+finishNumber
               where MAINTENANCE_CODE = mcode and MARK_FOR_DELETE='0';

               mcode := mould.COLUMN_VALUE || 'A0000000000000010';
               update TM_DEVICE_MAINTENANCE
               set LAST_COLLECT_TIIME = sysdate,CURRENT_VALUE=NVL(Current_Value,0)+finishNumber
               where MAINTENANCE_CODE = mcode and MARK_FOR_DELETE='0';
             end loop;
           end;
         end if;
     end if;
     commit;
   end if;
end TMP_PRODUCTION_TRIGGER;


#######################################################################################

CREATE OR REPLACE TRIGGER TMP_DEVICE_STATUS_TEMP_TRIGGER
  after insert
  on TMP_DEVICE_STATUS_TEMP
  for each row
declare
  equipmentNoNEW     varchar2(20);
  equipmentStatusNEW  varchar2(16);
  equipmentCodeNEW   varchar2(32);
  lineCodeNEW        varchar2(20);
  lineIdOld          number(20); ---MES基础数据（产线ID）
  equipmentIdOld     varchar2(20); ---MES基础数据（设备ID）
  currentWorkTimeId      number(20); -- 当前时间的工作日历ID
  currentClassManagerId  number(20); -- 当前班组ID
  is_rest            varchar2(20);
begin
  equipmentNoNEW:= :NEW.DEVICE_NO;
  equipmentStatusNEW := GET_ALLCODE_BY_BIN(num_to_bin(:NEW.DEVICE_STATUS),16);
  equipmentCodeNEW   := GET_ALLCODE_BY_BIN(num_to_bin(:NEW.DEVICE_FAULT_CODE),32);
  begin
    select sys_value into lineCodeNEW  from ts_global_configuration where sys_key ='MP_LINE_NO';
        exception
              when no_data_found then
                lineCodeNEW:='282';
  end;
  ---根据产线CODE获取产线id
  begin
    select  tl.id into lineIdOld from tm_line tl where tl.no = lineCodeNEW and rownum = 1;
  exception
    when no_data_found then
      dbms_output.put_line('没有找到设备信息');
  end;
  
  ---根据设备编号查询MES系统对应基础数据ID
  begin
    select te.id into equipmentIdOld from tm_equipment te where te.no = equipmentNoNEW and rownum = 1;
  exception
    when no_data_found then
      dbms_output.put_line('没有找到设备信息');
      return;
  end;
  
  ---根据设备编号查询MES系统对应基础数据ID
  begin
    select te.id into equipmentIdOld from tm_equipment te where te.no = equipmentNoNEW and rownum = 1;
  exception
    when no_data_found then
      dbms_output.put_line('没有找到设备信息');
      return;
  end;

  /*通过当前时间和产线ID查询当前工作日历ID和班次ID*/
  begin
    select tab.id, tab.classManagerId
      into currentWorkTimeId, currentClassManagerId
      from (select t.id as id,
                   t.tm_class_manager_id classManagerId,
                   to_char(t.work_date, 'yyyy-MM-dd') || ' ' || t.start_time as startTime,
                   case
                     when t.end_time < t.start_time then
                      to_char(t.work_date + 1, 'yyyy-MM-dd') || ' ' ||
                      t.end_time
                     else
                      to_char(t.work_date, 'yyyy-MM-dd') || ' ' || t.end_time
                   END endTime
              from tm_worktime t
             where t.tm_line_id = lineIdOld
               and t.enabled = 'ON') tab
     where rownum = 1
       and sysdate between to_date(tab.startTime, 'yyyy-MM-dd HH24:mi:ss') and
           to_date(tab.endTime, 'yyyy-MM-dd HH24:mi:ss');
  exception
    when no_data_found then
      currentWorkTimeId := NULL;
      dbms_output.put_line('没有找到班次信息');
  end;

  begin
    is_rest := CHECK_IS_REST(currentWorkTimeId, lineIdOld);
  end;

  tmp_equipment_status_procedure
    (equipmentNoNEW, equipmentIdOld,equipmentStatusNEW, equipmentCodeNEW,lineIdOld,
    currentWorkTimeId,currentClassManagerId,is_rest);
end TMP_DEVICE_STATUS_TEMP_TRIGGER;

#######################################################################################

create or replace procedure tmp_equipment_status_procedure(equipmentNoNEW       in varchar2,
                                                          equipmentId           in varchar2,
                                                          equipmentStatusNEW    in varchar2,
                                                          equipmentCodeNEW      in varchar2,
                                                          lineId                in number,
                                                          currentWorkTimeId     in number,
                                                          currentClassManagerId in number,
                                                          is_rest               in varchar2) is
                                                          
  oldIsExist             number(1); -- 1 存在  0 不存在
  currentTime            varchar2(20); --  当前时间 HH24:mi:ss
  newSysdate             date; --当前系统时间（年月日时分秒）
  is_fault               number(10);
  equipmentStatusSUB     varchar2(16);
  equipmentCodeSUB       varchar2(32);
  nowStatusValue         varchar2(1);
  nowCodeValue           varchar2(1);
  statusIsExist          number(1);
  codeIsExist            number(1);
  oldWorkTimeId          number(20);
begin
  newSysdate         := sysdate;
  currentTime        := to_char(newSysdate, 'hh24:mi:ss');
  oldIsExist         := 1;
  
 --循环遍历16位设备状态信息
 FOR nowStatusIndex IN 1..16 loop
    is_fault:=0;
    equipmentStatusSUB:=GET_BINCODE_BY_INDEX(nowStatusIndex,16);
    begin
      select case when count(t.id)!=0 then 1 else 0 end
        into statusIsExist from TM_CODE_RULE t where t.plc_code= equipmentNoNEW || ' '||equipmentStatusSUB;
    end;
    if statusIsExist=1 then
      nowStatusValue:= substr(equipmentStatusNEW,nowStatusIndex,1);
      begin
        select nvl(rownum,0),t.tm_work_time_id into oldIsExist,oldWorkTimeId from TMP_EQUIPMENT_STATUS t
        where t.RUNNING_STATE=equipmentStatusSUB and t.finish_time is null and t.tm_equipment_id=equipmentId and rownum=1;
        exception
          when no_data_found then
            oldIsExist:=0;
      end;
      --判断是否为故障的编码
      if equipmentStatusSUB!='0000000000000100' then
        if nowStatusValue=0 and oldIsExist=1 then
          --修改
           update TMP_EQUIPMENT_STATUS t
             set finish_time = currentTime, update_time = newSysdate,
             t.duration = CEIL((newSysdate-t.create_time) * 24*60*60)
           where t.RUNNING_STATE = equipmentStatusSUB and t.finish_time is null and t.tm_equipment_id=equipmentId;
        elsif nowStatusValue=1 and oldIsExist=0 then
          --新增
          insert into TMP_EQUIPMENT_STATUS
            (id,tm_work_time_id,tm_class_manager_id,is_rest,tm_line_id,tm_equipment_id,running_state,begin_time,create_time)
          values
            (SEQ_EQUIPMENT_STATUS.nextval,currentWorkTimeId,currentClassManagerId,is_rest,lineId,equipmentId,equipmentStatusSUB,currentTime,newSysdate);
        elsif nowStatusValue=1 then
          ----原设备状态不等于，当前最新状态的时候，证明状态发生了变化，新增一条数据，并且更新原设备状态结束时间
          if (oldWorkTimeId is not null and currentWorkTimeId is not null and
             oldWorkTimeId <> currentWorkTimeId) or
             (oldWorkTimeId is not null and currentWorkTimeId is null) or
             (oldWorkTimeId is null and currentWorkTimeId is not null)
              then
             --先把之前的班次的关闭
             update TMP_EQUIPMENT_STATUS t
               set finish_time = currentTime, update_time = newSysdate,
               t.duration = CEIL((newSysdate-t.create_time) * 24*60*60)
             where t.RUNNING_STATE = equipmentStatusSUB and t.finish_time is null and t.tm_equipment_id=equipmentId;
             --然后重新新增一条新的班次的故障数据
             insert into TMP_EQUIPMENT_STATUS
                (id,tm_work_time_id,tm_class_manager_id,is_rest,tm_line_id,tm_equipment_id,running_state,begin_time,create_time)
              values
                (SEQ_EQUIPMENT_STATUS.nextval,currentWorkTimeId,currentClassManagerId,is_rest,lineId,equipmentId,equipmentStatusSUB,currentTime,newSysdate);
          end if;
        end if;
      else
        --故障或异常时循环遍历32位故障码
          FOR nowCodeIndex IN 1..32 loop
            equipmentCodeSUB:=GET_BINCODE_BY_INDEX(nowCodeIndex,32);
            begin
              select  case when count(t.id)!=0 then 1 else 0 end into codeIsExist from TM_CODE_RULE t where t.plc_code= equipmentNoNEW || ' '||equipmentCodeSUB;
            end;
            if codeIsExist=1 then
              begin
                select nvl(rownum,0),t.tm_work_time_id into oldIsExist,oldWorkTimeId from TMP_EQUIPMENT_STATUS t
                where t.status_number = equipmentCodeSUB and t.finish_time is null and t.tm_equipment_id=equipmentId and rownum=1;
                exception
                  when no_data_found then
                    oldIsExist:=0;
              end;
              nowCodeValue:= substr(equipmentCodeNEW,nowCodeIndex,1);
              if nowCodeValue=0 and oldIsExist=1 then
                --修改
                update TMP_EQUIPMENT_STATUS t
                  set finish_time = currentTime, update_time = newSysdate,
                  t.duration = CEIL((newSysdate-t.create_time) * 24*60*60)
                where t.status_number = equipmentCodeSUB and t.finish_time is null and t.tm_equipment_id=equipmentId;
              elsif nowCodeValue=1 and oldIsExist=0 then
                --新增
                insert into TMP_EQUIPMENT_STATUS
                    (id,tm_work_time_id,tm_class_manager_id,is_rest,tm_line_id,tm_equipment_id,running_state,status_number,begin_time,create_time)
                values
                    (SEQ_EQUIPMENT_STATUS.nextval,currentWorkTimeId,currentClassManagerId,is_rest,lineId,equipmentId,equipmentStatusSUB,equipmentCodeSUB,currentTime,newSysdate);
               elsif nowStatusValue=1 then
                ----原设备状态不等于，当前最新状态的时候，证明状态发生了变化，新增一条数据，并且更新原设备状态结束时间
                if (oldWorkTimeId is not null and currentWorkTimeId is not null and
                   oldWorkTimeId <> currentWorkTimeId) or
                   (oldWorkTimeId is not null and currentWorkTimeId is null) or
                   (oldWorkTimeId is null and currentWorkTimeId is not null)
                   then
                   --先把之前的班次的关闭
                   update TMP_EQUIPMENT_STATUS t
                      set finish_time = currentTime, update_time = newSysdate,
                      t.duration = CEIL((newSysdate-t.create_time) * 24*60*60)
                    where t.status_number = equipmentCodeSUB and t.finish_time is null and t.tm_equipment_id=equipmentId;
                   --然后重新新增一条新的班次的故障数据
                   insert into TMP_EQUIPMENT_STATUS
                      (id,tm_work_time_id,tm_class_manager_id,is_rest,tm_line_id,tm_equipment_id,running_state,status_number,begin_time,create_time)
                   values
                      (SEQ_EQUIPMENT_STATUS.nextval,currentWorkTimeId,currentClassManagerId,is_rest,lineId,equipmentId,equipmentStatusSUB,equipmentCodeSUB,currentTime,newSysdate);
                 end if;
               end if;
            end if;
          end loop;
      end if;
    end if;
  end loop;
end tmp_equipment_status_procedure;

