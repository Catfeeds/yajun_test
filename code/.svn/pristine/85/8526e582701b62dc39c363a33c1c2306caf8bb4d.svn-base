package com.wis.mes.production.record.service;

import java.util.List;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecordUlocParameter;

public interface TpRecordUlocParameterService extends BaseService<TpRecordUlocParameter> {

	/**
	 * 查询工位上已经绑定的参数
	 * 
	 * @param SN
	 * @param ulocNo
	 * @param routingSeq
	 * @return
	 */
	List<TpRecordUlocParameter> getUlocAlreadyBindParameter(String SN, Integer ulocId, String routingSeq);

	/**
	 * 产品档案参数信息
	 * 
	 * @author zhaoxianquan
	 * @param criteria
	 * @return
	 */
	public PageResult<TpRecordUlocParameter> getParameterPageResult(BootstrapTableQueryCriteria criteria);

	/**
	 * 保存参数信息
	 * 
	 * @param parameterCode
	 * @param parameterType
	 * @param parameterValue
	 * @param tpRecordUlocId
	 * @param note
	 */
	void doSaveRecordUlocParameter(String[] parameterCode, String parameterType, String[] parameterValue,
			Integer tpRecordUlocId, String[] note, Integer tpRecordId, String isReplace);

	void updateUntrated(TpRecordUlocParameter newParam, String ordId);

	Object findRelObjById(Integer id);  

}
