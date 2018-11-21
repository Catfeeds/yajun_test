package com.wis.mes.production.repair.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.repair.dao.TpRepairUlocParameterDao;
import com.wis.mes.production.repair.entity.TpRepairUlocParameter;
import com.wis.mes.production.repair.service.TpRepairUlocParameterService;

@Service
public class TpRepairUlocParameterServiceImpl extends BaseServiceImpl<TpRepairUlocParameter> implements TpRepairUlocParameterService {

	@Autowired
	public void setDao(TpRepairUlocParameterDao dao) {
		this.dao = dao;
	}

}
