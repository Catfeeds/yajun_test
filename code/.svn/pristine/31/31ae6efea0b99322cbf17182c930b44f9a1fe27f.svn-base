package com.wis.mes.production.metalplate.controller;

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
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.production.metalplate.service.TbSchedulingRecorLogService;

/**
 * @author caixia
 *
 */
@Controller
@RequestMapping("/schedulingRecorLog")
public class TbSchedulingRecorLogController extends BaseController{
	@Autowired
	TbSchedulingRecorLogService service;
	@Autowired
	private DictEntryService entryService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("smProductionState",entryService.getEntryByTypeCode(ConstantUtils.SM_PRODUCTION_STATE));
		return new ModelAndView("/metalPlate/scheduling-recor-log/scheduling_recor_log_list");
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
	
}
