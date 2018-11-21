package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.StationDataAnalysisDao;

@Repository
public class StationDataAnalysisDaoImpl extends SqlSessionDaoSupport implements StationDataAnalysisDao {

	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public List<Map<String, Object>> equalNumberWork(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.equalNumberWork",param);
	}

	@Override
	public List<Map<String, Object>> getUlocStatusReport(String createTimeStart, String createTimeEnd,
			Integer checkBit) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		param.put("checkBit", checkBit);
		return getSqlSession().selectList("ReportMapper.getUlocStatusReport",param);
	}
}
