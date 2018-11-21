package com.wis.mes.production.equipmentstatus.dao;

import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.production.equipmentstatus.entity.TbEquipmentStatus;

public interface TbEquipmentStatusDao extends BaseDao<TbEquipmentStatus> {
	PageResult<TbEquipmentStatus> queryPageTbEquipmentStatus(QueryCriteria criteria);
}
