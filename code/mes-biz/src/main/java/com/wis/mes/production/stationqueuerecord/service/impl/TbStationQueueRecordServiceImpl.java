package com.wis.mes.production.stationqueuerecord.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.stationqueuerecord.dao.TbStationQueueRecordDao;
import com.wis.mes.production.stationqueuerecord.entity.TbStationQueueRecord;
import com.wis.mes.production.stationqueuerecord.service.TbStationQueueRecordService;

@Service
public class TbStationQueueRecordServiceImpl extends BaseServiceImpl<TbStationQueueRecord> implements TbStationQueueRecordService {

	@Autowired
	public void setDao(TbStationQueueRecordDao dao) {
		this.dao = dao;
	}
}
