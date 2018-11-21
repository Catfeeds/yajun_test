package com.wis.mes.master.equipment.dao;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.base.dao.MasterBaseDao;
import com.wis.mes.master.equipment.entity.TmEquipmentMaterials;

public interface TmEquipmentMaterialsDao extends MasterBaseDao<TmEquipmentMaterials> {

	PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria);
}
