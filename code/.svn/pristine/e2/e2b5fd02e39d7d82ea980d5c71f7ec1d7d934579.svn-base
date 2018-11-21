package com.wis.mes.master.path.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.master.path.entity.TmPathUlocParameter;

public interface TmPathUlocParameterService extends BaseService<TmPathUlocParameter> {

	/**
	 * 查询工艺路径站点参数信息
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	public List<TmPathUlocParameter> getTmPathUlocParameters(Integer tmPathUlocId);

	/**
	 * 查询参数信息
	 * 
	 * @param criteria
	 * @return
	 */
	public PageResult<TsParameter> getParameterPageResult(QueryCriteria criteria);

	/**
	 * 保存参数信息
	 * 
	 * @param tmPathUlocId
	 * @param tsParameterIds
	 * @param notes
	 * @param isRequeired
	 * @param enabled
	 * @return
	 */
	List<TmPathUlocParameter> doSavePathUlocParameter(Integer tmPathUlocId, String[] tsParameterIds, String[] notes,
			String[] isRequeired, String[] enabled);

	/**
	 * 获取参数信息
	 * 
	 * @param tmPathUlocId
	 * @param tsParameterIds
	 * @param notes
	 * @param isRequeired
	 * @param enabled
	 * @return
	 */
	List<TmPathUlocParameter> getPathUlocParameterData(Integer tmPathUlocId, String[] tsParameterIds, String[] notes,
			String[] isRequeired, String[] enabled, String rectSeq);

	/**
	 * 参数数据导出
	 * 
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 */
	void exportExcelData(HttpServletResponse response, List<TmPathUlocParameter> content, String filePath,
			String[] header);
	
	/**
	 * 根据工艺路径站点查询对应的参数
	 * @param criteria
	 * @return
	 */
	public PageResult<Map<String, Object>> pagePathUlocParamList(QueryCriteria criteria);
	
	/**
	 * 更加条件查询工艺路径站点可以绑定的参数
	 * @param criteria
	 * @return
	 */
	public PageResult<Map<String, Object>> pageEquipmentByParameterList(QueryCriteria criteria);
	
	/**
	 * 保存工艺路径对于的参数
	 * @param tmPathUlocId
	 * @param tsParameterIds
	 * @return
	 */
	public List<TmPathUlocParameter> doSavePathUlocParameterNew(Integer tmPathUlocId, String[] tsParameterIds);
	
	/**
	 * 根据条件查询站点对应的参数，返回集合
	 * @param params
	 * @return
	 */
	public List<TmPathUlocParameter> getTmPathUlocIdsByParameterList(Map<String, Object> params);

}
