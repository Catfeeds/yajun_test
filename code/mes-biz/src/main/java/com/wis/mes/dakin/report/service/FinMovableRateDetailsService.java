package com.wis.mes.dakin.report.service;

import java.util.Map;

public interface FinMovableRateDetailsService {
	public Map<String, String> getFinMovableRateWhiteAndNightData(String shiftNo, String createTimeStart, String createTimeEnd);

	public Map<String, String> getFaultAndNoFaultTime(String shiftNo, String createTime);

	public Map<String, String> getFaultAndNoFaultMessage(String shiftNo, String beginTime);

	Map<String,String> getMovableRateAndCalculate(String createTimeStart, String createTimeEnd, int figure,String shiftNo);
}
