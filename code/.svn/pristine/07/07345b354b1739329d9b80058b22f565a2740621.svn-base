package com.wis.mes.master.path.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.path.dao.TmPathUlocParameterDao;
import com.wis.mes.master.path.entity.TmPathUlocParameter;

@Repository
public class TmPathUlocParameterDaoImpl extends BaseDaoImpl<TmPathUlocParameter> implements TmPathUlocParameterDao {

	@Override
	public PageResult<TsParameter> getParameterPageResult(QueryCriteria criteria) {
		PageResult<TsParameter> pageResult = new PageResult<TsParameter>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getParameterCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TsParameter> content = getSqlSession().selectList("PathMapper.getParameterPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getParameterCount(Map<String, Object> params) {
		return getSqlSession().selectOne("PathMapper.getParameterCount", params);
	}
	
	private int getParameterCount(Map<String, Object> params,String pathMapper) {
		return getSqlSession().selectOne(pathMapper, params);
	}

	@Override
	public PageResult<Map<String, Object>> pagePathUlocParamList(QueryCriteria criteria) {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getParameterCount(params,"PathMapper.queryTmPathUlocParamCount"));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<Map<String, Object>> content = getSqlSession().selectList("PathMapper.queryTmPathUlocParamList", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	@Override
	public PageResult<Map<String, Object>> pageEquipmentByParameterList(QueryCriteria criteria) {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getParameterCount(params,"PathMapper.queryEquipmentByParameterCount"));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<Map<String, Object>> content = getSqlSession().selectList("PathMapper.queryEquipmentByParameter", params);
		pageResult.setContent(content);
		return pageResult;
	}

	@Override
	public List<TmPathUlocParameter> getTmPathUlocIdsByParameterList(Map<String, Object> params) {
		return getSqlSession().selectList("PathMapper.queryTmPathUlocIdsByParameterList", params);
	}

}
