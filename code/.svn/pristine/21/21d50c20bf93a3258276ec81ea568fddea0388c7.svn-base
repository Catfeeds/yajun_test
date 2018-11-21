package com.wis.mes.production.record.dao.impl; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.record.dao.OnePassRateDao;
import com.wis.mes.production.record.entity.TpRecord;

/**   
 * @author Jixueyuan   
 * @date 2017年6月25日
 * @Description: 一次合格率DaoImpl 
 */
@Repository
public class OnePassRateDaoImpl extends BaseDaoImpl<TpRecord> implements OnePassRateDao {

	@Override
	public List<TmPart> findPartTypeSum(String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate+" 00:00:00");
		param.put("endDate", endDate+" 23:59:59");
		return getSqlSession().selectList("TpRecordMapper.findPartTypeSum",param);
	}

	@Override
	public List<TmLine> findLineTypeSum(String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate+" 00:00:00");
		param.put("endDate", endDate+" 23:59:59");
		return getSqlSession().selectList("TpRecordMapper.findLineTypeSum",param);
	}

	@Override
	public List<TpRecord> findPorderTypeSum(String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate+" 00:00:00");
		param.put("endDate", endDate+" 23:59:59");
		return getSqlSession().selectList("TpRecordMapper.findPorderTypeSum",param);
	}
	
	@Override
	public List<TmUloc> finUlocTypeSum(String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate+" 00:00:00");
		param.put("endDate", endDate+" 23:59:59");
		return getSqlSession().selectList("TpRecordMapper.finUlocTypeSum",param);
	}

	@Override
	public Float getOnepassRateByPorderAndShift(String startDateStr,String endDateStr, String shift, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr);
		param.put("endDateStr", endDateStr);
		param.put("shift", shift);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByPorderAndShift",param);
	}

	@Override
	public Float getOnepassRateByPartAndShift(String startDateStr,String endDateStr, String shift, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr);
		param.put("endDateStr", endDateStr);
		param.put("shift", shift);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByPartAndShift",param);
	}

	@Override
	public Float getOnepassRateByUlocAndShift(String startDateStr,String endDateStr, String shift, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr);
		param.put("endDateStr", endDateStr);
		param.put("shift", shift);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByUlocAndShift",param);
	}

	@Override
	public Float getOnepassRateByLineAndShift(String startDateStr,
			String endDateStr, String shift, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr);
		param.put("endDateStr", endDateStr);
		param.put("shift", shift);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByLineAndShift",param);
	}

	@Override
	public Float getOnepassRateByPorderAndMonth(String month, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("month", month);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByPorderAndMonth",param);
	}

	@Override
	public Float getOnepassRateByPartAndMonth(String month, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("month", month);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByPartAndMonth",param);
	}

	@Override
	public Float getOnepassRateByLineAndMonth(String month, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("month", month);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByLineAndMonth",param);
	}

	@Override
	public Float getOnepassRateByUlocAndMonth(String month, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("month", month);
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByUlocAndMonth",param);
	}

	@Override
	public Float getOnepassRateByPorderAndWeek(String startDateStr,String endDateStr, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr+" 00:00:00");
		param.put("endDateStr", endDateStr+" 23:59:59");
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByPorderAndWeek",param);
	}

	@Override
	public Float getOnepassRateByPartAndWeek(String startDateStr,String endDateStr, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr+" 00:00:00");
		param.put("endDateStr", endDateStr+" 23:59:59");
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByPartAndWeek",param);
	}

	@Override
	public Float getOnepassRateByLineAndWeek(String startDateStr,String endDateStr, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr+" 00:00:00");
		param.put("endDateStr", endDateStr+" 23:59:59");
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByLineAndWeek",param);
	}

	@Override
	public Float getOnepassRateByUlocAndWeek(String startDateStr,String endDateStr, String legend) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startDateStr", startDateStr+" 00:00:00");
		param.put("endDateStr", endDateStr+" 23:59:59");
		param.put("legend", legend);
		return getSqlSession().selectOne("TpRecordMapper.getOnepassRateByUlocAndWeek",param);
	}

}
