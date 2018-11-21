package com.wis.mes.production.plan.porder.dao;

import java.util.List;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathSipDao
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:39:49
 * 
 * @Description: 质检项Dao
 */
public interface ToPorderAviPathSipDao extends BaseDao<ToPorderAviPathSip> {

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
	 * @param toPorderAviPathId
	 * @return
	 */
	List<TsParameter> getSipParameterExamineAndNotInSip(Integer toPorderAviPathId);

	/**
	 * 质检参数必检项
	 * 
	 * @param toPorderAviPathId
	 * @return
	 */
	List<TsParameter> getSipParameterExamine(Integer toPorderAviPathId);
}
