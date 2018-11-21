package com.wis.basis.systemadmin.controller;

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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.model.AuditConfigurationType;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.DictTypeService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/entry")
public class DictEntryController extends BaseController {

	@Autowired
	private DictTypeService typeService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/all")
	public void findPage(HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("entrys", typeService.findPageEntry());
		ActionUtils.handleResult(response, jsonObject);
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "enName", "code", "type.name", "type.enName" }));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, entryService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/core/entry/entry_list");
	}

	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(HttpServletRequest request, ModelMap modelMap) {
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/entry/entry_select"));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/core/entry/entry_add");
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("entry", entryService.findById(Integer.valueOf(id)));
		return new ModelAndView("/core/entry/entry_update");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, DictEntry bean) {
		bean = entryService.doSave(bean);
		logService.doAddLog(AuditConfigurationType.AUDIT_TYPE_DICT_ENTRY, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, DictEntry bean) {
		DictEntry before = entryService.findById(bean.getId());
		entryService.doUpdate(bean);
		bean.setTypeId(before.getTypeId());
		logService.doUpdateLog(AuditConfigurationType.AUDIT_TYPE_DICT_ENTRY, before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<DictEntry> list = entryService.findAllInIds(deleteIds);
		entryService.doDeleteById(deleteIds);
		logService.doDeleteLog(AuditConfigurationType.AUDIT_TYPE_DICT_ENTRY, list);
		return ActionUtils.handleResult(response);
	}
}
