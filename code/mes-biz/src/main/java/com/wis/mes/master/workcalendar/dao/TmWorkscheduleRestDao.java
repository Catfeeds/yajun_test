package com.wis.mes.master.workcalendar.dao;

import java.text.ParseException;
import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest;

public interface TmWorkscheduleRestDao extends BaseDao<TmWorkscheduleRest> {

	public List<TmWorkscheduleRest> getTimeList(Integer tmWorkscheduleId, String startTime, String endTime,
			String workscheduleStartTime, String workscheduleEndTime)
			throws ParseException;
}
