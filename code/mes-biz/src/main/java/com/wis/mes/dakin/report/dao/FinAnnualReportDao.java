package com.wis.mes.dakin.report.dao;

import java.util.List;
import java.util.Map;

public interface FinAnnualReportDao {
	public List<Integer> getFinNoFailStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinSwitchStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinChangeMaterialStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinAddOilStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinFaultedData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinNoFailStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinSwitchStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinChangeMaterialStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinAddOilStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinFaultedByParams(String year, String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinFaultedDetails(String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinAttendanceTime(String year, String createTimeStart, String createTimeEnd);
}
