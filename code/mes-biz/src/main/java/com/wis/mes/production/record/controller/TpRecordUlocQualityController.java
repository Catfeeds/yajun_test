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
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.production.record.service.TpRecordUlocQualityService;

@Controller
@RequestMapping(value = "/record/uloc/quality")
public class TpRecordUlocQualityController extends BaseController {

	@Autowired
	private TpRecordUlocQualityService service;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordUlocId", id);
		return new ModelAndView("/production-record/uloc/quality/record_uloc_quality_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "checkItem", "checkResult" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/qualityView")
	public ModelAndView qualityView(ModelMap modelMap, String id) {
		modelMap.addAttribute("tpRecordId", id);
		modelMap.addAttribute("checkResults", entryService.getEntryByTypeCode(ConstantUtils.QUALIRY_CHECK_RESULT));
		return new ModelAndView("/production-record/quality/record_quality_list");
	}

	@ResponseBody
	@RequestMapping(value = "/getQualityDate")
	public JsonActionResult getQualityDate(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, String tpRecordId) {
		criteria.getQueryCondition().put("tpRecordId", tpRecordId);
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "checkItem", "checkResult" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getQualityPageResult(criteria));
	}
}
