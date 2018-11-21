package com.wis.mes.production.record.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.record.dao.TpRecordUlocQualityDao;
import com.wis.mes.production.record.entity.TpRecordUlocQuality;

@Repository
public class TpRecordUlocQualityDaoImpl extends BaseDaoImpl<TpRecordUlocQuality> implements TpRecordUlocQualityDao {

	@Override
	public List<TpRecordUlocQuality> getUlocAlreadyBindQuality(String SN, Integer ulocId, String routingSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SN", SN);
		param.put("ulocId", ulocId);
		param.put("routingSeq", routingSeq);
		return getSqlSession().selectList("TpRecordMapper.getUlocAlreadyBindQuality", param);
	}

	@Override
	public PageResult<TpRecordUlocQuality> getQualityPageResult(BootstrapTableQueryCriteria criteria) {
		PageResult<TpRecordUlocQuality> pageResult = new PageResult<TpRecordUlocQuality>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getQualityCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((offsetIndex + pageSize) / pageSize);
		}
		List<TpRecordUlocQuality> content = getSqlSession().selectList("TpRecordMapper.getQualityPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getQualityCount(Map<String, Object> params) {
		return getSqlSession().selectOne("TpRecordMapper.getQualityCount", params);
	}
}
