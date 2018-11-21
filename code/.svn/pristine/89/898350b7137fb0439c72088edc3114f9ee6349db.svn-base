package com.wis.mes.master.line.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.line.entity.TmLine;

public interface TmLineService extends BaseService<TmLine> {
	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmLine> list, String filePath,
			String[] header);

	String doImportExcelData(Workbook workbook, HttpServletRequest req);

	
	/**
	 * 事部  二级联动 ajax处理
	 * @plantId
	 * @author ryj
	 * @return List<TmLine>
	 */
	List<TmLine> findLineNameOrIdById(Integer plantId);

	List<TmLine> findLineNameOrId();

	Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmLine> list);

	List<DictEntry> getDictItem(TmLine line);
	
	List<TmLine> findLIneNameOrPlantId(Integer plantId);
	
	/**
	 * 获取产线列表，返回MAP类型
	 * **/
	 Map<Integer,TmLine> lineMaps(TmLine line);
	 
	 /**
	  * @author yajun_ren
	  * @lineNo
	  * 根据产线编号获取bean
	  * ***/
	 public TmLine findByNo(String lineNo);
	 
	 /**
	 *
	 *封装数据字典公共接口 
	 * @return List<DictEntry>
	 * @author yajun_ren
	 */
	List<DictEntry> queryDictItem(QueryCriteria criteria);
	 
}
