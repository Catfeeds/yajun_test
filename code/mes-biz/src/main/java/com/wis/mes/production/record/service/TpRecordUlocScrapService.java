package com.wis.mes.production.record.service;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecordUlocScrap;

public interface TpRecordUlocScrapService extends BaseService<TpRecordUlocScrap> {

	/**
	 * 产品档案报废信息
	 * @author zhaoxianquan
	 * @param criteria
	 * @return
	 */
	public PageResult<TpRecordUlocScrap> getScrapPageResult(BootstrapTableQueryCriteria criteria);
}
