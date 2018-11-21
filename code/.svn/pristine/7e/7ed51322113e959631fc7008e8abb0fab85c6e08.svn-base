/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.audit.controller;

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
import com.wis.core.framework.service.AuditLogService;

/**
 * 
 * @author xiaogang
 *
 */
@Controller
@RequestMapping(value = "/audit")
public class AuditController extends BaseController {

	@Autowired
	private AuditLogService auditLogService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "afterOperateObj", "beforeOperateObj", "config.auditTypeName", "operatorName", "config.operationName" }));
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC); 
		return ActionUtils.handleListResult(response, auditLogService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response) {
		return new ModelAndView("/core/audit/audit_list");
	}
}
