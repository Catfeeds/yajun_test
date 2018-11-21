package com.wis.mes.production.metalplate.service;

import com.wis.core.service.BaseService;
import com.wis.mes.production.metalplate.entity.TbMetalPlateDefectiveRecord;

public interface TbMetalPlateDefectiveRecordService extends
		BaseService<TbMetalPlateDefectiveRecord> {
	
	Integer findFinishedNumberBySourceDataId(Integer sourceDataId);
}
