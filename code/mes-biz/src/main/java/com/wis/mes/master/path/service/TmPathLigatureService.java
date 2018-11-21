package com.wis.mes.master.path.service;

import com.wis.core.service.BaseService;
import com.wis.mes.master.path.entity.TmPathLigature;

public interface TmPathLigatureService extends BaseService<TmPathLigature> {

	/**
	 * 根据工艺路径id删除所有连线信息
	 * 
	 * @param tmPathId
	 */
	void doDeleteAllPathsByTmPathId(Integer tmPathId);
}
