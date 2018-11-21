package com.wis.mes.master.nc.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.utils.HqlUtil;
import com.wis.mes.master.nc.dao.TmNcGroupDao;
import com.wis.mes.master.nc.entity.TmNcGroup;

@Repository
public class TmNcGroupDaoImpl extends BaseDaoImpl<TmNcGroup> implements TmNcGroupDao {

   /* @Override
    public String getSqlBy(final QueryCriteria queryCriteria) {
        final Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
        if (queryCondition.isEmpty()) {
            return super.getSqlBy(queryCriteria);
        }
        final StringBuffer queryHql = new StringBuffer(generateQueryHql(queryCriteria));
        queryHql.append(" LEFT JOIN tm_nc tmnc ON tmnc.TM_NC_GROUP_ID = tmncgroup_0.ID WHERE 1=1 ");
        if (queryCondition.containsKey("idIN")) {
            queryHql.append(" AND tmncgroup_0.ID in (" + queryCondition.get("idIN") + ")");
        }
        if (queryCondition.containsKey("no")) {
            queryHql.append("AND (tmncgroup_0.NO LIKE '" + queryCondition.get("no") + "' OR tmnc.NO like '"
                    + queryCondition.get("no") + "')");
        }
        if (queryCondition.containsKey("extCode")) {
            queryHql.append("AND (tmncgroup_0.EXT_CODE LIKE '" + queryCondition.get("extCode")
                    + "' OR tmnc.EXT_CODE like '" + queryCondition.get("extCode") + "')");
        }
        if (queryCondition.containsKey("name")) {
            queryHql.append("AND (tmncgroup_0.NAME LIKE '" + queryCondition.get("name") + "' OR tmnc.NAME like '"
                    + queryCondition.get("name") + "')");
        }

        queryHql.append(
                HqlUtil.generateHqlOrderClause(queryCriteria.getOrderField(), queryCriteria.getOrderDirection()));
        queryHql.append(" GROUP BY tmncgroup_0.ID,tmncgroup_0.CREATE_TIME,tmncgroup_0.CREATE_USER,tmncgroup_0.EXT_CODE,tmncgroup_0. NAME，tmncgroup_0. NO,");
        queryHql.append(" tmncgroup_0.OPT_COUNTER,tmncgroup_0.REMARKS,tmncgroup_0.UPDATE_TIME，tmncgroup_0.UPDATE_USER, ");
        queryHql.append(" tmncgroup_0.TM_LINE_ID,tmline_0.CREATE_TIME,tmline_0.CREATE_USER,tmline_0.ENABLED,tmline_0.ID,tmline_0.NAME_CN,tmline_0.NAME_EN, ");
        queryHql.append("  tmline_0.NO,tmline_0.OPT_COUNTER,tmline_0.REMARK,tmline_0.TM_PLANT_ID,tmline_0.TM_WORKSHOP_ID,tmline_0.UPDATE_TIME,tmline_0.UPDATE_USER ");
        return queryHql.toString();
    }
*/
    /*
     * (non-Javadoc)
     * 
     * @see com.wis.mes.master.nc.dao.TmNcGroupDao#findByPage(java.lang.Integer)
     */
    @Override
    public List<TmNcGroup> findByPage(final Integer pageNow) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", pageNow);
        return getSqlSession().selectList("NcGroupMapper.findByPage", param);
    }

    @Override
    public int getCount() {
        return getSqlSession().selectOne("NcGroupMapper.getCount");
    }

}
