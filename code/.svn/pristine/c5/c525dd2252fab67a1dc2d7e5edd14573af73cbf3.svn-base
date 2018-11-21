/**
 * 
 */
package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.mes.dakin.report.dao.YearReportServiceDao;
import com.wis.mes.dakin.report.service.YearReportService;

import net.sf.json.JSONArray;

/**
 * @author caixia
 *
 */
@Service
public class YearReportServiceImpl implements YearReportService {
	@Autowired
	YearReportServiceDao yearReportServiceDao;
	@Override
	public Map<String, List> getMetalPlateYearDetailPieReportInfo(String shiftNo,
			String beginTime, String endTime) {
		Map<String, List> metalPlateYearPie = yearReportServiceDao.getMetalPlateYearDetailPieReportInfo(shiftNo, beginTime, endTime);
		return metalPlateYearPie;
	}
	
	@Override
	public List getMetalPlateYearListReport(String shiftNo, String beginTime,
			String endTime) {
		// TODO Auto-generated method stub
		Map<String, String> year = new HashMap<>();
		if(StringUtils.isNotEmpty(beginTime) && !StringUtils.isNotEmpty(endTime)){
			year.put("year", beginTime.split("-")[0]);
		}else if(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			year.put("year", endTime.split("-")[0]);
		}
		else if(!StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			year.put("year", endTime.split("-")[0]);
		}else{
			/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = new Date();
			String years = sdf.format(date);*/
			Calendar cal = Calendar.getInstance();
			String years = String.valueOf(cal.get(Calendar.YEAR));
			year.put("year", years);
		}
		
		List dataList = new ArrayList();
		List<Map<String,Object>> yearListReport = yearReportServiceDao.getMetalPlateYearListReport(shiftNo, beginTime, endTime);
		Map<String, String> yearCount = getMetalPlateYearCountShiftReport("",shiftNo,beginTime,endTime);
		Map<String, String> yearTime = getMetalPlateYearTimeReportInfo("",shiftNo,beginTime,endTime);
		
		dataList.add(yearListReport);
		dataList.add(yearCount);
		dataList.add(yearTime);
		dataList.add(year);
		
		return dataList;
	}

	@Override
	public Map<String, String> getMetalPlateYearCountShiftReport(String whitOrNight,String shiftNo,
			String beginTime, String endTime) {
		
		Map<String,Object> statusMap = new HashMap<String, Object>();
		
		// TODO Auto-generated method stub
		//白班
				JSONArray whiteShiftxData1 = new JSONArray();
				JSONArray whiteShiftxData2 = new JSONArray();
				JSONArray whiteShiftxData3 = new JSONArray();
				JSONArray whiteShiftxData4 = new JSONArray();
				JSONArray whiteShiftxData5 = new JSONArray();
				JSONArray whiteShiftxData6 = new JSONArray();
				JSONArray whiteShiftxData7 = new JSONArray();
				JSONArray whiteShiftxData8 = new JSONArray();
				List<Map<String, Object>> mouthList = new ArrayList<Map<String, Object>>();
				for (int i = 1; i <= 12; i++) {
					statusMap = new HashMap<String, Object>();
					statusMap.put("故障", null);
					statusMap.put("紧急停止", null);
					statusMap.put("暂停", null);
					statusMap.put("周期停止", null);
					statusMap.put("换料停机", null);
					statusMap.put("示教停机", null);
					statusMap.put("程序切换", null);
					statusMap.put("夹具切换", null);
					statusMap.put("模具切换", null);
					
					Map<String, Object> mouths = new HashMap<String, Object>();
					mouths.put(i+"", statusMap);
					mouthList.add(mouths);
				}
		
		List<Map<String,Object>> metalPlateYearDetailearShiftReport = yearReportServiceDao.getMetalPlateYearDetailearShiftReport(whitOrNight,shiftNo, beginTime, endTime);
		for(int i=1;i<mouthList.size();i++){
			for(int j=0;j<metalPlateYearDetailearShiftReport.size();j++){
				if(metalPlateYearDetailearShiftReport.get(j).get("MOUTH").equals(BigDecimal.valueOf(i))){
					//次数
					statusMap=(Map<String, Object>) mouthList.get(i-1).get(i+"");
					statusMap.put((String) metalPlateYearDetailearShiftReport.get(j).get("CODE_DESC"), metalPlateYearDetailearShiftReport.get(j).get("TOTALCOUNT"));
				}
		}
		}
		
		for(int i=0;mouthList.size()>i;i++ ){
			Map<String, Object> map = (Map<String, Object>)mouthList.get(i).get(i+1+"");
			BigDecimal gz=BigDecimal.valueOf(0);
			BigDecimal jt=BigDecimal.valueOf(0);
			BigDecimal zt=BigDecimal.valueOf(0);
			BigDecimal zqtz=BigDecimal.valueOf(0);
			BigDecimal hltj=BigDecimal.valueOf(0);
			BigDecimal jstj=BigDecimal.valueOf(0);
			BigDecimal cxqh=BigDecimal.valueOf(0);
			BigDecimal jjqh=BigDecimal.valueOf(0);
			BigDecimal mmqh=BigDecimal.valueOf(0);
			
			if(null != map.get("故障")){
				gz= (BigDecimal) map.get("故障");
			}
			if(null != map.get("紧急停止")){
				jt=(BigDecimal) map.get("紧急停止");
			}
			if(null != map.get("暂停")){
				zt= (BigDecimal) map.get("暂停");
			}
			
			if(null != map.get("周期停止")){
				zqtz= (BigDecimal) map.get("周期停止");
			}
			if(null != map.get("换料停机")){
				hltj= (BigDecimal) map.get("换料停机");
			}
			if(null != map.get("示教停机")){
				jstj= (BigDecimal) map.get("示教停机");
			}
			if(null != map.get("程序切换")){
				cxqh= (BigDecimal) map.get("程序切换");
			}
			if(null != map.get("夹具切换")){
				jjqh= (BigDecimal) map.get("夹具切换");
			}
			if(null != map.get("模具切换")){
				mmqh= (BigDecimal) map.get("模具切换");
			}
			whiteShiftxData1.add(gz.add(jt));
			whiteShiftxData2.add(zt);
			whiteShiftxData3.add(zqtz);
			whiteShiftxData4.add(hltj);
			whiteShiftxData5.add(jstj);
			whiteShiftxData6.add(cxqh);
			whiteShiftxData7.add(jjqh);
			whiteShiftxData8.add(mmqh);
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("gz",whiteShiftxData1.toString() );
		returnMap.put("zt", whiteShiftxData2.toString());
		returnMap.put("zqtz",whiteShiftxData3.toString());
		returnMap.put("hltj",whiteShiftxData4.toString());
		returnMap.put("jstj",whiteShiftxData5.toString());
		returnMap.put("cxqh",whiteShiftxData6.toString());
		returnMap.put("jjqh",whiteShiftxData7.toString());
		returnMap.put("mmqh",whiteShiftxData8.toString());
		return returnMap;
	}


	@Override
	public Map<String, String> getMetalPlateYearTimeReportInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String,Object> timeMap = new HashMap<String, Object>();
		
		// TODO Auto-generated method stub
		//白班
				JSONArray nightShiftxData1 = new JSONArray();
				JSONArray nightShiftxData2 = new JSONArray();
				JSONArray nightShiftxData3 = new JSONArray();
				JSONArray nightShiftxData4 = new JSONArray();
				JSONArray nightShiftxData5 = new JSONArray();
				JSONArray nightShiftxData6 = new JSONArray();
				JSONArray nightShiftxData7 = new JSONArray();
				JSONArray nightShiftxData8 = new JSONArray();
				List<Map<String, Object>> mouthList = new ArrayList<Map<String, Object>>();
				for (int i = 1; i <= 12; i++) {
					
					timeMap = new HashMap<String, Object>();
					timeMap.put("故障", null);
					timeMap.put("紧急停止", null);
					timeMap.put("暂停", null);
					timeMap.put("周期停止", null);
					timeMap.put("换料停机", null);
					timeMap.put("示教停机", null);
					timeMap.put("程序切换", null);
					timeMap.put("夹具切换", null);
					timeMap.put("模具切换", null);
					Map<String, Object> mouths = new HashMap<String, Object>();
					mouths.put(i+"", timeMap);
					mouthList.add(mouths);
				}
		
		List<Map<String,Object>> metalPlateYearDetailearShiftReport = yearReportServiceDao.getMetalPlateYearDetailearShiftReport(whitOrNight,shiftNo, beginTime, endTime);
		for(int i=1;i<mouthList.size();i++){
			for(int j=0;j<metalPlateYearDetailearShiftReport.size();j++){
				if(metalPlateYearDetailearShiftReport.get(j).get("MOUTH").equals(BigDecimal.valueOf(i))){
					//时间
					timeMap=(Map<String, Object>) mouthList.get(i-1).get(i+"");
					BigDecimal duration = (BigDecimal)metalPlateYearDetailearShiftReport.get(j).get("DURATION");
					BigDecimal lastDuration = duration.divide(new BigDecimal("60"),2,BigDecimal.ROUND_HALF_UP);
					timeMap.put((String) metalPlateYearDetailearShiftReport.get(j).get("CODE_DESC"), lastDuration);
				}
		}
		}
		
		for(int i=0;mouthList.size()>i;i++ ){
			Map<String, Object> map = (Map<String, Object>)mouthList.get(i).get(i+1+"");
			BigDecimal gz=BigDecimal.valueOf(0);
			BigDecimal jt=BigDecimal.valueOf(0);
			BigDecimal zt=BigDecimal.valueOf(0);
			BigDecimal zqtz=BigDecimal.valueOf(0);
			BigDecimal hltj=BigDecimal.valueOf(0);
			BigDecimal jstj=BigDecimal.valueOf(0);
			BigDecimal cxqh=BigDecimal.valueOf(0);
			BigDecimal jjqh=BigDecimal.valueOf(0);
			BigDecimal mmqh=BigDecimal.valueOf(0);
			
			if(null != map.get("故障")){
				gz= (BigDecimal) map.get("故障");
			}
			if(null != map.get("紧急停止")){
				jt=(BigDecimal) map.get("紧急停止");
			}
			if(null != map.get("暂停")){
				zt= (BigDecimal) map.get("暂停");
			}
			
			if(null != map.get("周期停止")){
				zqtz= (BigDecimal) map.get("周期停止");
			}
			if(null != map.get("换料停机")){
				hltj= (BigDecimal) map.get("换料停机");
			}
			if(null != map.get("示教停机")){
				jstj= (BigDecimal) map.get("示教停机");
			}
			if(null != map.get("程序切换")){
				cxqh= (BigDecimal) map.get("程序切换");
			}
			if(null != map.get("夹具切换")){
				jjqh= (BigDecimal) map.get("夹具切换");
			}
			if(null != map.get("模具切换")){
				mmqh= (BigDecimal) map.get("模具切换");
			}
			nightShiftxData1.add(gz.add(jt));
			nightShiftxData2.add(zt);
			nightShiftxData3.add(zqtz);
			nightShiftxData4.add(hltj);
			nightShiftxData5.add(jstj);
			nightShiftxData6.add(cxqh);
			nightShiftxData7.add(jjqh);
			nightShiftxData8.add(mmqh);
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("gz",nightShiftxData1.toString() );
		returnMap.put("zt", nightShiftxData2.toString());
		returnMap.put("zqtz",nightShiftxData3.toString());
		returnMap.put("hltj",nightShiftxData4.toString());
		returnMap.put("jstj",nightShiftxData5.toString());
		returnMap.put("cxqh",nightShiftxData6.toString());
		returnMap.put("jjqh",nightShiftxData7.toString());
		returnMap.put("mmqh",nightShiftxData8.toString());
		return returnMap;
	}

}
