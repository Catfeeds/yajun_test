package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.dao.FormPermissionSetDao;
import com.wis.core.framework.entity.FormPermissionSet;

@Repository
public class FormPermissionSetDaoImpl extends BaseDaoImpl<FormPermissionSet> implements FormPermissionSetDao{
	protected String getConditionHql(Map<String, Object> parameters, QueryCriteria queryCriteria) {
		StringBuffer result = new StringBuffer();
		Map<String, Object> thisParameters = new HashMap<String, Object>(parameters);
		if (parameters.containsKey("userId")) {
			result.append(" and (");
			result.append(getSqlTableShortName());
			result.append(".ID IN (SELECT t.PERMISSION_SET_ID FROM TR_USER_FORM_PERMISSION t WHERE t.USER_ID=");
			result.append(parameters.get("userId"));
			result.append("))");
			thisParameters.remove("userId");
		}
		result.append(super.getConditionHql(thisParameters, queryCriteria));
		return result.toString();
	}
}
