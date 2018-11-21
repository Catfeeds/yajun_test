package com.wis.mes.phone.service;

import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.entity.Permission;
import com.wis.core.framework.model.ComboTree;
import com.wis.core.service.BaseService;

import java.util.List;

public interface PhoneMenuService extends BaseService<Menu>{

    public List<Menu> findPhoneMenuBy(Integer userId);

    public List<Permission> findPermissionBy(Integer userId);

    public List<ComboTree> findMenuAndPermissionBy(Integer roleId);
}
