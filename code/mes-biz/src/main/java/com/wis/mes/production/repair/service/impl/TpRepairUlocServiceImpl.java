package com.wis.mes.production.repair.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.repair.dao.TpRepairUlocDao;
import com.wis.mes.production.repair.entity.TpRepairUloc;
import com.wis.mes.production.repair.service.TpRepairUlocService;

@Service
public class TpRepairUlocServiceImpl extends BaseServiceImpl<TpRepairUloc> implements TpRepairUlocService {

	@Autowired
	public void setDao(TpRepairUlocDao dao) {
		this.dao = dao;
	}

}
