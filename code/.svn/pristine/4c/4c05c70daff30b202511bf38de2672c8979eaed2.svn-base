package com.wis.mes.master.path.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.path.entity.TmPathUlocParameter;

public interface TmPathUlocParameterDao extends BaseDao<TmPathUlocParameter> {

	/**
	 * 查询参数列表
	 * 
	 * @param criteria
	 * @return
	 */
	public PageResult<TsParameter> getParameterPageResult(QueryCriteria criteria);
	
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
	 * 根据条件查询站点对应的参数，返回集合
	 * @param params
	 * @return
	 */
	public List<TmPathUlocParameter> getTmPathUlocIdsByParameterList(Map<String, Object> params);
}
