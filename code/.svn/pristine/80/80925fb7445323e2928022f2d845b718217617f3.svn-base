package com.wis.mes.master.uloc.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.uloc.entity.TmUlocSip;

public interface TmUlocSipService extends MasterBaseService<TmUlocSip> {
	/**
	 * 导出工位参数数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmUlocSip> list,
			final String filePath, final String[] header);

	/**
	 * 导入工位参数数据
	 * 
	 * @return void
	 * @param workbook,request,
	 *            response, fileName,tmUlocId
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	public String doImportExcelData(Workbook workbook, HttpServletRequest req, String tmUlocId);
	/**
	 * 导出模板
	 * 
	 * @return Workbook
	 * @param downName,templatePath,object
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	public Workbook getWorkbookTemp(String downName,String templatePath);
	/**
	 * 级联导入
	 * @author zhaoxianquan
	 * @param workbook
	 * @param excelImportPojo
	 * @param req
	 * @param TmUlocId
	 * @return
	 */
	public String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest req,String tmUlocId);
	
	
	/**
	 * 级联导出
	 * @author zhaoxianquan
	 * @param response
	 * @param ulocSipList
	 * @param parentHeadStr
	 * @param childHeadStr
	 * @param filePath
	 * @return
	 */
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmUlocSip> ulocSipList,
			String parentHeadStr, String childHeadStr, String filePath);

	
	
}
