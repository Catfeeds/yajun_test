package com.wis.mes.production.record.dao; 

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.record.entity.TpRecord;


/**   
 * @author Jixueyuan   
 * @date 2017年6月25日
 * @Description: 一次合格率DAO
 */
public interface OnePassRateDao extends BaseDao<TpRecord>{

	List<TpRecord> findPorderTypeSum(String startDate, String endDate);

	List<TmPart> findPartTypeSum(String startDate, String endDate);

	List<TmLine> findLineTypeSum(String startDate, String endDate);

	List<TmUloc> finUlocTypeSum(String startDate, String endDate);

	Float getOnepassRateByPorderAndShift(String startDateStr,String endDateStr, String shift, String legend);

	Float getOnepassRateByPartAndShift(String startDateStr, String endDateStr,String shift, String legend);

	Float getOnepassRateByUlocAndShift(String startDateStr, String endDateStr,String shift, String legend);

	Float getOnepassRateByLineAndShift(String startDateStr, String endDateStr,String shift, String legend);

	Float getOnepassRateByPorderAndMonth(String month, String legend);

	Float getOnepassRateByPartAndMonth(String month, String legend);

	Float getOnepassRateByLineAndMonth(String month, String legend);

	Float getOnepassRateByUlocAndMonth(String month, String legend);

	Float getOnepassRateByPorderAndWeek(String startDateStr, String endDateStr,String legend);

	Float getOnepassRateByPartAndWeek(String startDateStr, String endDateStr,String legend);

	Float getOnepassRateByLineAndWeek(String startDateStr, String endDateStr,String legend);

	Float getOnepassRateByUlocAndWeek(String startDateStr, String endDateStr,String legend);






}
