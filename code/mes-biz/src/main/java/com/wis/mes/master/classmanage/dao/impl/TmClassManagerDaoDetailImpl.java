package com.wis.mes.master.classmanage.dao.impl;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.classmanage.dao.TmClassManagerDetailDao;
import com.wis.mes.master.classmanage.entity.TmClassManagerDetail;

@Repository
public class TmClassManagerDaoDetailImpl extends BaseDaoImpl<TmClassManagerDetail> implements TmClassManagerDetailDao {
	
	
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		String orderSrc = "ORDER BY \"tmUloc.no\" "+queryCriteria.getOrderDirection();
		if(sql.indexOf(orderSrc) > -1){
			sql = sql.replace(orderSrc, "ORDER BY to_number(regexp_substr(tmuloc_0.no,'[0-9]*[0-9]',1)) "+queryCriteria.getOrderDirection());
		}
		return sql;
	}

}
