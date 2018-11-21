package com.wis.mes.dakin.report.service;

import java.util.Map;

public interface StationDataAnalysisService {
	
	/***
	 * @author yajun_ren
	 * @type 类型
	 * @createTimeStart 开始时间
	 * @createTimeEnd 结束时间
	 * @return Map<String,Object>
	 * 根据类型获取报表所需参数
	 * */
	Map<String,Object> stationDataAnalysisData(String type,String createTimeStart, String createTimeEnd);
}
