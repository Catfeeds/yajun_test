package com.wis.basis.systemadmin.service;

import java.util.List;

import com.wis.basis.systemadmin.entity.Position;
import com.wis.core.service.BaseService;

public interface PositionService extends BaseService<Position> {

	public List<Position> findBy(Integer userId);

	/**
	 * 根据用户ID删除岗位
	 * @param userId
	 * @param positionIds
	 */
	public void doDeletePosition(Integer userId, Integer... positionIds);
	
	/**
	 * 根据岗位ID删除用户
	 * @param positionId
	 * @param userIds
	 */
	public void doDeleteUser(Integer positionId, Integer... userIds);
	
	/**
	 * 根据岗位ID删除相关联的用户(包括用户组织表)
	 * @param positionId
	 * @param userIds
	 */
	public void doDeleteRelationUser(Integer positionId, Integer... userIds);
	
	/**
	 * 新增用户1，岗位*关系
	 * @param userId
	 * @param positionIds
	 */
	public void doUpdateUserPosition(Integer userId, Integer... positionIds);
	
	/**
	 * 新增岗位1，用户*关系
	 * @param positionId
	 * @param userIds
	 */
	public void doUpdatePositionUser(Integer positionId, Integer... userIds);

	public List<Position> findByAccount(String account);
	
	/**
	 * 根据用户账户查找其上所有的上级岗位ID
	 * @param account 用户账号
	 * @return 岗位ID集
	 */
	public List<String> findUpPositionByAccount(String account);
	
	
	/**
	 * 根据岗位ID查找用户ID
	 */
	public List<Integer> findUserBy(Integer ... positionIds);
	
	public Integer checkCode(String code);
	
	public List<String> findUserAccountsByPositionCode(String code);
	
	public String findUserAccountByPositionCode(String code);
	/**
	 * 根据用户查找上级领导账户
	 * @param account
	 * @return
	 */
	public String findUpByAccount(String account);
	/**
	 * 根据用户查找多个上级领导账户
	 * @param account
	 * @return
	 */
	public List<String> findUpListByAccount(String account);
	
	/**
	 * 根据部门CODE查找所有岗位ID
	 * @param orgCode
	 * @return
	 */
	public List<Integer> findPositionIdsByOrgCode(String orgCode);
	/**
	 * 根据部门CODE查找所有岗位ID
	 * @param orgCode
	 * @return
	 */
	public List<Integer> findPositionIdsByOrgCodes(String[] orgCodes);
	
	/**
	 * 根据部门CODE查找所有岗位ID
	 * @param orgCode 单个组织code
	 * @return
	 */
	public List<String> findPositiIdsByOrgCode(String orgCode);
}
