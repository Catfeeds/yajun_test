package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.mes.dakin.report.dao.MetalPlateFaultedDetailReportDao;
import com.wis.mes.dakin.report.service.MetalPlateFaultedDetailReportService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.production.regionstatus.entity.TmpRegionstatus;
import com.wis.mes.production.regionstatus.service.TmpRegionstatusService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author caixia
 *
 */
@Service
public class MetalPlateFaultedDetailReportServiceImpl implements MetalPlateFaultedDetailReportService {
	@Autowired
	MetalPlateFaultedDetailReportDao faultedDetailReportDao;
	
	@Autowired
	private TmWorktimeService worktimeService;
	@Autowired
	private GlobalConfigurationService configurationService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmpRegionstatusService regionstatusService;

	@Override
	public Map<String, String> getMetalPlateFaultedDetailReport(String whitOrNight, String shiftNo, String beginTime,
			String endTime) {

		Map<String, String> returnMap = new HashMap<String, String>();
		JSONArray data = new JSONArray();
		JSONArray dataCount = new JSONArray();
		JSONArray name = new JSONArray();
		List<Map<String, Object>> dataList = faultedDetailReportDao.getMetalPlateFaultedDetailReport(whitOrNight,
				shiftNo, beginTime, endTime);
		for (Map<String, Object> map : dataList) {
			if (null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal) map.get("DURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"), 2, BigDecimal.ROUND_HALF_UP);
				data.add(lastDuration);
			} else {
				data.add(0);
			}
			dataCount.add(map.get("NUMBERS"));
			name.add(map.get("CODE_DESC"));
		}
		dataCount.add(0);
		data.add(0);
		name.add("其他");
		returnMap.put("dataCount", dataCount.toString());
		returnMap.put("data", data.toString());
		returnMap.put("name", name.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> getMetalPlateFaultedDetailPieReportInfo(String whitOrNight, String shiftNo,
			String beginTime, String endTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		JSONArray data = new JSONArray();
		JSONArray name = new JSONArray();
		List<Map<String, Object>> dataList = faultedDetailReportDao.getMetalPlateFaultedDetailReport(whitOrNight,
				shiftNo, beginTime, endTime);
		for (Map<String, Object> map : dataList) {
			BigDecimal bigDecimal = new BigDecimal("0");
			if (null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal) map.get("DURATION");
				bigDecimal = duration.divide(new BigDecimal("60"), 2, BigDecimal.ROUND_HALF_UP);
			}
			JSONObject dataObj = new JSONObject();
			dataObj.put("name", map.get("CODE_DESC"));
			dataObj.put("value", bigDecimal);
			dataObj.put("dataCount", map.get("NUMBERS"));
			data.add(dataObj);
			name.add(map.get("CODE_DESC"));
		}

		JSONObject otherObj = new JSONObject();
		otherObj.put("name", "其他");
		otherObj.put("value", new BigDecimal("0"));
		otherObj.put("dataCount", 0);
		data.add(otherObj);
		name.add("其他");
		returnMap.put("data", data.toString());
		returnMap.put("name", name.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> getfaultedDetailsInfo(String beginTime,String endTime) {
		String year = "";
		if (StringUtils.isNotEmpty(beginTime)) {
			year = beginTime.split("-")[0];
		} else {
			Calendar cal = Calendar.getInstance();
			year = String.valueOf(cal.get(Calendar.YEAR));
			beginTime = year + "-01-01 00:00";
			endTime = year + "-12-31 23:59";
		}
		
		List<Map<String, Object>> faultedDetailsInfo = faultedDetailReportDao.getfaultedDetailsInfo(beginTime, endTime);
		JSONArray countList = new JSONArray();
		JSONArray minuteList = new JSONArray();
		List<Integer> countMaxList = new ArrayList<Integer>();
		List<Integer> minuteMaxList = new ArrayList<Integer>();
		for (Map<String, Object> map : faultedDetailsInfo) {
			String createTime = "",minute = "";
			int count = 0;
			if(null != map.get("CREATETIME")){
				createTime = map.get("CREATETIME").toString();
			}
			if(null != map.get("MINUTE")){
				minute = map.get("MINUTE").toString();
			}
			if(null != map.get("COUNT")){
				count = Integer.valueOf(map.get("COUNT").toString());
			}
			JSONArray countArray = new JSONArray();
			JSONArray minuteArray = new JSONArray();
			countArray.add(createTime);
			countArray.add(count);
			minuteArray.add(createTime);
			minuteArray.add(minute);
			countList.add(countArray);
			minuteList.add(minuteArray);
			countMaxList.add(count);
			int minute1=0;
			if(StringUtils.isNotBlank(minute)){	
				minute1=Integer.parseInt(minute);
			}
			minuteMaxList.add(Double.valueOf(minute1).intValue());
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("minute", minuteList.toString());
		map.put("count", countList.toString());
		map.put("year", year);
		map.put("countMax", String.valueOf(Collections.max(countMaxList)));
		map.put("minuteMax", String.valueOf(Collections.max(minuteMaxList)));
		return map;
	}

	@Override
	public Map<String, Object> noFaultDetailsReportData(Map<String, Object> parameters) {
		JSONArray seriesA = getDefaultData(5);
		JSONArray seriesB = getDefaultData(5);
		JSONArray seriesC = getDefaultData(5);
		JSONArray series1 = getDefaultData(4);
		JSONArray series3 = getDefaultData(4);
		JSONArray series4 = getDefaultData(4);
		JSONArray series5 = getDefaultData(4);
		JSONArray series7 = getDefaultData(4);
		JSONArray xAxisData = new JSONArray();
		List<DictEntry> circulateStates = entryService.getEntryByTypeCode(ConstantUtils.SM_CIRCULATE_STATE);
		Map<String,String> circulateStateMap = new HashMap<String,String>();
		for(DictEntry dic:circulateStates) {
			circulateStateMap.put(dic.getCode(), dic.getName());
		}
		if(circulateStateMap.containsKey(ConstantUtils.SM_CIRCULATE_STATE_1)) {
			xAxisData.add(circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_1));
			series1.set(0, circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_1));
		}
		if(circulateStateMap.containsKey(ConstantUtils.SM_CIRCULATE_STATE_3)) {
			xAxisData.add(circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_3));
			series3.set(0, circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_3));
		}
		if(circulateStateMap.containsKey(ConstantUtils.SM_CIRCULATE_STATE_4)) {
			xAxisData.add(circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_4));
			series4.set(0, circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_4));
		}
		if(circulateStateMap.containsKey(ConstantUtils.SM_CIRCULATE_STATE_5)) {
			xAxisData.add(circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_5));
			series5.set(0, circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_5));
		}
		if(circulateStateMap.containsKey(ConstantUtils.SM_CIRCULATE_STATE_7)) {
			xAxisData.add(circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_7));
			series7.set(0, circulateStateMap.get(ConstantUtils.SM_CIRCULATE_STATE_7));
		}
		Map<String,Integer> statusMap = new HashMap<String,Integer>();
		statusMap.put(ConstantUtils.SM_CIRCULATE_STATE_1, 0);
		statusMap.put(ConstantUtils.SM_CIRCULATE_STATE_3, 1);
		statusMap.put(ConstantUtils.SM_CIRCULATE_STATE_4, 2);
		statusMap.put(ConstantUtils.SM_CIRCULATE_STATE_5, 3);
		statusMap.put(ConstantUtils.SM_CIRCULATE_STATE_7, 4);
		Map<String,Integer> runningStateMap = new HashMap<String,Integer>();
		runningStateMap.put("A", 1);
		runningStateMap.put("B", 2);
		runningStateMap.put("C", 3);
		List<Map<String, Object>> dataList = faultedDetailReportDao.noFaultDetailsReportData(parameters);
		for(Map<String, Object> map:dataList) {
			String regionNo = null != map.get("REGION_NO") ? map.get("REGION_NO").toString():"未知";
			String runningState = null != map.get("RUNNING_STATE")?map.get("RUNNING_STATE").toString():"0";
			Double minute =null != map.get("MINUTE")?Double.valueOf(map.get("MINUTE").toString()):0.0;
			if(ConstantUtils.SM_CIRCULATE_STATE_1.equals(runningState)) {
				if(runningStateMap.containsKey(regionNo)) {
					series1.set(runningStateMap.get(regionNo), minute);
				}
			}else if(ConstantUtils.SM_CIRCULATE_STATE_3.equals(runningState)) {
				if(runningStateMap.containsKey(regionNo)) {
					series3.set(runningStateMap.get(regionNo), minute);
				}
			}else if(ConstantUtils.SM_CIRCULATE_STATE_4.equals(runningState)) {
				if(runningStateMap.containsKey(regionNo)) {
					series4.set(runningStateMap.get(regionNo), minute);
				}
			}else if(ConstantUtils.SM_CIRCULATE_STATE_5.equals(runningState)) {
				if(runningStateMap.containsKey(regionNo)) {
					series5.set(runningStateMap.get(regionNo), minute);
				}
			}else if(ConstantUtils.SM_CIRCULATE_STATE_7.equals(runningState)) {
				if(runningStateMap.containsKey(regionNo)) {
					series7.set(runningStateMap.get(regionNo), minute);
				}
			}
			if("A".equals(regionNo)) {
				if(statusMap.containsKey(runningState)) {
					seriesA.set(statusMap.get(runningState), minute);
				}
			}else if("B".equals(regionNo)) {
				if(statusMap.containsKey(runningState)) {
					seriesB.set(statusMap.get(runningState), minute);
				}
			}else if("C".equals(regionNo)) {
				if(statusMap.containsKey(runningState)) {
					seriesC.set(statusMap.get(runningState), minute);
				}
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("seriesA", seriesA);
		result.put("seriesB", seriesB);
		result.put("seriesC", seriesC);
		result.put("series1", series1);
		result.put("series3", series3);
		result.put("series4", series4);
		result.put("series5", series5);
		result.put("series7", series7);
		result.put("xAxisData", xAxisData);
		return result;
	}
	
	@Override
	public Map<String, Object> productionStatusData(Map<String,Object> parameters) {
		String dataTime = parameters.get("dataTime").toString();
		String regionNo = parameters.get("regionNo").toString();
		String shiftno = parameters.get("shiftno").toString();
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		JSONArray seriesData = new JSONArray();
		JSONArray circulateStateArray = new JSONArray(); 
		JSONObject jsonObject = new JSONObject();
		String lineNo = configurationService.getValueByKey(ConstantUtils.MP_LINE_NO);
		TmLine lineBean = lineService.findByNo(lineNo);
		TmWorktime worktime = null;
		if(StringUtils.isEmpty(dataTime)) {
			worktime = worktimeService.thisWorkTime(lineBean.getId());
		}else {
			TmWorktime eg = new TmWorktime();
			eg.setTmLineId(lineBean.getId());
			eg.setWorkDate(DateUtils.parseDate(dataTime));
			eg.setShiftno(shiftno);
			eg.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
			List<TmWorktime> worktimeList = worktimeService.findByEg(eg);
			if(null != worktimeList && worktimeList.size() > 0) {
				worktime = worktimeList.get(0);
			}
		}
		if(null != worktime) {
			Date startTime =DateUtils.parse(DateUtils.formatDate(worktime.getWorkDate())+" "+ worktime.getStartTime()+":00", DateUtils.FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
			Date endTime =DateUtils.parse(DateUtils.formatDate(worktime.getWorkDate())+" "+ worktime.getEndTime()+":59", DateUtils.FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
			int worktimeSum = 0;
			if(startTime.getTime() > endTime.getTime()) {
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(endTime);
				rightNow.add(Calendar.DATE, 1);
				worktimeSum = ((int) rightNow.getTimeInMillis() - (int) startTime.getTime());
			}else {
				worktimeSum = ((int) endTime.getTime()- (int)startTime.getTime());
			}
			TmpRegionstatus pregionstatusEg = new TmpRegionstatus();
			pregionstatusEg.setRegionNo(regionNo);
			pregionstatusEg.setTmWorkTimeId(Long.valueOf(worktime.getId()));
			List<TmpRegionstatus> regionstatusList = regionstatusService.findByEg(pregionstatusEg);
			
			//钣金设备运行类型数据字典
			Map<String, DictEntry> smCirculateState = entryService.getMapTypeCode(ConstantUtils.SM_CIRCULATE_STATE);
			Map<String,Object> yMap = new HashMap<String,Object>();
			int index = 0;
			for(String key:smCirculateState.keySet()) {
				circulateStateArray.add(smCirculateState.get(key).getName());
				if(!yMap.containsKey(key)) {
					yMap.put(key, index);
					index++;
				}
			}
			
			if(null != regionstatusList && regionstatusList.size() > 0) {
				for(TmpRegionstatus bean:regionstatusList ) {
					if(StringUtils.isNotBlank(bean.getFinishTime())) {
						if(yMap.containsKey(bean.getRunningState())) {
							int timeResults = 0;
							String beginTime = DateUtils.formatDate(bean.getCreateTime())+" "+bean.getBeginTime(); 
							String finishTime = DateUtils.formatDate(bean.getCreateTime())+" "+bean.getFinishTime(); 
							Date beginDate = DateUtils.parse(beginTime, DateUtils.FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
							Date finishDate = DateUtils.parse(finishTime, DateUtils.FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
							timeResults = ((int)finishDate.getTime()-(int)beginDate.getTime());
							String partialFill = getPartialFill(timeResults,worktimeSum);
							jsonObject.put("x", beginDate.getTime());
							jsonObject.put("x2",finishDate.getTime());
							jsonObject.put("y", yMap.get(bean.getRunningState()));
							jsonObject.put("partialFill", partialFill);
							seriesData.add(jsonObject);
						}
					}
				}
			}
		}
		resultMap.put("seriesData", seriesData.toString());
		resultMap.put("yCategories", circulateStateArray.toString());
		return resultMap;
	}
	
	private String getPartialFill(int num1,int num2) {
		 // 创建一个数值格式化对象  
        NumberFormat numberFormat = NumberFormat.getInstance();  
        // 设置精确到小数点后2位  
        numberFormat.setMaximumFractionDigits(4);  
        return numberFormat.format((float) num1 / (float) num2);  
	}

	@Override
	public Map<String, Object> productionStateYearData(Map<String, Object> parameters) {
		Map<String,Object> result = new HashMap<String, Object>();
		String createTime = parameters.get("createTime").toString(); 
		if(StringUtils.isBlank(createTime)) {
			createTime = DateUtils.formatDate(new Date(),DateUtils.FORMAT_DATE_YYYY_MM);
		}
		String[] times = createTime.split("-");
		String type = parameters.get("type").toString();
		if("yearBar".equals(type)) {
			parameters.put("timeYear", times[0]);
		}else if("monthBar".equals(type)) {
			parameters.put("timeMoon", createTime);
		}
		List<String> days = DateUtils.getLastDayOfMonth1(Integer.valueOf(times[0]),Integer.valueOf(times[1]),"d");
		List<String> months = DateUtils.getAllMonth();
		List<Map<String, Object>> productionStateYearData = faultedDetailReportDao.productionStateYearData(parameters);
		JSONArray seriesNoNg = new JSONArray();//非故障时间
		JSONArray seriesNg = new JSONArray();//故障时间
		JSONArray seriesHandling = new JSONArray();//加工时间
		if("yearBar".equals(type)) {
			seriesNoNg = getDefaultData(months.size());
			seriesNg = getDefaultData(months.size());
			seriesHandling = getDefaultData(months.size());
		}else if("monthBar".equals(type)) {
			seriesNoNg = getDefaultData(days.size());
			seriesNg = getDefaultData(days.size());
			seriesHandling = getDefaultData(days.size());
		}
		 if(null != productionStateYearData && productionStateYearData.size() > 0) {
			 for(Map<String, Object> map:productionStateYearData) {
				 String runningState = null != map.get("RUNNING_STATE")?map.get("RUNNING_STATE").toString():"0";//状态
				 Integer time = null != map.get("TIME")?Integer.valueOf(map.get("TIME").toString()):0;//刻度
				 Double minute = null != map.get("MINUTE")?Double.valueOf(map.get("MINUTE").toString()):0.0;//用时
				 if("2".equals(runningState)) {//加工
					 seriesHandling.set(time-1, minute);
				 }else if("7".equals(runningState)){//故障停机
					 seriesNg.set(time-1, minute);
				 }else if("10".equals(runningState)) {//非故障停机
					 seriesNoNg.set(time-1, minute);
				 }
			 }
		 }
		 result.put("xAxisDataMY", months);
		 result.put("xAxisDataM", days);
		 result.put("seriesNoNg", seriesNoNg);
		 result.put("seriesNg", seriesNg);
		 result.put("seriesHandling", seriesHandling);
		 return result;
	}
	
	public JSONArray getDefaultData(int length) {
		JSONArray jsonArray = new JSONArray();
		for(int i=0;i<length;i++) {
			jsonArray.add(0);
		}
		return jsonArray;
	}

	@Override
	public Map<String, Object> noHaltReportData(Map<String, Object> parameters) {
		List<Map<String, Object>> dataList = faultedDetailReportDao.noHaltReportData(parameters);
		JSONArray  xAxisData = new JSONArray();
		JSONArray  seriesData1 = new JSONArray();
		JSONArray  seriesData2 = new JSONArray();
		try {
			for(Map<String, Object> map:dataList) {
				String codeDesc = null != map.get("CODE_DESC")?map.get("CODE_DESC").toString():"未知";//描述
				String statusNumber = null != map.get("STATUS_NUMBER")?map.get("STATUS_NUMBER").toString():"0";//原状态编码
				Double minute =null != map.get("MINUTE")?Double.valueOf(map.get("MINUTE").toString()):0.0;
				Integer countNumber = null != map.get("COUNT_NUMBER")?Integer.valueOf(map.get("COUNT_NUMBER").toString()):0;//次数
				xAxisData.add(codeDesc);
				seriesData1.add(minute);
				seriesData2.add(countNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("xAxisData", xAxisData);
		result.put("seriesData1", seriesData1);
		result.put("seriesData2", seriesData2);
		return result;
	}
}
