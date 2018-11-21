package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.dao.DataPermissionDao;
import com.wis.core.framework.entity.DataPermission;

@Repository
public class DataPermissionDaoImpl extends BaseDaoImpl<DataPermission> implements DataPermissionDao{

	@Override
	public List<DataPermission> findDataPermissionBy(Integer userId, String moduleName) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("moduleName", moduleName);
		return changeMapsToBeans(getSqlSession().selectList("DataPermissionMapper.selectByUserAndModule", params));
	}

	@Override
	public List<DataPermission> findDataPermissionBy(Integer userId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("userId", userId);
		return changeMapsToBeans(getSqlSession().selectList("DataPermissionMapper.selectByUser", params));
	}

	@Override
	public void savePermissionRelation(Integer setId, Integer permissionId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("setId", setId);
		params.put("permissionId", permissionId);
		getSqlSession().insert("DataPermissionMapper.savePermissionRelation",params);
	}

	@Override
	public void deletePermissionRelation(Integer setId) {
		getSqlSession().delete("DataPermissionMapper.deletePermissionRelation",setId);
	}
	
	@Override
	protected String getConditionHql(Map<String, Object> parameters, QueryCriteria queryCriteria) {
		StringBuffer result = new StringBuffer();
		Map<String, Object> thisParameters = new HashMap<String, Object>(parameters);
		if (parameters.containsKey("permissionSetId")) {
			result.append(" and (");
			result.append(getSqlTableShortName());
			result.append(".ID IN (SELECT t.PERMISSION_ID FROM TR_DATA_PER_PER_SET t WHERE t.PERMISSION_SET_ID=");
			result.append(parameters.get("permissionSetId"));
			result.append("))");
			thisParameters.remove("permissionSetId");
		}
		result.append(super.getConditionHql(thisParameters, queryCriteria));
		return result.toString();
	}
}
