package com.wis.mes.master.boardmanage.service;

import java.util.List;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.boardmanage.entity.TmBoardManage;

public interface TmBoardManageService extends BaseService<TmBoardManage> {
	
	/**
	 * @author yajun_ren
	 * @desc 根据区域获取机台信息
	 * @date 2018/09/05
	 * @param regionMark
	 * @return TmBoardManage
	 * 
	 * **/
	TmBoardManage findBoardManageInfo(String regionMark) ;
	
	/**
	 * @author yajun_ren
	 * @desc 根据区域获取区域
	 * @date 2018/09/07
	 * @param criteria
	 * @return List<DictEntry>
	 * 
	 * **/
	List<DictEntry> queryDictItem(QueryCriteria criteria);
	
	/**
	 * @author yajun_ren
	 * @desc 目标可动率，发送至PLC
	 * @date 2018/10/30
	 * @param bean
	 * 
	 * **/
	void sendParameterToOPC(TmBoardManage bean);
}
