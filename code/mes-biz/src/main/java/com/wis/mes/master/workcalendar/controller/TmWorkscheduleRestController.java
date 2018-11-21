package com.wis.mes.master.workcalendar.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.workcalendar.entity.TmWorkschedule;
import com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleRestService;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleService;

@Controller
@RequestMapping(value = "/workschedulerest")
public class TmWorkscheduleRestController extends BaseController {

	@Autowired
	private TmWorkscheduleRestService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmWorkscheduleService workScheduleService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response,HttpServletRequest request,QueryCriteria criteria, 
			ModelMap modelMap,@Param(value = "tmWorkscheduleId") Integer tmWorkscheduleId) {
		modelMap.addAttribute("tmWorkscheduleId", tmWorkscheduleId);
		modelMap.addAttribute("currentPageId", request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/workcalendar/workschedulerest/workschedulerest_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, Integer tmWorkscheduleId) {
		modelMap.addAttribute("tmWorkschedule", workScheduleService.findById(tmWorkscheduleId));
		return new ModelAndView("/workcalendar/workschedulerest/workschedulerest_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmWorkscheduleRest bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TmWorkscheduleRest", bean);
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmWorkscheduleRest tmWorkscheduleRest = service.findById(Integer.valueOf(id));
		TmWorkschedule tmWorkschedule = workScheduleService.findById(tmWorkscheduleRest.getTmWorkscheduleId());
		modelMap.addAttribute("tmWorkscheduleRest", tmWorkscheduleRest);
		modelMap.addAttribute("tmWorkschedule", tmWorkschedule);
		return new ModelAndView("/workcalendar/workschedulerest/workschedulerest_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmWorkscheduleRest bean) {
		TmWorkscheduleRest before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmWorkscheduleRest", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmWorkscheduleRest> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmWorkscheduleRest", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/checkTimeOverlap")
	public Map<String, Object> checkTimeOverlap(Integer tmWorkscheduleId, String startTime, String endTime,
			String workscheduleStartTime, String workscheduleEndTime) {
		Map<String, Object> result = null;
		try {
			result = service.checkTimeOverlap(tmWorkscheduleId, startTime, endTime, workscheduleStartTime,
					workscheduleEndTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("数据格式转化异常！");
		}
		return result;
	}
}
