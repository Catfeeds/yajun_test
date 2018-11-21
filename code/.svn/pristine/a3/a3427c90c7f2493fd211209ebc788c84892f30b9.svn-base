package com.wis.mes.dakin.report.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.mes.dakin.report.service.ReportService;
import com.wis.mes.production.pmc.entity.TbPmcPmst;
import com.wis.mes.production.pmc.service.TbPmcPmstService;

@Controller
@RequestMapping(value = "/plcReport")
public class PLCReportController {

	@Autowired
	private ReportService reportService;
	@Autowired
	private TbPmcPmstService pmcPmstService;
	
	@RequestMapping(value = "/energyReport")
	public ModelAndView energyReport() {
		return new ModelAndView("/report/plc/energy_report");
	}

	@ResponseBody
	@RequestMapping(value = "/getEnergyReportData")
	public Map<String, String> getEnergyReportData() {
		Date currentDate = DateUtils.getCurrentDate();
		String nowDate = DateUtils.formatDate(currentDate);
		String startDateTime = nowDate + " 08:00:00";
		String endDateTime = DateUtils.formatDate(DateUtils.getPlusDay(1)) + " 08:00:00";
		Long currentTimeSs = currentDate.getTime();
		Long startDate = DateUtils.parseDateTime(nowDate + " 08:00:00").getTime();
		Long endDate = DateUtils.parseDateTime(nowDate + " 20:00:00").getTime();
		if (currentTimeSs >= startDate && startDate <= endDate) {
			startDateTime = nowDate + " 08:00:00";
			endDateTime = nowDate + " 20:00:00";
		}
		Map<String, String> map = reportService.plcEnergyReport(startDateTime, endDateTime);
		return map;
	}
	
	
	@RequestMapping(value = "/noticeReport")
	public ModelAndView noticeReport(ModelMap modelMap) {
		TbPmcPmst eg = new TbPmcPmst();
		eg.setModelType(ConstantUtils.PMST_AFFICHE);
		TbPmcPmst findUniqueByEg = pmcPmstService.findUniqueByEg(eg);
		modelMap.addAttribute("bean", findUniqueByEg);
		return new ModelAndView("/report/plc/notice");
	}
}
