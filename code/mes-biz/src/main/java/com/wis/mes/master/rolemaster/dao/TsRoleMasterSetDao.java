package com.wis.mes.master.rolemaster.dao;

import com.wis.mes.master.base.dao.MasterBaseDao;
import com.wis.mes.master.rolemaster.entity.TsRoleMasterSet;

public interface TsRoleMasterSetDao extends MasterBaseDao<TsRoleMasterSet> {
    TsRoleMasterSet queryRoleMasterSet(Integer roleId);

    void deletePlantRelationById(Integer userId, Integer roleId, Integer[] plantIds);

    String queryRoleIdsByUserId(Integer userId);

    String queryPlantIdsByUserIdAndRoleIds(Integer userId, Integer[] roleIds);

    String findUserIdsByRoleIds(Integer roleId);

    void deleteByRoleId(Integer roleId);

    /**
     * 根据 当前登录用户 查询 数据权限条数
     * 
     * @param currUser
     *            当前登录用户
     * 
     * @return 数据权限条数
     */
    int getCountByCurrUser(Integer currUser);
}
