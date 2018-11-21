package com.wis.mes.dakin.report.service;

import java.util.Map;

public interface ReportService {
	/**
	 * 故障机报表 班次、日期、数量、关系
	 * 
	 * @return
	 */
	Map<String, String> ngAndShiftNoRealationReport(String createTimeStart, String createTimeEnd);

	Map<String, String> ngAndCountRealationReport(String createTimeStart, String createTimeEnd);

	/**
	 * 故障内容、数量、NG来源 关系
	 * 
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @return
	 */
	Map<String, String> ngInfoOriginReport(String createTimeStart, String createTimeEnd);

	/**
	 * 故障内容、数量、NG来源 关系 拼图
	 * 
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @return
	 */
	Map<String, String> ngInfoOriginReportPIE(String createTimeStart, String createTimeEnd);

	/**
	 * 工位状态、时间、次数 分布报表
	 * 
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @return
	 */
	Map<String, String> ulocStatusTimeCountReport(String createTimeStart, String createTimeEnd);

	/**
	 * 工位状态 异常次数和异常原因关系
	 * 
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @return
	 */
	Map<String, String> ulocStatusCountAndDetailReport(String createTimeStart, String createTimeEnd);

	/** 不同异常源 时间 数量 详情 */
	Map<String, String> ulocStatusAbnormalSourceCountAndMintue(String abnormalSource, String createTimeStart,
			String createTimeEnd);

	/** 工位状态 年密度 统计报表 */
	Map<String, String> ulocStatusYearDensity(String year, String createTimeStart, String createTimeEnd);

	/** 能耗报表 柱状图 */
	Map<String, String> energReportBar(String timeFormate, String createTimeStart, String createTimeEnd);

	/** 能耗报表 饼状图 */
	Map<String, String> energReportPic(String timeFormate, String createTimeStart, String createTimeEnd);
	
	/**PLC能耗报表 饼图*/
	Map<String,String>plcEnergyReport(String startDateTime, String endDateTime);
}
