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
import com.wis.basis.systemadmin.vo.FormPermissionSetVo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.framework.entity.FormPermissionSet;
import com.wis.core.framework.service.FormPermissionSetService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/formPermissionSet")
public class FormPermissionSetController extends BaseController {

	@Autowired
	private FormPermissionSetService formPermissionSetService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "code" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, formPermissionSetService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput() {
		return new ModelAndView("/core/permission/form/form_permission_set_list");
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput() {
		return new ModelAndView("/core/permission/form/form_permission_set_add");
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("formPermissionSet", formPermissionSetService.findById(id));
		return new ModelAndView("/core/permission/form/form_permission_set_update");
	}

	@RequestMapping(value = "/grantPermissionInput")
	public ModelAndView grantPermissionInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("permissionSetId", id);
		TypeVo permissionModule=new TypeVo(ConstantUtils.TYPE_CODE_FORM_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/form/form_permission_grant"), permissionModule);
	}
	
	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(HttpServletRequest request,ModelMap modelMap) {
		TypeVo permissionModule=new TypeVo(ConstantUtils.TYPE_CODE_FORM_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/permission/form/form_permission_select"),permissionModule );  
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, FormPermissionSetVo formPermissionSetVo) {
		FormPermissionSet formPermissionSet = new FormPermissionSet();
		copy(formPermissionSetVo, formPermissionSet);
		formPermissionSetService.doSave(formPermissionSet);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, FormPermissionSetVo formPermissionSetVo) {
		FormPermissionSet formPermissionSet = formPermissionSetService.findById(formPermissionSetVo.getId());
		copy(formPermissionSetVo, formPermissionSet, new String[] { "id" });
		formPermissionSetService.doUpdate(formPermissionSet);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		formPermissionSetService.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/deletePermissionRelation")
	public JsonActionResult deletePermissionRelation(HttpServletResponse response, String ids ,Integer setId) {
		formPermissionSetService.doDeletePermissionRelation(setId, getIds(ids));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value = "/savePermissionRelation")
	public JsonActionResult savePermissionRelation(HttpServletResponse response, String permissionIds ,Integer setId) {
		formPermissionSetService.doAddPermissionRelation(setId, getIds(permissionIds));
		return ActionUtils.handleResult(response);
	}
	@RequestMapping(value="/checkCode")
	public void checkCode(HttpServletResponse response,String code,String param){
		Integer id=formPermissionSetService.checkCode(param);
		JSONObject result=new JSONObject();
		if (id !=null&& !param.equals(code)) {
			result.put("status", "n");
			result.put("info","代码已存在！" );
		}else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response,result);
	}
}
