package com.wis.mes.production.tksenergy.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import com.wis.mes.production.tksenergy.service.TksEnergyService;

@Controller
@RequestMapping(value = "/tksEnergy")
public class TksEnergyController extends BaseController{

	@Autowired
	private TksEnergyService service;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/tksEnergy/tksEnergy_list");
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
		ActionUtils.dateConditionDefault(criteria, "createTime", -1);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult exportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, final String ids) throws IOException, InvalidFormatException {
		String[] title = new String[] {"空调能耗","照明能耗","设备能耗","创建时间"};
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		service.doExport(request, response, service, "TKS_ENERGY", "能耗记录导出", title, criteria);
		return ActionUtils.handleResult(response);
	}
}
