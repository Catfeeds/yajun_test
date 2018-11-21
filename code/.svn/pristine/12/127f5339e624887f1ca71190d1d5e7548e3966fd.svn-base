package com.wis.mes.production.record.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.record.dao.TpRecordUlocScrapDao;
import com.wis.mes.production.record.entity.TpRecordUlocScrap;
import com.wis.mes.production.record.service.TpRecordUlocScrapService;

@Service
public class TpRecordUlocScrapServiceImpl extends BaseServiceImpl<TpRecordUlocScrap> implements TpRecordUlocScrapService {

	@Autowired
	public void setDao(TpRecordUlocScrapDao dao) {
		this.dao = dao;
	}

	@Override
	public PageResult<TpRecordUlocScrap> getScrapPageResult(BootstrapTableQueryCriteria criteria) {
		return ((TpRecordUlocScrapDao) dao).getScrapPageResult(criteria);
	}

}
