package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.DateUtils;
import com.wis.mes.dakin.report.dao.FinMovableRateDetailsDao;
import com.wis.mes.dakin.report.entity.FinMovableRate;
import com.wis.mes.dakin.report.service.FinMovableRateDetailsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class FinMovableRateDetailServiceImpl implements FinMovableRateDetailsService {

	@Autowired
	private FinMovableRateDetailsDao finMovableRateDetailsDao;

	@Override
	public Map<String, String> getFinMovableRateWhiteAndNightData(String shiftNo, String createTimeStart, String createTimeEnd) {
		// 日期显示
		JSONArray dateShow = new JSONArray();
		// 白班
		JSONArray showValue = new JSONArray();
		JSONArray avgValue = new JSONArray();
		BigDecimal targetValue = new BigDecimal("0");
		JSONObject avgValueMap = new JSONObject();//为了区分当天可动率
		Map<String, String> returnMap = new HashMap<String, String>();
		List<Map<String, Object>> getFinMovableInfoList = finMovableRateDetailsDao
				.getFinMovableRateWhiteAndNightData(shiftNo, createTimeStart, createTimeEnd);
		for (Map<String, Object> map : getFinMovableInfoList) {
			JSONArray value = new JSONArray();
			value.add(map.get("MINVALUE"));
			value.add(map.get("MAXVALUE"));
			value.add(map.get("MINVALUE"));
			value.add(map.get("MAXVALUE"));
			value.add(map.get("AVGVALUE"));
			avgValueMap.put(map.get("DATESHOW"), map.get("LASTRATE"));
			avgValue.add(map.get("LASTRATE"));
			showValue.add(value);
			dateShow.add(map.get("DATESHOW"));
			if (targetValue.equals(BigDecimal.ZERO) && null != map.get("TARGETVALUE")) {
				targetValue = (BigDecimal) map.get("TARGETVALUE");
			}
		}
		returnMap.put("dateShow", dateShow.toString());
		returnMap.put("showValue", showValue.toString());
		returnMap.put("avgValue", avgValue.toString());
		returnMap.put("avgValueMap", avgValueMap.toString());
		returnMap.put("targetValue", targetValue.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> getFaultAndNoFaultTime(String shiftNo, String createTime) {
		JSONArray timeShow = new JSONArray();
		JSONArray realRunRate = new JSONArray();
		Map<String, String> returnMap = new HashMap<String, String>();
		List<Map<String, String>> getDetailTimeList = finMovableRateDetailsDao.getFaultAndNoFaultTime(shiftNo,
				createTime);
		for (Map<String, String> map : getDetailTimeList) {
			timeShow.add(map.get("DETAILSTIME"));
			realRunRate.add(map.get("REALRUNRATE"));
		}
		returnMap.put("timeShow", timeShow.toString());
		returnMap.put("realRunRate", realRunRate.toString());
		return returnMap;
	}

	@Override
	public Map<String, String> getFaultAndNoFaultMessage(String shiftNo, String beginTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		Map<String, Object> durationMap = finMovableRateDetailsDao.getFaultAndNoFaultMessage(shiftNo, beginTime);
		if (null != durationMap && !durationMap.isEmpty()) {
			if (null == durationMap.get("NOFAULTDURATION")) {
				returnMap.put("noFaultDuration", "0");
			} else {
				returnMap.put("noFaultDuration", durationMap.get("NOFAULTDURATION").toString());
			}
			if (null == durationMap.get("FAULTEDDURATION")) {
				returnMap.put("faultedDuration", "0");
			} else {
				returnMap.put("faultedDuration", durationMap.get("FAULTEDDURATION").toString());
			}
		} else {
			returnMap.put("faultedDuration", "0");
			returnMap.put("noFaultDuration", "0");
		}
		return returnMap;
	}

	@Override
	public Map<String, String> getMovableRateAndCalculate(String createTimeStart, String createTimeEnd,
			final int figure, String shiftNo) {
		List<FinMovableRate> content = finMovableRateDetailsDao.getMovableRateByTime(createTimeStart, createTimeEnd,
				shiftNo);
		JSONArray elevationData = new JSONArray();
		for (FinMovableRate bean : content) {
			JSONArray array = new JSONArray();
			array.add(bean.getCreateDateTime().getTime());// y轴
			array.add(bean.getRealRunRate());// x轴
			elevationData.add(array);
		}
		JSONArray labels = new JSONArray();
		for (int i = 0; i < content.size() - 2; i++) {
			FinMovableRate first = content.get(i);
			for (int j = i + 1; j < content.size() - 1; j++) {
				FinMovableRate next = content.get(j);
				FinMovableRate last = content.get(j + 1);
				if (next.getRealRunRate() > last.getRealRunRate()) {
					if (j + 1 == content.size()) {
						next = last;
					} else {
						continue;
					}
				}
				if (first.getRealRunRate() - next.getRealRunRate() >= figure) {
					getDetails(labels, first, next);
				}
				i = j;
				break;
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("elevationData", elevationData.toString());
		map.put("labels", labels.toString());
		return map;
	}

	private void getDetails(JSONArray labels, FinMovableRate first, FinMovableRate next) {
		List<Map<String, Object>> faulitedOrNotCountAndDurationByTime = finMovableRateDetailsDao
				.getFaulitedOrNotCountAndDurationByTime(first.getCreateDateTime(), next.getCreateDateTime());
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
		for (Map<String, Object> map : faulitedOrNotCountAndDurationByTime) {
			Map<String, String> detailMap = new HashMap<String, String>();
			detailMap.put("count", map.get("COUNT").toString());
			Object duration = map.get("DURATION");
			if (duration == null) {
				detailMap.put("duration", "0");
			} else {
				detailMap.put("duration", duration.toString());
			}
			resultMap.put(map.get("TYPE").toString(), detailMap);
		}
		JSONObject label = new JSONObject();
		JSONObject point = new JSONObject();
		point.put("xAxis", 0);
		point.put("yAxis", 0);
		point.put("x", next.getCreateDateTime().getTime());
		point.put("y", next.getRealRunRate());
		label.put("point", point);
		//获取一级状态内容
		String faultedLevelContent = "";
		String noFalutedLevelContent = "";
		List<Map<String, Object>> oneLivelStatusContent = finMovableRateDetailsDao.getFinOneLivelStatusContent(first.getCreateDateTime(), next.getCreateDateTime());
		for (Map<String, Object> map : oneLivelStatusContent) {
			Object duration = map.get("DURATION");
			if (duration != null) {
				if ("FAULTED".equals(map.get("TYPE"))) {
					faultedLevelContent = " " + (null == map.get("CONTENT") ? "" : map.get("CONTENT")) + ":" + DateUtils.getDayFull(Long.valueOf(duration.toString()) * 1000);
				} else {
					noFalutedLevelContent =" " + (null == map.get("CONTENT") ? "" : map.get("CONTENT")) + ":" + DateUtils.getDayFull(Long.valueOf(duration.toString()) * 1000);
				}
			}
		}
		if (StringUtils.isBlank(noFalutedLevelContent)) {
			Long millis = DateUtils.getWorkMillis(first.getCreateDateTime(), next.getCreateDateTime());
			noFalutedLevelContent = " " + "其他:" + DateUtils.getDayFull(millis);
		}
		StringBuffer text = new StringBuffer();
		text.append("故障停机时间:")
				.append(DateUtils.getDayFull(Long.valueOf(resultMap.get("FAULTED").get("duration")) * 1000));
		text.append(faultedLevelContent);
		text.append("<br/>");
		text.append("非故障停机时间:")
				.append(DateUtils.getDayFull(Long.valueOf(resultMap.get("NO_FAULTED").get("duration")) * 1000));
		text.append(noFalutedLevelContent);
		label.put("text", text.toString());
		labels.add(label);
	}
}
