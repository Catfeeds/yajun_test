package com.wis.mes.master.workcalendar.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.workcalendar.dao.TmWorkSpecialDateDao;
import com.wis.mes.master.workcalendar.entity.TmWorkSpecialDate;

@Repository
public class TmWorkSpecialDateDaoImpl extends BaseDaoImpl<TmWorkSpecialDate> implements TmWorkSpecialDateDao {

	@Override
	public PageResult<TmWorkSpecialDate> getPageTmWorkSpecialDate(QueryCriteria criteria) {
		PageResult<TmWorkSpecialDate> pageResult = new PageResult<TmWorkSpecialDate>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		Integer tmEquipmentId = (Integer) criteria.getQueryCondition().get("tmEquipmentId");
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getTmWorkSpecialDateCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", (pageSize * criteria.getPage()));
		params.put("tmEquipmentId", tmEquipmentId);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TmWorkSpecialDate> content = getSqlSession().selectList("WorkTimeMapper.getPageTmWorkSpecialDate", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getTmWorkSpecialDateCount(Map<String, Object> params) {
		return getSqlSession().selectOne("WorkTimeMapper.getTmWorkSpecialDateCount", params);
	}
}
