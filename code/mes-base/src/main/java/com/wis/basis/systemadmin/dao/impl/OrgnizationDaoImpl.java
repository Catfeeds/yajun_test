package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.systemadmin.dao.OrgnizationDao;
import com.wis.basis.systemadmin.entity.Orgnization;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;

@Repository
public class OrgnizationDaoImpl extends BaseDaoImpl<Orgnization> implements OrgnizationDao {

	@Override
	protected String getConditionHql(Map<String, Object> parameters, QueryCriteria queryCriteria) {
		StringBuffer result = new StringBuffer();
		Map<String, Object> thisParameters = new HashMap<String, Object>(parameters);
		if (parameters.containsKey("parentAll")) {
			result.append(" and orgnization" + SUFFIX + ".PARENT_ID is null");
			thisParameters.remove("parentAll");
		}
		result.append(super.getConditionHql(thisParameters, queryCriteria));
		return result.toString();
	}

	@Override
	public void doAddUser(Map<String, Object> userOrgMapping) {
		getSqlSession().insert("OrgnizationMapper.addUser", userOrgMapping);
	}

	@Override
	public void doDeleteUserOrgById(Map<String, Object> mapping) {
		getSqlSession().delete("OrgnizationMapper.deleteUserOrgnizationById", mapping);
	}

	@Override
	public List<Orgnization> findByUserId(Integer userId) {
		return getSqlSession().selectList("OrgnizationMapper.findByUserId", userId);
	}
}
