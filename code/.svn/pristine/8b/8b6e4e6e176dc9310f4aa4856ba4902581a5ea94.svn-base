package com.wis.basis.systemadmin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.security.MD5Utils;
import com.wis.basis.systemadmin.dao.UserDao;
import com.wis.basis.systemadmin.entity.User;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.bpm.core.interaction.IBPMUserService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.entity.Removable;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.model.IUser;
import com.wis.core.framework.service.BaseUserService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService, BaseUserService, IBPMUserService {

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	public void setDao(UserDao dao) {
		this.dao = dao;
	}

	@Override
	public Integer checkAccount(String account) {
		List<User> rList = dao.findByEg(User.createUserByAccount(account));
		if (rList.size() > 0) {
			return rList.get(0).getId();
		}
		return null;
	}

	@Override
	public void doRemove(Integer[] ids, User currentUser) {
		for (Integer removeUserId : ids) {
			if (currentUser.getId().equals(removeUserId)) {
				throw new BusinessException("ERROR_DELETE_NOT_DELETE_SELF");
			}
			User user = dao.get(removeUserId);
			dao.remove(user);
		}
	}

	@Override
	public void doResetPasswordToDefault(Integer[] ids, User cureentUser) {
		if (Arrays.asList(ids).contains(cureentUser.getId())) {
			throw new BusinessException("ERROR_USER_NOT_RESET_SELF_PASSWORD");
		}
		for (int i = 0; i < ids.length; i++) {
			User user = dao.get(ids[i]);
			String realPassword = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_RESET_DEFAULT);
			String password = MD5Utils.passwordEncoder(realPassword, user.getAccount());
			user.setPassword(password);
			dao.update(user);
		}
	}

	@Override
	public void doLock(Integer[] ids) {
		Integer currentUserId = WebContextHolder.getCurrentUser().getId();
		for (int j = 0; j < ids.length; j++) {
			if (ids[j].intValue() == currentUserId.intValue()) {
				throw new BusinessException("ERROR_NOT_LOCK_SELF");
			}
		}

		for (int i = 0; i < ids.length; i++) {
			User user = dao.get(ids[i]);
			user.setStatus(ConstantUtils.USER_ACCOUNT_LOCKED);
			dao.update(user);
		}

	}

	@Override
	public void doUnlock(Integer[] ids) {
		Integer currentUserId = WebContextHolder.getCurrentUser().getId();
		for (int j = 0; j < ids.length; j++) {
			if (ids[j].intValue() == currentUserId.intValue()) {
				throw new BusinessException("ERROR_NOT_UNLOCK_SELF");
			}
		}

		for (int i = 0; i < ids.length; i++) {
			User user = dao.get(ids[i]);
			user.setStatus(ConstantUtils.USER_ACCOUNT_NORMAL);
			dao.update(user);
		}

	}

	@Override
	public User doSave(User user) {
		user.setPassword(MD5Utils.passwordEncoder(user.getPassword(), user.getAccount()));
		dao.save(user);
		return user;
	}

	public User doUpdate(User user) {
		return update(user);
	}

	private User update(User user) {
		User updateUser = dao.get(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setName(user.getName());
		updateUser.setAccount(user.getAccount());
		updateUser.setStatus(user.getStatus());
		updateUser.setGender(user.getGender());
		updateUser.setTmLineIds(user.getTmLineIds());
		dao.update(updateUser);
		return user;
	}

	@Override
	public User doResetPassword(String oldPassword, String newPassword) {
		User user = dao.get(WebContextHolder.getCurrentUser().getId());
		String password = MD5Utils.passwordEncoder(oldPassword, user.getAccount());
		if (!user.getPassword().equals(password)) {
			throw new BusinessException("ERROR_USER_RAW_PASSWORD_WRONG");
		}
		// 历史密码累计，用逗号隔开
		String[] historyPwds = StringUtils.split(user.getHistoryPwd(), ",");
		if (StringUtils.isBlank(user.getHistoryPwd())) {
			historyPwds = new String[0];
		}
		List<String> historyPwdsList = new ArrayList<String>(Arrays.asList(historyPwds));
		String delPvpCount = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_DEL_PVP_COUNT);
		if (StringUtils.isNotBlank(delPvpCount)) {
			int count = Integer.parseInt(delPvpCount) - 1;
			if (historyPwds.length >= count) {
				historyPwdsList.remove(0);
			}
			historyPwdsList.add(password);
		}

		user.setHistoryPwd(StringUtils.join(historyPwdsList, ","));
		user.setPwdUpdateTime(new Date());
		user.setPassword(MD5Utils.passwordEncoder(newPassword, user.getAccount()));
		dao.update(user);
		return user;
	}

	public List<String> getAuditMessage(String id) {
		User user = findById(Integer.valueOf(id));
		if (user == null)
			return null;
		List<String> message = new ArrayList<String>();
		message.add(user.getId().toString());
		message.add(user.getName());
		message.add(user.getAccount());
		message.add(user.getStatus());
		message.add(user.getEmail());
		message.add(user.getPhone());
		message.add(user.getGender());

		if (user.getLastLoginDate() != null) {
			message.add(DateFormatUtils.format(user.getLastLoginDate(), "yyyy-MM-dd HH:mm:ss"));
		} else {
			message.add("");
		}
		return message;
	}

	@Override
	public User doUpdateLoginDate(Integer userId) {
		User user = dao.get(userId);
		user.setLastLoginDate(new Date());
		dao.update(user);
		return user;
	}

	@Override
	public void doLock(String username) {
		try {
			User user = dao.findUniqueByEg(User.createUserByAccount(username));
			if (user != null) {
				user.setStatus(ConstantUtils.USER_ACCOUNT_LOCKED);
				dao.update(user);
			} else {
				log.error("Lock user error the user is not exists ,usernamee:" + username);
			}
		} catch (Exception e) {
			log.error("Lock user error:" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Map<String, User> getAllKeyAccount() {
		Map<String, User> result = new HashMap<String, User>();
		List<User> users = dao.findAll();
		for (User user : users) {
			result.put(user.getAccount(), user);
		}
		return result;
	}

	@Override
	public Map<Integer, User> getAllKeyId() {
		Map<Integer, User> result = new HashMap<Integer, User>();
		List<User> users = dao.findAll();
		for (User user : users) {
			result.put(user.getId(), user);
		}
		return result;
	}

	@Override
	public User getByAccount(String account) {
		User user = new User();
		user.setAccount(account);
		user.setRemoved(Removable.NOT_LOGIC_DELETE);
		return dao.findUniqueByEg(user);
	}

	@Override
	public void doAddUserRole(Integer userId, Integer[] roleIds) {
		((UserDao) dao).deleteRoleBy(userId, roleIds);
		for (Integer roleId : roleIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", roleId);
			map.put("userId", userId);
			((UserDao) dao).updateRole(map);
		}
		((UserDao) dao).saveFormPermissionSetRelation(userId, 1);
	}

	@Override
	public void doDeleteUserRole(Integer userId, Integer[] roleIds) {
		((UserDao) dao).deleteRoleBy(userId, roleIds);
	}

	@Override
	public List<User> queryUserByIds(String[] ids) {
		return ((UserDao) dao).queryUserByIds(ids);
	}

	public String findAccoutByProcessParam(String param) {
		if (param != null && !("").equals(param)) {
			return param;
		}
		return null;
	}

	@Override
	public List<User> queryUserByPermission(String permission) {
		return ((UserDao) dao).queryUserByPermission(permission);
	}

	@Override
	public List<User> queryUserByOrgnizationId(Integer orgnizationId) {
		return ((UserDao) dao).queryUserByOrgnizationId(orgnizationId);
	}

	@Override
	public List<IUser> getUserByRole(Integer roleId) {
		return ((UserDao) dao).queryUserByRole(roleId);
	}

	@Override
	public List<User> getUserByRole(String code) {
		return ((UserDao) dao).queryUserByRole(code);
	}

	@Override
	public String queryPlantPermissionByUserId(Integer userId) {
		return ((UserDao) dao).queryPlantPermissionByUserId(userId);
	}

	@Override
	public String queryWorkshopPermissionByUserId(Integer userId) {
		return ((UserDao) dao).queryWorkshopPermissionByUserId(userId);
	}

	@Override
	public String queryLinePermissionByUserId(Integer userId) {
		return ((UserDao) dao).queryLinePermissionByUserId(userId);
	}

	@Override
	public String queryUlocPermissionByUserId(Integer userId) {
		return ((UserDao) dao).queryUlocPermissionByUserId(userId);
	}


	@Override
	public List<DictEntry> getDictItem(User param) {
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final User bean : (param == null ? findAll() : findByEg(param))) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName(bean.getName());
			dictList.add(dict);
		}
		return dictList;
	}

	@Override
	public List<Map<String, Object>> getLines() {
		List<Map<String, Object>> linesMap = ((UserDao) dao).getLines();
		List<Map<String, Object>> resMap = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> line:linesMap) {
			Map<String,Object> map = new HashMap<String, Object>();
			String name = "";
			map.put("code", line.get("ID").toString());
			name = line.get("NO").toString();
			if(null != line.get("NAME")) {
				map.put("name", name+"-"+line.get("NAME").toString());
			}
			resMap.add(map);
		}
		return resMap;
	}

}
