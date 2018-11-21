package com.wis.mes.master.path.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.path.dao.TmPathLigatureDao;
import com.wis.mes.master.path.entity.TmPathLigature;

@Repository
public class TmPathLigatureDaoImpl extends BaseDaoImpl<TmPathLigature> implements TmPathLigatureDao {

	@Override
	public void deleteAllPathsByTmPathId(Integer tmPathId) {
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("tmPathId", tmPathId);
		getSqlSession().delete("PathMapper.deleteAllPathsByTmPathId", paramMap);
	}
}
