package com.wis.mes.production.producttracing.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.producttracing.dao.TbProductTracingDao;
import com.wis.mes.production.producttracing.entity.TbProductTracing;

@Repository
public class TbProductTracingDaoImpl extends BaseDaoImpl<TbProductTracing> implements TbProductTracingDao {

	@Override
	public String getEgModelName(String backNumber) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("backNumber", backNumber);
		String modelName = getSqlSession().selectOne("ProductTracingMapper.getEgModelName", params);
		return StringUtils.isNoneBlank(modelName)?modelName:"";
	}

	@Override
	public void doUpdatePlcRfidStatus(String sn,String readWriteFlag) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("sn", sn);
		params.put("readWriteFlag", readWriteFlag);
		getSqlSession().update("ProductTracingMapper.doUpdatePlcRfidStatus",params);
	}
	@Override
	public String readWriteFlag(String type) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("type", type);
		return getSqlSession().selectOne("ProductTracingMapper.readWriteFlag",params);
	}

	@Override
	public void updateFinshTime(String sn) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("sn", sn);
		getSqlSession().update("ProductTracingMapper.updateFinshTime",params);
	}

	@Override
	public TbProductTracing getTbProductTracingSn(String sn) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("sn", sn);
		return getSqlSession().selectOne("ProductTracingMapper.getTbProductTracingSn",params);
	}

	@Override
	public void updateUnhealthyCount(String sn) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("sn", sn);
		if(StringUtils.isNotBlank(sn)) {
			getSqlSession().update("ProductTracingMapper.updateUnhealthyCount",params);
		}
	}
}
