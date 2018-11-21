package com.wis.mes.production.record.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.record.dao.TpRecordUlocNcDao;
import com.wis.mes.production.record.entity.TpRecordUlocNc;

@Repository
public class TpRecordUlocNcDaoImpl extends BaseDaoImpl<TpRecordUlocNc> implements TpRecordUlocNcDao {

    @Override
    public PageResult<TpRecordUlocNc> getNcPageResult(final BootstrapTableQueryCriteria criteria) {
        final PageResult<TpRecordUlocNc> pageResult = new PageResult<TpRecordUlocNc>();
        final Integer offsetIndex = criteria.getCurrentIndex();
        final Integer pageSize = criteria.getRows();
        pageResult.setCurrentIndex(criteria.getCurrentIndex());
        pageResult.setPageSize(criteria.getRows());
        final Map<String, Object> params = criteria.getQueryCondition();
        pageResult.setTotalCount(getNcCount(params));
        params.put("firstRow", offsetIndex);
        params.put("pageSize", pageSize);
        if (0 != pageSize) {
            pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
            pageResult.setCurrentPage((offsetIndex + pageSize) / pageSize);
        }
        final List<TpRecordUlocNc> content = getSqlSession().selectList("TpRecordMapper.getNcPageResult", params);
        pageResult.setContent(content);
        return pageResult;
    }

    private int getNcCount(final Map<String, Object> params) {
        return getSqlSession().selectOne("TpRecordMapper.getNcCount", params);
    }

    @Override
    public List<Map<String, Object>> findByCondition(final String sn, final Integer ncId, final Integer ncGroupId,
            final String startTime, final String endTime, final String isAll, final Integer currUser) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("ncId", ncId);
        param.put("ncGroupId", ncGroupId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        return getSqlSession().selectList("TpRecordUlocNcMapper.findByCondition", param);
    }

    @Override
    public List<Map<String, Object>> StatisticByCondition(final String sn, final Integer ncId, final String startTime,
            final String endTime, final String isAll, final Integer currUser, final String type, final String time) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("ncId", ncId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        param.put("type", type);
        param.put("time", time);
        return getSqlSession().selectList("TpRecordUlocNcMapper.StatisticByCondition", param);
    }

    @Override
    public Map<String, Object> StatisticByShift(final String sn, final Integer ncId, final String startTime,
            final String endTime, final String isAll, final Integer currUser, final String shiftStart,
            final String shiftEnd, final String type) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("ncId", ncId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        param.put("shiftStart", shiftStart);
        param.put("shiftEnd", shiftEnd);
        param.put("type", type);
        return getSqlSession().selectOne("TpRecordUlocNcMapper.StatisticByShift", param);
    }

    @Override
    public Map<String, Object> StatisticByWeek(final String sn, final Integer ncId, final String startTime,
            final String endTime, final String isAll, final Integer currUser, final String weekStart,
            final String weekEnd, final String type) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sn", sn);
        param.put("ncId", ncId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("isAll", isAll);
        param.put("currUser", currUser);
        param.put("weekStart", weekStart);
        param.put("weekEnd", weekEnd);
        param.put("type", type);
        return getSqlSession().selectOne("TpRecordUlocNcMapper.StatisticByWeek", param);
    }

}
