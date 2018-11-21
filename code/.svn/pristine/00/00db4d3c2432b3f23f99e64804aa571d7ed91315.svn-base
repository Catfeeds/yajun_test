package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.wis.basis.systemadmin.dao.UserDao;
import com.wis.basis.systemadmin.entity.User;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.dao.BaseUserDao;
import com.wis.core.framework.model.IUser;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao, BaseUserDao {

	@Override
	public void deleteRoleBy(Integer userId,Integer [] roleIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleIds", roleIds);
		params.put("userId", userId);
		getSqlSession().delete("UserMapper.deleteRoleRelation", params);
	}

	@Override
	public void updateRole(Map<String, Object> userAndRoles) {
		getSqlSession().delete("UserMapper.updateRole", userAndRoles);
	}

	@Override
	public void saveFormPermissionSetRelation(Integer userId, Integer permissionSetId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("setId", permissionSetId);
		params.put("userId", userId);
		getSqlSession().insert("UserMapper.saveFormPermissionSetRelation", params);
	}

	@Override
	public void deleteFormPermissionSetRelation(Integer  userId,Integer [] permissionSetIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("setIds", permissionSetIds);
		params.put("userId", userId);
		getSqlSession().delete("UserMapper.deleteFormPermissionSetRelation", params);
	}

	@Override
	public void saveDataPermissionSetRelation(Integer userId, Integer permissionSetId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("setId", permissionSetId);
		params.put("userId", userId);
		getSqlSession().insert("UserMapper.saveDataPermissionSetRelation", params);
	}

	@Override
	public void deleteDataPermissionSetRelation(Integer  userId,Integer [] permissionSetIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("setIds", permissionSetIds);
		params.put("userId", userId);
		getSqlSession().delete("UserMapper.deleteDataPermissionSetRelation", params);
	}
	
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String result = super.getSqlBy(queryCriteria);
		if(result.indexOf(" ORDER BY user"+SUFFIX+".ID") > 0 ){
			result.replace(" ORDER BY user"+SUFFIX+".ID"," ORDER BY get_pinyin(name) ");
		}
		if(result.indexOf(" ORDER BY \"name\"") > 0 ){
			result.replace(" ORDER BY \"name\""," ORDER BY get_pinyin(name) ");
		}
		return result;
	}
	@Override
	protected String getConditionHql(Map<String, Object> parameters, QueryCriteria queryCriteria) {
		StringBuffer result = new StringBuffer();
		Map<String, Object> thisParameters = new HashMap<String, Object>(parameters);
		if (parameters.containsKey("selectOrgId")) {
			result.append(" and user"+SUFFIX+".ID in " + getOrgParentSql(parameters.get("selectOrgId")));
		}else{
			if (parameters.containsKey("orgParentId")) {
				result.append(" and user"+SUFFIX+".ID in " + getOrgSectionSql(parameters.get("orgParentId")));
			}
			if (parameters.containsKey("orgSectionId")) {
				result.append(" and user"+SUFFIX+".ID in " + getOrgParentSql(parameters.get("orgSectionId")));
			}
		}
		if (parameters.containsKey("orgnizationId")) {
			result.append(" and user"+SUFFIX+".ID in (select r.USER_ID from TR_USER_ORGNIZATION  r where r.ORGNIZATION_ID ="+parameters.get("orgnizationId")+")");
		}
		if(parameters.containsKey("positionId")){
			result.append(" and user"+SUFFIX+".ID in (select r.USER_ID from TR_USER_POSITION  r where r.POSITION_ID="+parameters.get("positionId")+")");
		}
		thisParameters.remove("selectOrgId");
		thisParameters.remove("orgSectionId");
		thisParameters.remove("orgParentId");
		thisParameters.remove("orgnizationId");
		thisParameters.remove("positionId");
		result.append(super.getConditionHql(thisParameters, queryCriteria));
		return result.toString();
	}
	
	private String getOrgParentSql(Object id){
		return "(select tr.user_id from tr_user_position tr where tr.position_id in (select t.id from ts_position t where t.orgnization_id in("+id+") or t.orgnization_id in("+
				"SELECT id FROM ts_orgnization START WITH parent_id=" +id+ " CONNECT BY PRIOR ID=PARENT_ID )))";
	}
	
	private String getOrgSectionSql(Object id){
		return "(select tr.user_id from tr_user_position tr where tr.position_id in (select t.id from ts_position t where t.orgnization_id in("
				+ "select t.id from ts_orgnization t where t.orgnization_type in ('SECTION','PART') start with t.id="+id
				+"connect by prior t.parent_id=t.id) or t.orgnization_id in("+
				"SELECT id FROM ts_orgnization where orgnization_type in ('SECTION','PART') START WITH parent_id in"
				+ "(select t.id from ts_orgnization t where t.orgnization_type='PART' start with t.id="+id
				+ " connect by prior t.parent_id=t.id) CONNECT BY PRIOR ID=PARENT_ID )))";
	}

	@Override
	public List<User> queryUserByIds(String [] ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return getSqlSession().selectList("UserMapper.queryUserByIds", params);
	}

	@Override
	public List<User> queryUserByPermission(String permission) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("permission", permission);
		return getSqlSession().selectList("UserMapper.queryUserByPermission", params);
	}

	@Override
	public List<User> queryUserByOrgnizationId(Integer orgnizationId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgnizationId", orgnizationId);
		return getSqlSession().selectList("UserMapper.queryUserByOrgnizationId", params);
	}

	@Override
	public User findByAccount(String account) {
		return this.findUniqueByEg(User.createUserByAccount(account));
	}

	@Override
	public List<IUser> queryUserByRole(Integer roleId) {
		return getSqlSession().selectList("UserMapper.getUserByRoleId", roleId);
	}
	
	@Override
	public List<User> queryUserByRole(String roleCode) {
		return getSqlSession().selectList("UserMapper.getUserByRoleCode", roleCode);
	}
	
	@Override
	public List<String> findOrgnizationCodes(Integer userId) {
		return getSqlSession().selectList("UserMapper.getUserOrgnizationCodes", userId);
	}

	@Override
	public String queryPlantPermissionByUserId(Integer userId) {
		List<String> srcs = getSqlSession().selectList("UserMapper.queryPlantPermissionByUserId", userId);
		return joins(srcs,',');
	}

	@Override
	public String queryWorkshopPermissionByUserId(Integer userId) {
		List<String> srcs = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId", userId);
		return joins(srcs,',');
	}

	@Override
	public String queryLinePermissionByUserId(Integer userId) {
		List<String> srcs = getSqlSession().selectList("UserMapper.queryLinePermissionByUserId", userId);
		return joins(srcs,',');
	}

	@Override
	public String queryUlocPermissionByUserId(Integer userId) {
		List<String> srcs = getSqlSession().selectList("UserMapper.queryUlocPermissionByUserId", userId);
		return joins(srcs,',');
	}
	private static String joins(List<String> arrays,char sign){
		StringBuffer buffer = new StringBuffer();
		if(null != arrays && arrays.size() > 0){
			for(String src:arrays){
				if(StringUtils.isNotBlank(src)){
					buffer.append(src+sign);
				}
			}
			if(buffer.length()>0){
				buffer.deleteCharAt(buffer.length()-1);
			}
		}
		return buffer.toString();
	}

	@Override
	public List<Map<String, Object>> getLines() {
		return getSqlSession().selectList("UserMapper.getLines");
	}
}
