package com.wis.mes.master.nc.controller;

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
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.nc.entity.TmFaultGrade;
import com.wis.mes.master.nc.service.TmFaultGradeService;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.production.producttracing.service.TbProductTracingService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/faultGrade")
public class TmFaultGradeController extends BaseController {

	@Autowired
	private TmFaultGradeService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TbProductTracingService productTracingService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(String currentPageId, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("optionNgEntrance", JSONArray.fromObject(ulocService.getUlocNgExitEnterMap()).toString());
		return new ModelAndView("/master/nc/faultGrade/faultGrade_list");
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
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		setModelMapDictionary(modelMap);
		return new ModelAndView("/master/nc/faultGrade/faultGrade_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmFaultGrade bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TmFaultGrade", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		setModelMapDictionary(modelMap);
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/master/nc/faultGrade/faultGrade_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmFaultGrade bean) {
		TmFaultGrade before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmFaultGrade", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmFaultGrade> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmFaultGrade", list);
		return ActionUtils.handleResult(response);
	}

	private void setModelMapDictionary(ModelMap modelMap) {
		/*
		 * modelMap.addAttribute("optionNgEntrance",
		 * entryService.getEntryByTypeCode(ConstantUtils.NG_ENTRANCE));
		 */
		modelMap.addAttribute("optionNgEntrance", ulocService.getUlocNgExitEnterMap());
		modelMap.addAttribute("optionNcLevel", entryService.getEntryByTypeCode(ConstantUtils.QUALITY_NC_LEVEL));
	}

}
