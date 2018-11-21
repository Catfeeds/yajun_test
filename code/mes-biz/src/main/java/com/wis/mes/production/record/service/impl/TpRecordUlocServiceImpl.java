package com.wis.mes.production.record.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.record.dao.TpRecordUlocDao;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.record.service.TpRecordUlocService;

@Service
public class TpRecordUlocServiceImpl extends BaseServiceImpl<TpRecordUloc> implements TpRecordUlocService {

	@Autowired
	public void setDao(TpRecordUlocDao dao) {
		this.dao = dao;
	}

	@Override
	public TpRecordUloc doSaveTpRecordUloc(Integer tpRecordId, Integer tmUlocId, String ulocNo, String ulocName,
			String nextUloc, String shiftNo, String operationType, String routingSeq, String note) {
		TpRecordUloc bean = new TpRecordUloc();
		bean.setTpRecordId(tpRecordId);
		bean.setTmUlocId(tmUlocId);
		bean.setUlocNo(ulocNo);
		bean.setUlocName(ulocName);
		bean.setNextUloc(nextUloc);
		bean.setShiftNo(shiftNo);
		bean.setOperationType(operationType);
		bean.setRoutingSeq(routingSeq);
		bean.setNote(note);
		return doSave(bean);
	}

}
