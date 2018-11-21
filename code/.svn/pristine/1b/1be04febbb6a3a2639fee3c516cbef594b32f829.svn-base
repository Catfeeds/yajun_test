package com.wis.mes.production.ulocstatus.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.production.ulocstatus.dao.TbUlocStatusDao;
import com.wis.mes.production.ulocstatus.entity.TbUlocStatus;
import com.wis.mes.production.ulocstatus.service.TbUlocStatusService;
import com.wis.mes.util.ExportPageResult;
import com.wis.mes.util.StringUtil;

@Service
public class TbUlocStatusServiceImpl extends BaseServiceImpl<TbUlocStatus>
		implements TbUlocStatusService, ExportPageResult<TbUlocStatus> {

	@Autowired
	public void setDao(TbUlocStatusDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService tmLineService;

	@Override
	public PageResult<TbUlocStatus> queryPageTbUlocStatus(QueryCriteria criteria) {
		return ((TbUlocStatusDao) dao).queryPageTbUlocStatus(criteria);
	}

	@Override
	public void exportData(List<TbUlocStatus> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		// 产线
		tmLineService.getDictItem(null);
		Map<Integer, TmLine> lineMaps = tmLineService.lineMaps(null);
		// CellStyle cellStyle = LoadUtils.getCellStyleTitile(wb, 14, false,
		// true);
		for (TbUlocStatus ulocStatus : content) {
			int cellnum = 0;
			String shiftno = "";
			String accout = "";
			String ulocNo = "";
			String ulocName = "";
			String lineNo = "";
			String lineName = "";
			if (null != ulocStatus.getWorktime()) {
				if (StringUtils.isNotBlank(ulocStatus.getWorktime().getShiftno())) {
					shiftno = shiftnoMap.get(ulocStatus.getWorktime().getShiftno()).getName();
				}
				if (null != ulocStatus.getWorktime().getTmLineId()) {
					lineNo = lineMaps.get(ulocStatus.getWorktime().getTmLineId()).getNo();
					lineName = lineMaps.get(ulocStatus.getWorktime().getTmLineId()).getNameCn();
				}
			}
			if (null != ulocStatus.getUloc()) {
				if (StringUtils.isNotBlank(ulocStatus.getUloc().getNo())) {
					ulocNo = ulocStatus.getUloc().getNo();
				}
				if (StringUtils.isNotBlank(ulocStatus.getUloc().getName())) {
					ulocName = ulocStatus.getUloc().getName();
				}
			}
			if (null != ulocStatus.getEmployeeNo() && StringUtils.isNotBlank(ulocStatus.getEmployeeNo().getNo())) {
				accout = ulocStatus.getEmployeeNo().getNo() + "-" + ulocStatus.getEmployeeNo().getName();
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.formatDate(ulocStatus.getCreateTime()));// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineNo);// 产线编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineName);// 产线名称
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(ulocNo);// 工位编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(ulocName);// 加工工艺
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(accout);// 作业员工号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(getStationState(ulocStatus.getStationState()));// 工位状态
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(ulocStatus.getBeginTime());// 开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(ulocStatus.getFinishTime());// 开始时刻
			if (ulocStatus.getCreateTime() != null && ulocStatus.getUpdateTime() != null) {
				long duration = ulocStatus.getUpdateTime().getTime()-ulocStatus.getCreateTime().getTime();
				ulocStatus.setDuration(DateUtils.msTos(duration));
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(ulocStatus.getDuration()));// 持续时间
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(getStationStatusName(ulocStatus.getStatusContent()));// 状态内容
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(StringUtil.getString(ulocStatus.getCodeRule().getCode()));// 内容代码
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(StringUtil.getString(ulocStatus.getCodeRule().getCodeDesc()));// 内容描述
			// LoadUtils.setCellStyle(sheet, cellStyle, 0, 14, rownum);
			rownum++;
		}
	}

	private String getStationState(String stationState) {
		if (stationState != null && stationState.length() == 8) {
			if ("1".equals(stationState.substring(4, 5)) || "1".equals(stationState.substring(5, 6))
					|| "1".equals(stationState.substring(7, 8)) || "1".equals(stationState.substring(6, 7))) {
				return "异常";
			} else if ("1".equals(stationState.substring(1, 2))) {
				return "通过";
			} else if ("1".equals(stationState.substring(2, 3))) {
				return "手动";
			} else if ("1".equals(stationState.substring(3, 4))) {
				return "正常";
			}
		}
		return "";
	}

	private String getStationStatusName(String station) {
		if (station != null && station.length() == 8) {
			if ("1".equals(station.substring(4, 5)) || "1".equals(station.substring(5, 6))
					|| "1".equals(station.substring(7, 8))) {
				return "异常";
			} else if ("1".equals(station.substring(0, 1)) || "1".equals(station.substring(6, 7))) {
				return "警告";
			}
		}
		return "";
	}
}
