package com.wis.mes.production.qualitytracing.service;

import java.util.Map;

import com.wis.core.service.BaseService;
import com.wis.mes.production.qualitytracing.entity.TbQualityTracing;
import com.wis.mes.util.ExportPageResult;

public interface TbQualityTracingService extends BaseService<TbQualityTracing>,ExportPageResult<TbQualityTracing>{

	/**
	 * @sn 通过sn验证故障修复状态 return key flag/ngEntry
	 *     true(NG已经全部修复)/false(NG有未修复的缺陷，或没有故障。),ngEntry NG入口
	 **/
	Map<String, Object> verifyFailureState(String sn);

	/**
	 * @sn
	 * @ulocNo 托盘从NG出口流出，就在故障机管理添加一条故障信息
	 **/
	void doSaveQualityTracing(String sn, String ulocNo);

	void doSaveTbQualityTracing(String SN, String ngExitUloc, String discoveryStation, String shiftNo,
			Integer classManagerId, Integer infoSource, String abnormalCode,Integer worktimeId);
	/**
	 * @author yajun_ren
	 * @param sn
	 * @param ulocNo
	 * 更新故障实际适口
	 * **/
	void doOnSnUpdateQualityTracing(String sn,String ulocNo); 
}
