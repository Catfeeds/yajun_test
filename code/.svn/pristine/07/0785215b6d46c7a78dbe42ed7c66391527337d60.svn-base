package com.wis.mes.production.record.service;

import java.util.List;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecordUlocQuality;

public interface TpRecordUlocQualityService extends BaseService<TpRecordUlocQuality> {
	/**
	 * 查询工位已经绑定的质量项
	 * 
	 * @param SN
	 * @param ulocNo
	 * @param routingSeq
	 * @return
	 */
	List<TpRecordUlocQuality> getUlocAlreadyBindQuality(String SN, Integer ulocId, String routingSeq);

	/**
	 * 产品档案质检项信息
	 * 
	 * @author zhaoxianquan
	 * @param criteria
	 * @return
	 */
	public PageResult<TpRecordUlocQuality> getQualityPageResult(BootstrapTableQueryCriteria criteria);

	/**
	 * 保存质量检查项信息
	 * 
	 * @param tpRecordUlocId
	 * @param checkItem
	 * @param checkResult
	 * @param note
	 */
	void doSaveTpRecordUlocQuality(Integer tpRecordUlocId, String[] checkItem, String[] checkResult, String[] note,
			Integer tpRecordId);

}
