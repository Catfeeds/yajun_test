package com.wis.mes.job;

import org.springframework.stereotype.Component;

import com.wis.mes.master.equipment.entity.TmEquipmentMaintenance;

/**
 * @author Jixueyuan
 * @date 2017年6月1日
 * @Description: 定时任务-设备保养剩余时间提醒
 */
@Component("maintenanceJob")
public class MaintenanceJob {
/*
	@Autowired
	private TmEquipmentMaintenanceService equipmentMaintenanceService;
	@Autowired
	private MailService mailService;
	@Autowired
	private TmEquipmentResponsiblePersonService tmEquipmentResponsiblePersonService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	*/
	//定时查询
	public void findMaintenance() {/*
		//查询所有的设备
		BootstrapTableQueryCriteria criteria = new BootstrapTableQueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		List<TmEquipmentMaintenance> maintenanceList = equipmentMaintenanceService.findBy(criteria).getContent();
		for (TmEquipmentMaintenance tmEquipmentMaintenance : maintenanceList) {
			Date currentDate = new Date();//当前时间
			Date lastTime = maintenanceList.get(0).getLastMaintenanceTime();//上次保养时间
			long differTime = 0L;
			if(maintenanceList.get(0).getNextMaintenanceTime()!=null){
				if (lastTime==null) {//判断是否是第一次
					differTime = (maintenanceList.get(0).getNextMaintenanceTime().getTime() - currentDate.getTime())/ (60 * 60 * 1000);//剩余保养时间
					//如果当前时间距下次保养时间达到设定值则发送邮件提醒
					if (differTime <= tmEquipmentMaintenance.getTimeWarningValue()) {
						saveToMail(tmEquipmentMaintenance);
					}
				} else {
					Calendar nextTime = new GregorianCalendar();
					nextTime.setTime(lastTime);
					if (maintenanceList.get(0).getType().equalsIgnoreCase("year")) {//年
						nextTime.set(Calendar.YEAR,
								nextTime.get(Calendar.YEAR) + maintenanceList.get(0).getTypeValue());
					}
					if (maintenanceList.get(0).getType().equalsIgnoreCase("month")) {//月
						nextTime.set(Calendar.MONTH,
								nextTime.get(Calendar.MONTH) + maintenanceList.get(0).getTypeValue());
					}
					if (maintenanceList.get(0).getType().equalsIgnoreCase("week")) {//周
						nextTime.set(Calendar.DAY_OF_MONTH,
								nextTime.get(Calendar.DAY_OF_MONTH) + 7 * (maintenanceList.get(0).getTypeValue()));
					}
					if (maintenanceList.get(0).getType().equalsIgnoreCase("day")) {//天
						nextTime.set(Calendar.DAY_OF_MONTH,
								nextTime.get(Calendar.DAY_OF_MONTH) + maintenanceList.get(0).getTypeValue());
					}
					differTime = (nextTime.getTimeInMillis() - currentDate.getTime()) / (60 * 60 * 1000);//剩余保养时间
					//如果当前时间距下次保养时间达到设定值则发送邮件提醒
					if (differTime <= tmEquipmentMaintenance.getTimeWarningValue()) {
						saveToMail(tmEquipmentMaintenance);
					}
				
			}
			}
			//如果剩余保养次数达到设定值发送邮件提醒
			if (tmEquipmentMaintenance.getRemainderCount() != null
					&& tmEquipmentMaintenance.getCountWarningValue() != null) {
				if (tmEquipmentMaintenance.getRemainderCount() <= tmEquipmentMaintenance.getCountWarningValue()) {
					saveToMail(tmEquipmentMaintenance);
				}
			}
		}*/
	}

	public void saveToMail(TmEquipmentMaintenance tmEquipmentMaintenance) {/*
		BootstrapTableQueryCriteria criteria = new BootstrapTableQueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentMaintenance.getTmEquipmentId().toString());
		List<TmEquipmentResponsiblePerson> responsiblePerson = tmEquipmentResponsiblePersonService.findBy(criteria).getContent();
		//如果发送人不为空则将要发送邮件的数据保存到ts_mail
		if (responsiblePerson.size() > 0 && responsiblePerson != null) {
			Mail mail = new Mail();
			mail.setFlag("0");
			mail.setSubject(globalConfigurationService.getValueByKey(ConstantUtils.EQUIPMENT_MAINTENANCE_EAMIL_SUBJECT));
			try {
				for (TmEquipmentResponsiblePerson tmEquipmentResponsiblePerson : responsiblePerson) {
					//判断今天是否已经发送过邮件
					Long startTimeOfToday = getStartTime().getTime();//今天的开始时间
					Long lastEmailTime = tmEquipmentResponsiblePerson.getLastEmailTime()==null?null:tmEquipmentResponsiblePerson.getLastEmailTime().getTime();//上次邮件通知时间
					if(lastEmailTime==null||lastEmailTime<startTimeOfToday){
						mail.setTo(tmEquipmentResponsiblePerson.getUser().getEmail());
						String content = globalConfigurationService.getValueByKey(ConstantUtils.EQUIPMENT_MAINTENANCE_EAMIL_TEXT);
						Object arr[] = { tmEquipmentResponsiblePerson.getUser().getName(),
								tmEquipmentMaintenance.getEquipment().getName() };
						content = MessageFormat.format(content, arr);
						mail.setText(content);
						WebSocket pushMessage =  new WebSocket();
						pushMessage.sendMsg(tmEquipmentResponsiblePerson.getTsUserId(),content);
						//保存数据到邮件表
						mailService.doSave(mail);
						//更新维护表中的上次邮件通知时间
						tmEquipmentResponsiblePerson.setLastEmailTime(new Date());
						tmEquipmentResponsiblePersonService.doUpdate(tmEquipmentResponsiblePerson);
					}
				}
				
			} catch (Exception e) {
				
			}

		}*/
	}
	
/*	//获取一天的开始时间
	private static Date getStartTime() {  
        Calendar todayStart = Calendar.getInstance();  
        todayStart.set(Calendar.HOUR, 0);  
        todayStart.set(Calendar.MINUTE, 0);  
        todayStart.set(Calendar.SECOND, 0);  
        todayStart.set(Calendar.MILLISECOND, 0);  
        return todayStart.getTime();  
    }  */
	
	//获取一天的结束时间
    /*private static Date getEndTime() {  
        Calendar todayEnd = Calendar.getInstance();  
        todayEnd.set(Calendar.HOUR, 23);  
        todayEnd.set(Calendar.MINUTE, 59);  
        todayEnd.set(Calendar.SECOND, 59);  
        todayEnd.set(Calendar.MILLISECOND, 999);  
        return todayEnd.getTime();  
    }  */
}
