package com.wis.mes.dakin.report.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.mes.dakin.report.service.FinNoFaultDetailsService;

@Controller
@RequestMapping(value = "/finNoFaultDetails")
public class FinNoFaultDetailsController extends BaseController{
	@Autowired
	private FinNoFaultDetailsService finNoFaultDetailsService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView stationAnomalyReport() {
		return new ModelAndView("report/fin/fin_no_fault_details_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getFinNoFaultDetailsInfo")
	public Map<String, String> getFinAnnualReportInfo(String createTimeStart, String createTimeEnd) {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = finNoFaultDetailsService.getFinNoFaultDetails(createTimeStart, createTimeEnd);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/noFaultedDetailsYearShow")
	public JsonActionResult noFaultedDetailsYearShow(String createTimeStart, String createTimeEnd) {
		String year = "";
		if (StringUtils.isNotEmpty(createTimeStart)) {
			year = createTimeStart.split("-")[0];
		} else {
			Calendar cal = Calendar.getInstance();
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		createTimeStart=year+"-01-01 00:00";
		createTimeEnd=year+"-12-31 23:59";
		return ActionUtils.handleResult(finNoFaultDetailsService.noFaultedDetailsYearShow(year, createTimeStart, createTimeEnd));
	}
}
