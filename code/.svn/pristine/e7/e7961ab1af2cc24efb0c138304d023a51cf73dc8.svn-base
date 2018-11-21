package com.wis.basis.systemadmin.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.systemadmin.entity.User;
import com.wis.core.dao.BaseDao;
import com.wis.core.framework.model.IUser;

public interface UserDao extends BaseDao<User> {


	public void deleteRoleBy(Integer userId, Integer[] roleIds);

	public void updateRole(Map<String, Object> userAndRoles);

	public void saveFormPermissionSetRelation(Integer userId, Integer permissionSetId);

	public void deleteFormPermissionSetRelation(Integer userId, Integer[] permissionSetIds);

	public void saveDataPermissionSetRelation(Integer userId, Integer permissionSetId);

	public void deleteDataPermissionSetRelation(Integer userId, Integer[] permissionSetIds);

	public List<User> queryUserByIds(String[] ids);

	public List<User> queryUserByPermission(String permission);

	public List<User> queryUserByOrgnizationId(Integer orgnizationId);

	public List<IUser> queryUserByRole(Integer roleId);

	public List<User> queryUserByRole(String roleCode);

	public String queryPlantPermissionByUserId(Integer userId);

	public String queryWorkshopPermissionByUserId(Integer userId);

	public String queryLinePermissionByUserId(Integer userId);

	public String queryUlocPermissionByUserId(Integer userId);
	
	List<Map<String,Object>> getLines();

}
