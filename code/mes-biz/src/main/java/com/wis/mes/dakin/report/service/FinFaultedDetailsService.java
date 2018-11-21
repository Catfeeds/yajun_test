package com.wis.mes.dakin.report.service;

import java.util.Map;

public interface FinFaultedDetailsService {
	public Map<String, String> getFaultedBarInfo(String shiftNo, String beginTime, String endTime);
	public Map<String, String> getFaultedPieInfo(String shiftNo, String beginTime, String endTime);
	public Map<String, String> faultedDetailsYearShow(String year, String createTimeStart, String createTimeEnd);
}
