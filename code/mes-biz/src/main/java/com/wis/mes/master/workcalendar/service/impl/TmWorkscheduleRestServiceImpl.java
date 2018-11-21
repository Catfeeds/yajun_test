package com.wis.mes.master.workcalendar.service.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.workcalendar.dao.TmWorkscheduleRestDao;
import com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleRestService;

@Service
public class TmWorkscheduleRestServiceImpl extends BaseServiceImpl<TmWorkscheduleRest> implements TmWorkscheduleRestService {

	@Autowired
	public void setDao(TmWorkscheduleRestDao dao) {
		this.dao = dao;
	}

	@Override
	public Map<String, Object> checkTimeOverlap(Integer tmWorkscheduleId, String startTime, String endTime,
			String workscheduleStartTime, String workscheduleEndTime)
			throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		List<TmWorkscheduleRest> workschRestTimes = ((TmWorkscheduleRestDao) dao).getTimeList(tmWorkscheduleId,
				startTime, endTime, workscheduleStartTime, workscheduleEndTime);
		if (workschRestTimes != null && workschRestTimes.size() != 0) {
			result.put("isExisted", "true");
			result.put("message", "不能与已有的休息时间段重合！");
		} else {
			result.put("isExisted", "false");
		}
		return result;
	}

}
