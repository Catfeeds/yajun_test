package com.wis.mes.production.metalplate.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSourceData;

public interface TbMetalPlateSourceDataDao extends BaseDao<TbMetalPlateSourceData> {

	List<TbMetalPlateSourceData> findByCollectEg(TbMetalPlateSourceData eg);

	Integer findBySumPlannedProductionByModel(TbMetalPlateSourceData eg);
	
	TbMetalPlateSourceData findSourceDataByRecordId(Integer recordId);
	
	Integer findConsumeApcTotalBySourceDataId(Integer sourceDataId);
	
	Integer findInProductionNumBySourceDataId(Integer sourceDataId);

	Integer findInventoryNumberByDate(Map<String, String> query);
}
