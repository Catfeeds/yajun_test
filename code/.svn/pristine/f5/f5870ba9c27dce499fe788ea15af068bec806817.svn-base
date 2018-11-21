package com.wis.mes.dakin.production.tracing.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.utils.SystemConfig;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.dakin.production.tracing.dao.DkDisZhTblDao;
import com.wis.mes.dakin.production.tracing.entity.DkDisZhTbl;
import com.wis.mes.dakin.production.tracing.vo.ProductPartVo;

@Repository
public class DkDisZhTblDaoImpl extends BaseDaoImpl<DkDisZhTbl> implements DkDisZhTblDao {
	private final static String TABLE_NAME_DAKIN_DIS_ZH_TBL = SystemConfig
			.getPropertyByKey("TABLE_NAME_DAKIN_DIS_ZH_TBL");

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		boolean isDk = false;
		if (queryCondition.containsKey("isDk")) {
			queryCondition.remove("isDk");
			isDk = true;
		}
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("part_0.ID", "part_0.NO");
		if (isDk) {
			sql = sql.replace("DK_DIS_ZH_TBL", TABLE_NAME_DAKIN_DIS_ZH_TBL);
		}
		return sql;
	}

	@Override
	public List<ProductPartVo> queryProductPart(Map<String, Object> param) {
		param.put("dkPartTable", TABLE_NAME_DAKIN_DIS_ZH_TBL);
		return getSqlSession().selectList("ProductPartMapper.queryProductPart", param);
	}
}
