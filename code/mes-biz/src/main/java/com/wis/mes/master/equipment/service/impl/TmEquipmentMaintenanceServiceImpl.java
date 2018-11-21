package com.wis.mes.master.equipment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentMaintenanceDao;
import com.wis.mes.master.equipment.entity.TmEquipmentMaintenance;
import com.wis.mes.master.equipment.service.TmEquipmentMaintenanceService;

@Service
public class TmEquipmentMaintenanceServiceImpl extends MasterBaseServiceImpl<TmEquipmentMaintenance> implements TmEquipmentMaintenanceService {

	@Autowired
	public void setDao(TmEquipmentMaintenanceDao dao) {
		this.dao = dao;
	}


}
