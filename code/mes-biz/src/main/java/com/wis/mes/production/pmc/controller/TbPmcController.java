package com.wis.mes.production.pmc.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.production.pmc.entity.TbPmc;
import com.wis.mes.production.pmc.service.TbPmcService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/tbPmc")
public class TbPmcController extends BaseController {

	@Autowired
	private TbPmcService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private TmClassManagerService classManagerService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("lines", JSONArray.fromObject(lineService.getDictItem(null)).toString());
		modelMap.addAttribute("classManagers", JSONArray.fromObject(classManagerService.getDictItemEntry(null)).toString());
		modelMap.addAttribute("classGroupOptions",classManagerService.getDictItemEntry(null));
		modelMap.addAttribute("shiftnoOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		return new ModelAndView("pmc/pmc_list");
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

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TbPmc bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TbManufacturingSchedule", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TbPmc bean) {
		TbPmc before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TbManufacturingSchedule", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbPmc> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbManufacturingSchedule", list);
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, final String ids) throws IOException, InvalidFormatException {
		String[] title = new String[] { "日期", "产线", "班次", "班组", "当日计划", "现时计划","现时完成", "延误时间(min)", "可动率(%)"};
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		if(queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd")== null){
			Date date = new Date();
			queryCondition.put("createTimeStart", DateUtils.getPlusDay(date, -7));
			queryCondition.put("createTimeEnd", date);
		}
		service.doExport(request, response, service, "TM_PMC", "生产进度计划数据导出", title, criteria);
		return ActionUtils.handleResult(response);
	}
}
