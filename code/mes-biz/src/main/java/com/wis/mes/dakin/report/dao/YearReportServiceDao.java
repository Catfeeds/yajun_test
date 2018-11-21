/**
 * 
 */
package com.wis.mes.dakin.report.dao;

import java.util.List;
import java.util.Map;

/**
 * @author caixia
 *
 */
public interface YearReportServiceDao {
	public Map<String, List> getMetalPlateYearDetailPieReportInfo(String shiftNo, String beginTime, String endTime);
	public List<Map<String, Object>>  getMetalPlateYearDetailearShiftReport(String whitOrNight,String shiftNo, String beginTime, String endTime);
	public List<Map<String, Object>>  getMetalPlateYearDetailearReport(String shiftNo, String beginTime, String endTime);
	public List<Map<String, Object>> getMetalPlateYearListReport(String shiftNo, String beginTime, String endTime);
}


