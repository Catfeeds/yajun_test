package com.wis.mes.production.pmc.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.pmc.dao.TbPmcDao;
import com.wis.mes.production.pmc.entity.TbPmc;
import com.wis.mes.util.StringUtil;

@Repository
public class TbPmcDaoImpl extends BaseDaoImpl<TbPmc> implements TbPmcDao {

	@Override
	public PageResult<Map<String, Object>> getPageTbPmc(BootstrapTableQueryCriteria criteria) {
		PageResult<Map<String,Object>> pageResult = new PageResult<Map<String,Object>>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getUserCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if(StringUtil.isNotEmpty(criteria.getQueryCondition().get("productModel"))){
			params.put("productModel",criteria.getQueryCondition().get("productModel"));
		}
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<Map<String, Object>> content = getSqlSession().selectList("PmcMapper.queryPageTbPmc", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	private int getUserCount(Map<String, Object> params) {
		return getSqlSession().selectOne("PmcMapper.queryPageTbPmcCount", params);
	}
	
}
