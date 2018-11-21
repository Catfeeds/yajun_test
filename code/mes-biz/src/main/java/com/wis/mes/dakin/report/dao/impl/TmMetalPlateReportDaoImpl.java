/**
 * 
 */
package com.wis.mes.dakin.report.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.TmMetalPlateReportDao;

/**
 * @author caixia
 *
 */
@Repository
public class TmMetalPlateReportDaoImpl extends SqlSessionDaoSupport implements TmMetalPlateReportDao {

	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	/* (non-Javadoc)
	 * @see com.wis.mes.dakin.report.dao.TmMetalPlateReportDao#getMetalPlateReport()
	 */
	@Override
	public List<Map<String, Object>> getMetalPlateReport(String createTime, Integer runingState) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTime", createTime);
		param.put("runingState", runingState);
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateReport", param);
		return selectList;
	}

	@Override
	public List<Map<String, Object>> getMetalPlateReportWarnByMouth(String createTime, Integer runingState) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTime", createTime);
		param.put("runingState", runingState);
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateReportWarnByMouth", param);
		return selectList;
	}

	@Override
	public List<Map<String, Object>> getMetalPlateReportunusualByMouth(String createTime, String runingState) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createTime", createTime);
		param.put("runingState", runingState);
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateReportunusualByMouth", param);
		return selectList;
	}
	
	@Override
	public List<String> getTmpEquipmentNos(){
		return getSqlSession().selectList("MetalPlateReportMapper.getTmpEquipmentNos", null);
	}
}
