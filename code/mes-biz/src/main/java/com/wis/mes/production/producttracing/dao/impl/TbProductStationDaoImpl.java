package com.wis.mes.production.producttracing.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.producttracing.dao.TbProductStationDao;
import com.wis.mes.production.producttracing.entity.TbProductStation;

@Repository
public class TbProductStationDaoImpl extends BaseDaoImpl<TbProductStation> implements TbProductStationDao {
	@Override
	public Map<String, Integer> getLineIdAndUlocIdByUlocNo(String ulocNo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ulocNo", ulocNo);
		return getSqlSession().selectOne("ProductTracingMapper.getLineIdAndUlocIdByNo", param);
	}

	@Override
	public Integer getStaffIdBy(Integer ulocId, Integer lineId, Integer tmClassManagerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ulocId", ulocId);
		param.put("lineId", lineId);
		param.put("tmClassManagerId", tmClassManagerId);
		return getSqlSession().selectOne("ProductTracingMapper.getStaffIdBy", param);
	}

	@Override
	public Integer getProductIdBySn(String sn) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sn", sn);
		return getSqlSession().selectOne("ProductTracingMapper.getProductIdBySn", param);
	}

	@Override
	public TmWorktime getCurrentWorkTime(Integer tmLineId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmLineId", tmLineId);
		return getSqlSession().selectOne("ProductTracingMapper.getCunrrentWorkTime", param);
	}
	
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		String sort = "\""+"uloc.no"+"\"";
		if(StringUtils.isNotBlank(queryCriteria.getOrderField()) && queryCriteria.getOrderField().equals(sort)){
			sql = sql.replace("ORDER BY "+sort+"", "ORDER BY to_number(regexp_substr(uloc_0.no,'[0-9]*[0-9]',1))");
		}
		return sql;
	}
	
}
