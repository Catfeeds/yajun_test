package com.wis.basis.systemadmin.service;

import java.util.List;
import java.util.Map;

import com.wis.basis.systemadmin.entity.User;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.model.IUser;
import com.wis.core.service.BaseService;

public interface UserService extends BaseService<User> {

	public void doRemove(Integer[] ids, User currentUser);

	public Integer checkAccount(String account);

	// public void doResetPassword(Integer[] ids, User user);

	public void doLock(Integer[] ids);

	public void doLock(String username);

	public void doUnlock(Integer[] ids);

	public User doUpdateLoginDate(Integer userId);

	public User doResetPassword(String oldPassword, String newPassword);

	public void doResetPasswordToDefault(Integer[] ids, User currentUser);

	public List<String> getAuditMessage(String id);

	/**
	 * 返回所有用户，以Account为Key
	 * 
	 * @return
	 */
	public Map<String, User> getAllKeyAccount();

	/**
	 * 返回所有用户，以Id为Key
	 * 
	 * @return
	 */
	public Map<Integer, User> getAllKeyId();

	public User getByAccount(String account);

	public void doAddUserRole(Integer userId, Integer[] roleIds);

	public void doDeleteUserRole(Integer userId, Integer[] roleIds);

	public List<User> queryUserByIds(String[] ids);

	public String findAccoutByProcessParam(String param);

	public List<User> queryUserByPermission(String permission);

	public List<User> queryUserByOrgnizationId(Integer orgnizationId);

	public List<User> getUserByRole(String code);

	public List<IUser> getUserByRole(Integer roleId);
	
	//查询工厂权限
	public String queryPlantPermissionByUserId(Integer userId);
	//查询车间权限
	public String queryWorkshopPermissionByUserId(Integer userId);
	//查询产线权限
	public String queryLinePermissionByUserId(Integer userId);
	//查询工位权限
	public String queryUlocPermissionByUserId(Integer userId);
	
	List<DictEntry> getDictItem(User param);
	
	List<Map<String,Object>> getLines();
}
