package com.wis.mes.production.equipmentstatus.service.impl;

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
import com.wis.mes.production.equipmentstatus.dao.TbEquipmentStatusDao;
import com.wis.mes.production.equipmentstatus.entity.TbEquipmentStatus;
import com.wis.mes.production.equipmentstatus.service.TbEquipmentStatusService;
import com.wis.mes.util.ExportPageResult;
import com.wis.mes.util.StringUtil;

@Service
public class TbEquipmentStatusServiceImpl extends BaseServiceImpl<TbEquipmentStatus>
		implements TbEquipmentStatusService, ExportPageResult<TbEquipmentStatus> {

	@Autowired
	public void setDao(TbEquipmentStatusDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public PageResult<TbEquipmentStatus> queryPageTbEquipmentStatus(QueryCriteria criteria) {
		return ((TbEquipmentStatusDao) dao).queryPageTbEquipmentStatus(criteria);
	}

	private String getRunningState(String state, Map<String, DictEntry> eqStatemap) {
		if ("0001".equals(state)) {
			state = "1";
		} else if ("0010".equals(state)) {
			state = "2";
		} else if ("0100".equals(state)) {
			state = "3";
		} else if ("1000".equals(state)) {
			state = "4";
		} else if ("0011".equals(state)) {
			state = "2";
		}
		if (eqStatemap.containsKey(state)) {
			return eqStatemap.get(state).getName();
		} else {
			return "";
		}
	}

	@Override
	public void exportData(List<TbEquipmentStatus> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		Map<String, DictEntry> eqStatemap = entryService.getMapTypeCode("EQ_CIRCULATE_STATE");
		for (TbEquipmentStatus equipmentStatus : content) {
			int cellnum = 0;
			String shiftno = "";
			String lineNoAndName = "";
			String equipmentNo = "";
			String equipmentName = "";
			if (null != equipmentStatus.getWorktime()
					&& StringUtils.isNotBlank(equipmentStatus.getWorktime().getShiftno())) {
				shiftno = shiftnoMap.get(equipmentStatus.getWorktime().getShiftno()).getName();
			}
			if (null != equipmentStatus.getTmEquipment()) {
				equipmentNo = equipmentStatus.getTmEquipment().getNo();
				equipmentName = equipmentStatus.getTmEquipment().getName();
			}
			if (null != equipmentStatus.getTmline()) {
				lineNoAndName = equipmentStatus.getTmline().getNo() + "-" + equipmentStatus.getTmline().getNameCn();
			}
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(DateUtils.formatDate(equipmentStatus.getCreateTime()));// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineNoAndName);// 产线
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentNo);// 设备编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentName);// 设备名称
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(getRunningState(equipmentStatus.getRunningState(), eqStatemap));// 运行状态
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentStatus.getBeginTime());// 开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentStatus.getFinishTime());// 结束时刻
			if (equipmentStatus.getCreateTime() != null && equipmentStatus.getUpdateTime() != null) {
				long duration = equipmentStatus.getUpdateTime().getTime()-equipmentStatus.getCreateTime().getTime();
				equipmentStatus.setDuration(DateUtils.msTos(duration));
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(equipmentStatus.getDuration()));// 持续时间
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(StringUtil.getString(equipmentStatus.getCodeRule().getCode()));// 状态编码
			LoadUtils.getCell(sheet, rownum, cellnum++)
					.setCellValue(StringUtil.getString(equipmentStatus.getCodeRule().getCodeDesc()));// 状态内容
			rownum++;
		}
	}
}
