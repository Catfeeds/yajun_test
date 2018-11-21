package com.wis.mes.production.pmc.service;

import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.production.pmc.entity.TbPmc;
import com.wis.mes.util.ExportPageResult;

public interface TbPmcService extends BaseService<TbPmc> ,ExportPageResult<TbPmc>{
	
	public PageResult<Map<String, Object>> getPageTbPmc(BootstrapTableQueryCriteria criteria);
}
