package com.wis.mes.master.workcalendar.service;

import java.text.ParseException;
import java.util.Map;

import com.wis.core.service.BaseService;
import com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest;

public interface TmWorkscheduleRestService extends BaseService<TmWorkscheduleRest> {

	// 检测时间段是否重叠
	Map<String, Object> checkTimeOverlap(Integer tmWorkscheduleId, String startTime, String endTime,
			String workscheduleStartTime, String workscheduleEndTime)
			throws ParseException;

}
