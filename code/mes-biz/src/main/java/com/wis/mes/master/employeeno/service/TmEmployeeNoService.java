package com.wis.mes.master.employeeno.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.employeeno.vo.TmEmployeeNoVo;

public interface TmEmployeeNoService extends BaseService<TmEmployeeNo>{

	/**
	 * @workbook
	 * @req
	 * 导入作业员编号
	 * @author ryj
	 * @Time 2018/5/3
	 * **/
	String doImportExcelData(Workbook workbook, HttpServletRequest req);

	/**
	 * @workbook
	 * @req
	 * 导出作业员编号
	 * @author ryj
	 * @Time 2018/5/3
	 * **/
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEmployeeNo> list, String filePath,
			String[] header);
	
	/**
	 * @workbook
	 * @req
	 * 模板导出
	 * @author ryj
	 * @Time 2018/5/3
	 * **/
	Workbook getWorkbookTemp(final String downName, final String templatePath);
	
	/**
	 * 获取作业员工号列表
	 * 
	 * @return List<TmEmployeeNoVo>
	 * @param TmEmployeeNo
	 * @author ryj
	 * @Time 2018/5/3
	 */
	List<TmEmployeeNoVo> getDictItem(TmEmployeeNo arg0);
}
