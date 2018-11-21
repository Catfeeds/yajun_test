package com.wis.mes.production.metalplate.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSchedul;

public interface TbMetalPlateSchedulDao extends BaseDao<TbMetalPlateSchedul> {
	TbMetalPlateSchedul findRelevanceById(Map<String, Object> params);

	Map<String,Object> findSchedulEgByPress(String press);

    public PageResult<Map<String,Object>> pmcReport(BootstrapTableQueryCriteria criteria);

	List<Map<String,Object>> queryPMCBoardProductionInfo();

	int queryBeProductionNumberByRegionMark(String regionMark);

	TbMetalPlateSchedul queryNextSchedulByRegionMark(String regionMark);
	
	int doUnLock(TbMetalPlateSchedul mps);
	int doLock(TbMetalPlateSchedul mps);
	int doMandatoryEnd(TbMetalPlateSchedul mps);
	
	void doWorkOrderStick(Map<String, Object> params);
}
