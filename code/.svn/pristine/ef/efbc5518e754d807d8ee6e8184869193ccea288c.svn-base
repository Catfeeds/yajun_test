package com.wis.mes.production.record.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.record.dao.TpRecordUlocPartDao;
import com.wis.mes.production.record.entity.TpRecordUlocPart;

@Repository
public class TpRecordUlocPartDaoImpl extends BaseDaoImpl<TpRecordUlocPart> implements TpRecordUlocPartDao {

	@Override
	public List<TpRecordUlocPart> getUlocAlreadyBindPart(String SN, Integer ulocId, String routingSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SN", SN);
		param.put("ulocId", ulocId);
		param.put("routingSeq", routingSeq);
		return getSqlSession().selectList("TpRecordMapper.getUlocAlreadyBindPart", param);
	}

	@Override
	public PageResult<TpRecordUlocPart> getPartPageResult(QueryCriteria criteria) {
		PageResult<TpRecordUlocPart> pageResult = new PageResult<TpRecordUlocPart>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getPartCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((offsetIndex + pageSize) / pageSize);
		}
		List<TpRecordUlocPart> content = getSqlSession().selectList("TpRecordMapper.getPartPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getPartCount(Map<String, Object> params) {
		return getSqlSession().selectOne("TpRecordMapper.getPartCount", params);
	}
}
