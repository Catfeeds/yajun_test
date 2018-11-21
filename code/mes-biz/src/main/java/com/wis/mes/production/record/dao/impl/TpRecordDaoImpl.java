package com.wis.mes.production.record.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.record.dao.TpRecordDao;
import com.wis.mes.production.record.entity.TpRecord;

@Repository
public class TpRecordDaoImpl extends BaseDaoImpl<TpRecord> implements TpRecordDao {

    @Override
    public List<Map<String, Object>> findByCondition(final String sn, final String partNo, final String partName,
            final String porder, final String isAll, final Integer currUser) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("partNo", partNo);
        param.put("partName", partName);
        param.put("porder", porder);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        return getSqlSession().selectList("TpRecordMapper.findByCondition", param);
    }

    @Override
    public List<Map<String, Object>> StatisticByCondition(final String sn, final Integer partId, final String startTime,
            final String endTime, final String isAll, final Integer currUser, final String time) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("partId", partId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        param.put("time", time);
        return getSqlSession().selectList("TpRecordMapper.StatisticByCondition", param);
    }

    @Override
    public Map<String, Object> StatisticByShift(final String sn, final Integer partId, final String startTime,
            final String endTime, final String isAll, final Integer currUser, final String shiftStart,
            final String shiftEnd) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("partId", partId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        param.put("shiftStart", shiftStart);
        param.put("shiftEnd", shiftEnd);
        return getSqlSession().selectOne("TpRecordMapper.StatisticByShift", param);
    }

    @Override
    public Map<String, Object> StatisticByWeek(final String sn, final Integer partId, final String startTime,
            final String endTime, final String isAll, final Integer currUser, final String weekStart,
            final String weekEnd) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("partId", partId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        param.put("weekStart", weekStart);
        param.put("weekEnd", weekEnd);
        return getSqlSession().selectOne("TpRecordMapper.StatisticByWeek", param);
    }

}
