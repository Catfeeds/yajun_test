package com.wis.mes.master.path.dao;

import java.util.List;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.path.entity.TmPathUlocSip;

public interface TmPathUlocSipDao extends BaseDao<TmPathUlocSip> {

	/**
	 * 查询质检参数
	 * 
	 * @param criteria
	 * @return
	 */
	PageResult<TsParameter> getEquipmentAndUlocParameter(QueryCriteria criteria);

	/**
	 * 质检参数必检项 并且不在站点质检表中
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	List<TsParameter> getSipParameterExamineAndNotInSip(Integer tmPathUlocId, Integer tmUlocId,
			String[] tsParameterIds);

	/**
	 * 质检参数必检项
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	List<TsParameter> getSipParameterExamine(Integer tmPathUlocId, Integer tmUlocId);

}
