package com.wis.mes.master.path.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.path.dao.TmPathUlocSipDao;
import com.wis.mes.master.path.entity.TmPathUlocSip;

@Repository
public class TmPathUlocSipDaoImpl extends BaseDaoImpl<TmPathUlocSip> implements TmPathUlocSipDao {
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
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TsParameter> content = getSqlSession().selectList("PathMapper.getEquipmentAndUlocParameter", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getEquipmentAndUlocParameterCount(Map<String, Object> params) {
		return getSqlSession().selectOne("PathMapper.getEquipmentAndUlocParameterCount", params);
	}

	@Override
	public List<TsParameter> getSipParameterExamineAndNotInSip(Integer tmPathUlocId, Integer tmUlocId,
			String[] tsParameterIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tmPathUlocId", tmPathUlocId);
		map.put("tmUlocId", tmUlocId);
		map.put("tsParameterIds", tsParameterIds);
		map.put("notIN", "YES");
		return getSqlSession().selectList("PathMapper.getSipParameterExamine", map);
	}

	@Override
	public List<TsParameter> getSipParameterExamine(Integer tmPathUlocId, Integer tmUlocId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tmPathUlocId", tmPathUlocId);
		map.put("tmUlocId", tmUlocId);
		return getSqlSession().selectList("PathMapper.getSipParameterExamine", map);
	}
}
