/**
 * @author caixia
 */
package com.wis.mes.dakin.report.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.wis.mes.dakin.report.dao.YearReportServiceDao;

@Repository
public class YearReportServiceDaoImpl extends SqlSessionDaoSupport implements YearReportServiceDao {
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	/* (non-Javadoc)
	 * @see com.wis.mes.dakin.report.dao.YearReportServiceDao#getMetalPlateYearDetailPieReportInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, List> getMetalPlateYearDetailPieReportInfo(String shiftNo,
			String beginTime, String endTime) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		param.put("endTime", endTime);
		if(StringUtils.isNotEmpty(beginTime) && !StringUtils.isNotEmpty(endTime)){
			param.put("year", beginTime.split("-")[0]);
		}else if(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}
		else if(!StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = new Date();
			String year = sdf.format(date);	
			param.put("year", year);
		}
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateYearDetailPieReportInfo", param);
		List<Map<String, Object>> selectList2 = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateYearDetailearReport", param);
		Map<String, List> selectMap = new HashMap<>();
		selectMap.put("data1", selectList);
		selectMap.put("data2", selectList2);
		return selectMap;
	}

	/* (non-Javadoc)
	 * @see com.wis.mes.dakin.report.dao.YearReportServiceDao#getMetalPlateYearDetailearShiftReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getMetalPlateYearDetailearShiftReport(String whitOrNight,String shiftNo,
			String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		param.put("whitOrNight", whitOrNight);
		if(StringUtils.isNotEmpty(beginTime) && !StringUtils.isNotEmpty(endTime)){
			param.put("year", beginTime.split("-")[0]);
		}else if(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}
		else if(!StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = new Date();
			String year = sdf.format(date);
			param.put("year", year);
		}
		
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateYearDetailearShiftReport", param);
		return selectList;
	}

	/* (non-Javadoc)
	 * @see com.wis.mes.dakin.report.dao.YearReportServiceDao#getMetalPlateYearDetailearReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getMetalPlateYearDetailearReport(String shiftNo,
			String beginTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		if(StringUtils.isNotEmpty(beginTime) && !StringUtils.isNotEmpty(endTime)){
			param.put("year", beginTime.split("-")[0]);
		}else if(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}
		else if(!StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = new Date();
			String year = sdf.format(date);
			param.put("year", year);
		}
		
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateYearDetailearReport", param);
		return selectList;
	}
	@Override
	public List<Map<String, Object>> getMetalPlateYearListReport(String shiftNo, String beginTime, String endTime) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", beginTime);
		param.put("endTime", endTime);
		if(StringUtils.isNotEmpty(beginTime) && !StringUtils.isNotEmpty(endTime)){
			param.put("year", beginTime.split("-")[0]);
		}else if(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}
		else if(!StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)){
			param.put("year", endTime.split("-")[0]);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date date = new Date();
			String year = sdf.format(date);
			param.put("year", year);
		}
		
		List<Map<String, Object>> selectList = getSqlSession().selectList("MetalPlateReportMapper.getMetalPlateYearListReport", param);
		return selectList;
	
	}

}
