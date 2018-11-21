package com.wis.mes.production.plan.porder.dao;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.plan.porder.entity.ToPorder;

public interface ToPorderDao extends BaseDao<ToPorder> {

	/**
	 * 删除工单下的所有AVI信息
	 * 
	 * @param toPorderId
	 */
	public void deleteAviByPorderId(Integer toPorderId);
}
