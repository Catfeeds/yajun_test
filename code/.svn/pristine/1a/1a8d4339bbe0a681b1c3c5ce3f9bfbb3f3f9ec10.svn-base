package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.FinAnnualReportDao;

@Repository
public class FinAnnualReportDaoImpl extends SqlSessionDaoSupport implements FinAnnualReportDao{
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public List<Integer> getFinNoFailStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinNoFailStopData", param);
	}

	@Override
	public List<Integer> getFinSwitchStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinSwitchStopData", param);
	}
	@Override
	public List<Integer> getFinChangeMaterialStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinChangeMaterialStopData", param);
	}
	@Override
	public List<Integer> getFinAddOilStopData(String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinAddOilStopData", param);
	}
	@Override
	public List<Integer> getFinFaultedData(String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinFaultedData", param);
	}
	@Override
	public List<Map<String, Object>> getFinNoFailStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinNoFailStopByParams", param);
	}
	@Override
	public List<Map<String, Object>> getFinSwitchStopByParams(String year, String shiftType, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinSwitchStopByParams", param);
	}

	@Override
	public List<Map<String, Object>> getFinChangeMaterialStopByParams(String year, String shiftType,
			String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinChangeMaterialStopByParams", param);
	}

	@Override
	public List<Map<String, Object>> getFinAddOilStopByParams(String year, String shiftType, String createTimeStart,
			String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinAddOilStopByParams", param);
	}

	@Override
	public List<Map<String, Object>> getFinFaultedByParams(String year, String shiftType, String createTimeStart,
			String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("shiftNo", shiftType);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinFaultedByParams", param);
	}

	@Override
	public List<Map<String, Object>> getFinFaultedDetails(String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinFaultedDetails", param);
	}

	@Override
	public List<Map<String, Object>> getFinAttendanceTime(String year, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinAnnualReportMapper.getFinAttendanceTime", param);
	}
	
}
