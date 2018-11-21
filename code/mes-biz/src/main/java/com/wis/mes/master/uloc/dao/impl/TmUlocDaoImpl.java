package com.wis.mes.master.uloc.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.uloc.dao.TmUlocDao;
import com.wis.mes.master.uloc.entity.TmUloc;

@Repository
public class TmUlocDaoImpl extends BaseDaoImpl<TmUloc> implements TmUlocDao {

	@Override
	public TmUloc findById(Integer tmUlocId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", tmUlocId);
		return getSqlSession().selectOne("UlocMapper.findUlocBy", param);
	}

	@Override
	public List<TmUloc> getUlocNgExitEnter(String isEntrance) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isEntrance", isEntrance);
		return getSqlSession().selectList("UlocMapper.findUlocEg",map);
	}
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		String sort = "\""+"no"+"\"";
		if(StringUtils.isNotBlank(queryCriteria.getOrderField()) && queryCriteria.getOrderField().equals(sort)){
			sql = sql.replace("ORDER BY "+sort+"", "ORDER BY to_number(regexp_substr(tmuloc_0.no,'[0-9]*[0-9]',1))");
		}
		return sql;
	}
	
}
