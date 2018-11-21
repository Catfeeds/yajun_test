package com.wis.mes.production.producttracing.controller;

import java.io.IOException;
import java.util.Arrays;
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
import com.wis.mes.dakin.production.tracing.service.VbProductOperationService;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductTracingService;

@Controller
@RequestMapping(value = "/tbProductTracing")
public class TbProductTracingController extends BaseController {

	@Autowired
	private TbProductTracingService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private VbProductOperationService vbProductOperationService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("classOrderOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("yesOrNo",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("producttracing/producttracing_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("backNumber", "machineOfName", "machineName"));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		if(queryCondition.containsKey("unhealthyCount")) {
			String unhealthyCount = queryCondition.get("unhealthyCount").toString();
			if(unhealthyCount.equals("YES")) {
				queryCondition.put("unhealthyCountStart","1");
				queryCondition.remove("unhealthyCount");
			}else {
				queryCondition.put("unhealthyCount", "0");
			}
		}
		
		if(queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd")== null){
			Date date = new Date();
			queryCondition.put("createTimeStart", DateUtils.getPlusDay(date, -7));
			queryCondition.put("createTimeEnd", date);
		}
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbProductTracing> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbProductTracing", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, final String ids) throws IOException, InvalidFormatException {
		String[] title = new String[] { "事部", "产线编号", "产线名称", "日期", "班次", "背番号", "机种名", "机号", "工装板ID", "工位编号", "加工工序",
				"作业员工号", "配套设备编号", "配套设备名称", "流入开始时间", "流入结束时间", "设备作业开始时间", "设备作业结束时间", "工位作业完成时间", "流出开始时间", "流出结束时间",
				"工位作业时长", "流入时长", "设备作业时长", "员工作业时长", "流出时长", "前等工时长", "后等工时长", "等工时长", "异常时长", "警告时长", "手动作业结果",
				"线体作业结果", "设备作业结果", "工位综合作业结果","不良区分", "故障代码", "故障类型", "故障内容", "信息来源", "NG出口", "NG口流出时间", "NG入口", "NG口流入时间" };
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		if(queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd")== null){
			Date date = new Date();
			queryCondition.put("createTimeStart", DateUtils.getPlusDay(date, -7));
			queryCondition.put("createTimeEnd", date);
		}
		if(queryCondition.containsKey("unhealthyCount")) {
			String unhealthyCount = queryCondition.get("unhealthyCount").toString();
			if(unhealthyCount.equals("YES")) {
				queryCondition.put("unhealthyCountStart","1");
				queryCondition.remove("unhealthyCount");
			}else {
				queryCondition.put("unhealthyCount", "0");
			}
		}
		if(queryCondition.containsKey("tmWorktime.shiftno")) {
			queryCondition.put("shiftno", queryCondition.get("tmWorktime.shiftno"));
			queryCondition.remove("tmWorktime.shiftno");
		}
		vbProductOperationService.doExport(request, response, vbProductOperationService, "TRACING_STATION", "产品生产作业参数报表", title, criteria);
		return ActionUtils.handleResult(response);
	}
}
