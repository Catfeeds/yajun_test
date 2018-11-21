package com.wis.mes.production.record.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.record.dao.TpRecordUlocScrapDao;
import com.wis.mes.production.record.entity.TpRecordUlocScrap;

@Repository
public class TpRecordUlocScrapDaoImpl extends BaseDaoImpl<TpRecordUlocScrap> implements TpRecordUlocScrapDao {

	@Override
	public PageResult<TpRecordUlocScrap> getScrapPageResult(BootstrapTableQueryCriteria criteria) {
		PageResult<TpRecordUlocScrap> pageResult = new PageResult<TpRecordUlocScrap>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getScrapCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TpRecordUlocScrap> content = getSqlSession().selectList("TpRecordMapper.getScrapPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	private int getScrapCount(Map<String, Object> params) {
		return getSqlSession().selectOne("TpRecordMapper.getScrapCount", params);
	}
}
