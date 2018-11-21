package com.wis.mes.master.equipment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentAbnormalDao;
import com.wis.mes.master.equipment.entity.TmEquipmentAbnormal;
import com.wis.mes.master.equipment.service.TmEquipmentAbnormalService;

@Service
public class TmEquipmentAbnormalServiceImpl extends BaseServiceImpl<TmEquipmentAbnormal>
		implements TmEquipmentAbnormalService {

	@Autowired
	public void setDao(TmEquipmentAbnormalDao dao) {
		this.dao = dao;
	}
}
