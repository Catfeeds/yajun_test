package com.wis.mes.master.nc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.nc.entity.TmNcGroup;

public interface TmNcGroupService extends BaseService<TmNcGroup> {

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
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmNcGroup> list, String filePath,
			String[] header);

	/**
	 * 将数据导出成Excel文件
	 * 
	 * @param workbook
	 *            req
	 * 
	 * 
	 * @param errorList
	 *            错误提示信息列表
	 */
	String doImportExcelData(Workbook workbook, HttpServletRequest req);

	/**
	 * 根据 条件实体 生成 字典列表
	 *
	 * @param eg
	 *            条件实体
	 * 
	 * @return 字典列表
	 */
	List<DictEntry> getDictItem(final TmNcGroup eg);

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
	void checkUnique(TmNcGroup operationBean, String operationType, List<String> errorList);

	/**
	 * 模板下载
	 * 
	 * @param templatePath
	 * @return
	 */
	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmNcGroup> list);

	/**
	 * 将全部数据导出成Excel文件
	 * 
	 * @param response
	 *            list parentHeadStr childHeadStr filePath
	 */
	Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmNcGroup> list, String parentHeadStr,
			String childHeadStr, String filePath);

	/**
	 * 不合格组级联导入
	 * 
	 * @param response
	 *            list parentHeadStr childHeadStr filePath
	 */
	String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest req);

	/**
	 *
	 *封装数据字典公共接口 
	 * @return List<DictEntry>
	 * @author yajun_ren
	 */
	List<DictEntry> queryDictItem(QueryCriteria criteria);
}
