package com.wis.mes.production.pmc.service.impl;

import java.util.HashMap;
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
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.production.pmc.dao.TbPmcDao;
import com.wis.mes.production.pmc.entity.TbPmc;
import com.wis.mes.production.pmc.service.TbPmcService;

@Service
public class TbPmcServiceImpl extends BaseServiceImpl<TbPmc> implements TbPmcService {

	
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmClassManagerService classManagerService;
	
	@Autowired
	public void setDao(TbPmcDao dao) {
		this.dao = dao;
	}

	@Override
	public PageResult<Map<String, Object>> getPageTbPmc(BootstrapTableQueryCriteria criteria) {
		return ((TbPmcDao) dao).getPageTbPmc(criteria);
	}

	@Override
	public void exportData(List<TbPmc> content, SXSSFWorkbook workbook, Sheet sheet, int rownum) {
		// 班次数据字典
		Map<String, DictEntry> shiftnoMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER);
		//产线基础数据
		final Map<String, DictEntry> classManagerMap = new HashMap<String, DictEntry>();
		for(DictEntry dict:classManagerService.getDictItemEntry(null)) {
			classManagerMap.put(dict.getCode(), dict);
		}
		for (TbPmc bean : content) {
			int cellnum = 0;
			String shiftno="",classGroup="",lineName="";
			if(null != bean.getTmLine()) {
				lineName = bean.getTmLine().getNameCn();
			}
			if(null != bean.getTmWorktime() && StringUtils.isNotBlank(bean.getTmWorktime().getShiftno())) {
				shiftno = shiftnoMap.get(bean.getTmWorktime().getShiftno()).getName();
			}
			if(null != bean.getTmWorktime()) {
				String classManagerId =String.valueOf(bean.getTmWorktime().getTmClassManagerId());
				if(null != classManagerId && classManagerMap.containsKey(classManagerId)) {
					classGroup = classManagerMap.get(classManagerId).getName();
				}
			}
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(DateUtils.formatDateTime(bean.getCreateTime()));// 日期
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(lineName);// 产线
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(shiftno);// 班次
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(classGroup);// 班组
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getDayPlan());// 当日计划
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getRealityPlan());// 现时计划
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getRealityCompletion());// 现时完成
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(bean.getDelayTime());// 延误时间(min)
			LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(null != bean.getAvailabilityRate()?bean.getAvailabilityRate():0);// 可动率(%)
			rownum++;
		}
	}
	
}
