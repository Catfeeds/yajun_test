package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.DateUtils;
import com.wis.mes.dakin.report.dao.TmMetalPlateReportDao;
import com.wis.mes.dakin.report.service.TmMetalPlateReportService;

import net.sf.json.JSONArray;

@Service
public class TmMetalPlateReportServiceImpl implements TmMetalPlateReportService {
	@Autowired
	TmMetalPlateReportDao tmMetalPlateReportDao;
	@Autowired
	TmMetalPlateReportDao metalPlateReportDao;
	

	@Override
	public Map<String, String> getMetalPlateReport(String createTime, Integer runingState) {
		// TODO Auto-generated method stub
		JSONArray warnData = new JSONArray();
		JSONArray unusualData = new JSONArray();

		// 日期显示
		JSONArray dateShow = new JSONArray();
		// 月份显示
		JSONArray mouthShow = new JSONArray();

		List<Map<String, Object>> ms = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String ctime = sdf.format(date);
		if (null == createTime || "" == createTime) {
			createTime = ctime;
		}
		// runingState = "0000000000000100";
		List<Map<String, Object>> metalPlateReport = tmMetalPlateReportDao.getMetalPlateReport(createTime,runingState);
		List<Map<String, Object>> initmouthAndWarmData = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			Map<String, Object> mouths = new HashMap<String, Object>();
			mouths.put("TIMES", (BigDecimal.valueOf(i)));// 构造12个月份
			mouths.put("TOTALCOUNT", 0);
			initmouthAndWarmData.add(mouths);
		}
		metalPlateReport.forEach(list -> {
			Map<String, Object> mouths = new HashMap<String, Object>();
			BigDecimal month = (BigDecimal) list.get("TIMES");
			String month1 = month.stripTrailingZeros().toPlainString();
			BigDecimal totalCount = (BigDecimal) list.get("TOTALCOUNT");
			mouths.put("TIMES", month1);
			mouths.put("TOTALCOUNT", totalCount);
			initmouthAndWarmData.set(Integer.valueOf(month1) - 1, mouths);
		});

		for (Map<String, Object> map : initmouthAndWarmData) {
			if (null != map.get("TIMES") && "" != map.get("TIMES")) {
				mouthShow.add(map.get("TIMES"));
			}
			if (null != map.get("TOTALCOUNT") && "" != map.get("TOTALCOUNT")) {
				warnData.add(map.get("TOTALCOUNT"));
			} else {
				warnData.add(new Integer(0));
			}
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("warnData", warnData.toString());
		returnMap.put("success", "true");
		return returnMap;
	}
	
	@Override
	public Map<String, String> getMetalPlateReportWarnByMouth(String createTime, Integer runingState) {
		List<String> tmpEquipmentNos = metalPlateReportDao.getTmpEquipmentNos();
		Map<String,Object> equipmentMap = new HashMap<String,Object>();
		JSONArray yAxisData =  new JSONArray();
		int eqNum = 0;
		for(String src:tmpEquipmentNos ){
			equipmentMap.put(src, eqNum);
			yAxisData.add(src);
			eqNum++;
		}
		
		int dateNum = 0;
		JSONArray xAxisData = new JSONArray();
		Map<String,Object> dateMap = new HashMap<String,Object>();
		List<String> dataList = DateUtils.getLastDayOfMonth1(2018,8,"");
		for(String data:dataList){
			dateMap.put(data, dateNum);
			String mm = data.split("-")[2];
			xAxisData.add(mm);
			dateNum++;
		}
		
		JSONArray dataArrays = new JSONArray();
		List<Map<String, Object>> metalPlateReportWarnByMouth = tmMetalPlateReportDao.getMetalPlateReportWarnByMouth(createTime, runingState);
		for(Map<String, Object> metalPlateMap:metalPlateReportWarnByMouth){
			JSONArray dataToEqs = new JSONArray();
			String equipmentNo = metalPlateMap.get("EQUIPMENTNO").toString();
			String times = metalPlateMap.get("TIMES").toString();
			String totalnum = metalPlateMap.get("TOTALNUM").toString();
			dataToEqs.add(equipmentMap.get(equipmentNo));
			dataToEqs.add(dateMap.get(times));
			dataToEqs.add(totalnum);
			dataArrays.add(dataToEqs);
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("yAxisData",yAxisData.toString());
		returnMap.put("xAxisData", xAxisData.toString());
		returnMap.put("dataArrays", dataArrays.toString());
		returnMap.put("success", "true");
		return returnMap;
	}
}
