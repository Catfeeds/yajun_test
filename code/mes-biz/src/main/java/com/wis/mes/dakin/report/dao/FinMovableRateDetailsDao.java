package com.wis.mes.dakin.report.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wis.mes.dakin.report.entity.FinMovableRate;

public interface FinMovableRateDetailsDao {
	List<Map<String, Object>> getFinMovableRateWhiteAndNightData(String shiftNo, String createTimeStart, String createTimeEnd);

	List<Map<String, String>> getFaultAndNoFaultTime(String shiftNo, String workCalendarTime);

	Map<String, Object> getFaultAndNoFaultMessage(String shiftNo, String beginTime);

	List<Map<String, Object>> getFaulitedOrNotCountAndDurationByTime(Date beginTime, Date finishTime);

	List<FinMovableRate> getMovableRateByTime(String createTimeStart, String createTimeEnd,String shiftNo);
	List<Map<String, Object>> getFinOneLivelStatusContent(Date beginTime, Date finishTime);
}
