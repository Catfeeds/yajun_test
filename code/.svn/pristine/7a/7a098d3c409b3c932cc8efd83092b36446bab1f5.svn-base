package com.wis.mes.master.nc.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.nc.dao.TmNcDao;
import com.wis.mes.master.nc.entity.TmNc;

@Repository
public class TmNcDaoImpl extends BaseDaoImpl<TmNc> implements TmNcDao {

    @Override
    public List<TmNc> findByNcGroupId(final Integer ncGroupId) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("ncGroupId", ncGroupId);
        return getSqlSession().selectList("NcMapper.findByNcGroupId", param);
    }

    @Override
    public List<TmNc> findByPage(final Integer pageNow) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", pageNow);
        return getSqlSession().selectList("NcMapper.findByPage", param);
    }

    @Override
    public int getCount() {
        return getSqlSession().selectOne("NcMapper.getCount");
    }

    @Override
    public List<Map<String, Object>> findByType(final String ncIds, final String type) {
        final StringBuffer sql = new StringBuffer("SELECT ID,NAME FROM ");
        sql.append("nc".equals(type) ? "TM_NC" : "TM_NC_GROUP").append(" WHERE ID IN (").append(ncIds).append(");");
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sql", sql.toString());
        return getSqlSession().selectList("NcMapper.findByType", param);
    }

}
