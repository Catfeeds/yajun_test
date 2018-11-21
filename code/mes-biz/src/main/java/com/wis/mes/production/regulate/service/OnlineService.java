package com.wis.mes.production.regulate.service;

import java.util.List;
import java.util.Map;

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

/**
 * 
 * @company:上海西信
 *
 * @ClassName: OnlineService
 * 
 * @author liuzejun
 *
 * @date 2017年6月21日 下午1:16:30
 * 
 * @Description: 产品上线Service
 */
public interface OnlineService {
	/**
	 * 查询工位信息
	 * 
	 * @param id
	 * @return
	 */
	TmUloc findUlocById(Integer id);

	/**
	 * 根据产线ID,查询工艺路径上线点工位集合
	 * 
	 * @param tmLineId
	 * @return
	 */
	List<TmUloc> getOnlineUlocByLineId(Integer tmLineId);

	/**
	 * 根据生产序列ID，工位ID,工位顺序号，查询BOM信息
	 * 
	 * @param toPorderAviId
	 *            生产序列ID
	 * @param tmUlocId
	 *            工位ID
	 * @param seq
	 *            工位顺序号
	 * @return 物料信息
	 */
	List<ToPorderAviBom> getAviBom(Integer toPorderAviId, Integer tmUlocId, String seq);

	/**
	 * 根据生产序列ID，工位ID,工位顺序号，查询参数信息
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
	 * 根据生产序列ID，工位ID,工位顺序号，查询质检项信息
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
	 *            生产序列状态not in(如果多个，则用逗号隔开)
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
	 *            操作类型(如果多个，则用逗号隔开)
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
	 * @param porderNo
	 * @param aviNo
	 * @param routingSeq
	 * @param tmUlocId
	 * @return
	 */
	List<TpWip> getWaitOnlineSn(Integer aviId, Integer tmUlocId);

	/**
	 * 查询工位的通过队列
	 * 
	 * @return
	 */
	List<TpRecordUloc> getAlreadyOnlineData(Integer tmUlocId);

	/**
	 * 生成SN
	 * 
	 * @param aviId
	 * @param tmUlocId
	 * @return
	 */
	String getSNBySNRule(Integer aviId, Integer tmUlocId);

	/**
	 * 检查哪些需要在该工位绑定
	 * 
	 * @param aviId
	 * @param tmUlocId
	 * @param sn
	 * @return
	 */
	Map<String, Object> checkWhickNeedBind(Integer aviId, Integer tmUlocId, String sn, String currentSeq, String isSip);

	/**
	 * 查询工位需要绑定的关键件
	 * 
	 * @param SN
	 * @param aviId
	 * @param ulocId
	 * @param currentUlocSeq
	 * @return
	 */
	List<ToPorderAviBom> getNeedBindPart(String SN, Integer aviId, Integer ulocId, String currentUlocSeq);

	/**
	 * 查询工位需要绑定的参数
	 * 
	 * @param SN
	 * @param aviId
	 * @param ulocId
	 * @param currentUlocSeq
	 * @return
	 */
	List<ToPorderAviPathParameter> getNeedBindParameter(String SN, Integer aviId, Integer ulocId,
			String currentUlocSeq);

	/**
	 * 查询工位需要绑定的质检项
	 * 
	 * @param SN
	 * @param aviId
	 * @param ulocId
	 * @param currentUlocSeq
	 * @return
	 */
	List<ToPorderAviPathSip> getNeedBindQuality(String SN, Integer aviId, Integer ulocId, String currentUlocSeq);

	/**
	 * 生成SN
	 * 
	 * @param aviId
	 * @param ulocId
	 * @return
	 */
	String doGenerateSN(Integer aviId, Integer ulocId);

	/**
	 * 扫描SN
	 * 
	 * @param aviId
	 * @param ulocId
	 * @param SN
	 * @return
	 */
	Map<String, Object> doScanSN(Integer aviId, Integer ulocId, String SN);

	/**
	 * 查询当前站点SEQ和工艺路径下一节点ids
	 * 
	 * @param tmUlocId
	 *            工位ID
	 * @param aviId
	 *            生产序列ID
	 * @param isOnlyGetCurrentUlocSeq
	 *            是否只获取当前工位SEQ，true只获取当前SEQ，false获取下一节点ulocIds
	 * @return key:currentUlocSeq,nextUlocIds
	 */
	Map<String, Object> getCurrentUlocSeqAndNextUlocIds(Integer tmUlocId, Integer aviId,
			boolean isOnlyGetCurrentUlocSeq);

	/**
	 * 检验SN数据
	 * 
	 * @param toPorderAviId
	 * @param tmUlocId
	 * @param sN
	 * @return currentUlocSeq
	 */
	Map<String, String> doCheckSN(Integer toPorderAviId, Integer tmUlocId, String sN);

	void doSaveNcGroupAndUntreated(Integer recordId, String shiftNo, Integer tmUlocId, String currentUlocSeq,
			String[] qualityCode, String[] checkResult, String[] qualityNote, Integer[] ncGroup, Integer[] ncCode,
			String[] ncTypeInput, String SN);

	/**
	 * 检验物料编码
	 * 
	 * @param partBarCode
	 * @return key tmPartId/partNo/partName
	 */
	Map<String, String> checkPartBarCode(List<ToPorderAviBom> needBindPart, Integer tmPlantId, Integer tmWorkshopId,
			Integer tmLineId, String partBarCode);

	/**
	 * 确认上线
	 * 
	 * @param aviId
	 * @param ulocId
	 * @param SN
	 * @return
	 */
	Map<String, Object> doConfirmOnline(Integer aviId, Integer ulocId, String SN);

	/**
	 * 过点数据
	 * 
	 * @param workTime
	 * @param ulocNo
	 * @return qualityQty、 unQualityQty
	 */
	Map<String, Integer> getCrossQty(TmWorktime workTime, Integer tmUlocId);

	/**
	 * 根据工位ID查询
	 * 
	 * @param tmUlocId
	 * @return
	 */
	List<TmUlocSipNc> findTmUlocSipNcByTmUlocId(Integer tmUlocId);

	/**
	 * 检查是否有NC
	 * 
	 * @param SN
	 */
	void checkIsHaveNc(String SN);

	/**
	 * 未处理缺陷数
	 * 
	 * @param SN
	 * @return
	 */
	int getUnTreatedNcBySN(String SN);
}
