package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.FinNoFaultDetailsDao;

@Repository
public class FinNoFaultDetailsDaoImpl extends SqlSessionDaoSupport implements FinNoFaultDetailsDao{
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public List<Map<String, Object>> getFinNoFaultStopDetails(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinNoFaultDetailsMapper.getFinNoFaultStopDetails", param);
	}

	@Override
	public List<Map<String, Object>> getFinSwitchStopDetails(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinNoFaultDetailsMapper.getFinNoFaultStopDetails", param);
	}

	@Override
	public List<Map<String, Object>> getFinChangeMaterialStopDetails(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinNoFaultDetailsMapper.getFinChangeMaterialStopDetails", param);
	}

	@Override
	public List<Map<String, Object>> getFinAddOilStopDetails(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinNoFaultDetailsMapper.getFinAddOilStopDetails", param);
	}

	@Override
	public List<Map<String, Object>> noFaultedDetailsYearShow(String year, String createTimeStart,
			String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinNoFaultDetailsMapper.noFaultedDetailsYearShow", param);
	}

}
