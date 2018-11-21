package com.wis.basis.systemadmin.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.systemadmin.entity.Position;
import com.wis.core.dao.BaseDao;

public interface PositionDao extends BaseDao<Position> {

	public List<Position> findBy(Integer userId);

	public void deleteUserPositionById(Map<String, Object> params);

	public void deletePositionById(Integer userId, Integer[] positionIds);

	public void deleteUserById(Integer positionId, Integer[] userIds);

	public void addUser(Map<String, Object> empAndPosition);

	public List<Integer> findUpByUserAccount(String account);
	
	public List<Integer> findUserBy(Integer [] positionIds);
	
	public List<Position> findByUserAccount(String account);
	
	public List<String> findUserAccountByPositionCode(String code);
	/**
	 * 根据部门CODE查找所有岗位ID
	 * @param orgCodes
	 * @return
	 */
	public List<Integer> findPositionIdsByOrgCode(String[] orgCodes);
}
