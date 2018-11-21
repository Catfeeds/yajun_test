package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.FinFaultedDetailsDao;

@Repository
public class FinFaultedDetailsDaoImpl extends SqlSessionDaoSupport implements FinFaultedDetailsDao{
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public List<Map<String, Object>> getFaultedBarInfo(String shiftNo, String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectList("FinFaultedDetailsMapper.getFaultedBarInfo", param);
	}
	
	@Override
	public List<Map<String, Object>> faultedDetailsYearShow(String year, String createTimeStart,
			String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinFaultedDetailsMapper.faultedDetailsYearShow", param);
	}

}
