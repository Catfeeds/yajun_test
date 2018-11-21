package com.wis.basis.systemadmin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.systemadmin.dao.PositionDao;
import com.wis.basis.systemadmin.entity.Position;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;

@Repository
public class PositionDaoImpl extends BaseDaoImpl<Position> implements PositionDao {

	@Override
	public List<Position> findBy(Integer userId) {
		return changeMapsToBeans(getSqlSession().selectList("OrgnizationMapper.findPostionByUerId", userId));
	}

	@Override
	public void deleteUserPositionById(Map<String, Object> params){
		getSqlSession().delete("PositionMapper.deleteUserPositionById", params);
	}

	@Override
	public void addUser(Map<String, Object> empAndPosition) {
		getSqlSession().insert("PositionMapper.addUser", empAndPosition);
	}

	@Override
	public void deletePositionById(Integer userId, Integer[] positionIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("positionIds", positionIds);
		getSqlSession().delete("PositionMapper.deletePositionById", params);
	}

	@Override
	public void deleteUserById(Integer positionId, Integer[] userIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("positionId", positionId);
		params.put("userIds", userIds);
		getSqlSession().delete("PositionMapper.deleteUserById", params);
	}
	
	@Override
	protected String getConditionHql(Map<String, Object> parameters, QueryCriteria queryCriteria) {
		StringBuffer result = new StringBuffer();
		Map<String, Object> thisParameters = new HashMap<String, Object>(parameters);
		if (parameters.containsKey("userId")) {
			result.append(" and position"+SUFFIX+".ID in (select r.POSITION_ID from TR_USER_POSITION  r where r.USER_ID in(select t.ID from TS_USER t where t.ID ="+parameters.get("userId")+"))");
			thisParameters.remove("userId");
		}
		result.append(super.getConditionHql(thisParameters, queryCriteria));
		return result.toString();
	}

	@Override
	public List<Integer> findUpByUserAccount(String account) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		return getSqlSession().selectList("PositionMapper.getUPByAccount", params);
	}

	@Override
	public List<Position> findByUserAccount(String account) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		return getSqlSession().selectList("PositionMapper.getPositionByAccount", params);
	}

	@Override
	public List<Integer> findUserBy(Integer[] positionIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("positionIds", positionIds);
		return getSqlSession().selectList("PositionMapper.getUserByPosition", params);
	}

	@Override
	public List<String> findUserAccountByPositionCode(String code) {
		
		return getSqlSession().selectList("PositionMapper.getUserAccountByPositionCode", code);
	}

	@Override
	public List<Integer> findPositionIdsByOrgCode(String[] orgCodes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCodes", orgCodes);
		return getSqlSession().selectList("PositionMapper.getPositionIdsByOrgCode", params);
	}
	
	
}
