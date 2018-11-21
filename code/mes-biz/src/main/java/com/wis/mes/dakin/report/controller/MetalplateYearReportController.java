package com.wis.mes.dakin.report.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.mes.dakin.report.service.YearReportService;

/**
 * @author caixia
 *
 */
@Controller
@RequestMapping("/metalplateYearReport")
public class MetalplateYearReportController {
	@Autowired
	YearReportService yearReportService;
	@RequestMapping("/yearReport")
	public ModelAndView productMrgReport() {
		return new ModelAndView("/report/metalplate/year_report");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCountReportInfo")
	public Map<String, String> getCountlReportInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = yearReportService.getMetalPlateYearCountShiftReport(whitOrNight,shiftNo, beginTime, endTime);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getTimeShiftInfo")
	public Map<String, String> getTimeShiftInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = yearReportService.getMetalPlateYearTimeReportInfo(whitOrNight,shiftNo, beginTime, endTime);
		return returnMap;
	}
	@ResponseBody
	@RequestMapping(value = "/getPieInfo")
	public Map<String, List> getPieInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String, List> returnMap = yearReportService.getMetalPlateYearDetailPieReportInfo(shiftNo, beginTime, endTime);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getListInfo")
	public List getListInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		List listReport = yearReportService.getMetalPlateYearListReport(shiftNo, beginTime, endTime);
		return listReport;
	}
	
}
