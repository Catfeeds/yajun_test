package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.dakin.report.dao.FinMovableRateDao;
import com.wis.mes.dakin.report.entity.FinMovableRate;

@Repository
public class FinMovableRateDaoImpl extends BaseDaoImpl<FinMovableRate> implements FinMovableRateDao {

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("where 1=1", "where 1=1 and finmovablerate_0.SHIFT_NO<>'0'");
		return sql;
	}

	@Override
	public List<Map<String, Object>> getFinMovableRateEveryDay(String shiftNo, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinMovableRateMapper.getFinMovableRateEveryDay", param);
	}

}
