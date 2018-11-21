package com.wis.mes.production.plan.porder.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;

public interface ToPorderAviPathDao extends BaseDao<ToPorderAviPath> {

	/**
	 * 查询生产序列下不是下线点的数据
	 * 
	 * @param aviId
	 * @return
	 */
	public List<Map<String, Object>> selectNotOfflineDataByAviId(Integer aviId);

	/**
	 * 根据生产序列工艺路径id查询工单所在的工厂
	 * 
	 * @param toPorderAviPathId
	 * @return
	 */
	Integer getPorderPlantIdByAviPathId(Integer toPorderAviPathId);
}
