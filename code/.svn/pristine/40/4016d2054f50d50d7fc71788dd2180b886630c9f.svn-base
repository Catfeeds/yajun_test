package com.wis.mes.production.wip.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.wip.dao.TpWipDao;
import com.wis.mes.production.wip.entity.TpWip;
import com.wis.mes.production.wip.service.TpWipService;

@Service
public class TpWipServiceImpl extends BaseServiceImpl<TpWip> implements TpWipService {

	@Autowired
	public void setDao(TpWipDao dao) {
		this.dao = dao;
	}

	@Override
	public TpWip getTpWip(String SN, Integer tmUlocId, String routingSeq) {
		TpWip wip = new TpWip();
		wip.setSn(SN);
		wip.setTmUlocId(tmUlocId);
		wip.setRoutingSeq(routingSeq);
		return findUniqueByEg(wip);
	}

}
