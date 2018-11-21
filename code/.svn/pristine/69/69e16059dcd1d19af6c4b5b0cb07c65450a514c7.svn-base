package com.wis.mes.rfid.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.rfid.entity.TbRfidLog;
import com.wis.mes.rfid.service.TbRfidLogService;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/tbRfidLog")
public class TbRfidLogController extends BaseController {

	@Autowired
	private TbRfidLogService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmLineService tmLineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("lineOptions", tmLineService.getDictItem(null));
		return new ModelAndView("rfid/rfid_log_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "sn", "backNumber", "machineName","fruit","epcInfo"}));
		changeParameter(criteria);
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	private void changeParameter(QueryCriteria criteria) {
		Map<String,Object> parameter = criteria.getQueryCondition();
		if(parameter.containsKey("createTimeStart")) {
			if(StringUtil.isNotEmpty(parameter.get("createTimeStart"))) {
				criteria.getQueryCondition().put("createTimeStart",DateUtils.formatMinDate(parameter.get("createTimeStart").toString()));
			}
		}
		if(parameter.containsKey("createTimeEnd")) {
			if(StringUtil.isNotEmpty(parameter.get("createTimeEnd"))) {
				criteria.getQueryCondition().put("createTimeEnd",  DateUtils.formatMaxDate(parameter.get("createTimeEnd").toString()));
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbRfidLog> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbRfidLog", list);
		return ActionUtils.handleResult(response);
	}
}
