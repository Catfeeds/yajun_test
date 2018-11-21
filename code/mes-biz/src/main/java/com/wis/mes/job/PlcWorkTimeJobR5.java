package com.wis.mes.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.opc.util.OpcHelper;
import com.wis.mes.util.StringUtil;

@Component(value = "plcWorkTimeJobR5")
public class PlcWorkTimeJobR5 {

	@Autowired
	private TmWorktimeService worktimeService;

	public void execue() {
		Map<String, Object> workTime = worktimeService.sendPlcWorkingCalendar();
		if (workTime.isEmpty()) {
			String[] items = new String[] { "Channel3.L72_6.Setting_Real.R5_Running" };
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("R5_Running", false);
			OpcHelper.sendDataToOpc(items, map);
			return;
		}
		String currentShiftno = workTime.get("currentShiftno").toString();// 当前班次
		if (StringUtil.isNotEmpty(currentShiftno)) {
			String restCalendar = workTime.get("Rest_Calendar").toString();// 休息时间
			String produceCalendar = workTime.get("Produce_Calendar").toString();// 工作时间
			Integer daulyPlan = Integer.valueOf(workTime.get("Dauly_Plan").toString());// 日计划
			if (!ConstantUtils.SHIFT_MORNING.equals(currentShiftno)) {
				String[] items = new String[] { "Channel3.L72_6.Setting_Real.HMI_S_Daily_Plan",
						"Channel3.L72_6.Setting_Real.HMI_S_Produce_Calendar1",
						"Channel3.L72_6.Setting_Real.HMI_S_Rest_Calendar1", "Channel3.L72_6.Setting_Real.R5_Running" };
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("HMI_S_Daily_Plan", daulyPlan);
				map.put("HMI_S_Produce_Calendar1", produceCalendar);
				map.put("HMI_S_Rest_Calendar1", restCalendar);
				map.put("R5_Running", true);
				OpcHelper.sendDataToOpc(items, map);
			} else {
				String[] items = new String[] { "Channel3.L72_6.Setting_Real.HMI_S_Daily_Plan",
						"Channel3.L72_6.Setting_Real.HMI_S_Produce_Calendar",
						"Channel3.L72_6.Setting_Real.HMI_S_Rest_Calendar", "Channel3.L72_6.Setting_Real.R5_Running" };
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("HMI_S_Daily_Plan", daulyPlan);
				map.put("HMI_S_Produce_Calendar", produceCalendar);
				map.put("HMI_S_Rest_Calendar", restCalendar);
				map.put("R5_Running", true);
				OpcHelper.sendDataToOpc(items, map);
			}
		}
	}
}
