package com.wis.mes.master.path.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.path.entity.TmPath;

public interface TmPathService extends BaseService<TmPath> {

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
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmPath> list, String filePath,
			String[] header);

	/**
	 * 根据 条件实体 生成 字典列表
	 *
	 * @param eg
	 *            条件实体
	 * 
	 * @return 字典列表
	 */
	List<DictEntry> getDictItem(TmPath eg);

	/**
	 * 将数据导出成Excel文件
	 * 
	 * @param workbook
	 *            要导入的Excel解析后的文件
	 * 
	 * @param errorList
	 *            错误提示信息列表
	 */
	String doImportExcelData(Workbook workbook, HttpServletRequest request);

	/**
	 * 拷贝工艺路径
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public TmPath doDuplicate(TmPath bean, String errMessags) throws Exception;

	/**
	 * 查询与产品相关的或者没有指定产品的工艺路径
	 * 
	 * @param tmPartId
	 * @return
	 */
	public List<TmPath> getPathByPlantAndParts(Integer tmPlantId, Integer tmPartId);

	/**
	 * 
	 * @param templatePath
	 * @return
	 */
	public Workbook getWorkbookTemp(String templatePath);

	/**
	 * 根据工艺路径站点ID查询 工艺路径信息
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	public TmPath getByPathUlocId(Integer tmPathUlocId);

	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmPath> list, String parentHeader,
			String childHeader, String filePath);

	/**
	 * 保存工艺路径站点的图形信息
	 * 
	 * @param data
	 *            json数据
	 * @param tmPathId
	 *            工艺路径id
	 * @param tmPlantId
	 *            工厂id
	 * @param tmWorkshopId
	 *            车间id
	 * @param tmLineId
	 *            产线id
	 * @param partArray
	 *            关键件信息
	 * @param parameterArray
	 *            参数信息
	 * @param sipArray
	 *            质检项信息
	 * @return
	 */
	TmPath doSavePathUlocRaphData(String data, Integer tmPathId, Integer tmPlantId, Integer tmWorkshopId,
			Integer tmLineId, String partArray, String parameterArray, String sipArray, Integer[] deleateTmPathUlocIds);
	
	 /**
	 * 删除工艺路径及工艺路径下站点和的站点的参数
	 * 
	 * @param tmPathUlocIds
	 *            以逗号隔开的id
	 */
	void doDeletePathUlocParam(Integer[] tmPathIds);

}
