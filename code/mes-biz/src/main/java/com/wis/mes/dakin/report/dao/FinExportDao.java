package com.wis.mes.dakin.report.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;

public interface FinExportDao {
	PageResult<Map<String, Object>> getFinExportInfo(QueryCriteria criteria);

	Map<String, String> getOneLevelStatusContent(String workDate, String shiftNo, String beginTime, String endTime);

	Map<String, String> getFinFaultedCodeName(String shiftNo, String beginTime, String endTime);

	List<Map<String, Object>> getFinNoFaultedName(String shiftNo, String beginTime, String endTime);

	List<Map<String, Object>> getFinNoFaultSwitchStopName(String shiftNo, String beginTime, String endTime);

	List<Map<String, Object>> getFinNoFaultChangeMaterial(String shiftNo, String beginTime, String endTime);

	List<Map<String, Object>> getFinNoFaultAddOil(String shiftNo, String beginTime, String endTime);
}
