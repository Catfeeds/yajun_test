package com.wis.mes.production.pmc.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;

@Controller
@RequestMapping(value = "/pmcTelnet")
public class PmcTelnetController extends BaseController {

	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("pmc/pmc_telnet_info");
	}
	@RequestMapping(value = "/testInput")
	public ModelAndView testInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("pmc/test");
	}
}
