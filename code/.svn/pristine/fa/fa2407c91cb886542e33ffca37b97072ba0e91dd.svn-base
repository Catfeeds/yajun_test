package com.wis.mes.master.nc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.nc.entity.TmScrap;

public interface TmScrapService extends MasterBaseService<TmScrap> {

	/**
	 * 将数据导出成Excel文件
	 * 
	 * @param response
	 *            响应信息
	 * 
	 * @param list
	 *            要导出的数据列表
	 * 
	 * @param filePath
	 *            文件路径
	 * 
	 * @param header
	 *            标题
	 * 
	 * @return 操作结果
	 */
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmScrap> list, String filePath,
			String[] header);

	/**
	 * 将数据导出成Excel文件
	 * 
	 * @param workbook  req
	 *            要导入的Excel解析后的文件
	 * 
	 * @param errorList
	 *            错误提示信息列表
	 */
	public String doImportExcelData(Workbook workbook, HttpServletRequest req);

	/**
	 * 校验业务唯一
	 * 
	 * @param operationBean
	 *            操作实体
	 * 
	 * @param operationType
	 *            操作类型[ADD:新增,UDPATE:修改]
	 * 
	 * @param errorList
	 *            错误提示信息列表
	 */
	void checkUnique(TmScrap operationBean, String operationType, List<String> errorList);

	/**
	 * 模板下载
	 * @param templatePath
	 * @return
	 */
	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmScrap> list);
}
