package com.wis.mes.production.untreated.service;

import java.util.List;

import com.wis.core.service.BaseService;
import com.wis.mes.production.untreated.entity.TpUntreatedNc;

public interface TpUntreatedNcService extends BaseService<TpUntreatedNc> {

	void doSaveUnTratedNc(String sn, Integer tmNcGroupId, Integer tmNcId, String ncType, String note, Integer tmUlocId,
			String rountingSeq);

	void doSaveUnTratedNc(String sn, Integer[] tmNcGroupId, Integer[] tmNcId, String[] ncType, String[] note,
			Integer tmUlocId, String rountingSeq);
	/**
	 *  TP_WIP 将状态为 “WAIT_REPAIR” 修改成 “PORDUCTION” 
	 * @param list
	 */
	void changeWipStatus(List<TpUntreatedNc> list); 

}
