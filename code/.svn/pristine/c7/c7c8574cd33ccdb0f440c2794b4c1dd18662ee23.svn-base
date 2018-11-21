package com.wis.mes.production.regulate.dao;

import java.util.List;

import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.wip.entity.TpWip;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: CrossPointDao
 * 
 * @author liuzejun
 *
 * @date 2017年6月21日 下午7:12:16
 * 
 * @Description: 过点DAO
 */
public interface CrossPointDao {

	/**
	 * 过点的工位信息
	 * 
	 * @param tmLineId
	 * @param tmUlocIds
	 * @return
	 */
	List<TmUloc> getCrossPointUlocByLineId(Integer tmLineId, String tmUlocIds);

	/**
	 * 获取上一工位的工艺路径顺序
	 * 
	 * @param SN
	 * @return
	 */
	String getLastRountingSeq(String SN);

	/**
	 * 删除其它工位的SN
	 * 
	 * @param SN
	 * @param id
	 */
	void doDeleteNotScanSNFromWip(String SN, Integer id);

	/**
	 * 查询等待过点的SN
	 * 
	 * @param tmUlocId
	 * @return
	 */
	List<TpWip> getWaitCrossSNByUlocId(Integer tmUlocId);
}
