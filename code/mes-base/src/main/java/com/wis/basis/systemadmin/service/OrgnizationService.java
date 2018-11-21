package com.wis.basis.systemadmin.service;

import java.util.List;

import com.wis.basis.systemadmin.entity.Orgnization;
import com.wis.basis.systemadmin.model.OrgnizationTree;
import com.wis.basis.systemadmin.vo.OrgnizationVo;
import com.wis.core.service.BaseService;

/**
 * @author vincent
 *
 */
public interface OrgnizationService extends BaseService<Orgnization> {

	public Integer checkCode(String code);

	public List<OrgnizationTree> getTree(String dimension);

	public Orgnization getParentDept(Integer orgId);

	/**
	 * 根据发起人帐号，角色CODE,组织类型，获得发起人部门组织+角色下的所有账户，如部长/副总/部门领导
	 * 
	 * @param init
	 *            发起人帐号
	 * @param roleCode
	 *            角色CODE
	 * @param orgType
	 *            组织类型
	 * @return
	 */
	public List<String> findAccountByRole(String init, String roleCode, String orgType);

	public String findOneByRole(String init, String roleCode, String orgType);

	/**
	 * 根据组织ID，角色CODE,组织类型，获得组织+角色下的所有账户，如部长/副总/部门领导
	 * 
	 * @param orgId
	 *            组织ID
	 * @param roleCode
	 *            角色CODE/如：部门领导CODE,副总CODE
	 * @param orgType
	 *            组织类型/如：部门领导是组织类型为部门,副总是组织类型为公司
	 * @return
	 */
	public List<String> findAccountRoleByOrgId(Integer orgId, String roleCode, String orgType);

	public String findOneRoleByOrgId(Integer orgId, String roleCode, String orgType);

	/**
	 * 根据组织CODE，角色CODE,组织类型，获得组织+角色下的所有账户，如部长/副总/部门领导
	 * 
	 * @param orgCode
	 *            组织CODE
	 * @param roleCode
	 *            角色CODE/如：部门领导CODE,副总CODE
	 * @param orgType
	 *            组织类型/如：部门领导是组织类型为部门,副总是组织类型为公司
	 * @return
	 */
	public List<String> findAccountRoleByOrgCode(String orgCode, String roleCode, String orgType);

	public String findOneRoleByOrgCode(String orgCode, String roleCode, String orgType);

	/**
	 * 添加用户到指定组织中
	 * 
	 * @param orgnizationId
	 *            组织ID
	 * @param ids
	 *            用户ID数组
	 */
	public void doAddUser(Integer orgnizationId, Integer[] ids);

	public void doRemoveUser(Integer orgnizationId, Integer[] ids);

	public void doAdd(OrgnizationVo orgnizationVo);

	/**
	 * 获取用户所属组织
	 * 
	 * @param userId
	 *            用户Id
	 * @return
	 */
	public List<Orgnization> findUserOrgnizations(Integer userId);
}
