package com.wis.mes.dakin.production.tracing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.dakin.production.tracing.dao.VbProductParametersDao;
import com.wis.mes.dakin.production.tracing.entity.VbProductParameters;
import com.wis.mes.dakin.production.tracing.service.VbProductParametersService;

@Service
public class VbProductParametersServiceImpl extends BaseServiceImpl<VbProductParameters>
		implements VbProductParametersService {

	@Autowired
	public void setDao(VbProductParametersDao dao) {
		this.dao = dao;
	}
}
