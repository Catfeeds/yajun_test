package com.wis.mes.production.record.service;

import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecordUloc;

public interface TpRecordUlocService extends BaseService<TpRecordUloc> {
	/**
	 * 保存产品档案站点信息
	 * 
	 * @param tpRecordId
	 * @param ulocNo
	 * @param ulocName
	 * @param nextUloc
	 * @param shiftNo
	 * @param operationType
	 * @param routingSeq
	 * @param note
	 * @return
	 */
	TpRecordUloc doSaveTpRecordUloc(Integer tpRecordId, Integer tmUlocId, String ulocNo, String ulocName,
			String nextUloc, String shiftNo, String operationType, String routingSeq, String note);

}
