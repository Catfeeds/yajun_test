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
import com.wis.mes.production.record.entity.TpRecordUlocPart;
import com.wis.mes.production.record.service.TpRecordUlocPartService;

@Controller
@RequestMapping(value = "/record/uloc/part")
public class TpRecordUlocPartController extends BaseController {

	@Autowired
	private TpRecordUlocPartService service;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordUlocId", id);
		return new ModelAndView("/production-record/uloc/part/record_uloc_part_list");
	}
	
	@RequestMapping(value = "/listViemPart")
	public ModelAndView listViemPart(HttpServletRequest request,HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,String currentPageId) {
		String sn = request.getParameter("sn"); 
		String ulocId = request.getParameter("ulocId");
		String routingSeq = request.getParameter("routingSeq");
		String params  = "queryCondition[tpRecord.sn]="+sn+"&queryCondition[tpRecordUloc.tmUlocId]="
		+ulocId+"&queryCondition[tpRecordUloc.routingSeq]="+routingSeq;
		modelMap.addAttribute("params", params);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		return new ModelAndView("/production-record/uloc/part/record_uloc_part_untratedNcList");
	}
	
	@RequestMapping(value = "/updateUntratedInput")
	public ModelAndView updateUntratedInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/production-record/uloc/part/record_uloc_part_untratedNcUpdate");
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateUntrated")
	public JsonActionResult updateUntrated(HttpServletResponse response, ModelMap modelMap,TpRecordUlocPart newPart,String ordId) {
		service.updateUntrated(newPart,ordId);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/partView")
	@ResponseBody
	public ModelAndView partView(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap,
			String id) {
		modelMap.addAttribute("tpRecordId", id);
		return new ModelAndView("/production-record/part/record_part_list");
	}

	@ResponseBody
	@RequestMapping(value = "/getPartDate")
	public JsonActionResult getPartDate(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, String tpRecordId) {
		criteria.getQueryCondition().put("tpRecordId", tpRecordId);
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "partNo", "partName" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getPartPageResult(criteria));
	}

}
