package com.wis.mes.dakin.production.tracing.dao.impl;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.dakin.production.tracing.dao.VbProductOperationDao;
import com.wis.mes.dakin.production.tracing.entity.VbProductOperation;

@Repository
public class VbProductOperationDaoImpl extends BaseDaoImpl<VbProductOperation> implements VbProductOperationDao {
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("ORDER BY vbproductoperation_0.id DESC",
				" ORDER BY  vbproductoperation_0.id ASC,to_number(regexp_substr(\"ulocNo\",'[0-9]*[0-9]',1))ASC ");
		return sql;
	}
}
