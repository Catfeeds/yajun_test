package com.wis.mes.master.workcalendar.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.master.workcalendar.entity.TmWorkSpecialDate;

public interface TmWorkSpecialDateService extends BaseService<TmWorkSpecialDate> {
	/**
	 * 导出数据
	 * @param response
	 * @param list
	 * @param filePath
	 * @param header
	 * @return
	 */
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmWorkSpecialDate> list, String filePath,
			String[] header);
	/**
	 * 导入
	 * @param workbook
	 * @param request
	 * @return
	 */
	public String doImportExcelData(Workbook workbook,HttpServletRequest request);
	
	public PageResult<TmWorkSpecialDate> getPageTmWorkSpecialDate(QueryCriteria queryCriteria);
}
