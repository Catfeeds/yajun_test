package com.wis.mes.master.rolemaster.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.mes.master.base.dao.impl.MasterBaseDaoImpl;
import com.wis.mes.master.rolemaster.dao.TsRoleMasterSetDao;
import com.wis.mes.master.rolemaster.entity.TsRoleMasterSet;

@Repository
public class TsRoleMasterSetDaoImpl extends MasterBaseDaoImpl<TsRoleMasterSet> implements TsRoleMasterSetDao {
    @Override
    public TsRoleMasterSet queryRoleMasterSet(final Integer roleId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        return getSqlSession().selectOne("RoleMasterSetMapper.queryRoleMasterSet", params);
    }

    @Override
    public void deletePlantRelationById(final Integer userId, final Integer roleId, final Integer[] plantIds) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("roleId", roleId);
        params.put("plantIds", plantIds);
        getSqlSession().delete("RoleMasterSetMapper.deletePlantRelationById", params);
    }

    @Override
    public String queryRoleIdsByUserId(final Integer userId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        return getSqlSession().selectOne("RoleMasterSetMapper.queryRoleIdsByUserId", params);
    }

    @Override
    public String queryPlantIdsByUserIdAndRoleIds(final Integer userId, final Integer[] roleIds) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("roleIds", roleIds);
        return getSqlSession().selectOne("RoleMasterSetMapper.queryPlantIdsByUserIdAndRoleIds", params);
    }

    @Override
    public String findUserIdsByRoleIds(final Integer roleId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        return getSqlSession().selectOne("RoleMasterSetMapper.queryUserIdByRoleIds", params);
    }

    @Override
    public void deleteByRoleId(final Integer roleId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        getSqlSession().delete("RoleMasterSetMapper.deleteByRoleId", params);

    }

    @Override
    public int getCountByCurrUser(final Integer currUser) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("currUser", currUser);
        return getSqlSession().selectOne("RoleMasterSetMapper.getCountByCurrUser", param);
    }

}
