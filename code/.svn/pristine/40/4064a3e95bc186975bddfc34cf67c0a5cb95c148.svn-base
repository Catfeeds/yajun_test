package com.wis.mes.production.regionstatus.service.impl;

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
import com.wis.mes.production.regionstatus.dao.TmpRegionstatusDao;
import com.wis.mes.production.regionstatus.entity.TmpRegionstatus;
import com.wis.mes.production.regionstatus.service.TmpRegionstatusService;

@Service
public class TmpRegionstatusServiceImpl extends BaseServiceImpl<TmpRegionstatus> implements TmpRegionstatusService {

	@Autowired
	public void setDao(TmpRegionstatusDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public void exportData(List<TmpRegionstatus> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		// 班组数据字典
		Map<String, DictEntry> groupMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP);
		Map<String, DictEntry> eqStatemap = entryService.getMapTypeCode(ConstantUtils.SM_CIRCULATE_STATE);
		for (TmpRegionstatus bean : content) {
			int cellnum = 0;
			String plantNo = "", lineName = "", shiftno = "", runningState = "", durationSrc = "", classGroup = "";
			if (null != bean.getTmPlant()) {
				plantNo = bean.getTmPlant().getNo();
			}
			if (null != bean.getTmLine()) {
				lineName = bean.getTmLine().getNo() + "-" + bean.getTmLine().getNameCn();
			}
			if (null != bean.getTmWorktime() && StringUtils.isNotBlank(bean.getTmWorktime().getShiftno())) {
				shiftno = shiftnoMap.get(bean.getTmWorktime().getShiftno()).getName();
			}
			if (null != bean.getTmClassManager() && StringUtils.isNotBlank(bean.getTmClassManager().getClassGroup())) {
				classGroup = groupMap.get(bean.getTmClassManager().getClassGroup()).getName();
			}
			if (StringUtils.isNotBlank(bean.getRunningState())) {
				if (eqStatemap.containsKey(bean.getRunningState())) {
					runningState = eqStatemap.get(bean.getRunningState()).getName();
				}
			}
			if (bean.getCreateTime() != null && bean.getUpdateTime() != null) {
				long duration = bean.getUpdateTime().getTime() - bean.getCreateTime().getTime();
				durationSrc = LoadUtils.getInfo(DateUtils.msTos(duration));
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.formatDate(bean.getCreateTime()));// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(plantNo);// 事部
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineName);// 产线
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(classGroup);// 班组
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getRegionNo());// 区域
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(runningState);// 运行状态
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(bean.getBeginTime()));// 开始时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(LoadUtils.getInfo(bean.getFinishTime()));// 结束时刻
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(durationSrc);// 时长
			rownum++;
		}
	}
}
