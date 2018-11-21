package com.wis.mes.production.pmc.dao;

import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.production.pmc.entity.TbPmc;

public interface TbPmcDao extends BaseDao<TbPmc> {
	
	public PageResult<Map<String, Object>> getPageTbPmc(BootstrapTableQueryCriteria criteria);
}
