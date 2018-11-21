/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.systemadmin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.framework.entity.DataPermissionNew;
import com.wis.core.framework.entity.FormPermissionNew;
import com.wis.core.framework.entity.Permission;
import com.wis.core.framework.entity.Role;
import com.wis.core.framework.entity.RoleDataPermissionNew;
import com.wis.core.framework.model.AuditConfigurationType;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DataPermissionNewService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.FormPermissionNewService;
import com.wis.core.framework.service.MenuService;
import com.wis.core.framework.service.PermissionService;
import com.wis.core.framework.service.RoleDataPermissionNewService;
import com.wis.core.framework.service.RoleService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author vincent
 *
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private FormPermissionNewService formPermissionService;
	@Autowired
	private DataPermissionNewService dataPermissionService;
	@Autowired
	private RoleDataPermissionNewService roleDataPermissionService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public String listInput() {
		return "/core/role/role_list";
	}

	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(ModelMap modelMap, HttpServletRequest request) {
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/role/role_select"));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap) {
		addEntry(modelMap);
		return new ModelAndView("/core/role/role_add");
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		addEntry(modelMap);
		modelMap.addAttribute("role", roleService.findById(id));
		return new ModelAndView("/core/role/role_update");
	}

	@RequestMapping(value = "/viewInput")
	public ModelAndView viewInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("role", roleService.findById(id));
		return new ModelAndView("/core/role/role_view");
	}

	@RequestMapping(value = "/roleSetInput")
	public ModelAndView setRoleInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("id", id);
		return new ModelAndView("/core/role/role_set");
	}

	private List<Integer> getRoleFunctionPermissionIds(Integer roleId, Integer menuId) {
		List<Integer> result = new ArrayList<Integer>();
		List<Permission> permissions = permissionService.getPermissionBy(roleId, menuId);
		for (Permission permission : permissions) {
			result.add(permission.getId());
		}
		return result;
	}

	@RequestMapping(value = "/functionPermissionList")
	public ModelAndView functionPermissionList(Integer menuId, Integer roleId, ModelMap modelMap) {
		List<Integer> roleHasPermissions = getRoleFunctionPermissionIds(roleId, menuId);
		List<Permission> permissions = permissionService.getPermissionByMenu(new Integer[] { menuId });
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (Permission permission : permissions) {
			if (!permission.getIsMenuPermission()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", permission.getId());
				map.put("code", permission.getCode());
				map.put("name", permission.getName());
				map.put("checked", roleHasPermissions.contains(permission.getId()));
				result.add(map);
			}
		}

		modelMap.addAttribute("roleId", roleId);
		modelMap.addAttribute("menuId", menuId);
		modelMap.addAttribute("permissionList", result);
		return new ModelAndView("/core/role/role_function_permission");
	}

	@RequestMapping(value = "/formPermissionList")
	public ModelAndView formPermissionList(Integer menuId, Integer roleId, ModelMap modelMap) {
		List<Integer> roleHasPermissions = getRoleFormPermissionIds(roleId, menuId);
		List<FormPermissionNew> permissions = formPermissionService.getPermissionByMenu(new Integer[] { menuId });
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (FormPermissionNew permission : permissions) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", permission.getId());
			map.put("code", permission.getPermissionCode());
			map.put("name", permission.getPermissionName());
			map.put("checked", roleHasPermissions.contains(permission.getId()));
			result.add(map);
		}

		modelMap.addAttribute("roleId", roleId);
		modelMap.addAttribute("menuId", menuId);
		modelMap.addAttribute("permissionList", result);
		return new ModelAndView("/core/role/role_form_permission");
	}

	private List<Integer> getRoleFormPermissionIds(Integer roleId, Integer menuId) {
		List<Integer> result = new ArrayList<Integer>();
		List<FormPermissionNew> permissions = formPermissionService.getPermissionBy(roleId, menuId);
		for (FormPermissionNew permission : permissions) {
			result.add(permission.getId());
		}
		return result;
	}

	@RequestMapping(value = "/dataPermissionList")
	public ModelAndView dataPermissionList(Integer menuId, Integer roleId, ModelMap modelMap) {
		DataPermissionNew permission = dataPermissionService.getPermissionByMenu(menuId);
		if (null != permission) {
			RoleDataPermissionNew roleDataPermission = roleDataPermissionService.getPermissionBy(roleId, permission.getId());
			modelMap.addAttribute("hasDataPermission", "true");
			modelMap.addAttribute("roleId", roleId);
			modelMap.addAttribute("dataPermissionId", permission.getId());
			modelMap.addAttribute("permissionType", (roleDataPermission == null) ? "" : roleDataPermission.getPermissionType());
			modelMap.addAttribute("script", (roleDataPermission == null) ? "" : roleDataPermission.getScript());
			modelMap.addAttribute("dataPermissionTypeOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_DATA_PERMISSION_TYPE));
		} else {
			modelMap.addAttribute("hasDataPermission", "false");
		}
		return new ModelAndView("/core/role/role_data_permission");
	}

	@RequestMapping(value = "/menuList")
	public void menuList(Integer id, HttpServletResponse response) {
		JSONArray arrays = JSONArray.fromObject(menuService.findMenuAndPermissionBy(id));
		ActionUtils.handleResult(response, arrays);
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "code", "name", "desc" }));
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		return ActionUtils.handleListResult(response, roleService.findBy(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/rowAdd")
	public JsonActionResult add(HttpServletResponse response, List<Role> records) {
		roleService.doSaveOrUpdate(records);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, Role role) {
		roleService.doSave(role);
		logService.doAddLog(AuditConfigurationType.AUDIT_TYPE_ROLE, role);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/saveDataPermission")
	public JsonActionResult saveDataPermission(HttpServletResponse response, RoleDataPermissionNew dataPermission) {
		roleService.doSaveDataPermissions(dataPermission);
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/saveFormPermission")
	public JsonActionResult saveFormPermission(HttpServletResponse response, String ids, Integer roleId, Integer menuId) {
		roleService.doSaveFormPermissions(getIds(ids), roleId, menuId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/saveFunctionPermission")
	public JsonActionResult saveFunctionPermission(HttpServletResponse response, String ids, Integer roleId, Integer menuId) {
		roleService.doSaveFunctionPermissions(getIds(ids), roleId, menuId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/saveMenu")
	public JsonActionResult saveMenu(HttpServletResponse response, String menuIds, Integer roleId) {
		roleService.doSaveMenu(getIds(menuIds), roleId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		List<Role> rolesTmp = roleService.findAllInIds(getIds(ids));
		roleService.doRemove(getIds(ids));
		logService.doDeleteLog(AuditConfigurationType.AUDIT_TYPE_ROLE, rolesTmp);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, Role role) {
		Role before = roleService.findById(role.getId());
		roleService.doUpdate(role);
		logService.doUpdateLog(AuditConfigurationType.AUDIT_TYPE_ROLE, before, role);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/checkRole")
	public void checkRole(HttpServletResponse response, String code, String param) {
		Integer id = roleService.checkCode(param);
		JSONObject result = new JSONObject();
		if (id != null && !param.equals(code)) {
			result.put("status", "n");
			result.put("info", "角色代码已存在！");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}

	private void addEntry(ModelMap modelMap) {
		modelMap.addAttribute("systemDataOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
	}
	
	@RequestMapping(value = "/roleMasterSetInput")
	public ModelAndView setRoleMasterInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("id", id);
		return new ModelAndView("/core/role/role_master_set");
	}

}
