package com.wis.mes.master.uloc.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.uloc.entity.TmUloc;

public interface TmUlocDao extends BaseDao<TmUloc> {
	TmUloc findById(Integer tmUlocId);
	
	/**
	 * 获取是NG入口,NG出入口的工位
	 * @isEntrance 工位类型
	 * **/
	List<TmUloc> getUlocNgExitEnter(String isEntrance);
}
