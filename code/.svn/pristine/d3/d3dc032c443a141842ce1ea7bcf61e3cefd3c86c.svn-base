package com.wis.mes.production.tksenergy.service.impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.tksenergy.dao.TksEnergyDao;
import com.wis.mes.production.tksenergy.entity.TksEnergy;
import com.wis.mes.production.tksenergy.service.TksEnergyService;

@Service
public class TksEnergyServiceImpl extends BaseServiceImpl<TksEnergy> implements TksEnergyService {

	@Autowired
	public void setDao(TksEnergyDao dao) {
		this.dao = dao;
	}

	@Override
	public void exportData(List<TksEnergy> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		for (TksEnergy bean : content) {
			int cellnum = 0;
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getAcEnergy());// 空调能耗
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getLightEnergy());// 照明能耗
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getDeviceEnergy());// 设备能耗
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.formatDateTime(bean.getCreateTime()));// 创建时间
			rownum++;
		}
	}
}
