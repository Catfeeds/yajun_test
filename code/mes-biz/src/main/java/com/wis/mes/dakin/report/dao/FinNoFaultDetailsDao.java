package com.wis.mes.dakin.report.dao;

import java.util.List;
import java.util.Map;

public interface FinNoFaultDetailsDao {
	public List<Map<String, Object>> getFinNoFaultStopDetails(String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinSwitchStopDetails(String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinChangeMaterialStopDetails(String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinAddOilStopDetails(String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> noFaultedDetailsYearShow(String year, String createTimeStart, String createTimeEnd);
	
}
