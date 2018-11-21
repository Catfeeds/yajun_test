package com.wis.mes.dakin.report.service.impl;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.DateUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.dakin.report.dao.FinMovableRateDao;
import com.wis.mes.dakin.report.entity.FinMovableRate;
import com.wis.mes.dakin.report.service.FinMovableRateService;

import net.sf.json.JSONArray;

@Service
public class FinMovableRateServiceImpl extends BaseServiceImpl<FinMovableRate> implements FinMovableRateService {

	@Autowired
	public void setDao(FinMovableRateDao dao) {
		this.dao = dao;
	}

	private FinMovableRateDao getDao() {
		return ((FinMovableRateDao) dao);
	}

	private final Log logger = LogFactory.getLog(FinMovableRateServiceImpl.class);

	@Override
	public List<Map<String, String>> getFinMovableRateEveryDayInfo(String shiftNo, String createTimeStart, String createTimeEnd) {
		Map<String, String> returnMap = new HashMap<String, String>();

		JSONArray whiteRealRunRateShow = new JSONArray();
		JSONArray nightRealRunRateShow = new JSONArray();
		// 日期显示
		JSONArray dateShow = new JSONArray();
		// 星期显示
		JSONArray weekShow = new JSONArray();
		JSONArray allTimeShow = new JSONArray();
		List<Map<String, String>> movableRateEveryDayInfo = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> everyDayInfoList = getDao().getFinMovableRateEveryDay(shiftNo, createTimeStart, createTimeEnd);
		if (null != everyDayInfoList && !everyDayInfoList.isEmpty()) {
			for (Map<String, Object> map : everyDayInfoList) {
				if (null != map.get("WHITEREALRUNRATESHOW")) {
					Clob clob = (Clob) map.get("WHITEREALRUNRATESHOW");
					String whiteRealRunRateStr = clobToString(clob);
					whiteRealRunRateShow.add(whiteRealRunRateStr);
				} else {
					whiteRealRunRateShow.add("0");
				}
				if (null != map.get("NIGHTREALRUNRATESHOW")) {
					Clob clob = (Clob) map.get("NIGHTREALRUNRATESHOW");
					String nightRealRunRateStr = clobToString(clob);
					nightRealRunRateShow.add(nightRealRunRateStr);
				} else {
					nightRealRunRateShow.add("0");
				}
				if (null != map.get("ALLTIMESHOW")) {
					Clob clob = (Clob) map.get("ALLTIMESHOW");
					String allTimeStr = clobToString(clob);
					allTimeShow.add(allTimeStr);
				} else {
					allTimeShow.add("0");
				}
				dateShow.add(map.get("DATESHOW"));
				weekShow.add(map.get("WEEKSHOW"));
				returnMap.put("whiteRealRunRateShow", whiteRealRunRateShow.toString());
				returnMap.put("dateShow", dateShow.toString());
				returnMap.put("weekShow", weekShow.toString());
				returnMap.put("nightRealRunRateShow", nightRealRunRateShow.toString());
				returnMap.put("allTimeShow", allTimeShow.toString());
				movableRateEveryDayInfo.add(returnMap);
			}
		}
		return movableRateEveryDayInfo;
	}

	private String clobToString(Clob clob) {
		String reString = "";
		try {
			Reader reader = clob.getCharacterStream();
			BufferedReader bReader = new BufferedReader(reader);
			String s = bReader.readLine();
			StringBuffer sb = new StringBuffer();
			while (null != s) {
				sb.append(s);
				s = bReader.readLine();
			}
			reString = sb.toString();
			reader.close();

		} catch (Exception e) {
			logger.info("oracle Clob类型转换为String失败！");
		}
		return reString;
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<FinMovableRate> list, String path, String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			FinMovableRate bean = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(header[0], DateUtils.formatDate(bean.getCreateDate()));
			map.put(header[1], getShiftNo(bean.getShiftNo()));
			map.put(header[2], DateUtils.formatDate(bean.getCreateTime(), "HH:mm:ss"));
			map.put(header[3], bean.getRealRunRate());
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, path, header);
	}

	private String getShiftNo(String shiftno) {
		if ("1".equals(shiftno)) {
			return "白班";
		} else if ("2".equals(shiftno)) {
			return "晚班";
		} else {
			return "";
		}
	}

}
