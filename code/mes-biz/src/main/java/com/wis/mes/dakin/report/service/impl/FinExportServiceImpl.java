package com.wis.mes.dakin.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.dakin.report.dao.FinExportDao;
import com.wis.mes.dakin.report.service.FinExportService;
import com.wis.mes.dakin.report.vo.FinExportVo;
import com.wis.mes.util.StringUtil;

@Service
public class FinExportServiceImpl implements FinExportService {
	@Autowired
	private FinExportDao finExportDao;

	public PageResult<FinExportVo> getFinExportInfo(QueryCriteria criteria) {
		PageResult<Map<String, Object>> finExportInfo = finExportDao.getFinExportInfo(criteria);
		List<Map<String, Object>> getFinExportInfoPart = finExportInfo.getContent();
		List<FinExportVo> getAllExportInfo = new ArrayList<FinExportVo>();
		for (Map<String, Object> map : getFinExportInfoPart) {
			String dateShow="",shiftNo="",noShow="",deviceStatusShow="",
					beginTime="",finishTime="",timeLong="",workTime="",
					paramShift="",numShift="";
			BigDecimal columnsShow,segmentShow,slicesShow;
			dateShow = (String) map.get("DATESHOW");
			if(null != map.get("SHIFTNO") && !"".equals(map.get("SHIFTNO"))) {
				shiftNo = (String) map.get("SHIFTNO");
			}
			noShow = (String) map.get("NOSHOW");
			columnsShow = (BigDecimal) map.get("COLUMNSSHOW");
			segmentShow = (BigDecimal) map.get("SEGMENTSHOW");
			slicesShow = (BigDecimal) map.get("SLICESSHOW");
			deviceStatusShow = (String) map.get("DEVICESTATUSSHOW");
			beginTime = (String) map.get("BEGINTIME");
			finishTime = (String) map.get("FINISHTIME");
			timeLong = (String) map.get("TIMELONG");
			workTime = (String) map.get("WORKTIME");
			int deviceStatus = 0;
			if (null != map.get("DEVICESTATUS")) {
				deviceStatus = (Integer.parseInt((String) map.get("DEVICESTATUS")));
			}
			String beginTimeAllShow = (String) map.get("BEGINTIMEALLSHOW");
			String finishTimeAllShow = (String) map.get("FINISHTIMEALLSHOW");
			String oneLevelStatusContent = null;
			String twoLevelStatusContent = null;
			if ("白班".equals(shiftNo)) {
				paramShift = "MORNINGSHIFT";
				numShift = "1";
			} else if ("晚班".equals(shiftNo)) {
				paramShift = "NIGHTSHIFT";
				numShift = "2";
			}else {
				numShift="0";
			}
			// 1-停止 2-故障 3-运行 4-非故障停机 5-通讯丢失
			if (1 == deviceStatus) {
				Map<String, String> oneLevelContentMap = finExportDao.getOneLevelStatusContent(workTime, paramShift,
						beginTime, finishTime);
				if (null != oneLevelContentMap && !oneLevelContentMap.isEmpty()) {
					oneLevelStatusContent = oneLevelContentMap.get("ONELEVELSTATUSCONTENT");
				}
			} else if (2 == deviceStatus) {
				oneLevelStatusContent = "故障";
				Map<String, String> faultCodeNameMap = finExportDao.getFinFaultedCodeName(numShift, beginTimeAllShow,
						finishTimeAllShow);
				if (null != faultCodeNameMap && !faultCodeNameMap.isEmpty()) {
					String twoLevelStatusCode =	 faultCodeNameMap.get("FAULTEDCODE");
					twoLevelStatusContent = "["+twoLevelStatusCode+"]"+StringUtil.getString(faultCodeNameMap.get("ENTRYNAME"));
				}
			} else if (3 == deviceStatus) {
				oneLevelStatusContent = "运转";
			} else if (4 == deviceStatus) {
				boolean isHaveData = false;
				List<Map<String, Object>> getNoFaultNameMapList = finExportDao.getFinNoFaultedName(numShift,
						beginTimeAllShow, finishTimeAllShow);
				if (null != getNoFaultNameMapList && !getNoFaultNameMapList.isEmpty()) {
					for (Map<String, Object> getNoFaultNameMap : getNoFaultNameMapList) {
						oneLevelStatusContent = String.valueOf(getNoFaultNameMap.get("NOFALULTSTOP"));
						String duration = String.valueOf(getNoFaultNameMap.get("DURATION"));
						String oneLevelBeginTime = String.valueOf(getNoFaultNameMap.get("BEGINTIME"));
						String oneLevelFinishTime = String.valueOf(getNoFaultNameMap.get("FINISHTIME"));

						if (StringUtils.isNotBlank(oneLevelStatusContent)) {
							if (ConstantUtils.FIN_NO_FAULT_SWITCH_STOP.equals(oneLevelStatusContent)) {
								List<Map<String, Object>> getSwitchStopNameMapList = finExportDao
										.getFinNoFaultSwitchStopName(numShift, beginTimeAllShow, finishTimeAllShow);
								if (null != getSwitchStopNameMapList && !getSwitchStopNameMapList.isEmpty()) {
									isHaveData = true;
									for (Map<String, Object> getSwitchStopNameMap : getSwitchStopNameMapList) {
										String switchStopName = getSwitchStopNameMap.get("TWOLEVELNAME").toString();
										if (StringUtils.isNotBlank(switchStopName)) {
											twoLevelStatusContent = switchStopName;
											String twoLevelDuration = getSwitchStopNameMap.get("DURATION").toString();
											String twoLevelBeginTime = getSwitchStopNameMap.get("BEGINTIME").toString();
											String twoLevelFinishTime = getSwitchStopNameMap.get("FINISHTIME")
													.toString();
											//twoLevelDuration =getDayFull(Long.valueOf(twoLevelDuration));
											setFinExportVos(getAllExportInfo, dateShow, shiftNo, noShow, columnsShow,
													segmentShow, slicesShow, deviceStatusShow, oneLevelStatusContent,
													twoLevelStatusContent, twoLevelDuration, twoLevelBeginTime,
													twoLevelFinishTime);
										}
									}
								}
							}
						}
						if (!isHaveData) {
							//duration =getDayFull(Long.valueOf(duration));
							setFinExportVos(getAllExportInfo, dateShow, shiftNo, noShow, columnsShow, segmentShow,
									slicesShow, deviceStatusShow, oneLevelStatusContent, twoLevelStatusContent,
									duration, oneLevelBeginTime, oneLevelFinishTime);
						}
					}
				}
				if (!isHaveData) {
					if(StringUtils.isEmpty(oneLevelStatusContent)) {oneLevelStatusContent="其他";}
					setFinExportVos(getAllExportInfo, dateShow, shiftNo, noShow, columnsShow,
							segmentShow, slicesShow, deviceStatusShow, oneLevelStatusContent,
							twoLevelStatusContent, timeLong, beginTime,
							finishTime);
				}
			}
			if (deviceStatus != 4) {
				setFinExportVos(getAllExportInfo, dateShow, shiftNo, noShow, columnsShow,
						segmentShow, slicesShow, deviceStatusShow, oneLevelStatusContent,
						twoLevelStatusContent, timeLong, beginTime,
						finishTime);
			}
		}

		PageResult<FinExportVo> result = new PageResult<FinExportVo>();
		result.setContent(getAllExportInfo);
		result.setTotalCount(finExportInfo.getTotalCount());
		return result;
	}

	private void setFinExportVos(List<FinExportVo> getAllExportInfo, String dateShow, String shiftNo, String noShow,
			BigDecimal columnsShow, BigDecimal segmentShow, BigDecimal slicesShow, String deviceStatusShow,
			String oneLevelStatusContent, String twoLevelStatusContent, String twoLevelDuration,
			String twoLevelBeginTime, String twoLevelFinishTime) {
		FinExportVo finExportVo = new FinExportVo();
		finExportVo.setDateShow(dateShow);
		finExportVo.setShiftNo(shiftNo);
		finExportVo.setNoShow(noShow);
		finExportVo.setColumnsShow(columnsShow);
		finExportVo.setSegmentShow(segmentShow);
		finExportVo.setSlicesShow(slicesShow);
		finExportVo.setDeviceStatusShow(deviceStatusShow);
		finExportVo.setBeginTime(twoLevelBeginTime);
		finExportVo.setFinishTime(twoLevelFinishTime);
		finExportVo.setTimeLong(twoLevelDuration);
		finExportVo.setOenLevelStatusContent(oneLevelStatusContent);
		finExportVo.setTwoLevelStatusContent(twoLevelStatusContent);
		// finExportVo.setThreeLevelStatusContent(threeLevelStatusContent);
		getAllExportInfo.add(finExportVo);
	}

	public Map<String, Object> exportExcelData(HttpServletResponse response, List<FinExportVo> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			FinExportVo finExportVo = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(header[0], finExportVo.getDateShow());
			map.put(header[1], finExportVo.getShiftNo());
			map.put(header[2], finExportVo.getNoShow());
			map.put(header[3], null == finExportVo.getColumnsShow() ? "" : finExportVo.getColumnsShow());
			map.put(header[4], null == finExportVo.getSegmentShow() ? "" : finExportVo.getSegmentShow());
			map.put(header[5], null == finExportVo.getSlicesShow() ? "" : finExportVo.getSlicesShow());
			map.put(header[6],
					StringUtils.isBlank(finExportVo.getDeviceStatusShow()) ? "" : finExportVo.getDeviceStatusShow());
			map.put(header[7], finExportVo.getBeginTime());
			map.put(header[8], finExportVo.getFinishTime());
			map.put(header[9], finExportVo.getTimeLong());
			map.put(header[10], StringUtils.isBlank(finExportVo.getOenLevelStatusContent()) ? ""
					: finExportVo.getOenLevelStatusContent());
			map.put(header[11], StringUtils.isBlank(finExportVo.getTwoLevelStatusContent()) ? ""
					: finExportVo.getTwoLevelStatusContent());
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}


	private String getDayFull(long diff) {
		diff = diff * 1000;
		StringBuffer strBuf = new StringBuffer();
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		long day = diff / nd; // 计算差多少天
		long hour = diff % nd / nh; // 计算差多少小时
		long min = diff % nd % nh / nm; // 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果 //
		if (day > 0) {
			strBuf.append(day + "day");// 天
		}
		if (hour > 0) {
			strBuf.append(hour + "hour");// 时
		}
		if (min > 0) {
			strBuf.append(min + "min");// 分
		}
		if (sec > 0) {
			strBuf.append(sec + "s");// 秒
		}
		return strBuf.toString();
	}
}
