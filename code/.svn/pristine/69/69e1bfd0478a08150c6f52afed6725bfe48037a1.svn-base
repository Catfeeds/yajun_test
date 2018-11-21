package com.wis.mes.master.coderule.controller;

import java.util.Arrays;
import java.util.List;

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
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.coderule.entity.TmCodeRule;
import com.wis.mes.master.coderule.service.TmCodeRuleService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;

@Controller
@RequestMapping(value = "/tmCodeRule")
public class TmCodeRuleController extends BaseController {
	@Autowired
	private TmCodeRuleService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService lineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("lineOption", lineService.getDictItem(null));
		modelMap.addAttribute("abnormalDistinguishOption",
				entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_DISTINGUISH));
		modelMap.addAttribute("abnormalSourceOption",
				entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_SOURCE));
		return new ModelAndView("/master/coderule/codeRule_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {"codeDesc","plcCode"}));
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("lineOption", lineService.getDictItem(null));
		modelMap.addAttribute("abnormalDistinguishOption",
				entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_DISTINGUISH));
		modelMap.addAttribute("abnormalSourceOption",
				entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_SOURCE));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/coderule/codeRule_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmCodeRule bean)
			throws Exception {
		//checkValue(bean);
		TmLine line = lineService.findById(bean.getTmLineId());
		bean.setCode(line.getNameCn()+bean.getDistinguish()+bean.getDataSource()+bean.getCode());
		bean = service.doSave(bean);
		logService.doAddLog("TmPlant", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("lineOption", lineService.getDictItem(null));
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("lineOption", lineService.getDictItem(null));
		modelMap.addAttribute("abnormalDistinguishOption",
				entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_DISTINGUISH));
		modelMap.addAttribute("abnormalSourceOption",
				entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_ABNORMAL_SOURCE));
		return new ModelAndView("/master/coderule/codeRule_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmCodeRule bean) {
		//checkValue(bean);
		TmCodeRule before = service.findById(bean.getId());
		TmLine line = lineService.findById(bean.getTmLineId());
		bean.setCode(line.getNameCn()+bean.getDistinguish()+bean.getDataSource()+bean.getCode());
		service.doUpdate(bean);
		logService.doUpdateLog("TmCodeRule", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmCodeRule> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmPlant", list);
		return ActionUtils.handleResult(response);
	}

	@SuppressWarnings("unused")
	private void checkValue(TmCodeRule bean) {
		TmCodeRule eg = new TmCodeRule();
		eg.setCode(bean.getCode());
		List<TmCodeRule> findByEg = service.findByEg(eg);
		for (TmCodeRule t : findByEg) {
			if (!t.getId().equals(bean.getId())) {
				throw new BusinessException("ERROR_CODE_RULE_NEW_CODE_UNIQUE");
			}
		}
		eg = new TmCodeRule();
		eg.setPlcCode(bean.getPlcCode());
		findByEg = service.findByEg(eg);
		for (TmCodeRule t : findByEg) {
			if (!t.getId().equals(bean.getId())) {
				throw new BusinessException("ERROR_CODE_RULE_OLD_CODE_UNIQUE");
			}
		}
	}
}
