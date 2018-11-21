package com.wis.mes.production.metalplate.service;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSchedul;

import java.util.Map;

public interface TbMetalPlateSchedulService extends
		BaseService<TbMetalPlateSchedul> {
	TbMetalPlateSchedul findRelevanceById(Map<String, Object> params);
	
	Map<String,Object> findSchedulEgByPress(String press);

    PageResult<Map<String,Object>> pmcReport(BootstrapTableQueryCriteria criteria);

	void pmcBoardProductionInfo();

	void mpregionMarkMonitor();
	
	void doUnLock(TbMetalPlateSchedul mps);
	void doLock(TbMetalPlateSchedul mps);
	void doMandatoryEnd(TbMetalPlateSchedul mps);
	/**
	 * @author yajun_ren
	 * @desc 工单列表  工单置顶
	 * @param params
	 * 
	 * **/
	void doWorkOrderStick(Map<String, Object> params);
}
