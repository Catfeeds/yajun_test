package com.wis.mes.master.labelmanage.controller;

import java.util.Arrays;
import java.util.List;

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
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.mes.master.labelmanage.entity.TbLabelManage;
import com.wis.mes.master.labelmanage.service.TbLabelManageService;
import com.wis.mes.master.line.service.TmLineService;

@Controller
@RequestMapping(value = "/tbLabelManage")
public class TbLabelManageController extends BaseController {

	@Autowired
	private TbLabelManageService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmLineService lineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/master/labelmanage/label_manage_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "epcId", "salverNo"}));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("lineOptions", lineService.getDictItem(null));
		return new ModelAndView("/master/labelmanage/label_manage_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TbLabelManage bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TbLabelManage", bean);
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(null));
		return new ModelAndView("/master/labelmanage/label_manage_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TbLabelManage bean) {
		TbLabelManage before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TbLabelManage", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbLabelManage> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbLabelManage", list);
		return ActionUtils.handleResult(response);
	}
}
