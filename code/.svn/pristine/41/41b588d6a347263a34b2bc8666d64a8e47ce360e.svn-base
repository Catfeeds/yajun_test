package com.wis.mes.production.producttracing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.producttracing.dao.TbProductTracingDao;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductTracingService;

@Service
public class TbProductTracingServiceImpl extends BaseServiceImpl<TbProductTracing> implements TbProductTracingService {

	@Autowired
	public void setDao(TbProductTracingDao dao) {
		this.dao = dao;
	}

	@Override
	public String getEgModelName(String backNumber) {
		return ((TbProductTracingDao) dao).getEgModelName(backNumber);
	}

	@Override
	public void doUpdatePlcRfidStatus(String sn, String readWriteFlag) {
		((TbProductTracingDao) dao).doUpdatePlcRfidStatus(sn, readWriteFlag);
	}

	@Override
	public String readWriteFlag(String type) {
		return ((TbProductTracingDao) dao).readWriteFlag(type);
	}


	@Override
	public void updateFinshTime(String sn) {
		((TbProductTracingDao) dao).updateFinshTime(sn);
	}

	@Override
	public TbProductTracing getTbProductTracingSn(String sn) {
		return ((TbProductTracingDao) dao).getTbProductTracingSn(sn);
	}

	@Override
	public void updateUnhealthyCount(String sn) {
		((TbProductTracingDao) dao).updateUnhealthyCount(sn);
	}
}
