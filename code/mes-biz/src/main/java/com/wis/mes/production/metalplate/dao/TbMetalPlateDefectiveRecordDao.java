package com.wis.mes.production.metalplate.dao;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateDefectiveRecord;

public interface TbMetalPlateDefectiveRecordDao extends BaseDao<TbMetalPlateDefectiveRecord> {
	Integer findFinishedNumberBySourceDataId(Integer sourceDataId);
}
