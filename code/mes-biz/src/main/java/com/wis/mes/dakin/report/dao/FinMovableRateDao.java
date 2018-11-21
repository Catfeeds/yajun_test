package com.wis.mes.dakin.report.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.dakin.report.entity.FinMovableRate;

public interface FinMovableRateDao extends BaseDao<FinMovableRate> {
	public List<Map<String, Object>> getFinMovableRateEveryDay(String shiftNo, String createTimeStart, String createTimeEnd);

}
