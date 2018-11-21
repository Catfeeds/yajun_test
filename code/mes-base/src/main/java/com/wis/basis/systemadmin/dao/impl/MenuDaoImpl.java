package com.wis.basis.systemadmin.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wis.basis.systemadmin.dao.MenuSetDao;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.framework.dao.MenuDao;
import com.wis.core.framework.entity.Menu;

@Repository
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDao,MenuSetDao {

    @Override
    public List<Menu> getMenuByUserId(Integer empId) {
        return this.getSqlSession().selectList("MenuMapper.getMenuByUserId", empId);
    }

    @Override
    public Menu getMenuById(Integer id) {
        return this.getSqlSession().selectOne("MenuMapper.getMenuById", id);
    }

	@Override
	public List<Menu> getMenuByRoleId(Integer roleId) {
		return getSqlSession().selectList("MenuMapper.getMenuByRoleId", roleId);
	}

	@Override
	public List<Menu> getAllTopMenu() {
		return getSqlSession().selectList("MenuMapper.getAllTopMenu");
	}

	@Override
	public List<Menu> getChildMenu(Integer id) {
		return getSqlSession().selectList("MenuMapper.getChildMenu",id);
	}
	@Override
	public void addMenu(Menu menu) {
		getSqlSession().insert("MenuMapper.addMenu", menu);

	}

	@Override
	public List<Menu> getAllMenus() {
		return getSqlSession().selectList("MenuMapper.getAllMenus");
	}

}
