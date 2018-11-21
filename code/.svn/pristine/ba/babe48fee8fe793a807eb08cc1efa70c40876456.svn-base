package com.wis.mes.dakin.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.mes.dakin.report.dao.StationDataAnalysisDao;
import com.wis.mes.dakin.report.service.StationDataAnalysisService;

import net.sf.json.JSONArray;

@Service
public class StationDataAnalysisServiceImpl implements StationDataAnalysisService {

	@Autowired
	private StationDataAnalysisDao dao;
	
	public Map<String,Object> stationDataAnalysisData(String type,String createTimeStart, String createTimeEnd){
		Map<String,Object> resultMap =  null;
		if(ConstantUtils.EQUAL_NUMBER_WORK.equals(type)){
			resultMap  = assembleData(dao.equalNumberWork(createTimeStart, createTimeEnd));
		}else if(ConstantUtils.TIGHT_STOP_TIMES.equals(type)){
			resultMap = assembleData(dao.getUlocStatusReport(createTimeStart, createTimeEnd, ConstantUtils.CHECKBIT_SUSPEND));
		}else if(ConstantUtils.NUMBER_REQUESTS_HELP.equals(type)){
			resultMap = assembleData(dao.getUlocStatusReport(createTimeStart, createTimeEnd, ConstantUtils.CHECKBIT_FALL_BACK));
		}else if(ConstantUtils.FREQUENCY_STATISTICS.equals(type)){
			resultMap = assembleData(dao.getUlocStatusReport(createTimeStart, createTimeEnd, ConstantUtils.CHECKBIT_TIGHT_STOP));
		}
		return resultMap;
	}
	
	private Map<String,Object> assembleData(List<Map<String,Object>> listMap){
		Map<String,Object> resultMap =  new HashMap<String,Object>();
		JSONArray stationDatas = new JSONArray();
		JSONArray timeOutData = new JSONArray();
		JSONArray timeOffData = new JSONArray();
		for(Map<String,Object> map: listMap){
			if(null != map.get("ULOC_NO")){
				stationDatas.add(map.get("ULOC_NO"));
				timeOutData.add(map.get("TIME_OUT"));
				timeOffData.add(map.get("TIME_OFF"));
			}
		}
		resultMap.put("stationDatas", stationDatas);
		resultMap.put("timeOutData", timeOutData);
		resultMap.put("timeOffData", timeOffData);
		return  resultMap;
	}
}
