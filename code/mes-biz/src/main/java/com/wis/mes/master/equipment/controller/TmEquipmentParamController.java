package com.wis.mes.master.equipment.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.equipment.service.TmEquipmentParamService;
import com.wis.mes.master.equipment.service.TmEquipmentService;

@Controller
@RequestMapping(value = "/equipmentParam")
public class TmEquipmentParamController extends BaseController {

	@Autowired
	private TmEquipmentParamService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmEquipmentService tmEquipmentService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("optionIfCnfScope", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("optionIfEquipment", tmEquipmentService.getDictItem(null));
		return new ModelAndView("/master/equipment/param/param_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("parameterName","parameterExplain"));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, ModelMap modelMap,Integer currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("optionIfCnfScope", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("optionIfEquipment", tmEquipmentService.getDictItem(null));
		return ActionUtils.handleEntryResult(modelMap,new ModelAndView("/master/equipment/param/param_add"));
	}
	
	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response,TmEquipmentParam bean) {
		Integer destId = service.checkParameterName(bean.getParameterName(),bean.getTmEquipmentId());
		if (destId == null) {
			bean = service.doSave(bean);
			logService.doAddLog("TmEquipmentParam", bean);
		} else {
			throw new BusinessException("EQUIPMENT_TS_PARAMETER_NAME_REPETITION");
		}
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/paramListInput")
	public ModelAndView paramListInput(HttpServletRequest request, ModelMap modelMap,Integer tmEquipmentId){
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/master/equipment/param/param_system_list"));
	}
	
	@ResponseBody
	@RequestMapping(value = "/getParamList")
	public JsonActionResult getUserData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			Integer tmEquipmentId) {
		criteria.setFuzzyQueryFileds(Arrays.asList("code","name"));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId);
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getParamPageResult(criteria));
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, Integer id, ModelMap modelMap) {
		modelMap.addAttribute("param", service.findById(id));
		modelMap.addAttribute("optionIfEquipment", tmEquipmentService.getDictItem(null));
		modelMap.addAttribute("optionIfCnfScope", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/equipment/param/param_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmEquipmentParam bean) {
		Integer destId = service.checkParameterName(bean.getParameterName(),bean.getTmEquipmentId());
		if (destId == null || destId.equals(bean.getId())) {
			TmEquipmentParam before = service.findById(bean.getId());
			service.doUpdate(bean);
			logService.doUpdateLog("TmEquipmentParam", before, bean);
		} else {
			throw new BusinessException("EQUIPMENT_TS_PARAMETER_NAME_REPETITION");
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmEquipmentParam> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmEquipmentParam", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * @desc 导出
	 * @author ryy
	 */
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids, Integer tmEquipmentId) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId.toString());
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmEquipmentParam> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "参数", "参数编号", "参数表达式", "参数变量名", "类型", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
