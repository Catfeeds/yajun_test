package com.wis.basis.systemadmin.controller;

import java.util.Arrays;

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
import com.wis.basis.common.vo.TypeVo;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.vo.FormPermissionVo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.framework.entity.FormPermission;
import com.wis.core.framework.service.FormPermissionService;

@Controller
@RequestMapping(value = "/formPermission")
public class FormPermissionController extends BaseController {
	@Autowired
	private FormPermissionService formPermissionService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "code","module" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, formPermissionService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap) {
		TypeVo permissionModule=new TypeVo(ConstantUtils.TYPE_CODE_FORM_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/form/form_permission_list"), permissionModule);
	}
	@RequestMapping(value="/listSelect")
	public ModelAndView listSelect(HttpServletRequest request,ModelMap modelMap){
		TypeVo permissionModule=new TypeVo(ConstantUtils.TYPE_CODE_FORM_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/permission/form/form_permission_select"),permissionModule);  
		
	}
	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap) {
		TypeVo permissionModule=new TypeVo(ConstantUtils.TYPE_CODE_FORM_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/form/form_permission_add"), permissionModule);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		TypeVo permissionModule=new TypeVo(ConstantUtils.TYPE_CODE_FORM_PERMISSION_MUDULE, "moduleOptions");
		modelMap.addAttribute("formPermission", formPermissionService.findById(id));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/form/form_permission_update"), permissionModule);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, FormPermissionVo formPermissionVo) {
		FormPermission formPermission = new FormPermission();
		copy(formPermissionVo, formPermission);
		formPermissionService.doSave(formPermission);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, FormPermissionVo formPermissionVo) {
		FormPermission formPermission = formPermissionService.findById(formPermissionVo.getId());
		copy(formPermissionVo, formPermission, new String[] { "id" });
		formPermissionService.doUpdate(formPermission);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		formPermissionService.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}
}
