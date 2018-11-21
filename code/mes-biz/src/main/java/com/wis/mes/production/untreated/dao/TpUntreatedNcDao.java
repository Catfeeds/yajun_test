package com.wis.mes.production.untreated.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.untreated.entity.TpUntreatedNc;

public interface TpUntreatedNcDao extends BaseDao<TpUntreatedNc> {

	void changeWipStatus(List<TpUntreatedNc> untreatedList, String productWipStatusWaitRepair,
			String productWipStatusProduct); 
}
