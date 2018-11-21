package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.dao.RoleDao;
import com.wis.core.framework.entity.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role>implements RoleDao {

	@Override
	public List<Role> getRoleByUserId(Integer empId) {
		return this.getSqlSession().selectList("RoleMapper.getRoleByUserId", empId);
	}

	@Override
	public void deleteAllFunctionPermissionBy(Integer roleId) {
		getSqlSession().delete("RoleMapper.deletePermissionRelation", roleId);
	}

	@Override
	public void addFunctionPermission(Map<String, Object> roleAndPermission) {
		getSqlSession().delete("RoleMapper.addFunctionPermisson", roleAndPermission);
	}

	@Override
	public void deleteAllFunctionPermissionBy(Integer roleId, Integer[] permissionIds) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("roleId", roleId);
		parms.put("permissionIds", permissionIds);
		getSqlSession().delete("RoleMapper.deleteFunctionPermissionRelationById", parms);
	}

	@Override
	public void deleteAllFormPermissionBy(Integer roleId, Integer[] permissionIds) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("roleId", roleId);
		parms.put("permissionIds", permissionIds);
		getSqlSession().delete("RoleMapper.deleteFormPermissionRelationById", parms);
	}

	@Override
	public void addFormPermission(Map<String, Object> roleAndPermission) {
		getSqlSession().delete("RoleMapper.addFormPermisson", roleAndPermission);
	}

	@Override
	protected String getConditionHql(Map<String, Object> parameters, QueryCriteria queryCriteria) {
		StringBuffer result = new StringBuffer();
		Map<String, Object> thisParameters = new HashMap<String, Object>(parameters);
		if (parameters.containsKey("userId")) {
			result.append(" and (");
			result.append(getSqlTableShortName());
			result.append(".ID IN (SELECT t.ROLE_ID FROM TR_ROLE_USER t WHERE t.USER_ID=");
			result.append(parameters.get("userId"));
			result.append("))");
			thisParameters.remove("userId");
		}
		result.append(super.getConditionHql(thisParameters, queryCriteria));
		return result.toString();
	}

}
