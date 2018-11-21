package com.wis.mes.production.record.dao;

import java.util.List;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.production.record.entity.TpRecordUlocParameter;

public interface TpRecordUlocParameterDao extends BaseDao<TpRecordUlocParameter> {
	/**
	 * 查询工位上已经绑定的参数
	 * 
	 * @param SN
	 * @param ulocNo
	 * @param routingSeq
	 * @return
	 */
	List<TpRecordUlocParameter> getUlocAlreadyBindParameter(String SN, Integer ulocId, String routingSeq);

	/**
	 * 产品档案参数信息
	 * 
	 * @author zhaoxianquan
	 * @param criteria
	 * @return
	 */
	public PageResult<TpRecordUlocParameter> getParameterPageResult(BootstrapTableQueryCriteria criteria);
}
