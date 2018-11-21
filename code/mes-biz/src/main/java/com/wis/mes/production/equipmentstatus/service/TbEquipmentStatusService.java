package com.wis.mes.production.equipmentstatus.service;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.production.equipmentstatus.entity.TbEquipmentStatus;
import com.wis.mes.util.ExportPageResult;

public interface TbEquipmentStatusService extends BaseService<TbEquipmentStatus>,ExportPageResult<TbEquipmentStatus> {
	PageResult<TbEquipmentStatus> queryPageTbEquipmentStatus(QueryCriteria criteria);
}
