/**
 * 2013-5-17 下午2:16:14
 */
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
import com.wis.core.framework.entity.DictType;
import com.wis.core.framework.model.AuditConfigurationType;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictTypeService;

import net.sf.json.JSONObject;

/**
 * @author vincent
 * 
 */
@Controller
@RequestMapping(value = "/type")
public class DictTypeController extends BaseController {

	@Autowired
	private DictTypeService typeService;
	@Autowired
	private AuditLogService logService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name","enName", "code" }));
		if (criteria.getOffset() >= 0) {
			criteria.setRows(criteria.getLimit());
			criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		}
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, typeService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/core/type/type_list");
	}

	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(HttpServletRequest request, ModelMap modelMap) {
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/type/type_select"));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/core/type/type_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, DictType bean) {
		bean = typeService.doSave(bean);
		logService.doAddLog(AuditConfigurationType.AUDIT_TYPE_DICT_TYPE, bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, Integer id, ModelMap modelMap) {
		modelMap.addAttribute("type", typeService.findById(id));
		return new ModelAndView("/core/type/type_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, DictType bean) {
		DictType before = typeService.findById(bean.getId());
		typeService.doUpdate(bean);
		logService.doUpdateLog(AuditConfigurationType.AUDIT_TYPE_DICT_TYPE, before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<DictType> list = typeService.findAllInIds(deleteIds);
		typeService.doDeleteById(deleteIds);
		logService.doDeleteLog(AuditConfigurationType.AUDIT_TYPE_DICT_TYPE, list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/checkType")
	public void checkType(HttpServletResponse response, String code, String param) {
		JSONObject result = new JSONObject();
		if (typeService.checkCode(param) && !param.equals(code)) {
			result.put("status", "n");
			result.put("info", "类型代码已存在！");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}
}
