package com.wis.mes.dakin.report.service;

import java.util.List;
import java.util.Map;

public interface FinAnnualReportService {
	public List<Integer> getFinNoFailStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinSwitchStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinChangeMaterialStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinAddOilStopData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Integer> getFinFaultedData(String shiftType, String createTimeStart, String createTimeEnd);
	public List<Map<String, Object>> getFinNoFailStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd);
	public Map<String, String> getFinAnnualReportInfo(String shiftType, String createTimeStart, String createTimeEnd);
}
