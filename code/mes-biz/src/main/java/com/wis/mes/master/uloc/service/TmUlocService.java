package com.wis.mes.master.uloc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.uloc.entity.TmUloc;

public interface TmUlocService extends BaseService<TmUloc> {
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmUloc> list, String filePath,
			String[] header);

	List<DictEntry> getDictItem(TmUloc param);

	List<Map<String, Object>> getDictItemMap(TmUloc param);

	public String doImportExcelData(Workbook workbook, HttpServletRequest request);

	/** 唯一NO验证 */
	public Integer checkNo(String no);

	/** 工位模版导出 */
	public Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmUloc> list);

	/**
	 * 导出工位与质检点参数数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmUloc> list, String parentHeadStr,
			String childHeadStr, String filePath);

	Map<String, TmUloc> initUlocMapByConditions(Integer plantId, Integer workshopId, Integer lineId);

	/**
	 * 导出工位与质检点参数数据
	 * 
	 * @return Map<String,Object>
	 * @param workbook,excelImportPojo,req
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest req);

	TmUloc getUlocById(Integer tmUlocId);

	/**
	 * 获取是NG入口，NG出口，NG出入口的工位
	 **/
	List<Map<String, Object>> getUlocNgExitEnterMap();

	TmUloc getRedisUloc(String lineNo, String ulocNo);

	/**
	 * @tmUloc
	 * @author yajun_ren
	 * @return Map<Integer, TmUloc> 获取所有工位信息
	 **/
	Map<Integer, TmUloc> ulocMap(TmUloc tmUloc);

	public List<DictEntry> getDictItemExitEntrance(String parameter);

	List<DictEntry> getUlocAll();
	/**
	 * @author yajun_ren
	 * @return TmUloc
	 * @param (String)ulocNo
	 * ***/
	TmUloc getUloc(String ulocNo);
	
	/**
	 *
	 *封装数据字典公共接口 
	 * @return List<DictEntry>
	 * @author yajun_ren
	 */
	List<DictEntry> queryDictItem(QueryCriteria criteria,boolean isName);
}
