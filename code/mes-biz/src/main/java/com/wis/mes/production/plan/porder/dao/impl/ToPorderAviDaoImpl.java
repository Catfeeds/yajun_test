package com.wis.mes.production.plan.porder.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.plan.porder.dao.ToPorderAviDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;

@Repository
public class ToPorderAviDaoImpl extends BaseDaoImpl<ToPorderAvi> implements ToPorderAviDao {

	@Override
	public void deleteAviBomAndPathByAviId(Integer aviId) {
		getSqlSession().delete("PorderMapper.deleteAviBomAndPathByAviId", aviId);
	}

	@Override
	public Integer getPorderPlantIdByAviId(Integer aviId) {
		return getSqlSession().selectOne("PorderMapper.getPorderPlantIdByAviId", aviId);
	}

	@Override
	public void deleteAviPathDetailByAviId(Integer[] aviPathIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("aviPathIds", aviPathIds);
		getSqlSession().delete("PorderMapper.deleteAviPathDetailByAviId", param);
	}

	@Override
	public Integer getLineId(Integer tmPathId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmPathId", tmPathId);
		param.put("isOnline", "NO");
		Integer tmLineId = getSqlSession().selectOne("AviPathMapper.getLineId", param);
		if (tmLineId == null) {
			param.put("isOnline", "YES");
			tmLineId = getSqlSession().selectOne("AviPathMapper.getLineId", param);
		}
		return tmLineId;
	}
}
