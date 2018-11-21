package com.wis.mes.production.plan.porder.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathSipDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;

@Repository
public class ToPorderAviPathSipDaoImpl extends BaseDaoImpl<ToPorderAviPathSip> implements ToPorderAviPathSipDao {

	@Override
	public PageResult<TsParameter> getEquipmentAndUlocParameter(QueryCriteria criteria) {
		PageResult<TsParameter> pageResult = new PageResult<TsParameter>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getEquipmentAndUlocParameterCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((offsetIndex + pageSize) / pageSize);
		}
		List<TsParameter> content = getSqlSession().selectList("AviPathMapper.getEquipmentAndUlocParameter", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getEquipmentAndUlocParameterCount(Map<String, Object> params) {
		return getSqlSession().selectOne("AviPathMapper.getEquipmentAndUlocParameterCount", params);
	}

	@Override
	public List<TsParameter> getSipParameterExamineAndNotInSip(Integer toPorderAviPathId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("toPorderAviPathId", toPorderAviPathId);
		map.put("notIN", "YES");
		return getSqlSession().selectList("AviPathMapper.getSipParameterExamine", map);
	}

	@Override
	public List<TsParameter> getSipParameterExamine(Integer toPorderAviPathId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("toPorderAviPathId", toPorderAviPathId);
		return getSqlSession().selectList("AviPathMapper.getSipParameterExamine", map);
	}
}
