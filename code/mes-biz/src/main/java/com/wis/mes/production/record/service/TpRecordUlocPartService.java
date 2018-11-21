package com.wis.mes.production.record.service;

import java.util.List;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecordUlocPart;

public interface TpRecordUlocPartService extends BaseService<TpRecordUlocPart> {

	/**
	 * 查询已经绑定的关键件
	 * 
	 * @param SN
	 * @param ulocNo
	 *            工位编号
	 * @param routingSeq
	 *            工位顺序
	 * @return
	 */
	public List<TpRecordUlocPart> getUlocAlreadyBindPart(String SN, Integer ulocId, String routingSeq);

	/**
	 * 产品档案装配物料详情
	 * 
	 * @author zhaoxianquan
	 * @param criteria
	 * @return PageResult
	 */
	public PageResult<TpRecordUlocPart> getPartPageResult(QueryCriteria criteria);
	
	/**
	 * 缺陷品  物料 更新 - 数量
	 * @param newPart
	 * @param ordId
	 */
	public void updateUntrated(TpRecordUlocPart newPart, String ordId); 

	/**
	 * 关键件信息保存
	 * 
	 * @param tpRecordId
	 * @param tpRecordUlocId
	 * @param tmPartId
	 * @param qty
	 * @param barCode
	 * @param partNo
	 * @param partName
	 * @param isReplace
	 * @param note
	 * @return
	 */
	TpRecordUlocPart doSaveTpRecordUlocPart(Integer tpRecordId, Integer tpRecordUlocId, Integer tmPartId, Integer qty,
			String barCode, String partNo, String partName, String isReplace, String note);
}
