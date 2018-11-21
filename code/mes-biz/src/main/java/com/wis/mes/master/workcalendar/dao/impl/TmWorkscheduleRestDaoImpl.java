package com.wis.mes.master.workcalendar.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.utils.DateUtils;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.workcalendar.dao.TmWorkscheduleRestDao;
import com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest;

@Repository
public class TmWorkscheduleRestDaoImpl extends BaseDaoImpl<TmWorkscheduleRest> implements TmWorkscheduleRestDao {

	private SimpleDateFormat timeFormat = new SimpleDateFormat(DateUtils.FORMAT_TIME_HH_MM);

	@Override
	public List<TmWorkscheduleRest> getTimeList(Integer tmWorkscheduleId, String startTime, String endTime,
			String workscheduleStartTime, String workscheduleEndTime) throws ParseException {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("tmWorkscheduleId", tmWorkscheduleId);
		conditions.put("startTime", startTime);
		conditions.put("endTime", endTime);
		List<TmWorkscheduleRest> workschRestTimes = null;
		if (timeFormat.parse(workscheduleStartTime).before(timeFormat.parse(workscheduleEndTime))) {
			workschRestTimes = getSqlSession().selectList("WorkScheduleRestMapper.getTimeListMorOrNoon", conditions);
		} else {
			workschRestTimes = getSqlSession().selectList("WorkScheduleRestMapper.getTimeListNight", conditions);
		}
		return workschRestTimes;
	}
}
