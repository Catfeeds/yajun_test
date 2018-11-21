package com.wis.mes.production.record.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.record.dao.TpRecordUlocParameterDao;
import com.wis.mes.production.record.entity.TpRecordUlocParameter;

@Repository
public class TpRecordUlocParameterDaoImpl extends BaseDaoImpl<TpRecordUlocParameter>
		implements TpRecordUlocParameterDao {

	@Override
	public List<TpRecordUlocParameter> getUlocAlreadyBindParameter(String SN, Integer ulocId, String routingSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SN", SN);
		param.put("ulocId", ulocId);
		param.put("routingSeq", routingSeq);
		return getSqlSession().selectList("TpRecordMapper.getUlocAlreadyBindParameter", param);
	}

	@Override
	public PageResult<TpRecordUlocParameter> getParameterPageResult(BootstrapTableQueryCriteria criteria) {
		PageResult<TpRecordUlocParameter> pageResult = new PageResult<TpRecordUlocParameter>();
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
		List<TpRecordUlocParameter> content = getSqlSession().selectList("TpRecordMapper.getParameterPageResult",
				params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getParameterCount(Map<String, Object> params) {
		return getSqlSession().selectOne("TpRecordMapper.getParameterCount", params);
	}
}
