package com.wis.mes.master.workcalendar.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.workcalendar.dao.TmWorktimeDao;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

@Repository
public class TmWorktimeDaoImpl extends BaseDaoImpl<TmWorktime> implements TmWorktimeDao {
	@Override
	public String queryWorkTimeByParams(final Integer tmPlantId, final Integer tmWorkshopId, final Integer tmLineId,
			final String shiftno, final String[] workDate) {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("tmPlantId", tmPlantId);
		params.put("tmWorkshopId", tmWorkshopId);
		params.put("tmLineId", tmLineId);
		params.put("shiftno", shiftno);
		params.put("workDates", workDate);
		return getSqlSession().selectOne("WorkTimeMapper.queryWorkTimeByParams", params);
	}

	@Override
	public List<Map<String, Object>> findByCondition(final String startTime, final String endTime, final String isAll,
			final Integer currUser) {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("isAll", isAll);
		params.put("currUser", currUser);
		return getSqlSession().selectList("WorkTimeMapper.findByCondition", params);
	}

	@Override
	public Integer getWorkTime(Map<String, Object> map) {
		return (Integer) getSqlSession().selectOne("WorkTimeMapper.findByWorkTime", map);
	}

	@Override
	public TmWorktime thisWorkTime(Integer tmLineId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tmLineId", tmLineId);
		return getSqlSession().selectOne("WorkTimeMapper.thisWorkTime", parameters);
	}

	@Override
	public String breathingSpace(Integer tmWorktimeId, Integer rownum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tmWorktimeId", tmWorktimeId);
		parameters.put("rownum", rownum);
		return getSqlSession().selectOne("WorkTimeMapper.breathingSpace", parameters);
	}

	@Override
	public TmWorktime getNextFlight(String shiftno, Integer tmLineId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shiftno", shiftno);
		parameters.put("tmLineId", tmLineId);
		return getSqlSession().selectOne("WorkTimeMapper.getNextFlight", parameters);
	}

	@Override
	public TmWorktime getWorkTimeByDayAndShiftno(String queryDay, String lineNo, String shiftNo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("queryDay", queryDay);
		parameters.put("lineNo", lineNo);
		parameters.put("shiftNo", shiftNo);
		return getSqlSession().selectOne("WorkTimeMapper.getWorkTimeByDayAndShiftno", parameters);
	}

	@Override
	public TmWorktime getDateWorkTime(Date queryDate, String lineNo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("queryDate", queryDate);
		parameters.put("lineNo", lineNo);
		return getSqlSession().selectOne("WorkTimeMapper.getDateWorkTime", parameters);
	}

	@Override
	public TmWorktime lastDayWordkTime(String shiftno, Integer tmLineId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("shiftno", shiftno);
		parameters.put("tmLineId", tmLineId);
		return getSqlSession().selectOne("WorkTimeMapper.lastDayWordkTime", parameters);
	}

	@Override
	public List<TmWorktime> todayWorkTime(Integer tmLineId,String shiftNo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tmLineId", tmLineId);
		parameters.put("shiftNo", shiftNo);
		return getSqlSession().selectList("WorkTimeMapper.todayWorkTime", parameters);
	}
}
