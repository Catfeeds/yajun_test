package com.wis.mes.master.uloc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;

public interface TmUlocSipNcService extends MasterBaseService<TmUlocSipNc> {

	/**
	 * 导出数据
	 * 
	 * @author zhaoxianquan
	 * @param response
	 * @param list
	 * @param filePath
	 * @param header
	 * @return
	 */
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmUlocSipNc> list, String filePath,
			String[] header);

	/**
	 * 导出模板
	 * 
	 * @author zhaoxianquan
	 * @param downName
	 * @param templatePath
	 * @return
	 */
	public Workbook getWorkbookTemp(String downName, String templatePath);

	/**
	 * 数据导入
	 * @author zhaoxianquan
	 * @param book
	 * @param request
	 * @param tmUlocSipId
	 * @return
	 */
	public String doImportExcelData(Workbook book, HttpServletRequest request, String tmUlocSipId);

}
