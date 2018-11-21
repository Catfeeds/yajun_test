/**
 * 
 */
package com.wis.mes.dakin.report.service;

import java.util.List;
import java.util.Map;

/**
 * @author caixia
 *
 */
public interface YearReportService {
	public Map<String, List> getMetalPlateYearDetailPieReportInfo(String shiftNo, String beginTime, String endTime);
	public Map<String, String> getMetalPlateYearTimeReportInfo(String whitOrNight,String shiftNo, String beginTime, String endTime);
	public Map<String, String> getMetalPlateYearCountShiftReport(String whitOrNight,String shiftNo, String beginTime, String endTime);
	public List getMetalPlateYearListReport(String shiftNo, String beginTime, String endTime);
}
