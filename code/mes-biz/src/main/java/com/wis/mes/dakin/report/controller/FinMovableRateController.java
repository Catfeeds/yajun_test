package com.wis.mes.dakin.report.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.dakin.report.entity.FinMovableRate;
import com.wis.mes.dakin.report.service.FinMovableRateService;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/finMovableRate")
public class FinMovableRateController extends BaseController {
	@Autowired
	private FinMovableRateService finMovableRateService;

	@RequestMapping(value = "/movableRateListInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("currentPageId", "movableRateDataPageId");
		return new ModelAndView("report/fin/fin_movable_rate_data_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "nameCn", "no" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, finMovableRateService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView stationAnomalyReport() {
		return new ModelAndView("report/fin/fin_movable_rate_list");
	}

	@ResponseBody
	@RequestMapping(value = "/getFinMovableRateInfo")
	public List<Map<String, String>> getFinMovableRateInfo(String shiftNo, String createTimeStart,
			String createTimeEnd) {
		if (StringUtil.isEmpty(createTimeStart)) {
			createTimeStart = DateUtils.formatDate(new Date());
		}
		createTimeStart = createTimeStart.substring(0, 7);
		Date parse = DateUtils.parse(createTimeStart, "yyyy-MM");
		int monthLastDay = DateUtils.getMonthLastDay(parse);
		List<Map<String, String>> returnMap = new ArrayList<Map<String, String>>();
		returnMap = finMovableRateService.getFinMovableRateEveryDayInfo(shiftNo, createTimeStart + "-01",
				createTimeStart + "-" + monthLastDay);
		return returnMap;
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		} else {
			Map<String, Object> queryCondition = criteria.getQueryCondition();
			if (queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd") == null) {
				Date currentDate = DateUtils.getCurrentDate();
				queryCondition.put("createTimeStart", DateUtils.getPlusDay(currentDate, -30));
				queryCondition.put("createTimeEnd", currentDate);
			}
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "nameCn" }));
		PageResult<FinMovableRate> findBy = finMovableRateService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "日期", "班次", "时刻", "实际可动率" };
		finMovableRateService.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

}
