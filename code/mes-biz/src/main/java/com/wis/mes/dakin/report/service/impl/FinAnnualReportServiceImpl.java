package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.mes.dakin.report.dao.FinAnnualReportDao;
import com.wis.mes.dakin.report.service.FinAnnualReportService;

import net.sf.json.JSONArray;

@Service
public class FinAnnualReportServiceImpl implements FinAnnualReportService{
	@Autowired
	private FinAnnualReportDao finAnnualReportDao;
	
	@Override
	public List<Integer> getFinNoFailStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		return finAnnualReportDao.getFinNoFailStopData(shiftType, createTimeStart, createTimeEnd);
	}
	@Override
	public List<Integer> getFinSwitchStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		return finAnnualReportDao.getFinSwitchStopData(shiftType, createTimeStart, createTimeEnd);
	}
	@Override
	public List<Integer> getFinChangeMaterialStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		return finAnnualReportDao.getFinChangeMaterialStopData(shiftType, createTimeStart, createTimeEnd);
	}
	@Override
	public List<Integer> getFinAddOilStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		return finAnnualReportDao.getFinAddOilStopData(shiftType, createTimeStart, createTimeEnd);
	}
	@Override
	public List<Integer> getFinFaultedData(String shiftType, String createTimeStart, String createTimeEnd) {
		return finAnnualReportDao.getFinFaultedData(shiftType, createTimeStart, createTimeEnd);
	}
	@Override
	public List<Map<String, Object>> getFinNoFailStopByParams(String year, String shiftType, String createTimeStart,
			String createTimeEnd) {
		return finAnnualReportDao.getFinNoFailStopByParams(year, shiftType, createTimeStart, createTimeEnd);
	}
	@Override
	public Map<String, String> getFinAnnualReportInfo(String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, String> returnMap = new HashMap<String, String>();
		//白班
		JSONArray whiteShiftNoFailStopData = new JSONArray();
		JSONArray whiteShiftSwitchStopData = new JSONArray();
		JSONArray whiteShiftChangeMaterialStopData = new JSONArray();
		JSONArray whiteShiftAddOilStopData = new JSONArray();
		JSONArray whiteShiftFaultedData = new JSONArray();
		JSONArray whiteShiftNoFailStopMinData =  new JSONArray();
		JSONArray whiteShiftSwitchStopMinData =  new JSONArray();
		JSONArray whiteShiftChangeMaterialStopMinData = new JSONArray();
		JSONArray whiteShiftAddOilStopMinData = new JSONArray();
		JSONArray whiteShiftFaultedMinData = new JSONArray();
		//晚班
		JSONArray nightShiftNoFailStopData = new JSONArray();
		JSONArray nightShiftSwitchStopData = new JSONArray();
		JSONArray nightShiftChangeMaterialStopData = new JSONArray();
		JSONArray nightShiftAddOilStopData = new JSONArray();
		JSONArray nightShiftFaultedData = new JSONArray();
		JSONArray nightShiftNoFailStopMinData =  new JSONArray();
		JSONArray nightShiftSwitchStopMinData = new JSONArray();
		JSONArray nightShiftChangeMaterialStopMinData = new JSONArray();
		JSONArray nightShiftAddOilStopMinData = new JSONArray();
		JSONArray nightShiftFaultedMinData = new JSONArray();
		
		JSONArray months = new JSONArray();
		String year = "";
		if (StringUtils.isNotEmpty(createTimeStart)) {
			year = createTimeStart.split("-")[0];
		} else {
			Calendar cal = Calendar.getInstance();
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		//非故障停机
		JSONArray finNoFailStopDurationData = new JSONArray();
		JSONArray finNoFailStopCountData = new JSONArray();
		List<Map<String, Object>> finNoFailStopList = finAnnualReportDao.getFinNoFailStopByParams(year, null, createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finNoFailStopList) {
			if(null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal)map.get("DURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				finNoFailStopDurationData.add(lastDuration);
			} else {
				finNoFailStopDurationData.add(0);
			}
			if(map.get("WHITEMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("WHITEMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				whiteShiftNoFailStopMinData.add(lastDuration);
			}else {
				whiteShiftNoFailStopMinData.add(0);
			}
			if(map.get("NIGHTMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("NIGHTMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				nightShiftNoFailStopMinData.add(lastDuration);
			}else {
				nightShiftNoFailStopMinData.add(0);
			}
			finNoFailStopCountData.add(map.get("SHOWCOUNT"));
			months.add(map.get("SHOWMONTH"));
			whiteShiftNoFailStopData.add(map.get("WHITESHIFT"));
			nightShiftNoFailStopData.add(map.get("NIGHTSHIFT"));
		}
		//切换停机
		JSONArray finSwitchStopDurationData = new JSONArray();
		JSONArray finSwitchStopCountData = new JSONArray();
		List<Map<String, Object>> finSwitchStopList = finAnnualReportDao.getFinSwitchStopByParams(year, null, createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finSwitchStopList) {
			if(null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal)map.get("DURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				finSwitchStopDurationData.add(lastDuration);
			} else {
				finSwitchStopDurationData.add(0);
			}
			if(map.get("WHITEMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("WHITEMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				whiteShiftSwitchStopMinData.add(lastDuration);
			}else {
				whiteShiftSwitchStopMinData.add(0);
			}
			if(map.get("NIGHTMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("NIGHTMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				nightShiftSwitchStopMinData.add(lastDuration);
			}else {
				nightShiftSwitchStopMinData.add(0);
			}
			finSwitchStopCountData.add(map.get("SHOWCOUNT"));
			whiteShiftSwitchStopData.add(map.get("WHITESHIFT"));
			nightShiftSwitchStopData.add(map.get("NIGHTSHIFT"));
		}
		//换料停机
		JSONArray finChangeMaterialStopDurationData = new JSONArray();
		JSONArray finChangeMaterialStopCountData = new JSONArray();
		List<Map<String, Object>> finChangeMaterialStopList = finAnnualReportDao.getFinChangeMaterialStopByParams(year, null, createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finChangeMaterialStopList) {
			if(null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal)map.get("DURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				finChangeMaterialStopDurationData.add(lastDuration);
			} else {
				finChangeMaterialStopDurationData.add(0);
			}
			if(map.get("WHITEMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("WHITEMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				whiteShiftChangeMaterialStopMinData.add(lastDuration);
			}else {
				whiteShiftChangeMaterialStopMinData.add(0);
			}
			if(map.get("NIGHTMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("NIGHTMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				nightShiftChangeMaterialStopMinData.add(lastDuration);
			}else {
				nightShiftChangeMaterialStopMinData.add(0);
			}
			finChangeMaterialStopCountData.add(map.get("SHOWCOUNT"));
			whiteShiftChangeMaterialStopData.add(map.get("WHITESHIFT"));
			nightShiftChangeMaterialStopData.add(map.get("NIGHTSHIFT"));
		}
		
		//加油停机
		JSONArray finAddOilStopDurationData = new JSONArray();
		JSONArray finAddOilStopCountData = new JSONArray();
		List<Map<String, Object>> finAddOilStopList = finAnnualReportDao.getFinAddOilStopByParams(year, null, createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finAddOilStopList) {
			if(null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal)map.get("DURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				finAddOilStopDurationData.add(lastDuration);
			} else {
				finAddOilStopDurationData.add(0);
			}
			if(map.get("WHITEMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("WHITEMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				whiteShiftAddOilStopMinData.add(lastDuration);
			}else {
				whiteShiftAddOilStopMinData.add(0);
			}
			if(map.get("NIGHTMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("NIGHTMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				nightShiftAddOilStopMinData.add(lastDuration);
			}else {
				nightShiftAddOilStopMinData.add(0);
			}
			finAddOilStopCountData.add(map.get("SHOWCOUNT"));
			whiteShiftAddOilStopData.add(map.get("WHITESHIFT"));
			nightShiftAddOilStopData.add(map.get("NIGHTSHIFT"));
		}
		//故障
		JSONArray finFaultedDurationData = new JSONArray();
		JSONArray finFaultedCountData = new JSONArray();
		List<Map<String, Object>> finFaultedList = finAnnualReportDao.getFinFaultedByParams(year, null, createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finFaultedList) {
			if(null != map.get("DURATION")) {
				BigDecimal duration = (BigDecimal)map.get("DURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				finFaultedDurationData.add(lastDuration);
			} else {
				finFaultedDurationData.add(0);
			}
			if(map.get("WHITEMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("WHITEMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				whiteShiftFaultedMinData.add(lastDuration);
			}else {
				whiteShiftFaultedMinData.add(0);
			}
			if(map.get("NIGHTMIN") != null){
				BigDecimal duration = (BigDecimal)map.get("NIGHTMIN");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				nightShiftFaultedMinData.add(lastDuration);
			}else {
				nightShiftFaultedMinData.add(0);
			}
			finFaultedCountData.add(map.get("SHOWCOUNT"));
			whiteShiftFaultedData.add(map.get("WHITESHIFT"));
			nightShiftFaultedData.add(map.get("NIGHTSHIFT"));
		}
		//故障总数已经各错误信息
		
		List<Map<String, Object>> faultedDetailsList = finAnnualReportDao.getFinFaultedDetails(createTimeStart, createTimeEnd);
		JSONArray faultedDetails = new JSONArray();
		faultedDetails.add(faultedDetailsList);
		
		//获取出勤时间
		JSONArray finAttendanceTime = new JSONArray();
		List<Map<String, Object>> getFinAttendanceTimeList = finAnnualReportDao.getFinAttendanceTime(year, createTimeStart, createTimeEnd);
		for(Map<String, Object> map : getFinAttendanceTimeList) {
			finAttendanceTime.add(map.get("WORKTIME"));
		}
		
		returnMap.put("whiteShiftNoFailStopData", whiteShiftNoFailStopData.toString());
		returnMap.put("whiteShiftNoFailStopMinData", whiteShiftNoFailStopMinData.toString());
		returnMap.put("whiteShiftSwitchStopData", whiteShiftSwitchStopData.toString());
		returnMap.put("whiteShiftSwitchStopMinData", whiteShiftSwitchStopMinData.toString());
		returnMap.put("whiteShiftChangeMaterialStopData", whiteShiftChangeMaterialStopData.toString());
		returnMap.put("whiteShiftChangeMaterialStopMinData", whiteShiftChangeMaterialStopMinData.toString());
		returnMap.put("whiteShiftAddOilStopData", whiteShiftAddOilStopData.toString());
		returnMap.put("whiteShiftAddOilStopMinData", whiteShiftAddOilStopMinData.toString());
		returnMap.put("whiteShiftFaultedData", whiteShiftFaultedData.toString());
		returnMap.put("whiteShiftFaultedMinData", whiteShiftFaultedMinData.toString());
		
		returnMap.put("nightShiftNoFailStopData", nightShiftNoFailStopData.toString());
		returnMap.put("nightShiftNoFailStopMinData", nightShiftNoFailStopMinData.toString());
		returnMap.put("nightShiftSwitchStopData", nightShiftSwitchStopData.toString());
		returnMap.put("nightShiftSwitchStopMinData", nightShiftSwitchStopMinData.toString());
		returnMap.put("nightShiftChangeMaterialStopData", nightShiftChangeMaterialStopData.toString());
		returnMap.put("nightShiftChangeMaterialStopMinData", nightShiftChangeMaterialStopMinData.toString());
		returnMap.put("nightShiftAddOilStopData", nightShiftAddOilStopData.toString());
		returnMap.put("nightShiftAddOilStopMinData", nightShiftAddOilStopMinData.toString());
		returnMap.put("nightShiftFaultedData", nightShiftFaultedData.toString());
		returnMap.put("nightShiftFaultedMinData", nightShiftFaultedMinData.toString());
		
		returnMap.put("finNoFailStopDurationData", finNoFailStopDurationData.toString());
		returnMap.put("finNoFailStopCountData", finNoFailStopCountData.toString());
		returnMap.put("finSwitchStopDurationData", finSwitchStopDurationData.toString());
		returnMap.put("finSwitchStopCountData", finSwitchStopCountData.toString());
		
		returnMap.put("finChangeMaterialStopDurationData", finChangeMaterialStopDurationData.toString());
		returnMap.put("finChangeMaterialStopCountData", finChangeMaterialStopCountData.toString());
		
		returnMap.put("finAddOilStopDurationData", finAddOilStopDurationData.toString());
		returnMap.put("finAddOilStopCountData", finAddOilStopCountData.toString());
		returnMap.put("finFaultedDurationData", finFaultedDurationData.toString());
		returnMap.put("finFaultedCountData", finFaultedCountData.toString());
		
		returnMap.put("months", months.toString());
		returnMap.put("year", year);
		returnMap.put("faultedDetails", faultedDetails.toString());
		returnMap.put("finAttendanceTime", finAttendanceTime.toString());
		return returnMap;
	}

}
