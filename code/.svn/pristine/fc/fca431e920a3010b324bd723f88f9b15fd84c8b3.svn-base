package com.wis.mes.master.workcalendar.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.workcalendar.entity.TmWorkschedule;

public interface TmWorkscheduleService extends BaseService<TmWorkschedule> {
	List<DictEntry> getDictItem(TmWorkschedule tmWorkschedule);

	String generateCurrentWeekTemplete(String ids, String isCoveredValue);

	String generateNextWeekTemplete(String ids);

	String checkWorkScheduleNo(String code, String param);

	Integer verifyUniqueValues(TmWorkschedule tmWorkschedule);

	String checkIsGenerateWorkTime(String ids, String isCoveredValue);

	String checkTime(TmWorkschedule tmWorkschedule, String[] errors);

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmWorkschedule> list, String filePath,
			String[] header);

	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmWorkschedule> list,
			String parentHeadStr, String childHeadStr, String filePath);

	String doImportExcelData(Workbook workbook, HttpServletRequest request);

	String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest request);
}
