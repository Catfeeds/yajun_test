package com.wis.mes.production.regulate.service;

import java.util.List;
import java.util.Map;

import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.wip.entity.TpWip;

/**
 * @company:上海西信
 *
 * @ClassName: CrossPointService
 * 
 * @author liuzejun
 *
 * @date 2017年6月21日 下午7:12:27
 * 
 * @Description: 过点Service
 */
public interface CrossPointService {
	/**
	 * 过点的工位信息
	 * 
	 * @param tmLineId
	 * @return
	 */
	List<TmUloc> getCrossPointUlocByLineId(Integer tmLineId);

	/**
	 * 扫描SN
	 * 
	 * @param tmUlocId
	 * @param SN
	 * @return
	 */
	Map<String, Object> doScanSN(Integer tmUlocId, String SN);

	/**
	 * 检查SN
	 * 
	 * @param SN
	 * @param tmUlocId
	 * @param isOnlyGetCurrentSeq
	 * @return
	 */
	Map<String, Object> checkSN(String SN, Integer tmUlocId, boolean isOnlyGetCurrentSeq);

	/**
	 * 确认过点
	 * 
	 * @param aviId
	 * @param ulocId
	 * @param SN
	 * @return
	 */
	Map<String, Object> doConfirmCrossPoint(Integer aviId, Integer ulocId, String SN);

	/**
	 * 查询等待过点的SN
	 * 
	 * @param tmUlocId
	 * @return
	 */
	List<TpWip> getWaitCrossSNByUlocId(Integer tmUlocId);
	
	Map<String, Object> holdCheckSn(String SN, Integer tmUlocId);
}
