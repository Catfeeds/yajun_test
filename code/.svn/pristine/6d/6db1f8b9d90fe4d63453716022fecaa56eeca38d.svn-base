package com.wis.basis.configuration.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;

public interface TsParameterService extends BaseService<TsParameter> {
	/**
	 * 获取参数列表
	 * 
	 * @return List<DictEntry>
	 * @param tmParameter
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	List<DictEntry> getDictItem(TsParameter tmParameter);

	/**
	 * 导出参数数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TsParameter> list,
			final String filePath, final String[] header);

	/**
	 * 参数模板下载
	 * 
	 * @param downName templatePath list
	 * @author 赵宪泉
	 * @return Workbook
	 */
	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TsParameter> list);
	/**
	 * 参数导入
	 * 
	 * @return String
	 * @param workbook,request
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	String doImportExcelData(Workbook book, HttpServletRequest request);
	/**
	 * 生成正则表达式
	 * 
	 * @return StringBuffer 表达式
	 * @param max,min
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	public StringBuffer  generatorExpression(String max,String min);
}
