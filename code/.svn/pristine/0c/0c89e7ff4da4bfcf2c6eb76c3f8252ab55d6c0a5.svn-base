package com.wis.mes.dakin.report.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.FinMovableRateDetailsDao;
import com.wis.mes.dakin.report.entity.FinMovableRate;

@Repository
public class FinMovableRateDetailsDaoImpl extends SqlSessionDaoSupport implements FinMovableRateDetailsDao {
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public List<Map<String, Object>> getFinMovableRateWhiteAndNightData(String shiftNo, String createTimeStart, String createTimeEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		return getSqlSession().selectList("FinMovableRateDetailsMapper.getFinMovableRateWhiteAndNightData", param);
	}

	@Override
	public List<Map<String, String>> getFaultAndNoFaultTime(String shiftNo, String workCalendarTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("workCalendarTime", workCalendarTime);
		return getSqlSession().selectList("FinMovableRateDetailsMapper.getFaultAndNoFaultTime", param);
	}

	@Override
	public Map<String, Object> getFaultAndNoFaultMessage(String shiftNo, String beginTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		return getSqlSession().selectOne("FinMovableRateDetailsMapper.getFaultAndNoFaultMessage", param);
	}

	@Override
	public List<Map<String, Object>> getFaulitedOrNotCountAndDurationByTime(Date beginTime, Date finishTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("finishTime", finishTime);
		List<Map<String, Object>> selectList = getSqlSession().selectList("FinMovableRateDetailsMapper.getFaulitedOrNotCountAndDurationByTime", param);
		if(selectList!=null){
			return selectList;
		}
		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public List<FinMovableRate> getMovableRateByTime(String createTimeStart, String createTimeEnd,String shiftNo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTimeStart", createTimeStart);
		param.put("createTimeEnd", createTimeEnd);
		param.put("shiftNo", shiftNo);
		return  getSqlSession().selectList("FinMovableRateDetailsMapper.getMovableRateByTime", param);
	}

	@Override
	public List<Map<String, Object>> getFinOneLivelStatusContent(Date beginTime, Date finishTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("finishTime", finishTime);
		List<Map<String, Object>> selectList = getSqlSession().selectList("FinMovableRateDetailsMapper.getFinOneLivelStatusContent", param);
		if(selectList!=null){
			return selectList;
		}
		return new ArrayList<Map<String, Object>>();
	}
}
