package com.wis.mes.production.record.controller;

import java.util.Arrays;

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
import com.wis.mes.production.record.service.TpRecordUlocNcService;

@Controller
@RequestMapping(value = "/record/uloc/nc")
public class TpRecordUlocNcController extends BaseController {

	@Autowired
	private TpRecordUlocNcService service;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordUlocId", id);
		return new ModelAndView("/production-record/uloc/nc/record_uloc_nc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "ncGroup", "ncCode", "note" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/ncView")
	public ModelAndView ncView(ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordId", id);
		return new ModelAndView("/production-record/nc/record_nc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/getNcDate")
	public JsonActionResult getNcDate(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, String tpRecordId) {
		criteria.getQueryCondition().put("tpRecordId", tpRecordId);
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "ncGroup", "ncCode", "note" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
	//	return ActionUtils.handleListResult(response, service.getNcPageResult(criteria));
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}
}
