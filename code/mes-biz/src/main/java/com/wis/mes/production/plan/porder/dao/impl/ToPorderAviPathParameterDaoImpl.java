package com.wis.mes.production.plan.porder.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathParameterDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;

@Repository
public class ToPorderAviPathParameterDaoImpl extends BaseDaoImpl<ToPorderAviPathParameter>
		implements ToPorderAviPathParameterDao {

	@Override
	public PageResult<TsParameter> getParameterPageResult(QueryCriteria criteria) {
		PageResult<TsParameter> pageResult = new PageResult<TsParameter>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getParameterCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((offsetIndex + pageSize) / pageSize);
		}
		List<TsParameter> content = getSqlSession().selectList("AviPathMapper.getParameterPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getParameterCount(Map<String, Object> params) {
		return getSqlSession().selectOne("AviPathMapper.getParameterCount", params);
	}
}
