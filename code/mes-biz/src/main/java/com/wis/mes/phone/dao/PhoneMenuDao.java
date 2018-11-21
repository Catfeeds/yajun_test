package com.wis.mes.phone.dao;

import com.wis.core.dao.BaseDao;
import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.entity.Permission;

import java.util.List;

public interface PhoneMenuDao extends BaseDao<Menu> {

    public List<Menu> getMenuByParent(Integer userId, Integer parentId);

    public List<Menu> getMenuByRoleId(Integer roleId);

    public List<Menu> getAllTopMenu();

    public List<Menu> getChildMenu(Integer id);

    public List<Permission> findPermissionBy(Integer userId);
}
