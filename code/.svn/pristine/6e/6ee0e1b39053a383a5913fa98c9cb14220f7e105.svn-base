package com.wis.mes.production.wip.dao.impl;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.wip.dao.TpWipDao;
import com.wis.mes.production.wip.entity.TpWip;

@Repository
public class TpWipDaoImpl extends BaseDaoImpl<TpWip> implements TpWipDao {
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String hql = super.getSqlBy(queryCriteria);
		hql = hql.replace("tpwip_0.NEXT_ULOC as ",
				"(SELECT GROUP_CONCAT(CONCAT(u.`NO`,'-',u.`NAME`)) from tm_uloc u WHERE FIND_IN_SET(u.id,tpwip_0.NEXT_ULOC)) as ");
		return hql;
	}
}
