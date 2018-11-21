package com.wis.mes.production.regulate.dao;

import java.util.List;

import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.wip.entity.TpWip;

public interface OnlineDao {

	/**
	 * 根据工位ID查询
	 * 
	 * @param tmUlocId
	 * @return
	 */
	List<TmUlocSipNc> findTmUlocSipNcByTmUlocId(Integer tmUlocId);

	/**
	 * 查询工位信息
	 * 
	 * @param id
	 * @return
	 */
	TmUloc findUloc(Integer id);

	List<TmUloc> getOnlineUlocByLineId(Integer tmLineId, String ulocIds);

	/**
	 * 根据生产序列ID，工位ID,工位顺序号，查询需要绑定的物料
	 * 
	 * @param toPorderAviId
	 *            生产序列ID
	 * @param tmUlocId
	 *            工位ID
	 * @param seq
	 *            工位顺序号
	 * @return 物料信息
	 */
	List<ToPorderAviBom> getAviBom(Integer toPorderAviId, Integer tmUlocId, String seq, String globalConfig);

	/**
	 * 根据生产序列ID，工位ID,工位顺序号，查询需要绑定的参数
	 * 
	 * @param toPorderAviId
	 *            生产序列ID
	 * @param tmUlocId
	 *            工位ID
	 * @param seq
	 *            工位顺序号
	 * @return 参数信息
	 */
	List<ToPorderAviPathParameter> getAviPathParameter(Integer toPorderAviId, Integer tmUlocId, String seq);

	/**
	 * 根据生产序列ID，工位ID,工位顺序号，查询需要绑定的质检项
	 * 
	 * @param toPorderAviId
	 *            生产序列ID
	 * @param tmUlocId
	 *            工位ID
	 * @param seq
	 *            工位顺序号
	 * @return 参数信息
	 */
	List<ToPorderAviPathSip> getAviPathSip(Integer toPorderAviId, Integer tmUlocId, String seq);

	/**
	 * 根据工位查询待过点工单
	 * 
	 * @param tmUlocId
	 *            工位
	 * @return
	 */
	List<ToPorder> getPorderByUlocId(Integer tmUlocId);

	/**
	 * 根据生产序列Id，和生产序列状态查询待过点生产序列
	 * 
	 * @param aviStatus
	 *            生产序列状态
	 * @param toPorderId
	 *            工单ID
	 * @return
	 */
	List<ToPorderAvi> getAviByPorderId(String aviStatus, Integer toPorderId);

	/**
	 * 根据产线查询当前工作日历
	 * 
	 * @param tmLineId
	 *            产线ID
	 * @return 工作日历
	 */
	TmWorktime getNowWorkTimeByLineId(Integer tmLineId);

	/**
	 * 查询工位一段时间内,在某个操作下的过点数量
	 * 
	 * @param ulocNo
	 *            工位编号
	 * @param operationType
	 *            操作类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 数量
	 */
	Integer getUlocCrossQty(Integer tmUlocId, String operationType, String startTime, String endTime);

	/**
	 * 查询待上线SN
	 * 
	 * @param aviNo
	 * @param routingSeq
	 * @param tmUlocId
	 * @return
	 */
	List<TpWip> getWaitOnlineSn(String aviNo, String routingSeq, Integer tmUlocId);

	/**
	 * 查询工位的通过队列
	 * 
	 * @return
	 */
	List<TpRecordUloc> getAlreadyOnlineData(Integer tmUlocId);

	/**
	 * 未处理缺陷数
	 * 
	 * @param SN
	 * @return
	 */
	int getUnTreatedNcBySN(String SN);
}
