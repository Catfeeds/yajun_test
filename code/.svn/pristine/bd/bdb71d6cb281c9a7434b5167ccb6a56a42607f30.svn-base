package com.wis.mes.production.metalplate.dao.impl;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.metalplate.dao.TbMetalPlateDefectiveRecordDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateDefectiveRecord;

@Repository
public class TbMetalPlateDefectiveRecordDaoImpl extends BaseDaoImpl<TbMetalPlateDefectiveRecord>
		implements TbMetalPlateDefectiveRecordDao {

	@Override
	public Integer findFinishedNumberBySourceDataId(Integer sourceDataId) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findFinishedNumberBySourceDataId", sourceDataId);
	}

}
