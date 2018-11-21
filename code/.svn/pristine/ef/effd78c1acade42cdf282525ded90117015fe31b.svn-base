package com.wis.mes.dakin.report.service.impl;

import com.wis.mes.dakin.report.dao.FinNoFaultDetailsDao;
import com.wis.mes.dakin.report.service.TbNoFaultDetailsService;
import com.wis.mes.master.equipment.dao.TmpEquipmentStatusDao;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TbNoFaultDetailsServiceImpl implements TbNoFaultDetailsService {
	
	@Autowired
	private FinNoFaultDetailsDao finNoFaultDetailsDao;

    @Autowired
    private TmpEquipmentStatusDao tmpEquipmentStatusDao;


    @Override
	public Map<String, String> getFinNoFaultDetails(String createTimeStart, String createTimeEnd) {
		Map<String, String> returnMap = new HashMap<String, String>();
		//白班
		JSONArray whiteShiftNoFailStopData = new JSONArray();
		JSONArray whiteShiftSwitchStopData = new JSONArray();
		JSONArray whiteShiftChangeMaterialStopData = new JSONArray();
		JSONArray whiteShiftAddOilStopData = new JSONArray();
		JSONArray whiteShiftNoFailStopMinData = new JSONArray();
		JSONArray whiteShiftSwitchStopDurationData = new JSONArray();
		JSONArray whiteShiftChangeMaterialStopDurationData = new JSONArray();
		JSONArray whiteShiftAddOilStopDurationData = new JSONArray();
		//晚班
		JSONArray nightShiftNoFailStopData = new JSONArray();
		JSONArray nightShiftSwitchStopData = new JSONArray();
		JSONArray nightShiftChangeMaterialStopData = new JSONArray();
		JSONArray nightShiftAddOilStopData = new JSONArray();
		JSONArray nightShiftNoFailStopMinData = new JSONArray();
		JSONArray nightShiftSwitchStopDurationData = new JSONArray();
		JSONArray nightShiftChangeMaterialStopDurationData = new JSONArray();
		JSONArray nightShiftAddOilStopDurationData = new JSONArray();
		//日期显示
		JSONArray dateShow = new JSONArray();
		//星期显示
		JSONArray weekShow = new JSONArray();
		//非故障停机
		List<Map<String, Object>> finNoFaultDetailsList = finNoFaultDetailsDao.getFinNoFaultStopDetails(createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finNoFaultDetailsList) {
			whiteShiftNoFailStopData.add(map.get("WHITESHIFT"));
			nightShiftNoFailStopData.add(map.get("NIGHTSHIFT"));
			dateShow.add(map.get("DATESHOW"));
			weekShow.add(map.get("WEEKSHOW"));
			BigDecimal whiteDuration = (BigDecimal)map.get("WHITEDURATIONCOUNT");
			BigDecimal lastWhiteDuration = new BigDecimal("0");
			if (null != whiteDuration) {
				lastWhiteDuration = whiteDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal lastNightDuration = new BigDecimal("0");
			BigDecimal nightDuration = (BigDecimal)map.get("NIGHTDURATIONCOUNT");
			if (null != nightDuration) {
				lastNightDuration = nightDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			whiteShiftNoFailStopMinData.add(lastWhiteDuration);
			nightShiftNoFailStopMinData.add(lastNightDuration);
		}
		//切换停机
		List<Map<String, Object>> finSwitchStopList = finNoFaultDetailsDao.getFinSwitchStopDetails(createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finSwitchStopList) {
			whiteShiftSwitchStopData.add(map.get("WHITESHIFT"));
			nightShiftSwitchStopData.add(map.get("NIGHTSHIFT"));
			BigDecimal whiteDuration = (BigDecimal)map.get("WHITEDURATIONCOUNT");
			BigDecimal lastWhiteDuration = new BigDecimal("0");
			if (null != whiteDuration) {
				lastWhiteDuration = whiteDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal lastNightDuration = new BigDecimal("0");
			BigDecimal nightDuration = (BigDecimal)map.get("NIGHTDURATIONCOUNT");
			if (null != nightDuration) {
				lastNightDuration = nightDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			whiteShiftSwitchStopDurationData.add(lastWhiteDuration);
			nightShiftSwitchStopDurationData.add(lastNightDuration);
		}
		//换料停机
		List<Map<String, Object>> finChangeMaterialStopList = finNoFaultDetailsDao.getFinChangeMaterialStopDetails(createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finChangeMaterialStopList) {
			whiteShiftChangeMaterialStopData.add(map.get("WHITESHIFT"));
			nightShiftChangeMaterialStopData.add(map.get("NIGHTSHIFT"));
			BigDecimal whiteDuration = (BigDecimal)map.get("WHITEDURATIONCOUNT");
			BigDecimal lastWhiteDuration = new BigDecimal("0");
			if (null != whiteDuration) {
				lastWhiteDuration = whiteDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal lastNightDuration = new BigDecimal("0");
			BigDecimal nightDuration = (BigDecimal)map.get("NIGHTDURATIONCOUNT");
			if (null != nightDuration) {
				lastNightDuration = nightDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			whiteShiftChangeMaterialStopDurationData.add(lastWhiteDuration);
			nightShiftChangeMaterialStopDurationData.add(lastNightDuration);
		}
		//加油停机
		List<Map<String, Object>> finAddOilStopList = finNoFaultDetailsDao.getFinAddOilStopDetails(createTimeStart, createTimeEnd);
		for(Map<String, Object> map : finAddOilStopList) {
			whiteShiftAddOilStopData.add(map.get("WHITESHIFT"));
			nightShiftAddOilStopData.add(map.get("NIGHTSHIFT"));
			BigDecimal whiteDuration = (BigDecimal)map.get("WHITEDURATIONCOUNT");
			BigDecimal lastWhiteDuration = new BigDecimal("0");
			if (null != whiteDuration) {
				lastWhiteDuration = whiteDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal lastNightDuration = new BigDecimal("0");
			BigDecimal nightDuration = (BigDecimal)map.get("NIGHTDURATIONCOUNT");
			if (null != nightDuration) {
				lastNightDuration = nightDuration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
			}
			whiteShiftAddOilStopDurationData.add(lastWhiteDuration);
			nightShiftAddOilStopDurationData.add(lastNightDuration);
		}
		returnMap.put("whiteShiftNoFailStopData", whiteShiftNoFailStopData.toString());
		returnMap.put("nightShiftNoFailStopData", nightShiftNoFailStopData.toString());
		returnMap.put("whiteShiftNoFailStopMinData", whiteShiftNoFailStopMinData.toString());
		returnMap.put("nightShiftNoFailStopMinData", nightShiftNoFailStopMinData.toString());
		
		returnMap.put("dateShow", dateShow.toString());
		returnMap.put("weekShow", weekShow.toString());
		
		returnMap.put("whiteShiftSwitchStopData", whiteShiftSwitchStopData.toString());
		returnMap.put("nightShiftSwitchStopData", nightShiftSwitchStopData.toString());
		returnMap.put("whiteShiftSwitchStopDurationData", whiteShiftSwitchStopDurationData.toString());
		returnMap.put("nightShiftSwitchStopDurationData", nightShiftSwitchStopDurationData.toString());
		
		returnMap.put("whiteShiftChangeMaterialStopData", whiteShiftChangeMaterialStopData.toString());
		returnMap.put("nightShiftChangeMaterialStopData", nightShiftChangeMaterialStopData.toString());
		returnMap.put("whiteShiftChangeMaterialStopDurationData", whiteShiftChangeMaterialStopDurationData.toString());
		returnMap.put("nightShiftChangeMaterialStopDurationData", nightShiftChangeMaterialStopDurationData.toString());
		
		returnMap.put("whiteShiftAddOilStopData", whiteShiftAddOilStopData.toString());
		returnMap.put("nightShiftAddOilStopData", nightShiftAddOilStopData.toString());
		returnMap.put("whiteShiftAddOilStopDurationData", whiteShiftAddOilStopDurationData.toString());
		returnMap.put("nightShiftAddOilStopDurationData", nightShiftAddOilStopDurationData.toString());
		
		return returnMap;
	}

	@Override
	public Map<String, String> noFaultedDetailsYearShow(String year, String createTimeStart, String createTimeEnd) {
		List<Map<String, Object>> ulocStatusYearDensity = finNoFaultDetailsDao.noFaultedDetailsYearShow(year, createTimeStart,
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
