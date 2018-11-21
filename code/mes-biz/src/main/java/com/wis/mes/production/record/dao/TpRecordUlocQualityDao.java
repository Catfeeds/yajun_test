package com.wis.mes.production.record.dao;

import java.util.List;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.production.record.entity.TpRecordUlocQuality;

public interface TpRecordUlocQualityDao extends BaseDao<TpRecordUlocQuality> {
	/**
	 * 查询工位已经绑定的质量项
	 * 
	 * @param SN
	 * @param ulocNo
	 * @param routingSeq
	 * @return
	 */
	List<TpRecordUlocQuality> getUlocAlreadyBindQuality(String SN, Integer ulocId, String routingSeq);

	/**
	 * 产品档案质检项信息
	 * 
	 * @author zhaoxianquan
	 * @param criteria
	 * @return
	 */
	public PageResult<TpRecordUlocQuality> getQualityPageResult(BootstrapTableQueryCriteria criteria);
}
