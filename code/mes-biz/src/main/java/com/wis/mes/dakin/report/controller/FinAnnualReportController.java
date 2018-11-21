package com.wis.mes.dakin.report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.mes.dakin.report.service.FinAnnualReportService;

@Controller
@RequestMapping(value = "/finAnnualReport")
public class FinAnnualReportController extends BaseController{
	@Autowired
	private FinAnnualReportService finAnnualReportService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView stationAnomalyReport() {
		return new ModelAndView("report/fin/fin_annual_report_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getFinAnnualReportInfo")
	public Map<String, String> getFinAnnualReportInfo(String createTimeStart, String createTimeEnd) {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = finAnnualReportService.getFinAnnualReportInfo(null, createTimeStart, createTimeEnd);
		return returnMap;
	}
}
