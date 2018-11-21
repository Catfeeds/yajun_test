package com.wis.mes.production.regionPrdInfo.service.impl;

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
import com.wis.mes.production.regionPrdInfo.dao.TmpRegionPrdInfoDao;
import com.wis.mes.production.regionPrdInfo.entity.TmpRegionPrdInfo;
import com.wis.mes.production.regionPrdInfo.service.TmpRegionPrdInfoService;

@Service
public class TmpRegionPrdInfoServiceImpl extends BaseServiceImpl<TmpRegionPrdInfo> implements TmpRegionPrdInfoService {

	@Autowired
	public void setDao(TmpRegionPrdInfoDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public void exportData(List<TmpRegionPrdInfo> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		// 班组数据字典
		Map<String, DictEntry> groupMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		for (TmpRegionPrdInfo bean : content) {
			int cellnum = 0;
			String createTime = "", plantName = "", lineName = "", shiftno = "", classGroup = "";
			if (null != bean.getCreateTime()) {
				createTime = DateUtils.formatDateTime(bean.getCreateTime());
			}
			if (null != bean.getPlant()) {
				plantName = bean.getPlant().getNameCn();
			}
			if (null != bean.getLine()) {
				lineName = bean.getLine().getNameCn();
			}
			if (null != bean.getWorkTime() && StringUtils.isNotBlank(bean.getWorkTime().getShiftno())) {
				shiftno = shiftnoMap.get(bean.getWorkTime().getShiftno()).getName();
			}
			if (null != bean.getTmClassManager() && StringUtils.isNotBlank(bean.getTmClassManager().getClassGroup())) {
				classGroup = groupMap.get(bean.getTmClassManager().getClassGroup()).getName();
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(createTime);// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(plantName);// 事部
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineName);// 产线
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(classGroup);// 班组
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getRegionNo());// 区域
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getMachiningSurface());//钣金加工用图号
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getGrainMovingTime());// 稼动时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getProcessTime());// 加工时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getDownTime());// 故障时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getNoDownTime());// 非故障时间
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getGrainMovingTime());// 可动率从
			rownum++;
		}
	}
}
