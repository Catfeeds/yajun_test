package com.wis.mes.production.metalplate.service;

import java.util.List;

import com.wis.core.service.BaseService;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSourceData;

public interface TbMetalPlateSourceDataService extends
		BaseService<TbMetalPlateSourceData> {

	List<TbMetalPlateSourceData> findByCollectEg(TbMetalPlateSourceData eg);

	Integer findBySumPlannedProductionByModel(TbMetalPlateSourceData eg);

	TbMetalPlateSourceData findSourceDataByRecordId(Integer recordId);
	
	Integer findConsumeApcTotalBySourceDataId(Integer sourceDataId);
	
	Integer findInProductionNumBySourceDataId(Integer sourceDataId);

	public void apc21SourceDataAcquire();
	
	public Integer getInventoryNumber(String model, String date);
}
