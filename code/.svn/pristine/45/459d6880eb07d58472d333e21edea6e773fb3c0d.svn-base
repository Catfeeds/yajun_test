package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.mes.dakin.report.dao.FinFaultedDetailsDao;
import com.wis.mes.dakin.report.service.FinFaultedDetailsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class FinFaultedDetailsServiceImpl implements FinFaultedDetailsService{
	@Autowired
	private FinFaultedDetailsDao faultedDetailsDao;

	@Override
	public Map<String, String> getFaultedBarInfo(String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		JSONArray data = new JSONArray();
		JSONArray dataCount = new JSONArray();
		JSONArray name = new JSONArray();
		List<Map<String, Object>> dataList = faultedDetailsDao.getFaultedBarInfo(shiftNo, beginTime, endTime);
		for(Map<String, Object> map:dataList) {
			if(null != map.get("FAULTEDDURATION")) {
				BigDecimal duration = (BigDecimal)map.get("FAULTEDDURATION");
				BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
				data.add(lastDuration);
			} else {
				data.add(0);
			}
			dataCount.add(map.get("FAULTEDCOUNT"));
			name.add(map.get("ENTRYNAME"));
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
	public Map<String, String> getFaultedPieInfo(String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		JSONArray data = new JSONArray();
		JSONArray name = new JSONArray();
		List<Map<String, Object>> dataList = faultedDetailsDao.getFaultedBarInfo(shiftNo, beginTime, endTime);
		for(Map<String, Object> map:dataList) {
			BigDecimal bigDecimal = new BigDecimal("0");
			if(null != map.get("FAULTEDDURATION")) {
				BigDecimal duration = (BigDecimal)map.get("FAULTEDDURATION");
				bigDecimal = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			JSONObject dataObj = new JSONObject();
			dataObj.put("name", map.get("ENTRYNAME"));
			dataObj.put("value", bigDecimal);
			data.add(dataObj);
			
			name.add(map.get("ENTRYNAME"));
		}
		
		JSONObject otherObj = new JSONObject();
		otherObj.put("name", "其他");
		otherObj.put("value", new BigDecimal("0"));
		data.add(otherObj);
		name.add("其他");			
		
		returnMap.put("data", data.toString());
		returnMap.put("name", name.toString());
		return returnMap;
	}	
	@Override
	public Map<String, String> faultedDetailsYearShow(String year, String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> ulocStatusYearDensity = faultedDetailsDao.faultedDetailsYearShow(year, createTimeStart,
				createTimeEnd);
		JSONArray countList = new JSONArray();
		JSONArray minuteList = new JSONArray();
		List<Integer> countMaxList = new ArrayList<Integer>();
		List<Integer> minuteMaxList = new ArrayList<Integer>();
		for (Map<String, Object> map : ulocStatusYearDensity) {
			String createTime = map.get("CREATETIME").toString();
			String minute = map.get("MINUTE").toString();
			int count = Integer.valueOf(map.get("COUNT").toString());
			JSONArray countArray = new JSONArray();
			JSONArray minuteArray = new JSONArray();
			countArray.add(createTime);
			countArray.add(count);
			minuteArray.add(createTime);
			minuteArray.add(minute);
			countList.add(countArray);
			minuteList.add(minuteArray);
			countMaxList.add(count);
			minuteMaxList.add(Double.valueOf(minute).intValue());
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("minute", minuteList.toString());
		map.put("count", countList.toString());
		map.put("year", year);
		map.put("countMax", String.valueOf(Collections.max(countMaxList)));
		map.put("minuteMax", String.valueOf(Collections.max(minuteMaxList)));
		return map;
	}
	
}
