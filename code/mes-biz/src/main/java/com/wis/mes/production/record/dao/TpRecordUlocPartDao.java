package com.wis.mes.production.record.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.production.record.entity.TpRecordUlocPart;

public interface TpRecordUlocPartDao extends BaseDao<TpRecordUlocPart> {

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
}
