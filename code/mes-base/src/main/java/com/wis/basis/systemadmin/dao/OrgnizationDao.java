package com.wis.basis.systemadmin.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.systemadmin.entity.Orgnization;
import com.wis.core.dao.BaseDao;

public interface OrgnizationDao extends BaseDao<Orgnization> {

	void doAddUser(Map<String, Object> userOrgMapping);

	void doDeleteUserOrgById(Map<String, Object> mapping);

	List<Orgnization> findByUserId(Integer userId);

}
