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
import com.wis.basis.systemadmin.vo.DataPermissionVo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.framework.entity.DataPermission;
import com.wis.core.framework.service.DataPermissionService;
import com.wis.core.framework.service.DictEntryService;

@Controller
@RequestMapping(value = "/dataPermission")
public class DataPermissionController extends BaseController {

	@Autowired
	private DataPermissionService dataPermissionService;
	@Autowired
	private DictEntryService entryService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "module", "code", "url" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, dataPermissionService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap) {
		TypeVo permissionModule = new TypeVo(ConstantUtils.TYPE_CODE_DATA_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/data/data_permission_list"), permissionModule);
	}

	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(HttpServletRequest request, ModelMap modelMap) {
		TypeVo permissionModule = new TypeVo(ConstantUtils.TYPE_CODE_DATA_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/permission/data/data_permission_select"), permissionModule);
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap) {
		TypeVo permissionModule = new TypeVo(ConstantUtils.TYPE_CODE_DATA_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/data/data_permission_add"), permissionModule);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("bean", dataPermissionService.findById(id));
		TypeVo permissionModule = new TypeVo(ConstantUtils.TYPE_CODE_DATA_PERMISSION_MUDULE, "moduleOptions");
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/data/data_permission_update"), permissionModule);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, DataPermissionVo dataPermission) {
		dataPermissionService.doSave((DataPermission) copy(dataPermission, new DataPermission()));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, DataPermissionVo dataPermission) {
		DataPermission dest = (DataPermission) copy(dataPermission, dataPermissionService.findById(dataPermission.getId()));
		dataPermissionService.doUpdate(dest);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		dataPermissionService.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}
}
