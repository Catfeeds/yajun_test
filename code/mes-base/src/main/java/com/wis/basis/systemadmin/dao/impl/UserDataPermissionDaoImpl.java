package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.dao.UserDataPermissionDao;
import com.wis.core.framework.entity.UserDataPermission;

@Repository
public class UserDataPermissionDaoImpl extends BaseDaoImpl<UserDataPermission> implements UserDataPermissionDao{
	
	@Override
	public List<UserDataPermission> findUserDataPermissionBy(Integer userId) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("userId", userId);
		return changeMapsToBeans(getSqlSession().selectList("DataPermissionMapper.selectAllByUser", params));
	}
}
