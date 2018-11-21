package com.wis.mes.master.part.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.part.dao.TmPartDao;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.util.StringUtil;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 物料数据持久层接口实现
 *
 */
@Repository
public class TmPartDaoImpl extends BaseDaoImpl<TmPart> implements TmPartDao {

    @Override
    public List<TmPart> getDictItem(final Map<String, Object> param) {
        final String sqlByParam = StringUtil.getSqlByParam(TmPart.class, param);
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sql", sqlByParam);
        return getSqlSession().selectList("PartMapper.queryPartBy", paramMap);
    }

    @Override
    public List<Map<String, Object>> findByPage(final Integer pageNow) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("pageNow", pageNow);
        return getSqlSession().selectList("PartMapper.findByPage", param);
    }

    @Override
    public List<Map<String, Object>> findByIds(final String ids) {
        final StringBuffer sql = new StringBuffer(
                "SELECT ID,CONCAT(NO,'-',NAME_CN) AS NAME FROM TM_PART WHERE ID IN (");
        sql.append(ids).append(");");
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("sql", sql.toString());
        return getSqlSession().selectList("PartMapper.findByIds", param);
    }

}
