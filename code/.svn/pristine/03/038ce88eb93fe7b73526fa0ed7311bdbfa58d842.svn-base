/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.systemadmin.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.security.MD5Utils;
import com.wis.basis.common.vo.TypeVo;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.entity.User;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.Role;
import com.wis.core.framework.model.AuditConfigurationType;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DataPermissionSetService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.FormPermissionSetService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.RoleService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author vincent
 * 
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private FormPermissionSetService formPermissionSetService;
	@Autowired
	private DataPermissionSetService dataPermissionSetService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.addFuzzyQueryFiled("name");
		criteria.addFuzzyQueryFiled("account");
		criteria.addFuzzyQueryFiled("cdsId");
		criteria.setQueryRelationEntity(true);
		if (criteria.getOffset() >= 0) {
			criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		}
		return ActionUtils.handleListResult(response, userService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("lineOptions", JSONArray.fromObject(userService.getLines()).toString());
		modelMap.addAttribute("options", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_STATUS));
		modelMap.addAttribute("deptOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_DEPT));
		modelMap.addAttribute("deptOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_DEPT));
		return new ModelAndView("/core/user/user_list");
	}

	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(HttpServletRequest request, String activeOrgId, ModelMap modelMap) {
		modelMap.addAttribute("options", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_STATUS));
		modelMap.addAttribute("deptOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_DEPT));
		TypeVo statusEntry = new TypeVo(ConstantUtils.TYPE_CODE_USER_STATUS, "options");
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/user/user_select"), statusEntry);
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		addEntry(modelMap);
		modelMap.addAttribute("pwdRegExp", globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_REG_EXP));
		modelMap.addAttribute("lineOptions", userService.getLines());
		return new ModelAndView("/core/user/user_add");
	}

	@RequestMapping(value = "/userAddInput")
	public ModelAndView userAddInput(Integer orgnizationId, ModelMap modelMap) {
		modelMap.put("orgnizationId", orgnizationId);
		addEntry(modelMap);
		modelMap.addAttribute("pwdRegExp", globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_REG_EXP));
		return new ModelAndView("/core/position/user_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, User user, String positionId) {
		userService.doSave(user);
		logService.doAddLog(AuditConfigurationType.AUDIT_TYPE_USER, user);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/lock")
	public JsonActionResult lock(HttpServletResponse response, String ids) {
		userService.doLock(getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/unlock")
	public JsonActionResult unlock(HttpServletResponse response, String ids) {
		userService.doUnlock(getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, Integer id, ModelMap modelMap) {
		addEntry(modelMap);
		modelMap.addAttribute("user", setBean(userService.findById(id)));
		return new ModelAndView("/core/user/user_update");
	}

	private User setBean(User bean) {

		return bean;
	}

	@RequestMapping(value = "/resetPwdInput")
	public ModelAndView resetPwdInput(ModelMap modelMap) {
		modelMap.addAttribute("pwdRegExp", globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_REG_EXP));
		return new ModelAndView("/core/user/user_password");
	}

	@RequestMapping(value = "/manualInput")
	public ModelAndView manualInput() {
		return new ModelAndView("/core/user/user_manual");
	}

	@ResponseBody
	@RequestMapping(value = "/resetPwd")
	public JsonActionResult resetPwd(HttpServletResponse response, String oldPassword, String newPassword) {
		userService.doResetPassword(oldPassword, newPassword);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/resetPwdToDefault")
	public JsonActionResult resetPwdToDefault(HttpServletResponse response, String ids) {
		userService.doResetPasswordToDefault(getIds(ids), (User) WebContextHolder.getCurrentUser());
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, User user) {
		User before = userService.findById(user.getId());
		userService.doUpdate(user);
		logService.doUpdateLog(AuditConfigurationType.AUDIT_TYPE_USER, before, user);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "viewInput")
	public ModelAndView viewInput(HttpServletResponse response, Integer id, ModelMap modelMap) {
		User user = userService.findById(id);
		generateRole(modelMap, user.getId());
		addEntry(modelMap);
		modelMap.addAttribute("user", user);
		return new ModelAndView("/core/user/user_view");
	}

	@RequestMapping(value = "userinfo")
	public ModelAndView userinfo(HttpServletResponse response, ModelMap modelMap) {
		return viewInput(response, WebContextHolder.getCurrentUser().getId(), modelMap);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		List<User> userListTmp = userService.findAllInIds(getIds(ids));
		User user = (User) WebContextHolder.getCurrentUser();
		userService.doRemove(getIds(ids), user);
		logService.doDeleteLog(AuditConfigurationType.AUDIT_TYPE_USER, userListTmp);
		return ActionUtils.handleResult(response);
	}

	private void addEntry(ModelMap modelMap) {
		modelMap.addAttribute("lineOptions", userService.getLines());
		modelMap.addAttribute("sexOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_SEX));
		modelMap.addAttribute("options", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_STATUS));
		modelMap.addAttribute("deptOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_DEPT));
	}

	private void generateRole(ModelMap modelMap, Integer userId) {
		List<Role> roles = roleService.findRoleByUserId(userId);
		String[] roleIds = new String[roles.size()];
		String[] roleNames = new String[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			roleIds[i] = roles.get(i).getId().toString();
			roleNames[i] = roles.get(i).getName();
		}
		modelMap.addAttribute("roleIds", JSONArray.fromObject(Arrays.asList(roleIds)).toString());
		modelMap.addAttribute("roleNames", StringUtils.join(roleNames, ","));
	}

	@RequestMapping(value = "/listOrgInput")
	public ModelAndView listOrgInput(Integer orgnizationId, ModelMap modelMap) {
		modelMap.addAttribute("orgnizationId", orgnizationId);
		addEntry(modelMap);
		return new ModelAndView("/core/orgnization/orgnization_user_list");
	}

	@RequestMapping(value = "/listPositionInput")
	public ModelAndView listPositionInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("userId", id);
		return new ModelAndView("/core/user/position_list");
	}

	@RequestMapping(value = "/permissionSetListSelect")
	public ModelAndView permissionSetListSelect(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("userId", id);
		return new ModelAndView("/core/user/form_permission_set_select");
	}

	@RequestMapping(value = "/permissionSetList")
	public ModelAndView permissionSetList(Integer id, ModelMap modelMap) {
		return new ModelAndView("/core/user/form_permission_set_list");
	}

	@RequestMapping(value = "/allotDataPermissionSet")
	public ModelAndView allotDataPermissionSet(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("userId", id);
		return new ModelAndView("/core/user/data_permission_set");
	}

	@RequestMapping(value = "/addDataPermissionSet")
	public ModelAndView addDataPermissionSet(Integer id, ModelMap modelMap) {
		return new ModelAndView("/core/user/data_permission_set_select");
	}

	@ResponseBody
	@RequestMapping(value = "/deleteUserRelation")
	public JsonActionResult deleteUserRelation(HttpServletResponse response, String ids, Integer userId) {
		formPermissionSetService.doDeleteUserRelation(userId, getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/addUserRelation")
	public JsonActionResult addUserRelation(HttpServletResponse response, String setIds, Integer userId) {
		formPermissionSetService.doAddUserRelation(getIds(setIds), userId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteDataUserRelation")
	public JsonActionResult deleteDataUserRelation(HttpServletResponse response, String ids, Integer userId) {
		dataPermissionSetService.doDeleteUserRelation(userId, getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/addDataUserRelation")
	public JsonActionResult addDataUserRelation(HttpServletResponse response, String setIds, Integer userId) {
		dataPermissionSetService.doAddUserRelation(userId, getIds(setIds));
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/checkCode")
	public void checkCode(HttpServletRequest request, HttpServletResponse response, String code, String param) {
		Integer id = userService.checkAccount(param);
		JSONObject result = new JSONObject();
		if (id != null && !param.equals(code)) {
			result.put("status", "n");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}

	@RequestMapping(value = "/userSelectRole")
	public ModelAndView userSelectRole(HttpServletResponse response, String userIds, ModelMap modelMap) {
		return new ModelAndView("/core/user/role_select");
	}

	@RequestMapping(value = "/setSelectRole")
	public ModelAndView setSelectRole(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("userId", id);
		return new ModelAndView("/core/user/user_select_role");
	}

	@ResponseBody
	@RequestMapping(value = "/deleteUserRole")
	public JsonActionResult deleteUserRole(HttpServletResponse response, String ids, Integer userId) {
		userService.doDeleteUserRole(userId, getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/addUserRole")
	public JsonActionResult addUserRole(HttpServletResponse response, String roleIds, Integer userId) {
		userService.doAddUserRole(userId, getIds(roleIds));
		return ActionUtils.handleResult(response);
	}

	/**
	 *
	 * 修改密码不能与前几次设置的密码重复（次数在《系统配置管理》中设置）
	 */
	@RequestMapping(value = "/checkValidateNewPwd")
	public void checkValidateNewPwd(HttpServletRequest request, HttpServletResponse response, String param) {
		JSONObject result = new JSONObject();
		if (!"true".equalsIgnoreCase(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_DUPLICATION_VERIFY))) {
			result.put("status", "y");
		} else {
			User user = userService.findById(WebContextHolder.getCurrentUser().getId());
			String newPwd = MD5Utils.passwordEncoder(param, user.getAccount());
			String delPvpCount = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_DEL_PVP_COUNT);
			if (user.getPassword().equals(newPwd)) {
				result.put("status", "n");
				result.put("info", getMessage(request, "USER_RESET_PASSWORD_DUPLICATION", delPvpCount));
			} else if (StringUtils.isNotEmpty(user.getHistoryPwd())) {
				String[] split = user.getHistoryPwd().split(",");
				List<String> list = Arrays.asList(split);
				int num = Integer.parseInt(delPvpCount) - 1;
				if (StringUtils.isNotEmpty(delPvpCount)) {
					if (list.size() > num) {
						list = list.subList(list.size() - num, list.size()); // 获取子列表
					}
				}

				if (list.contains(newPwd)) {
					result.put("status", "n");
					result.put("info", getMessage(request, "USER_RESET_PASSWORD_DUPLICATION", num + 1));
				} else {
					result.put("status", "y");
				}
			} else {
				result.put("status", "y");
			}
		}
		ActionUtils.handleResult(response, result);
	}
}
