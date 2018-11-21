package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.dakin.report.dao.ReportDao;
import com.wis.mes.dakin.report.service.ReportService;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	private ReportDao dao;
	@Autowired
	private DictEntryService entryService;

	@Override
	public Map<String, String> ngAndShiftNoRealationReport(String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> queryNgGroupShiftnoRelation = dao.queryNgGroupShiftnoRelation(createTimeStart,
				createTimeEnd);
		// 日期
		LinkedHashSet<String> timeArray = new LinkedHashSet<String>();
		// ngname
		Set<String> ngNameSet = new HashSet<String>();

		// key shiftNo value ngNames
		LinkedHashMap<String, Set<String>> shiftNoNgNameMap = new LinkedHashMap<String, Set<String>>();
		// key 日期 key shiftNo + ngNames value 数量
		Map<String, Map<String, String>> timeShiftCount = new HashMap<String, Map<String, String>>();
		for (Map<String, Object> map : queryNgGroupShiftnoRelation) {
			if (map == null) {
				continue;
			}
			String workDate = "";
			if (map.get("WORKDATE") != null) {
				workDate = map.get("WORKDATE").toString();
			}
			String name = "未知";
			if (map.get("NAME") != null) {
				name = map.get("NAME").toString();
			}
			String shiftNo = "";
			if (map.get("SHIFTNO") != null) {
				shiftNo = map.get("SHIFTNO").toString().equals("MORNINGSHIFT")?"A":"B";
				timeArray.add(workDate);
				String keyName = name+shiftNo;
				ngNameSet.add(keyName);
				if (timeShiftCount.containsKey(workDate)) {
					Map<String, String> shiftCount = timeShiftCount.get(workDate);
					shiftCount.put(keyName, map.get("COUNT").toString());
				} else {
					Map<String, String> shiftCount = new HashMap<String, String>();
					shiftCount.put(keyName, map.get("COUNT").toString());
					timeShiftCount.put(workDate, shiftCount);
				}
				if (shiftNoNgNameMap.containsKey(shiftNo)) {
					Set<String> list = shiftNoNgNameMap.get(shiftNo);
					list.add(keyName);
				} else {
					Set<String> list = new HashSet<String>();
					list.add(keyName);
					shiftNoNgNameMap.put(shiftNo, list);
				}
			}
		}
		JSONArray series = new JSONArray();
		for (Map.Entry<String, Set<String>> entry : shiftNoNgNameMap.entrySet()) {
			Set<String> ngNameList = entry.getValue();
			for (String ngName : ngNameList) {
				JSONObject obj = new JSONObject();
				obj.put("name", ngName);
				obj.put("type", "bar");
				obj.put("stack", entry.getKey());
				obj.put("data", getDateData(timeShiftCount, ngName, timeArray));
				series.add(obj);
			}
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("time", JSONArray.fromObject(timeArray).toString());
		returnMap.put("series", series.toString());
		returnMap.put("ngName", JSONArray.fromObject(ngNameSet).toString());
		return returnMap;
	}

	private JSONArray getDateData(Map<String, Map<String, String>> timeShiftCount, String shiftNoNgName,
			LinkedHashSet<String> timeArray) {
		JSONArray array = new JSONArray();
		for (String time : timeArray) {
			Map<String, String> map = timeShiftCount.get(time);
			if (map.containsKey(shiftNoNgName)) {
				array.add(map.get(shiftNoNgName));
			} else {
				array.add("0");
			}
		}
		return array;
	}

	@Override
	public Map<String, String> ngAndCountRealationReport(String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> queryNgGroupCount = dao.queryNgGroupCount(createTimeStart, createTimeEnd);
		JSONArray ngArray = new JSONArray();
		JSONArray ngCountArray = new JSONArray();
		for (Map<String, Object> map : queryNgGroupCount) {
			if (map == null) {
				continue;
			}
			String name = "其它";
			if (map.get("NAME") != null) {
				name = map.get("NAME").toString();
			}
			ngArray.add(name);
			ngCountArray.add(map.get("COUNT").toString());
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("count", ngCountArray.toString());
		returnMap.put("ngName", ngArray.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> ngInfoOriginReport(String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> ngAndInfoOrigin = dao.ngAndInfoOrigin(createTimeStart, createTimeEnd);
		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.INFO_SOURCES);
		JSONArray ngArray = new JSONArray();
		JSONArray ngCountArray = new JSONArray();
		Map<String, List<String>> ngInfoOrigin = new HashMap<String, List<String>>();
		Set<String> infoSourcesArray = new HashSet<String>();
		Map<String, String> nameCountMap = new HashMap<String, String>();
		for (Map<String, Object> map : ngAndInfoOrigin) {
			String name = map.get("NAME").toString();
			String infoSources = map.get("INFOSOURCES").toString();
			ngArray.add(name);
			String count = map.get("COUNT").toString();
			ngCountArray.add(count);
			String infoSourceName = "未知";
			if (mapTypeCode.containsKey(infoSources)) {
				infoSourceName = mapTypeCode.get(infoSources).getName();
			}
			infoSourcesArray.add(infoSourceName);
			if (ngInfoOrigin.containsKey(infoSourceName)) {
				ngInfoOrigin.get(infoSourceName).add(name);
			} else {
				List<String> list = new ArrayList<String>();
				list.add(name);
				ngInfoOrigin.put(infoSourceName, list);
			}
			nameCountMap.put(name, count);
		}
		JSONArray seriesArray = new JSONArray();
		for (String infoSource : infoSourcesArray) {
			JSONArray countArray = new JSONArray();
			List<String> list = ngInfoOrigin.get(infoSource);
			for (Object obj : ngArray) {
				String name = obj.toString();
				if (list.contains(name)) {
					countArray.add(nameCountMap.get(name));
				} else {
					countArray.add(0);
				}
			}
			JSONObject object = new JSONObject();
			object.put("name", infoSource);
			object.put("type", "bar");
			object.put("stack", "总量");
			object.put("data", countArray.toString());
			seriesArray.add(object);
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("content", ngArray.toString());
		returnMap.put("infoSourceName", JSONArray.fromObject(infoSourcesArray).toString());
		returnMap.put("series", seriesArray.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> ngInfoOriginReportPIE(String createTimeStart, String createTimeEnd) {
//		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.INFO_SOURCES);
		List<Map<String, Object>> ngAndInfoOrigin = dao.ngAndInfoOrigin(createTimeStart, createTimeEnd);
		JSONArray ngArray = new JSONArray();
		JSONArray infoSourceArray = new JSONArray();
		JSONArray ngCountArray = new JSONArray();
		Map<String, Integer> InfoSourceCount = new HashMap<String, Integer>();
		for (Map<String, Object> map : ngAndInfoOrigin) {
			String name = map.get("NAME").toString();
			int count = Integer.valueOf(map.get("COUNT").toString());
			//String infoSources = map.get("INFOSOURCES").toString();
			/*if (mapTypeCode.containsKey(infoSources)) {
				infoSources = mapTypeCode.get(infoSources).getName();
			}
			if (InfoSourceCount.containsKey(infoSources)) {
				Integer integer = InfoSourceCount.get(infoSources);
				InfoSourceCount.put(infoSources, integer + count);
			} else {
				InfoSourceCount.put(infoSources, count);
				infoSourceArray.add(infoSources);
			}*/
			JSONObject ngDataMap = new JSONObject();
			ngDataMap.put("value", count);
			ngDataMap.put("name", name);
			ngArray.add(name);
			ngCountArray.add(ngDataMap);
		}
		infoSourceArray.addAll(ngArray);
		JSONArray infoSourceCountArray = new JSONArray();
		decisionType(createTimeStart,createTimeEnd,infoSourceArray,infoSourceCountArray);
		/*for (Map.Entry<String, Integer> entry : InfoSourceCount.entrySet()) {
			JSONObject infoSourceMap = new JSONObject();
			infoSourceMap.put("value", entry.getValue());
			infoSourceMap.put("name", entry.getKey());
			infoSourceCountArray.add(infoSourceMap);
		}*/
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("infoSourceArray", infoSourceArray.toString());
		returnMap.put("ngCountArray", ngCountArray.toString());
		returnMap.put("infoSourceCountArray", infoSourceCountArray.toString());
		return returnMap;
	}

	private void decisionType(String createTimeStart,String createTimeEnd,JSONArray ngArray,JSONArray infoSourceCountArray) {
		List<Map<String, Object>> queryNgGroupCount = dao.queryNgGroupCount(createTimeStart, createTimeEnd);
		for (Map<String, Object> map : queryNgGroupCount) {
			if (map == null) {
				continue;
			}
			JSONObject infoSourceMap = new JSONObject();
			String name = "其它";
			if (map.get("NAME") != null) {
				name = map.get("NAME").toString();
			}
			ngArray.add(name);
			infoSourceMap.put("name", name);
			infoSourceMap.put("value", map.get("COUNT").toString());
			infoSourceCountArray.add(infoSourceMap);
		}
	}
	
	@Override
	public Map<String, String> ulocStatusTimeCountReport(String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> ulocStatusTimeCount = dao.ulocStatusTimeCount(createTimeStart, createTimeEnd);
		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_SOURCE);
		JSONArray abnormalSourceList = new JSONArray();
		JSONArray timeArray = new JSONArray();
		JSONArray countArray = new JSONArray();
		for (Map<String, Object> map : ulocStatusTimeCount) {
			String minute = map.get("MINUTE").toString();
			String count = map.get("COUNT").toString();
			String abnormalSource = "";
			if (map.get("ABNORMALSOURCE") == null) {
				abnormalSource = "其它";
			} else {
				abnormalSource = map.get("ABNORMALSOURCE").toString();
				if (mapTypeCode.containsKey(abnormalSource)) {
					abnormalSource = mapTypeCode.get(abnormalSource).getName();
				} else {
					abnormalSource = "其它";
				}
			}
			abnormalSourceList.add(abnormalSource);
			timeArray.add(minute);
			countArray.add(count);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("source", abnormalSourceList.toString());
		map.put("time", timeArray.toString());
		map.put("count", countArray.toString());
		return map;
	}

	@Override
	public Map<String, String> ulocStatusCountAndDetailReport(String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> ulocStatusCountAndDetail = dao.ulocStatusCountAndDetail(createTimeStart,
				createTimeEnd);
		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_SOURCE);
		JSONArray abnormalSouce = new JSONArray();
		JSONArray abnormalContent = new JSONArray();
		JSONArray abnormalContentData = new JSONArray();
		for (Map<String, Object> map : ulocStatusCountAndDetail) {
			String code = map.get("ABNORMALSOURCE").toString();// 异常源
			String codeDesc = map.get("CODEDESC").toString();// 异常描述
			Double minute =Double.valueOf(map.get("MINUTE").toString());// 数量
			abnormalContent.add(codeDesc);
			if (mapTypeCode.containsKey(code)) {
				code = mapTypeCode.get(code).getName();
			} else {
				code = "其它未知异常";
			}
			JSONObject obj = new JSONObject();
			obj.put("value", minute);
			obj.put("name", codeDesc);
			abnormalContentData.add(obj);
		}
		//信息来源
		List<Map<String, Object>> ulocStatusTimeCountMap = dao.ulocStatusTimeCount(createTimeStart, createTimeEnd);
		JSONArray abnormalSourceData = new JSONArray();
		for(Map<String, Object> ulocStatusTimeCount:ulocStatusTimeCountMap) {
			String minute = ulocStatusTimeCount.get("MINUTE").toString();
			String abnormalSource = "";
			if (null != ulocStatusTimeCount.get("ABNORMALSOURCE")) {
				abnormalSource = ulocStatusTimeCount.get("ABNORMALSOURCE").toString();
				JSONObject obj = new JSONObject();
				obj.put("value", minute);
				obj.put("name", switchCode(abnormalSource));
				abnormalSourceData.add(obj);
			}
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("abnormalContentData", abnormalContentData.toString());
		map.put("abnormalSourceData", abnormalSourceData.toString());
		abnormalSouce.addAll(abnormalContent);
		map.put("abnormal", abnormalSouce.toString());
		return map;
	}

	private String switchCode(String code) {
		String name= "";
		switch (code) {
			case "L":
				name = "线体源";
				break;
			case "W":
				name = "作业源";
				break;
			case "E":
				name = "设备源";
				break;
		}		
		return name;
	}
	
	@Override
	public Map<String, String> ulocStatusAbnormalSourceCountAndMintue(String abnormalSource, String createTimeStart,
			String createTimeEnd) {
		List<Map<String, Object>> ulocStatusAbnormalSourceCountAndMintue = dao
				.ulocStatusAbnormalSourceCountAndMintue(abnormalSource, createTimeStart, createTimeEnd);
		JSONArray abnormalSourceList = new JSONArray();
		JSONArray timeArray = new JSONArray();
		JSONArray countArray = new JSONArray();
		for (Map<String, Object> map : ulocStatusAbnormalSourceCountAndMintue) {
			String minute = map.get("MINUTE").toString();
			String count = map.get("COUNT").toString();
			String codeDesc = "";
			if (map.get("CODEDESC") == null) {
				codeDesc = "其它";
			} else {
				codeDesc = map.get("CODEDESC").toString();
			}
			abnormalSourceList.add(codeDesc);
			timeArray.add(minute);
			countArray.add(count);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("source", abnormalSourceList.toString());
		map.put("time", timeArray.toString());
		map.put("count", countArray.toString());
		return map;
	}

	@Override
	public Map<String, String> ulocStatusYearDensity(String year, String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> ulocStatusYearDensity = dao.ulocStatusYearDensity(year, createTimeStart,
				createTimeEnd);
		JSONArray countList = new JSONArray();
		JSONArray minuteList = new JSONArray();
		int max = 0;
		for (Map<String, Object> map : ulocStatusYearDensity) {
			String createTime = map.get("CREATETIME").toString();
			String minute = map.get("MINUTE").toString();
			int count = Integer.valueOf(map.get("COUNT").toString());
			if (count > max) {
				max = count;
			}
			JSONArray countArray = new JSONArray();
			JSONArray minuteArray = new JSONArray();
			countArray.add(createTime);
			countArray.add(count);
			minuteArray.add(createTime);
			minuteArray.add(minute);
			countList.add(countArray);
			minuteList.add(minuteArray);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("minute", minuteList.toString());
		map.put("count", countList.toString());
		map.put("year", year);
		map.put("max", String.valueOf(max));
		return map;
	}

	@Override
	public Map<String, String> energReportBar(String timeFormate, String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> energReport = dao.energReport(timeFormate, createTimeStart, createTimeEnd);
		JSONArray timeArray = new JSONArray();
		JSONArray acEnergyArray = new JSONArray();
		JSONArray deviceEnergyArray = new JSONArray();
		JSONArray lightEnergyArray = new JSONArray();
		JSONArray totalEnergyArray = new JSONArray();
		JSONArray timeEnergyArray = new JSONArray();
		for (Map<String, Object> map : energReport) {
			BigDecimal acEnergy = new BigDecimal(String.valueOf(map.get("AC_ENERGY")));// 空调能耗
			BigDecimal deviceEnergy = new BigDecimal(String.valueOf(map.get("DEVICE_ENERGY")));// 设备能耗
			BigDecimal lightEnergy = new BigDecimal(String.valueOf(map.get("LIGHT_ENERGY")));// 照明能耗
			BigDecimal totalEnergy = acEnergy.add(deviceEnergy).add(lightEnergy);
			totalEnergyArray.add(totalEnergy);
			acEnergyArray.add(acEnergy);
			deviceEnergyArray.add(deviceEnergy);
			lightEnergyArray.add(lightEnergy);
			timeArray.add(String.valueOf(map.get("TIME")));

			JSONArray timeEnergy = new JSONArray();
			JSONObject object = new JSONObject();
			object = new JSONObject();
			object.put("name", "设备能耗N");
			object.put("value", deviceEnergy);
			timeEnergy.add(object);
			object = new JSONObject();
			object.put("name", "空调能耗N");
			object.put("value", acEnergy);
			timeEnergy.add(object);
			object = new JSONObject();
			object.put("name", "照明能耗N");
			object.put("value", lightEnergy);
			timeEnergy.add(object);
			timeEnergyArray.add(timeEnergy);
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("time", timeArray.toString());
		returnMap.put("timeEnergy", timeEnergyArray.toString());
		returnMap.put("acEnergy", acEnergyArray.toString());
		returnMap.put("deviceEnergy", deviceEnergyArray.toString());
		returnMap.put("lightEnergy", lightEnergyArray.toString());
		returnMap.put("totalEnergy", totalEnergyArray.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> energReportPic(String timeFormate, String createTimeStart, String createTimeEnd) {
		String year = "";
		if (StringUtil.isNotEmpty(createTimeStart)) {
			year = createTimeStart.substring(0, 4);
		} else {
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR) + "";
		}
		String[] months = new String[] { year + "-01", year + "-02", year + "-03", year + "-04", year + "-05",
				year + "-06", year + "-07", year + "-08", year + "-09", year + "-10", year + "-11", year + "-12" };
		List<Map<String, Object>> energReport = dao.energReport(timeFormate, createTimeStart, createTimeEnd);
		JSONObject timeTotalEnergyMap = new JSONObject();
		JSONObject timeAcEnergyMap = new JSONObject();
		JSONObject timeDeviceEnergyMap = new JSONObject();
		JSONObject timeLightEnergyMap = new JSONObject();
		for (Map<String, Object> map : energReport) {
			BigDecimal acEnergy = new BigDecimal(String.valueOf(map.get("AC_ENERGY")));// 空调能耗
			BigDecimal deviceEnergy = new BigDecimal(String.valueOf(map.get("DEVICE_ENERGY")));// 设备能耗
			BigDecimal lightEnergy = new BigDecimal(String.valueOf(map.get("LIGHT_ENERGY")));// 照明能耗
			BigDecimal totalEnergy = acEnergy.add(deviceEnergy).add(lightEnergy);// 总能耗
			String time = String.valueOf(map.get("TIME"));// 时间
			timeTotalEnergyMap.put(time, totalEnergy);
			timeAcEnergyMap.put(time, acEnergy);
			timeDeviceEnergyMap.put(time, deviceEnergy);
			timeLightEnergyMap.put(time, lightEnergy);
		}
		JSONArray acEnergyArray = new JSONArray();
		acEnergyArray.add("空调能耗N");
		JSONArray deviceEnergyArray = new JSONArray();
		deviceEnergyArray.add("设备能耗N");
		JSONArray lightEnergyArray = new JSONArray();
		lightEnergyArray.add("照明能耗N");
		JSONArray totalEnergyArray = new JSONArray();
		for (String month : months) {
			if (timeTotalEnergyMap.containsKey(month)) {
				acEnergyArray.add(timeAcEnergyMap.get(month));
				deviceEnergyArray.add(timeDeviceEnergyMap.get(month));
				lightEnergyArray.add(timeLightEnergyMap.get(month));
				totalEnergyArray.add(timeTotalEnergyMap.get(month));
			} else {
				acEnergyArray.add(0);
				deviceEnergyArray.add(0);
				lightEnergyArray.add(0);
				totalEnergyArray.add(0);
			}
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("totalEnergy", getContantTotalEnergyArray(totalEnergyArray).toString());
		returnMap.put("acEnergy", getContantTotalEnergyArray(acEnergyArray).toString());
		returnMap.put("deviceEnergy", getContantTotalEnergyArray(deviceEnergyArray).toString());
		returnMap.put("lightEnergy", getContantTotalEnergyArray(lightEnergyArray).toString());
		return returnMap;
	}

	private JSONArray getContantTotalEnergyArray(JSONArray array) {
		BigDecimal bigDecimal = new BigDecimal("0");
		for (int i = 1; i < array.size(); i++) {
			bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(array.get(i))));
		}
		array.add(bigDecimal);
		return array;
	}

	@Override
	public Map<String, String> plcEnergyReport(String startDateTime, String endDateTime) {
		Map<String, Object> plcEnergyReport = dao.plcEnergyReport(startDateTime, endDateTime);
		JSONArray dataArray = new JSONArray();
		if (plcEnergyReport != null && !plcEnergyReport.isEmpty()) {
			JSONObject obj = new JSONObject();
			obj.put("value", plcEnergyReport.get("AC_ENERGY"));
			obj.put("name", "空调能耗");
			dataArray.add(obj);
			obj = new JSONObject();
			obj.put("value", plcEnergyReport.get("DEVICE_ENERGY"));
			obj.put("name", "设备能耗");
			dataArray.add(obj);
			obj = new JSONObject();
			obj.put("value", plcEnergyReport.get("LIGHT_ENERGY"));
			obj.put("name", "照明能耗");
			dataArray.add(obj);
		}
		Map<String,String>resultMap = new HashMap<String, String>();
		resultMap.put("dataArray", dataArray.toString());
		return resultMap;
	}
}
