package com.wis.mes.production.plan.porder.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;

@Repository
public class ToPorderAviPathDaoImpl extends BaseDaoImpl<ToPorderAviPath> implements ToPorderAviPathDao {

	@Override
	public List<Map<String, Object>> selectNotOfflineDataByAviId(Integer aviId) {
		return getSqlSession().selectList("AviPathMapper.selectNotOfflineDataByAviId", aviId);
	}

	@Override
	public Integer getPorderPlantIdByAviPathId(Integer toPorderAviPathId) {
		return getSqlSession().selectOne("PorderMapper.getPorderPlantIdByAviPathId", toPorderAviPathId);
	}
}
