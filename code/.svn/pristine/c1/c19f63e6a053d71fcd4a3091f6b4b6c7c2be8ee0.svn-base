package com.wis.mes.production.producttracing.dao;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.producttracing.entity.TbProductTracing;

public interface TbProductTracingDao extends BaseDao<TbProductTracing> {

	String getEgModelName(String backNumber);

	void doUpdatePlcRfidStatus(String sn, String readWriteFlag);

	String readWriteFlag(String type);

	void updateFinshTime(String sn);

	TbProductTracing getTbProductTracingSn(String sn);
	
	void updateUnhealthyCount(String sn);
}
