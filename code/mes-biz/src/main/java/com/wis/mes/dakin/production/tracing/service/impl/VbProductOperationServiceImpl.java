package com.wis.mes.dakin.production.tracing.service.impl;

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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.dakin.production.tracing.dao.VbProductOperationDao;
import com.wis.mes.dakin.production.tracing.entity.VbProductOperation;
import com.wis.mes.dakin.production.tracing.service.VbProductOperationService;
import com.wis.mes.util.ExportPageResult;

@Service
public class VbProductOperationServiceImpl extends BaseServiceImpl<VbProductOperation>
		implements VbProductOperationService, ExportPageResult<VbProductOperation> {

	@Autowired
	public void setDao(VbProductOperationDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public void exportData(List<VbProductOperation> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		// 信息来源
		Map<String, DictEntry> infoSourcesMap = entryService.getMapTypeCode(ConstantUtils.INFO_SOURCES);
		for (VbProductOperation bean : content) {
			int cellnum = 0;
			String shiftno = "";
			String infoSources = "";
			if (StringUtils.isNotBlank(bean.getShiftno())) {
				shiftno = shiftnoMap.get(bean.getShiftno()).getName();
			}
			if (StringUtils.isNotBlank(bean.getInfoSources())) {
				infoSources = infoSourcesMap.get(bean.getInfoSources()).getName();
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getPlantNo());// 事部
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getLineNo());// 产线编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getLineName());// 产线名称
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.formatDate(bean.getCreateTime()));// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getBackNumber());// 背番号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getMachineOfName());// 机种名
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getMachineName());// 机号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getTpId());// 工装板ID
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getUlocNo());// 工位编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getUlocName());// 加工工序
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(bean.getEmployeeNo()));// 作业员工号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEquipmentNo());// 配套设备编号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEquipmentName());// 配套设备名称
			// LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue("");//
			// 上顺位流出结束时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEnterBeginTime());// 流入开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEnterFinishTime());// 流入结束时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEquipmentBeginTime());// 设备作业开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEquipmentFinishTime());// 设备作业结束时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getStationFinishTime());// 工位作业完成时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getOutBeginTime());// 流出开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getOutFinishTime());// 流出结束时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getStationWorkDuration());// 工位作业时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.msTosInt(DateUtils.getDuration(bean.getUlocCreateTime(), bean.getEnterBeginTime(), bean.getEnterFinishTime())));// 流入时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getEquipmentWorkDuration());// 设备作业时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getStaffWorkDuration());// 员工作业时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.msTosInt(DateUtils.getDuration(bean.getUlocCreateTime(), bean.getOutBeginTime(), bean.getOutFinishTime())));// 流出时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getFontWaitDuration());// 前等工时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getBackWaitDuration());// 后等工时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(Integer.valueOf(getPlusTime(bean.getFontWaitDuration(), bean.getBackWaitDuration())));// 等工时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getAbnormalDuration());// 异常时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getWarningDuration());// 警告时间

			String employeeJudge = "OK";
			String equipmentJudge = "OK";
			String pcJudge = "OK";
			String synthetical = "OK";
			if (ConstantUtils.PRODUCT_OK_NG_PC_JUDGE.equals(bean.getInfoSources())) {
				pcJudge = "NG";
				synthetical = "NG";
			} else if (ConstantUtils.EQUIPMENT_JUDGE.equals(bean.getInfoSources())) {
				equipmentJudge = "NG";
				synthetical = "NG";
			} else if (ConstantUtils.EMPLOYEE_JUDGE.equals(bean.getInfoSources())) {
				employeeJudge = "NG";
				synthetical = "NG";
			}

			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(employeeJudge);// 手动作业结果
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(pcJudge);// 线体作业结果
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(equipmentJudge);// 设备作业结果
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(synthetical);// 工位综合作业结果
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getUnhealthyCount());// 工位综合作业结果
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(bean.getNcGroupNo()));// 故障代码
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(bean.getNcGroupName()));// 故障类型
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(bean.getNcName()));// 故障内容
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(infoSources);// 信息来源
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getNgExitNumber());// NG出口
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getNgExitTime());// NG口流出时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getNgEntryNumber());// NG入口
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getNgEntryTime());// NG口流入时刻
			rownum++;
		}
	}

	private String getPlusTime(Long beaginVal, Long finishVal) {
		String duration = "";
		if (null != beaginVal && null != finishVal) {
			duration = String.valueOf(beaginVal + finishVal);
		}
		return duration;
	}
}
