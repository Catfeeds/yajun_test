package com.wis.mes.phone.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.entity.Permission;
import com.wis.mes.phone.dao.PhoneMenuDao;

@Repository
public class PhoneMenuDaoImpl extends BaseDaoImpl<Menu> implements PhoneMenuDao {

    @Override
    public List<Menu> getMenuByParent(Integer userId, Integer parentId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("parentId", parentId);
        return this.getSqlSession().selectList("PhoneMenuMapper.getMenuByParent", param);
    }

    @Override
    public List<Menu> getMenuByRoleId(Integer roleId) {
        return getSqlSession().selectList("PhoneMenuMapper.getMenuByRoleId", roleId);
    }

    @Override
    public List<Menu> getAllTopMenu() {
        return getSqlSession().selectList("PhoneMenuMapper.getAllTopMenu");
    }

    @Override
    public List<Menu> getChildMenu(Integer id) {
        return getSqlSession().selectList("PhoneMenuMapper.getChildMenu",id);
    }

    @Override
    public List<Permission> findPermissionBy(Integer userId) {
        return getSqlSession().selectList("PhoneMenuMapper.getPermissionByUser", userId);
    }
}
