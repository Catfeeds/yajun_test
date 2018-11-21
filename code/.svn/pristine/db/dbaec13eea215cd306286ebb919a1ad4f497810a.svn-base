package com.wis.mes.master.supplier.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.master.supplier.entity.TmSupplier;

public interface TmSupplierService extends BaseService<TmSupplier> {

	/**
	 * 主表数据出
	 * @param response
	 * @param list
	 * @param string
	 * @param header
	 * @return
	 */
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmSupplier> list, String string,
			String[] header);

	/**
	 * 关联导出
	 * @param response
	 * @param list
	 * @param parentHeader
	 * @param childHeader
	 * @param string
	 * @return
	 */
	Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmSupplier> list, String parentHeader,
			String childHeader, String filePath);

	/**
	 * 主表导入
	 * @param book
	 * @param request
	 * @return
	 */
	String doImportExcelData(Workbook book, HttpServletRequest request);

}
