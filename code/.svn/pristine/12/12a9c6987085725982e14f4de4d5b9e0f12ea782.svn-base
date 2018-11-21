package com.wis.mes.worktime;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;

import net.sf.json.JSONObject;

public class WorkTimeTest extends BizBaseTestCase{

	@Autowired
	private TmWorktimeService worktimeService;
	
	@Test
	public void sendPlcWorkingCalendar(){
		Map<String, Object> sendPlcWorkingCalendar = worktimeService.sendPlcWorkingCalendar();
		System.err.println(JSONObject.fromObject(sendPlcWorkingCalendar).toString());
	}
	
	@Test
	public void checkWorkTimeMain(){
		worktimeService.checkWorkTimeMain();
	}
}
