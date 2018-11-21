package com.wis.mes.master.path.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.master.path.entity.TmPathUloc;

public interface TmPathUlocService extends BaseService<TmPathUloc> {

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
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmPathUloc> list, String filePath,
			String[] header);

	/**
	 * 将Excel文件数据导入
	 * 
	 * @param workbook
	 *            要导入的Excel解析后的文件
	 * 
	 * @param errorList
	 *            错误提示信息列表
	 */
	public String doImportExcelData(Workbook workbook, HttpServletRequest req, String tmPathId);

	/**
	 * 根据工艺路径ID查询站点信息
	 * 
	 * @param tmPathId
	 * @return
	 */
	public List<TmPathUloc> getPathUlocs(Integer tmPathId);

	/**
	 * 查询站点信息
	 * 
	 * @param tmPathId
	 * @return
	 */
	public List<Map<String, Object>> queryPathUloc(Integer tmPathId);

	/***
	 * 根据ppathUlocId查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> findByIds(String ids);

	/**
	 * 查询工位信息根据ppathUlocIds
	 * 
	 * @return
	 */
	public String getUlocsByIds(String ids);

	/**
	 * 获取Echarts的JSON数据,返回到前端
	 * 
	 * @param tmPathId
	 * @return
	 */
	public Map<String, String> getEchartsData(String tmPathId);

	/**
	 * 保存工艺路径节点信息
	 * 
	 * @param tmPathId
	 * @return
	 */
	public TmPathUloc doSavePathUloc(TmPathUloc bean, String ulocType);

	/**
	 * 根据工艺路径ID和序状态查询
	 * 
	 * @param tmPathId
	 * @param ulocType
	 *            in () 或者 = '' 或者 ！=‘’
	 * @return
	 */
	public List<Map<String, Object>> queryByPathIdAndUlocType(Integer tmPathId, String ulocType);

	/**
	 * 删除选中的ID，并且修改剩余的seq
	 * 
	 * @param deleteIds
	 * @param id
	 * @param list
	 */
	public void doDeleteIdsAndUpdateSeq(Integer[] deleteIds, Integer id, List<TmPathUloc> list);

	public void doUpdatePathUloc(TmPathUloc before, TmPathUloc bean, String ulocType);

	/**
	 * 根据ID排序，并且查询关联实体
	 * 
	 * @param tmPathId
	 * @return
	 */
	public List<TmPathUloc> getPathUlocAndRelationEntity(String tmPathId);

	/**
	 * 删除站点下的所有子信息
	 * 
	 * @param tmPathUlocIds
	 *            以逗号隔开的id
	 */
	void doDeletePathUlocChilds(String tmPathUlocIds);

	/**
	 * 通过外键条件关联查询
	 * 
	 * @param forginId
	 * @return
	 */
	public List<TmPathUloc> findByForginKey(Integer forginId);

	/**
	 * 获取工艺路径图形json数据
	 * 
	 * @param tmPathId
	 * @return
	 */
	String getGraphJSONData(Integer tmPathId);
	
	
	/**
	 * 删除站点及站点下的参数
	 * 
	 * @param tmPathUlocIds
	 *            以逗号隔开的id
	 */
	void doDeletePathUlocOrParam(Integer[] tmPathUlocIds);
	
	/**
	 * 根据工艺路径ids查询对应所以有的站点ids
	 * @param tmPathIds
	 * @return
	 */
	List<Map<String, Object>> getTmPathIdsByTmPathUlocIds(Integer[] tmPathIds);
}
