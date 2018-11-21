package com.wis.mes.production.producttracing.dao;

import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.producttracing.entity.TbProductStation;

public interface TbProductStationDao extends BaseDao<TbProductStation> {

	Map<String, Integer> getLineIdAndUlocIdByUlocNo(String ulocNo);

	Integer getStaffIdBy(Integer ulocId, Integer lineId,Integer tmClassManagerId);

	TmWorktime getCurrentWorkTime(Integer tmLineId);

	Integer getProductIdBySn(String sn);

}
