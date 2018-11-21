package com.wis.mes.master.equipment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.equipment.entity.TmEquipmentMaintenance;
import com.wis.mes.master.equipment.service.TmEquipmentMaintenanceService;

@Controller
@RequestMapping(value = "/equipmentMaintenance")
public class TmEquipmentMaintenanceController extends BaseController {

	@Autowired
	private TmEquipmentMaintenanceService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria,Integer tmEquipmentId,
			ModelMap modelMap,String currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		TmEquipmentMaintenance tmEquipmentMaintenance = new TmEquipmentMaintenance();
		tmEquipmentMaintenance.setTmEquipmentId(tmEquipmentId);
		List<TmEquipmentMaintenance> maintenanceList = service.findByEg(tmEquipmentMaintenance);
		List<DictEntry> dictEntry = entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_MAINTENANCE_TIME_TYPE);// 时间保养周期类型
		if(maintenanceList.size()>0 && maintenanceList !=null){
			Date currentDate = new Date();//当前时间
			Date lastTime = maintenanceList.get(0).getLastMaintenanceTime();//上次保养时间
			long differTime = 0L;
			if(maintenanceList.get(0).getNextMaintenanceTime()!=null){
				if(lastTime==null){//判断是否是第一次
					differTime = (maintenanceList.get(0).getNextMaintenanceTime().getTime() - currentDate.getTime())/(60*60*1000);//剩余保养时间
					modelMap.addAttribute("remainderTime", differTime);
					modelMap.addAttribute("nextMaintenanceTime", maintenanceList.get(0).getNextMaintenanceTime());
				}else{
					Calendar nextTime =  new GregorianCalendar();  
					nextTime.setTime(lastTime);
					if(maintenanceList.get(0).getType().equalsIgnoreCase("year")){//年
						nextTime.set(Calendar.YEAR,nextTime.get(Calendar.YEAR)+maintenanceList.get(0).getTypeValue());
					}
					if(maintenanceList.get(0).getType().equalsIgnoreCase("month")){//月
						nextTime.set(Calendar.MONTH,nextTime.get(Calendar.MONTH)+maintenanceList.get(0).getTypeValue());
					}
					if(maintenanceList.get(0).getType().equalsIgnoreCase("week")){//周
						nextTime.set(Calendar.DAY_OF_MONTH,nextTime.get(Calendar.DAY_OF_MONTH)+7*(maintenanceList.get(0).getTypeValue()));
					}
					if(maintenanceList.get(0).getType().equalsIgnoreCase("day")){//天
						nextTime.set(Calendar.DAY_OF_MONTH,nextTime.get(Calendar.DAY_OF_MONTH)+maintenanceList.get(0).getTypeValue());
					}
						differTime = (nextTime.getTimeInMillis()-currentDate.getTime())/(60*60*1000);//剩余保养时间
						modelMap.addAttribute("remainderTime", differTime);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						modelMap.addAttribute("nextMaintenanceTime", sdf.format(new Date(nextTime.getTimeInMillis())));
				}
				}
			modelMap.addAttribute("type", maintenanceList.get(0).getType());
			modelMap.addAttribute("maintenance",maintenanceList.get(0));
		}else{
			modelMap.addAttribute("maintenance",tmEquipmentMaintenance);
			
		}
		modelMap.addAttribute("typeList",dictEntry);
		return new ModelAndView("/master/equipment/maintenance/maintenance_list");
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdateTimeAndCount")
	public JsonActionResult saveOrUpdateTimeAndCount(HttpServletResponse response,String beans) throws ParseException{
		JSONObject json = JSONObject.fromObject(beans);
		TmEquipmentMaintenance bean = getBean(JSONObject.fromObject(json));
		if (StringUtils.isNotBlank(json.getString("id"))) {
			TmEquipmentMaintenance before = service.findById(json.getInt("id"));
			if(before.getLastMaintenanceTime()!=null){
				bean.setLastMaintenanceTime(before.getLastMaintenanceTime());
			}
			if(bean.getCount()!=null){
				bean.setRemainderCount(bean.getCount());
			}
			service.doUpdate(bean);
			logService.doUpdateLog("TmEquipmentMaintenance", before, bean);
		} else {
			if(bean.getCount()!=null){
				bean.setRemainderCount(bean.getCount());
			}
			bean = service.doSave(bean);
			logService.doAddLog("TmEquipmentMaintenance", bean);
		}
		return ActionUtils.handleResult(response);
	}
	
	
	public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
        return mer.find();  
    } 
	
	
	private TmEquipmentMaintenance getBean(JSONObject json) throws ParseException {
		TmEquipmentMaintenance bean = new TmEquipmentMaintenance();
		if (StringUtils.isNotBlank(json.getString("id"))) {
			bean.setId(json.getInt("id"));
		}
		bean.setTmEquipmentId(json.getInt("tmEquipmentId"));
		
		if (StringUtils.isNotBlank(json.getString("type"))) {
			bean.setType(json.getString("type"));
		}
		if (StringUtils.isNotBlank(json.getString("typeValue"))) {
			bean.setTypeValue(json.getInt("typeValue"));
		}
		if (StringUtils.isNotBlank(json.getString("nextMaintenanceTime"))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			bean.setNextMaintenanceTime(sdf.parse(json.getString("nextMaintenanceTime")));
		}
		if (StringUtils.isNotBlank(json.getString("timeWarningValue"))) {
			bean.setTimeWarningValue(json.getInt("timeWarningValue"));
		}
		if (StringUtils.isNotBlank(json.getString("count"))) {
			bean.setCount(json.getInt("count"));
		}
		if (StringUtils.isNotBlank(json.getString("countWarningValue"))) {
			bean.setCountWarningValue(json.getInt("countWarningValue"));
		}
		return bean;
	}

	@ResponseBody
	@RequestMapping(value = "/updateLastMaintenanceTime")
	public JsonActionResult updateLastMaintenanceTime(HttpServletResponse response,Integer id){
		TmEquipmentMaintenance equipmentMaintenance  = service.findById(id);
		equipmentMaintenance.setLastMaintenanceTime(new Date());
		service.doUpdate(equipmentMaintenance);
		return ActionUtils.handleResult(response);
	}
	

}
