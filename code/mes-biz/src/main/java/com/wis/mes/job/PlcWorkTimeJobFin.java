package com.wis.mes.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.opc.util.OpcHelper;
import com.wis.mes.util.StringUtil;

@Component(value = "plcWorkTimeJobFin")
public class PlcWorkTimeJobFin {
	@Autowired
	private TmWorktimeService worktimeService;

	public void execue() {
		Map<String, String> workTime = worktimeService.sendFinPlcWorkingCalendar();
		if (workTime.isEmpty()) {
			return;
		}
		String daySet = StringUtil.getString(workTime.get("daySet"));
		String netSet = StringUtil.getString(workTime.get("netSet"));
		StringBuffer rest = new StringBuffer(StringUtil.getString(workTime.get("rest")));
		Map<String, Object> map = new HashMap<String, Object>();
		String[] items = new String[] { "Channel4.L33.Setting.Day_Night.Day_Set",
				"Channel4.L33.Setting.Day_Night.Night_Set" };
		map.put("Day_Set", daySet);
		map.put("Night_Set", netSet);
		OpcHelper.sendDataToOpc(items, map);
		items = new String[] { "Channel4.L33.Setting.Rest_Setting.Rest1", "Channel4.L33.Setting.Rest_Setting.Rest2",
				"Channel4.L33.Setting.Rest_Setting.Rest3", "Channel4.L33.Setting.Rest_Setting.Rest4",
				"Channel4.L33.Setting.Rest_Setting.Rest5", "Channel4.L33.Setting.Rest_Setting.Rest6",
				"Channel4.L33.Setting.Rest_Setting.Rest7", "Channel4.L33.Setting.Rest_Setting.Rest8",
				"Channel4.L33.Setting.Rest_Setting.Rest9", "Channel4.L33.Setting.Rest_Setting.Rest10" };
		List<String> list = getRest(rest);
		map = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			map.put("Rest" + (i + 1), list.get(i));
		}
		OpcHelper.sendDataToOpc(items, map);
		/*
		items = new String[]{"Channel4.L33.Setting.Confirm_All_Input"};
		map = new HashMap<String, Object>();
		map.put("Confirm_All_Input", true);
		OpcHelper.sendDataToOpc(items, map);
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
		}
		map.put("Confirm_All_Input", false);
		OpcHelper.sendDataToOpc(items, map);
		*/
	}

	public List<String> getRest(StringBuffer str) {
		List<String> strs = new ArrayList<String>(10);
		while (str.length() > 0) {
			strs.add(str.substring(0, 8));
			str.delete(0, 8);
		}
		return strs;
	}
}
