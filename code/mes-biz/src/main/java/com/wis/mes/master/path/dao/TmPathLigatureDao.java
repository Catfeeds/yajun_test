package com.wis.mes.master.path.dao;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.path.entity.TmPathLigature;

public interface TmPathLigatureDao extends BaseDao<TmPathLigature> {

	void deleteAllPathsByTmPathId(Integer tmPathId);
}
