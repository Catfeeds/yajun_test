package com.wis.basis.systemadmin.dao;

import com.wis.core.dao.BaseDao;
import com.wis.core.framework.entity.Menu;

public interface MenuSetDao extends BaseDao<Menu> {
	public void addMenu(Menu menu);
}
