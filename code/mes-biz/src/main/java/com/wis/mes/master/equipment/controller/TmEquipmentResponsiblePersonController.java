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
import com.wis.mes.master.equipment.entity.TmEquipmentResponsiblePerson;
import com.wis.mes.master.equipment.service.TmEquipmentResponsiblePersonService;

@Controller
@RequestMapping(value = "/equipmentResponsiblePerson")
public class TmEquipmentResponsiblePersonController extends BaseController {

	@Autowired
	private TmEquipmentResponsiblePersonService service;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, Integer tmEquipmentId) {
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		return new ModelAndView("/master/equipment/person/person_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap,
			Integer tmEquipmentId) {
		criteria.setFuzzyQueryFileds(Arrays.asList("user.name", "note"));
		criteria.setQueryRelationEntity(true);
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId.toString());
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, Integer tmEquipmentId, Integer currentPageId,
			ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/equipment/person/person_add"));
	}

	@RequestMapping(value = "/userListInput")
	public ModelAndView userListInput(HttpServletRequest request, ModelMap modelMap, Integer tmEquipmentId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return ActionUtils.handleSelectResult(request, modelMap,
				new ModelAndView("/master/equipment/person/person_user_list"));
	}

	@ResponseBody
	@RequestMapping(value = "/getUserList")
	public JsonActionResult getUserData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			Integer tmEquipmentId) {
		criteria.setFuzzyQueryFileds(Arrays.asList("name", "account"));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId);
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getUserPageResult(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, Integer tmEquipmentId, String[] tsUserId, String[] note) {
		List<TmEquipmentResponsiblePerson> tmEquipmentResponsiblePerson = service.doSaveBatch(tmEquipmentId, tsUserId,
				note);
		logService.doAddLog("TmEquipmentResponsiblePerson", tmEquipmentResponsiblePerson);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap, Integer tmEquipmentId) {
		modelMap.addAttribute("responsiblePerson", service.findById(Integer.valueOf(id), true));
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return new ModelAndView("/master/equipment/person/person_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmEquipmentResponsiblePerson bean) {
		TmEquipmentResponsiblePerson before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmEquipmentResponsiblePerson", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmEquipmentResponsiblePerson> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmEquipmentResponsiblePerson", list);
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
		final PageResult<TmEquipmentResponsiblePerson> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "姓名", "号码", "邮箱", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
