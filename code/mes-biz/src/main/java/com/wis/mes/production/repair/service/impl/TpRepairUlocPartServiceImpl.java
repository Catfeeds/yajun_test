package com.wis.mes.production.repair.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.repair.dao.TpRepairUlocPartDao;
import com.wis.mes.production.repair.entity.TpRepairUlocPart;
import com.wis.mes.production.repair.service.TpRepairUlocPartService;

@Service
public class TpRepairUlocPartServiceImpl extends BaseServiceImpl<TpRepairUlocPart> implements TpRepairUlocPartService {

	@Autowired
	public void setDao(TpRepairUlocPartDao dao) {
		this.dao = dao;
	}

}
