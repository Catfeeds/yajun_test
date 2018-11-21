package com.wis.mes.master.equipment.dao;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.base.dao.MasterBaseDao;
import com.wis.mes.master.equipment.entity.TmEquipmentResponsiblePerson;

public interface TmEquipmentResponsiblePersonDao extends MasterBaseDao<TmEquipmentResponsiblePerson> {

	PageResult<?> getUserPageResult(QueryCriteria criteria);
}
