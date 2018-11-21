package com.wis.mes.production.record.controller;

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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.production.record.entity.TpRecordUlocParameter;
import com.wis.mes.production.record.service.TpRecordUlocParameterService;

@Controller
@RequestMapping(value = "/record/uloc/parameter")
public class TpRecordUlocParameterController extends BaseController {

	@Autowired
	private TpRecordUlocParameterService service;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordUlocId", id);
		return new ModelAndView("/production-record/uloc/parameter/record_uloc_parameter_list");
	}

	@RequestMapping(value = "/listViemParam")
	public ModelAndView listViemParam(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			ModelMap modelMap, String currentPageId) {
		String sn = request.getParameter("sn");
		String ulocId = request.getParameter("ulocId");
		String routingSeq = request.getParameter("routingSeq");
		String params = "queryCondition[tpRecord.sn]=" + sn + "&queryCondition[tpRecordUloc.tmUlocId]=" + ulocId
				+ "&queryCondition[tpRecordUloc.routingSeq]=" + routingSeq;
		modelMap.addAttribute("params", params);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		return new ModelAndView("/production-record/uloc/parameter/record_uloc_parameter_untratedNcList");
	}

	@RequestMapping(value = "/updateUntratedInput")
	public ModelAndView updateUntratedInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findRelObjById(Integer.valueOf(id)));
		return new ModelAndView("/production-record/uloc/parameter/record_uloc_parameter_untratedNcUpdate");
	}

	@ResponseBody
	@RequestMapping(value = "/updateUntrated")
	public JsonActionResult updateUntrated(HttpServletResponse response, ModelMap modelMap,
			TpRecordUlocParameter newParam, String ordId) {
		service.updateUntrated(newParam, ordId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "parameterCode", "parameterValue" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);

		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		//TODO add return view here
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TpRecordUlocParameter bean) {
		bean = service.doSave(bean);
		//logService.doAddLog("TpRecordUlocParameter", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TpRecordUlocParameter bean) {
		//TpRecordUlocParameter before = service.findById(bean.getId());
		service.doUpdate(bean);
		//	logService.doUpdateLog("TpRecordUlocParameter", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		//List<TpRecordUlocParameter> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		//	logService.doDeleteLog("TpRecordUlocParameter", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/parameterView")
	public ModelAndView parameterView(ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordId", id);
		return new ModelAndView("/production-record/parameter/record_parameter_list");
	}

	@ResponseBody
	@RequestMapping(value = "/getParameterDate")
	public JsonActionResult getParameterDate(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, String tpRecordId) {
		criteria.getQueryCondition().put("tpRecordId", tpRecordId);
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "parameterCode", "parameterValue" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getParameterPageResult(criteria));
	}
}
