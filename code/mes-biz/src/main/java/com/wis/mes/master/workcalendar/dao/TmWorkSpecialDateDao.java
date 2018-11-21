package com.wis.mes.master.workcalendar.dao;

import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.workcalendar.entity.TmWorkSpecialDate;

public interface TmWorkSpecialDateDao extends BaseDao<TmWorkSpecialDate> {
	public PageResult<TmWorkSpecialDate> getPageTmWorkSpecialDate(QueryCriteria queryCriteria);
}
