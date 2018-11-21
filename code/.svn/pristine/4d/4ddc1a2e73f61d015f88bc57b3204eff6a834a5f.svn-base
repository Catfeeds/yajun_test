package com.wis.mes.production.plan.porder.dao;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;

public interface ToPorderAviDao extends BaseDao<ToPorderAvi> {
	/**
	 * 删除生产序列下的BOM和生产序列
	 * 
	 * @param aviId
	 */
	public void deleteAviBomAndPathByAviId(Integer aviId);

	/**
	 * 根据生产序列ID查询工单的工厂ID
	 * 
	 * @param aviId
	 * @return
	 */
	public Integer getPorderPlantIdByAviId(Integer aviId);

	/**
	 * 删除生产序列下的工艺路径的详细信息
	 * 
	 * @param aviId
	 */
	public void deleteAviPathDetailByAviId(Integer[] aviPathIds);

	/**
	 * 查询产线ID
	 * 
	 * @param tmPathId
	 * @return
	 */
	public Integer getLineId(Integer tmPathId);
}
