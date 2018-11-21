package com.wis.mes.dakin.report.service;
import java.util.List;
import java.util.Map;

/**
 * @author caixia
 *
 */
public interface MetalPlateFaultedDetailReportService {
	
	public Map<String, String> getMetalPlateFaultedDetailPieReportInfo(String whitOrNight,String shiftNo, String beginTime, String endTime);
	
	public Map<String, String> getMetalPlateFaultedDetailReport(String whitOrNight,String shiftNo, String beginTime, String endTime);
	
	public Map<String, String> getfaultedDetailsInfo(String beginTime, String endTime);
	
	/**
	 * @author yajun_ren
	 * @desc 钣金非故障明细报表数据
	 * @param parameters
	 * @return result
	 * @date 2018/11/12
	 * **/
	Map<String, Object> noFaultDetailsReportData(Map<String,Object> parameters);
	/**
	 * @author yajun_ren
	 * @desc 钣金生产状况表
	 * @param parameters
	 * @return result
	 * @date 2018/11/12
	 * **/
	Map<String, Object> productionStatusData(Map<String,Object> parameters);
	/**
	 * @author yajun_ren
	 * @desc BJ-生产状态年报
	 * @param parameters
	 * @return result
	 * @date 2018/11/14
	 * **/
	Map<String, Object> productionStateYearData(Map<String,Object> parameters);
	/**
	 * @author yajun_ren
	 * @desc BJ-故障停机明细
	 * @param parameters
	 * @return result
	 * @date 2018/11/14
	 * **/
	Map<String, Object> noHaltReportData(Map<String,Object> parameters);

}
