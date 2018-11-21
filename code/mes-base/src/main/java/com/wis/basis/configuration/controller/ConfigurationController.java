package com.wis.basis.configuration.controller;

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
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.GlobalConfiguration;
import com.wis.core.framework.service.GlobalConfigurationService;

@Controller
@RequestMapping(value = "/basis/configuration")
public class ConfigurationController extends BaseController {

	@Autowired
	private GlobalConfigurationService service;
	//	@Autowired
	//	private JobServer jobServer;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/core/configuration/configuration_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "key", "remark" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/core/configuration/configuration_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, GlobalConfiguration bean) {
		GlobalConfiguration entity = service.findById(bean.getId());
		entity.setValue(bean.getValue());
		entity.setRemark(bean.getRemark());
		service.doUpdate(entity);
		//		GlobalConfigurations.getInstance().setValue(bean.getSysKey(), bean.getValue());
		//		if(ConstantUtils.SYS_CONFIG_JOB_SCAN_INTERVAL.equalsIgnoreCase(bean.getSysKey())){
		//			jobServer.setJobInterval(Integer.parseInt(bean.getValue())* 1000);
		//		}
		return ActionUtils.handleResult(response);
	}
}