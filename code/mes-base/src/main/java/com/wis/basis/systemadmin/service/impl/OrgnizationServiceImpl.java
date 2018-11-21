package com.wis.basis.systemadmin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.systemadmin.dao.OrgnizationDao;
import com.wis.basis.systemadmin.entity.Orgnization;
import com.wis.basis.systemadmin.entity.Position;
import com.wis.basis.systemadmin.model.OrgnizationTree;
import com.wis.basis.systemadmin.service.OrgnizationService;
import com.wis.basis.systemadmin.service.PositionService;
import com.wis.basis.systemadmin.vo.OrgnizationVo;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.core.utils.SerialNumUtil;

@Service("orgnizationService")
public class OrgnizationServiceImpl extends BaseServiceImpl<Orgnization> implements OrgnizationService {

	@Autowired
	private PositionService positionService;

	@Autowired
	public void setDao(OrgnizationDao dao) {
		this.dao = dao;
	}

	@Override
	public void doAdd(OrgnizationVo orgnizationVo) {
		Orgnization orgnization = new Orgnization();
		BeanUtils.copyProperties(orgnizationVo, orgnization);
		String key = "ORG_SYS_CODE_" + orgnizationVo.getParentSystemCode();
		int serNo = SerialNumUtil.getInstance().nextInt(key);
		orgnization.setSystemCode(String.format(orgnizationVo.getParentSystemCode() + "_" + "%03d", serNo));
		doSave(orgnization);
	}

	@Override
	public Orgnization doUpdate(Orgnization orgnization) {
		Orgnization savedoOrgnization = dao.get(orgnization.getId());
		BeanUtils.copyProperties(orgnization, savedoOrgnization);
		return dao.save(savedoOrgnization);
	}

	@Override
	public Integer checkCode(String code) {
		Orgnization orgnization = new Orgnization();
		orgnization.setCode(code);
		List<Orgnization> rList = dao.findByEg(orgnization);
		if (rList.size() > 0) {
			return rList.get(0).getId();
		}
		return null;
	}

	private String getIconCls(Orgnization org) {
		if (!StringUtils.isEmpty(org.getType())) {
			if (org.getType().equals(OrgnizationTree.TREE_TYPE_COMPANY)) {
				return "icon-org-company";
			} else if (org.getType().equals(OrgnizationTree.TREE_TYPE_PART)) {
				return "icon-org-part";
			} else if (org.getType().equals(OrgnizationTree.TREE_TYPE_SECTION)) {
				return "icon-org-section";
			}
		}
		return null;
	}

	@Override
	public List<OrgnizationTree> getTree(String dimension) {
		List<Orgnization> parents = getParent(dimension);
		List<OrgnizationTree> result = new ArrayList<OrgnizationTree>();
		for (Orgnization parent : parents) {
			OrgnizationTree tree = new OrgnizationTree();
			tree.setId(parent.getId());
			tree.setCode(parent.getCode());
			tree.setText(parent.getName());
			tree.setNodes(getSon(parent));
			List<String> tags = new ArrayList<String>();
			tags.add("available");
			tree.setTags(tags);
			result.add(tree);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<Orgnization> getParent(String dimension) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dimension", dimension);
		return (List<Orgnization>) dao.query("OrgnizationMapper.findParent", param);
	}

	private List<OrgnizationTree> getSon(Orgnization orgnization) {
		List<Orgnization> sons = getSonBy(orgnization.getId());
		List<OrgnizationTree> result = new ArrayList<OrgnizationTree>();
		if (sons.size() > 0) {
			for (Orgnization son : sons) {
				OrgnizationTree sonTree = new OrgnizationTree();
				sonTree.setId(son.getId());
				sonTree.setCode(son.getCode());
				sonTree.setText(son.getName());
				sonTree.setNodes(getSon(son));
				List<String> tags = new ArrayList<String>();
				tags.add("available");
				sonTree.setTags(tags);
				result.add(sonTree);
			}
		}
		return result;
	}

	private List<Orgnization> getSonBy(Integer parentId) {
		Orgnization orgnization = new Orgnization();
		orgnization.setParentId(parentId);
		return dao.findByEg(orgnization);
	}

	@Override
	public void doDeleteById(Integer[] ids) {
		for (Integer id : ids) {
			super.doDeleteById(id);
			List<Orgnization> list = getSonBy(id);
			for (Orgnization orgnization : list) {
				super.doDeleteById(orgnization.getId());
			}
		}
	}

	@Override
	public Orgnization getParentDept(Integer orgId) {
		Orgnization orgnization = dao.get(orgId);
		if (orgnization != null && orgnization.getType().equals("PART"))
			return orgnization;
		Orgnization org = dao.get(orgnization.getParentId());
		if (org != null && !org.getType().equals("PART")) {
			getParentDept(org.getId());
		} else if (org != null && org.getType().equals("PART")) {
			return org;
		}
		return null;
	}

	@Override
	public List<String> findAccountByRole(String init, String roleCode, String orgType) {
		return getAccounts(init, roleCode, orgType);
	}

	private List<String> getAccounts(String init, String roleCode, String orgType) {
		List<Integer> orgIds = new ArrayList<Integer>();
		List<Orgnization> list = getOrg(init);
		List<String> positionCodes = new ArrayList<String>();
		for (Orgnization orgnization : list) {
			Orgnization parent = getParentDept(orgnization.getId(), orgType);
			if (parent != null) {
				orgIds.add(parent.getId());
			}
			// 解决多个副总但不是管理相同部门的问题：如果是公司根据发起人找部门CODE,用部门CODE去匹配公司下岗位CODE的用户
			if (orgType.equals(ConstantUtils.ENTRY_CODE_COMPANY)) {
				Orgnization parentPart = getParentDept(orgnization.getId(), ConstantUtils.ENTRY_CODE_PART);
				positionCodes.add(ConstantUtils.ENTRY_CODE_DAE + "_" + parentPart.getCode());
			}
		}
		return getAccount(orgIds, roleCode, positionCodes);
	}

	private List<String> getAccounts(Integer orgId, String roleCode, String orgType) {
		List<Integer> orgIds = new ArrayList<Integer>();
		List<String> positionCodes = new ArrayList<String>();
		Orgnization parent = getParentDept(orgId, orgType);
		if (parent != null) {
			orgIds.add(parent.getId());
		}
		// 解决多个副总但不是管理相同部门的问题：如果是公司根据发起人找部门CODE,用部门CODE去匹配公司下岗位CODE的用户
		if (orgType.equals(ConstantUtils.ENTRY_CODE_COMPANY)) {
			Orgnization parentPart = getParentDept(orgId, ConstantUtils.ENTRY_CODE_PART);
			positionCodes.add(ConstantUtils.ENTRY_CODE_DAE + "_" + parentPart.getCode());
		}
		return getAccount(orgIds, roleCode, positionCodes);
	}

	@SuppressWarnings("unchecked")
	private List<String> getAccount(List<Integer> orgIds, String roleCode, List<String> positionCodes) {
		if (orgIds == null || orgIds.size() < 1 || StringUtils.isEmpty(roleCode)) {
			return null;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleCode", roleCode);
		param.put("orgIds", orgIds.toArray(new Integer[orgIds.size()]));
		if (positionCodes != null && positionCodes.size() > 0) {
			param.put("positionCodes", positionCodes.toArray(new String[positionCodes.size()]));
		}
		List<Map<Object, Object>> result = (List<Map<Object, Object>>) dao.queryByCustom("OrgnizationMapper.getAccountByRole", param);
		List<String> list = new ArrayList<String>();
		for (Map<Object, Object> map : result) {
			Object account = map.get("ACCOUNT");
			if (!StringUtils.isEmpty(account)) {
				list.add(account.toString());
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Orgnization> getOrg(String account) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("account", account);
		return (List<Orgnization>) dao.query("OrgnizationMapper.findOrgByAccount", param);
	}

	private Orgnization getParentDept(Integer orgId, String orgType) {
		Orgnization orgnization = dao.get(orgId);
		if (orgnization != null && orgnization.getType().equals(orgType))
			return orgnization;
		if (orgnization.getParentId() == null) {
			return null;
		}
		Orgnization org = dao.get(orgnization.getParentId());
		if (org != null && !org.getType().equals(orgType)) {
			return getParentDept(org.getId(), orgType);
		} else if (org != null && org.getType().equals(orgType)) {
			return org;
		}
		return null;
	}

	@Override
	public String findOneByRole(String init, String roleCode, String orgType) {
		List<String> accounts = getAccounts(init, roleCode, orgType);
		if (accounts != null && accounts.size() == 1) {
			return accounts.get(0);
		}
		return null;
	}

	@Override
	public List<String> findAccountRoleByOrgId(Integer orgId, String roleCode, String orgType) {
		return getAccounts(orgId, roleCode, orgType);
	}

	@Override
	public String findOneRoleByOrgId(Integer orgId, String roleCode, String orgType) {
		List<String> accounts = getAccounts(orgId, roleCode, orgType);
		if (accounts != null && accounts.size() == 1) {
			return accounts.get(0);
		}
		return null;
	}

	@Override
	public List<String> findAccountRoleByOrgCode(String orgCode, String roleCode, String orgType) {
		Orgnization org = new Orgnization();
		org.setCode(orgCode);
		org = findUniqueByEg(org);
		return getAccounts(org.getId(), roleCode, orgType);
	}

	@Override
	public String findOneRoleByOrgCode(String orgCode, String roleCode, String orgType) {
		Orgnization org = new Orgnization();
		org.setCode(orgCode);
		org = findUniqueByEg(org);
		List<String> accounts = getAccounts(org.getId(), roleCode, orgType);
		if (accounts != null && accounts.size() == 1) {
			return accounts.get(0);
		}
		return null;
	}

	@Override
	public void doAddUser(Integer orgnizationId, Integer[] ids) {
		for (Integer id : ids) {
			Map<String, Object> mapping = new HashMap<String, Object>();
			mapping.put("userId", id);
			mapping.put("orgnizationId", orgnizationId);
			((OrgnizationDao) dao).doDeleteUserOrgById(mapping);
			((OrgnizationDao) dao).doAddUser(mapping);
		}
	}

	@Override
	public void doRemoveUser(Integer orgnizationId, Integer[] ids) {
		for (Integer id : ids) {
			Map<String, Object> mapping = new HashMap<String, Object>();
			mapping.put("userId", id);
			mapping.put("orgnizationId", orgnizationId);
			((OrgnizationDao) dao).doDeleteUserOrgById(mapping);
		}

		Position eg = new Position();
		eg.setOrgnizationId(orgnizationId);
		List<Position> positions = positionService.findByEg(eg);
		for (Position position : positions) {
			positionService.doDeleteUser(position.getId(), ids);
		}
	}
	
	@Override
	public List<Orgnization> findUserOrgnizations(Integer userId) {
		return ((OrgnizationDao)dao).findByUserId(userId);
	}
}
