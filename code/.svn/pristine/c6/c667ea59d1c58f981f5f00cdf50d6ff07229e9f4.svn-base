package com.wis.mes.master.workcalendar.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.workcalendar.dao.TmWorktimeRestDao;
import com.wis.mes.master.workcalendar.entity.TmWorktimeRest;

@Repository
public class TmWorktimeRestDaoImpl extends BaseDaoImpl<TmWorktimeRest> implements TmWorktimeRestDao {
	public String queryRestIdsByWorkTimeId(Integer[] workTimeIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tmWorkTimeId", workTimeIds);
		return getSqlSession().selectOne("WorkTimeRestMapper.queryRestIdsByWorkTimeId", params);
	}
}
