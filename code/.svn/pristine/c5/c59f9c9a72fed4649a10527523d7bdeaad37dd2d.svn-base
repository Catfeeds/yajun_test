package com.wis.mes.production.regulate.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.regulate.dao.CrossPointDao;
import com.wis.mes.production.wip.entity.TpWip;

@Repository
public class CrossPointDaoImpl extends SqlSessionDaoSupport implements CrossPointDao {

	@Override
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public List<TmUloc> getCrossPointUlocByLineId(Integer tmLineId, String tmUlocIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmLineId", tmLineId);
		param.put("tmUlocIds", tmUlocIds);
		return getSqlSession().selectList("CrossPointMapper.getCrossPointUlocByLineId", param);
	}

	@Override
	public String getLastRountingSeq(String SN) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SN", SN);
		return getSqlSession().selectOne("CrossPointMapper.getLastRountingSeq", param);
	}

	@Override
	public void doDeleteNotScanSNFromWip(String SN, Integer id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SN", SN);
		param.put("id", id);
		getSqlSession().delete("CrossPointMapper.deleteNotScanSNFromWip", param);
	}

	@Override
	public List<TpWip> getWaitCrossSNByUlocId(Integer tmUlocId) {
		return getSqlSession().selectList("CrossPointMapper.getWaitCrossSNByUlocId", tmUlocId);
	}

}
