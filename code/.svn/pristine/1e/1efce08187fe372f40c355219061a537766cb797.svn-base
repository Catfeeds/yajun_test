package com.wis.basis.menu.controller;

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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.menu.vo.MenuVo;
import com.wis.basis.systemadmin.service.MenuManagementService;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.Language;
import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.service.AuditLogService;

@Controller
@RequestMapping(value = "/menu")
public class TsMenuController extends BaseController {

	@Autowired
	private MenuManagementService service;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/core/menu/menu_list");
	}

	@ResponseBody
	@RequestMapping(value = "/menusList")
	public List<Menu> findUlocListToCrossPoint() {
		return service.getAllMenus();
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
		return new ModelAndView("/core/menu/menu_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, MenuVo bean) {
		addMenu(bean);
		return ActionUtils.handleResult(response);
	}

	private void addMenu(MenuVo menuVo) {
		Menu menu = menuVo.getMenu();
		Language language = menuVo.getLanguage();
		language.setCode(menu.getName());
		menu.setId(menuVo.getMenuId());
		service.addMenu(menu);
		language = service.addLanguage(language);
		logService.doAddLog("TsMenu", menu);
		logService.doAddLog("TsLanguage", language);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, Menu bean) {
		Menu before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TsMenu", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<Menu> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TsMenu", list);
		return ActionUtils.handleResult(response);
	}
}
