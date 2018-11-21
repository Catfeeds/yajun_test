package com.wis.mes.master.workcalendar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
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
import com.wis.core.framework.service.AuditLogService;
import com.wis.mes.master.workcalendar.entity.TmWorktimeRest;
import com.wis.mes.master.workcalendar.service.TmWorktimeRestService;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;

@Controller
@RequestMapping(value = "/worktimerest")
public class TmWorktimeRestController extends BaseController {

	@Autowired
	private TmWorktimeRestService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmWorktimeService workTimeService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap,
			@Param(value = "tmWorktimeId") Integer tmWorktimeId) {
		modelMap.addAttribute("tmWorktimeId", tmWorktimeId);
		modelMap.addAttribute("currentPageId", request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/workcalendar/worktimerest/worktimerest_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer tmWorktimeId) {
		modelMap.addAttribute("workTime", workTimeService.findById(tmWorktimeId));
		return new ModelAndView("/workcalendar/worktimerest/worktimerest_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response,HttpServletRequest request, TmWorktimeRest bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TmWorktimeRest", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmWorktimeRest worktimeRest = service.findById(Integer.valueOf(id));

		modelMap.addAttribute("workTimeRest", worktimeRest);
		modelMap.addAttribute("workTime", workTimeService.findById(worktimeRest.getTmWorktimeId()));
		return new ModelAndView("/workcalendar/worktimerest/worktimerest_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmWorktimeRest bean) {
		TmWorktimeRest before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmWorktimeRest", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmWorktimeRest> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmWorktimeRest", list);
		return ActionUtils.handleResult(response);
	}
}
