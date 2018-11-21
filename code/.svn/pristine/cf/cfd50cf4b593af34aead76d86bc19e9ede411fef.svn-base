package com.wis.mes.master.rolemaster.service;

import java.util.List;

import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.rolemaster.entity.TsRoleMasterSet;

public interface TsRoleMasterSetService extends MasterBaseService<TsRoleMasterSet> {
	List<Integer> getPlantIds(Integer roleId);
	List<Integer> getWorkshopIds( Integer roleId);
	List<Integer> getLineIds(Integer roleId);
	List<Integer> getUlocIds(Integer roleId);
	void doSaveRoleMenu(Integer[] plantId,Integer[] workshopId, Integer[] lineId, Integer[] ulocId,Integer roleId);
	void deletePlantRelationById(Integer userId, Integer roleId, Integer[] plantIds);
	String queryRoleIdsByUserId(Integer userId);
	String queryPlantIdsByUserIdAndRoleIds(Integer userId, Integer[] roleIds);
}
