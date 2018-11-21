package com.wis.mes.production.untreated.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.untreated.dao.TpUntreatedNcDao;
import com.wis.mes.production.untreated.entity.TpUntreatedNc;

@Repository
public class TpUntreatedNcDaoImpl extends BaseDaoImpl<TpUntreatedNc> implements TpUntreatedNcDao {

	@Override
	public void changeWipStatus(List<TpUntreatedNc> untreatedList, String productWipStatusWaitRepair,
			String productWipStatusProduct) {
		HashMap<String , Object> param = new HashMap<String , Object>();
		param.put("productWipStatusWaitRepair", productWipStatusWaitRepair);
		param.put("productWipStatusProduct", productWipStatusProduct);
		param.put("untreatedList", untreatedList);
		this.getSqlSession().update("UlocMapper.changeWipStatus", param);
	}
}
