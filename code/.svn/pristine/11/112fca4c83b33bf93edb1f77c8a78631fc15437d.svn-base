package com.wis.mes.master.part.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.part.entity.TmPart;

public interface TmPartService extends BaseService<TmPart> {

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
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmPart> list, String filePath,
			String[] header);

	/**
	 * 根据 条件实体 生成 字典列表
	 *
	 * @param eg
	 *            条件实体
	 * 
	 * @return 字典列表
	 */
	List<DictEntry> getDictItem(TmPart eg);

	/**
	 * 根据 条件实体 生成 字典列表
	 * 
	 * @param param
	 *            查询条件
	 * 
	 *            注：key必须为字段名，如：NAME_CN
	 * 
	 *            注：value必须为 String、List以及其他基本类型的包装类
	 * 
	 * @return 字典列表
	 */
	List<DictEntry> getDictItem(Map<String, Object> param);

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
	void checkUnique(TmPart operationBean, String operationType, List<String> errorList);

	/**
	 * 导出模板
	 */
	public Workbook getWorkbook(final String templatePath);

	/**
	 * 将数据导出成Excel文件
	 * 
	 * @param workbook
	 *            要导入的Excel解析后的文件
	 * 
	 * @param errorList
	 *            错误提示信息列表
	 */
	public String doImportExcelData(Workbook workbook, HttpServletRequest request);

	/**
	 * 初始化指定工厂下的所有物料map
	 * 
	 * @param plantId
	 * @return
	 */
	Map<String, TmPart> initPartMapByAppointPlant(Integer plantId);

}
