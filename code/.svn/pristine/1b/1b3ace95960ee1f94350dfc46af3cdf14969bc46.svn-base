package com.wis.mes.dakin.report.dao;

import java.util.List;
import java.util.Map;

public interface ReportDao {
	List<Map<String, Object>> queryNgGroupShiftnoRelation(String createTimeStart, String createTimeEnd);

	List<Map<String, Object>> queryNgGroupCount(String createTimeStart, String createTimeEnd);

	List<Map<String, Object>> ngAndInfoOrigin(String createTimeStart, String createTimeEnd);

	/** 工位异常 时间、次数、异常源分布 报表 */
	List<Map<String, Object>> ulocStatusTimeCount(String createTimeStart, String createTimeEnd);

	/** 工位状态 异常次数和异常原因关系 */
	List<Map<String, Object>> ulocStatusCountAndDetail(String createTimeStart, String createTimeEnd);

	/** 不同异常源 时间 数量 详情 */
	List<Map<String, Object>> ulocStatusAbnormalSourceCountAndMintue(String abnormalSource, String createTimeStart,
			String createTimeEnd);

	/** 工位状态 年密度 统计报表 */
	List<Map<String, Object>> ulocStatusYearDensity(String year, String createTimeStart, String createTimeEnd);

	/** 能耗报表 */
	List<Map<String, Object>> energReport(String timeFormate, String createTimeStart, String createTimeEnd);
	
	/**PLC能耗报表*/
	Map<String, Object> plcEnergyReport(String startDateTime, String endDateTime);
}
