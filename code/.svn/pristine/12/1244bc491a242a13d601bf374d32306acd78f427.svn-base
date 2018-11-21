package com.wis.mes.production.metalplate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.metalplate.dao.TbMetalPlateDefectiveRecordDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateDefectiveRecord;
import com.wis.mes.production.metalplate.service.TbMetalPlateDefectiveRecordService;

@Service
public class TbMetalPlateDefectiveRecordServiceImpl extends BaseServiceImpl<TbMetalPlateDefectiveRecord>
		implements TbMetalPlateDefectiveRecordService {

	@Autowired
	public void setDao(TbMetalPlateDefectiveRecordDao dao) {
		this.dao = dao;
	}

	@Override
	public Integer findFinishedNumberBySourceDataId(Integer sourceDataId) {
		return ((TbMetalPlateDefectiveRecordDao) dao).findFinishedNumberBySourceDataId(sourceDataId);
	}

}
