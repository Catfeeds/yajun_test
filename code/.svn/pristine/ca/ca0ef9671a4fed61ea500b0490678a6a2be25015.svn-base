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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.vo.DataPermissionSetVo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.framework.entity.DataPermissionSet;
import com.wis.core.framework.service.DataPermissionSetService;

@Controller
@RequestMapping(value = "/dataPermissionSet")
public class DataPermissionSetController extends BaseController {
	@Autowired
	private DataPermissionSetService dataPermissionSetService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "code","description" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, dataPermissionSetService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput() {
		return new ModelAndView("/core/permission/data/data_permission_set_list");
	}
	@RequestMapping(value="/listSelect")
	public ModelAndView listSelect(HttpServletRequest request,ModelMap modelMap){
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/permission/data/data_permission_set_select") );  
	}
	@RequestMapping(value = "/addInput")
	public ModelAndView addInput() {
		return new ModelAndView("/core/permission/data/data_permission_set_add");
	}
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("bean", dataPermissionSetService.findById(id));
		return new ModelAndView("/core/permission/data/data_permission_set_update");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, DataPermissionSetVo dataPermissionSet) {
		dataPermissionSetService.doSave((DataPermissionSet) copy(dataPermissionSet, new DataPermissionSet()));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, DataPermissionSetVo dataPermissionSet) {
		DataPermissionSet dest = (DataPermissionSet) copy(dataPermissionSet, dataPermissionSetService.findById(dataPermissionSet.getId()));
		dataPermissionSetService.doUpdate(dest);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		dataPermissionSetService.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}
}
