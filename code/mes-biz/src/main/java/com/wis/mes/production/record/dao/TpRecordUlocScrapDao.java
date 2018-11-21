package com.wis.mes.production.record.dao;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.production.record.entity.TpRecordUlocScrap;

public interface TpRecordUlocScrapDao extends BaseDao<TpRecordUlocScrap> {
	
	/**
	 * 产品档案报废信息
	 * @author zhaoxianquan
	 * @param criteria
	 * @return
	 */
	public PageResult<TpRecordUlocScrap> getScrapPageResult(BootstrapTableQueryCriteria criteria);
}
