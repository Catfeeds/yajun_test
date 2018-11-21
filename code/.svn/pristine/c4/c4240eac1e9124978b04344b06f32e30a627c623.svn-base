package com.wis.mes.master.equipment.service.impl;

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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.equipment.dao.TmpEquipmentStatusDao;
import com.wis.mes.master.equipment.entity.TmpEquipmentStatus;
import com.wis.mes.master.equipment.service.TmEquipmentStatusService;
import com.wis.mes.util.ExportPageResult;

@Service
public class TmEquipmentStatusServiceImpl extends BaseServiceImpl<TmpEquipmentStatus>
		implements TmEquipmentStatusService, ExportPageResult<TmpEquipmentStatus> {

	@Autowired
	public void setDao(TmpEquipmentStatusDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	/**
	 * 设备报警报表
	 *
	 * @param criteria
	 * @return
	 */
	@Override
	public PageResult<Map<String, Object>> warmReport(BootstrapTableQueryCriteria criteria) {
		return ((TmpEquipmentStatusDao) this.dao).warmReport(criteria);
	}

	@Override
	public PageResult<TmpEquipmentStatus> queryPageTbEquipmentStatus(BootstrapTableQueryCriteria criteria) {
		return ((TmpEquipmentStatusDao) dao).queryPageTbEquipmentStatus(criteria);
	}

	@Override
	public void exportData(List<TmpEquipmentStatus> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		Map<String, DictEntry> eqStatemap = entryService.getMapTypeCode(ConstantUtils.SM_CIRCULATE_STATE);
		for (TmpEquipmentStatus equipmentStatus : content) {
			int cellnum = 0;
			String equipmentNo = "",equipmentName="",shiftno="",lineNoAndName="",runningState="",
					durationSrc="",ruleCode="",ruleCodeDesc="";
			if (null != equipmentStatus.getTmEquipment()) {
				equipmentNo = equipmentStatus.getTmEquipment().getNo();
				equipmentName = equipmentStatus.getTmEquipment().getName();
			}
			if (null != equipmentStatus.getTmWorktime() && StringUtils.isNotBlank(equipmentStatus.getTmWorktime().getShiftno())) {
				shiftno = shiftnoMap.get(equipmentStatus.getTmWorktime().getShiftno()).getName();
			}
			if (null != equipmentStatus.getTmLine()) {
				lineNoAndName = equipmentStatus.getTmLine().getNo() + "-" + equipmentStatus.getTmLine().getNameCn();
			}
			if(StringUtils.isNotBlank(equipmentStatus.getRunningState())) {
				if(eqStatemap.containsKey(equipmentStatus.getRunningState())) {
					runningState = eqStatemap.get(equipmentStatus.getRunningState()).getName();
				}
			}
			if (equipmentStatus.getCreateTime() != null && equipmentStatus.getUpdateTime() != null) {
				long duration = equipmentStatus.getUpdateTime().getTime()-equipmentStatus.getCreateTime().getTime();
				durationSrc = LoadUtils.getInfo(DateUtils.msTos(duration));
			}
			if(null != equipmentStatus.getCodeRule()) {
				ruleCode = LoadUtils.getInfo( equipmentStatus.getCodeRule().getCode());
				ruleCodeDesc = LoadUtils.getInfo( equipmentStatus.getCodeRule().getCodeDesc());
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.formatDate(equipmentStatus.getCreateTime()));// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineNoAndName);// 产线
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentNo);// 设备编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentName);// 设备名称
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(runningState);// 运行状态
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(equipmentStatus.getBeginTime()));// 开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(equipmentStatus.getFinishTime()));//结束时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(durationSrc);// 持续时长
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(ruleCode);// 状态编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(ruleCodeDesc);// 状态内容
			rownum++;
		}
	}
}
