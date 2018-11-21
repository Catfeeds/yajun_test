package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.ReportDao;

@Repository
public class ReportDaoImpl extends SqlSessionDaoSupport implements ReportDao {
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public List<Map<String, Object>> queryNgGroupShiftnoRelation(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.queryNgGroupShiftnoRelation", param);
	}

	@Override
	public List<Map<String, Object>> queryNgGroupCount(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.queryNgGroupCount", param);
	}

	@Override
	public List<Map<String, Object>> ngAndInfoOrigin(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.ngAndInfoOrigin", param);
	}

	@Override
	public List<Map<String, Object>> ulocStatusTimeCount(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.ulocStatusTimeCount", param);
	}

	@Override
	public List<Map<String, Object>> ulocStatusCountAndDetail(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.ulocStatusCountAndDetail", param);
	}

	@Override
	public List<Map<String, Object>> ulocStatusAbnormalSourceCountAndMintue(String abnormalSource,
			String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("abnormalSource", abnormalSource);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.ulocStatusAbnormalSourceCountAndMintue_" + abnormalSource,
				param);
	}

	@Override
	public List<Map<String, Object>> ulocStatusYearDensity(String year, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.ulocStatusYearDensity", param);
	}

	@Override
	public List<Map<String, Object>> energReport(String timeFormate, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("timeFormate", timeFormate);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("ReportMapper.energReport", param);
	}

	@Override
	public Map<String, Object> plcEnergyReport(String startDateTime, String endDateTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDateTime", startDateTime);
		param.put("endDateTime", endDateTime);
		return getSqlSession().selectOne("PLCReportMapper.energyReport", param);
	}
}
