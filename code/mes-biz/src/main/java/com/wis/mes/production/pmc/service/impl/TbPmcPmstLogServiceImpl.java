package com.wis.mes.production.pmc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.pmc.dao.TbPmcPmstLogDao;
import com.wis.mes.production.pmc.entity.TbPmcPmstLog;
import com.wis.mes.production.pmc.service.TbPmcPmstLogService;

@Service
public class TbPmcPmstLogServiceImpl extends BaseServiceImpl<TbPmcPmstLog> implements TbPmcPmstLogService {

	@Autowired
	public void setDao(TbPmcPmstLogDao dao) {
		this.dao = dao;
	}

}
