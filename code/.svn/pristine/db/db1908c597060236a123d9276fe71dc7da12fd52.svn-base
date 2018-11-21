package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.dakin.report.dao.FinExportDao;

@Repository
public class FinExportDaoImpl extends SqlSessionDaoSupport implements FinExportDao {
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public PageResult<Map<String, Object>> getFinExportInfo(QueryCriteria criteria) {
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		Map<String, Object> params = criteria.getQueryCondition();
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		params.put("lastRow", criteria.getRows() + offsetIndex);
		params.put("orderField", criteria.getOrderField());
		params.put("orderDirection", criteria.getOrderDirection());
		List<Map<String, Object>> content = getSqlSession().selectList("FinExportMapper.getFinExportInfo", params);
		pageResult.setTotalCount(queryPageTbUlocStatusCount(params));
		pageResult.setContent(content);
		return pageResult;
	}

	private int queryPageTbUlocStatusCount(Map<String, Object> param) {
		return getSqlSession().selectOne("FinExportMapper.getFinExportInfoCount", param);
	}

	/*
	 * public List<Map<String, Object>> getFinExportInfo(String createTimeStart,
	 * String createTimeEnd) { Map<String, Object> param = new HashMap<String,
	 * Object>(); param.put("createTimeStart", createTimeStart);
	 * param.put("createTimeEnd", createTimeEnd); return
	 * getSqlSession().selectList("FinExportMapper.getFinExportInfo", param); }
	 */
	@Override
	public Map<String, String> getOneLevelStatusContent(String workDate, String shiftNo, String beginTime,
			String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("workDate", workDate);
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectOne("FinExportMapper.getOneLevelStatusContent", param);
	}

	@Override
	public Map<String, String> getFinFaultedCodeName(String shiftNo, String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectOne("FinExportMapper.getFinFaultedCodeName", param);
	}

	@Override
	public List<Map<String, Object>> getFinNoFaultedName(String shiftNo, String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectList("FinExportMapper.getFinNoFaultedName", param);
	}

	@Override
	public List<Map<String, Object>> getFinNoFaultSwitchStopName(String shiftNo, String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectList("FinExportMapper.getFinNoFaultSwitchStopName", param);
	}

	@Override
	public List<Map<String, Object>> getFinNoFaultChangeMaterial(String shiftNo, String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectList("FinExportMapper.getFinNoFaultChangeMaterial", param);
	}

	@Override
	public List<Map<String, Object>> getFinNoFaultAddOil(String shiftNo, String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shiftNo", shiftNo);
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		return getSqlSession().selectList("FinExportMapper.getFinNoFaultAddOil", param);
	}
}
