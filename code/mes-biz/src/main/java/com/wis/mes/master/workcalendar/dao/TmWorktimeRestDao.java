package com.wis.mes.master.workcalendar.dao;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.workcalendar.entity.TmWorktimeRest;

public interface TmWorktimeRestDao extends BaseDao<TmWorktimeRest> {
	String queryRestIdsByWorkTimeId(Integer[] workTimeIds);
}
