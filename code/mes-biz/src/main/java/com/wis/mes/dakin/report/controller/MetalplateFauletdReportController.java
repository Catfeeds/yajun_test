package com.wis.mes.dakin.report.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.dakin.report.service.MetalPlateFaultedDetailReportService;

/**
 * @author caixia
 *
 */
@Controller
@RequestMapping("/metalplateFauletdReport")
public class MetalplateFauletdReportController {
	@Autowired
	MetalPlateFaultedDetailReportService detailReportService;
	@Autowired
	private DictEntryService entryService;
	
	
	@RequestMapping("/fauletdReport")
	public ModelAndView productMrgReport() {
		return new ModelAndView("/report/metalplate/fauletd_report");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getFaultedBarInfo")
	public Map<String, String> getFinAnnualReportInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = detailReportService.getMetalPlateFaultedDetailReport(whitOrNight,shiftNo, beginTime, endTime);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getFaultedPieInfo")
	public Map<String, String> getFinFaultedPieInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if(StringUtils.isEmpty(beginTime) && StringUtils.isEmpty(endTime)) {
			Date date = new Date();
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.add(Calendar.MONTH, -1);
			beginTime = DateUtils.formatDate(rightNow.getTime(), DateUtils.FORMAT_DATETIME_YYYY_MM_DD_HH_MM);
			endTime = DateUtils.formatDate(date, DateUtils.FORMAT_DATETIME_YYYY_MM_DD_HH_MM);
		}
		returnMap = detailReportService.getMetalPlateFaultedDetailPieReportInfo(whitOrNight,shiftNo, beginTime, endTime);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getfaultedDetailsInfo")
	public Map<String, String> getfaultedDetailsInfo(String whitOrNight,String shiftNo, String beginTime, String endTime) {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = detailReportService.getfaultedDetailsInfo(beginTime, endTime);
		return returnMap;
	}
	//----------------------------------------非故障明细---------------------------------//
	@RequestMapping(value = "/noFaultDetailsReport")
	public ModelAndView noFaultDetailsReport() {
		return new ModelAndView("report/metalplate/no_fault_details_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/noFaultDetailsReportData")
	public Map<String,Object> noFaultDetailsReportData(QueryCriteria criteria) {
		Map<String,Object> queryCondition = criteria.getQueryCondition();
		return ActionUtils.handleMap(detailReportService.noFaultDetailsReportData(queryCondition));
	}
	
	@RequestMapping("/productionStatusInput")
	public ModelAndView productionStatusInput(ModelMap modelMap) {
		return new ModelAndView("/report/metalplate/production_status_report");
	}
	
	//----------------------------------------BJ-生产状况明细---------------------------------//
	@RequestMapping(value = "/productionStatusReport")
	public ModelAndView productionStatusReport(ModelMap modelMap) {
		modelMap.addAttribute("regionMarks", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
		modelMap.addAttribute("shiftArray", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		return new ModelAndView("report/metalplate/product_report");
	}
	
	@ResponseBody
	@RequestMapping("/productionStatusData")
	public Map<String,Object> productionStatusData(QueryCriteria criteria){
		Map<String, Object> productionStatusData = detailReportService.productionStatusData(criteria.getQueryCondition());
		return ActionUtils.handleMap(productionStatusData);
	}
	
	//----------------------------------------BJ-生产状态年报---------------------------------//
	@RequestMapping(value = "/productionStateYearReport")
	public ModelAndView productionStateYearReport(ModelMap modelMap) {
		modelMap.addAttribute("regionMarks", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
		return new ModelAndView("report/metalplate/production_state_year");
	}
	
	@ResponseBody
	@RequestMapping("/productionStateYearData")
	public Map<String,Object> productionStateYearData(QueryCriteria criteria){
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		return ActionUtils.handleMap(detailReportService.productionStateYearData(queryCondition));
	}
	
	
	//----------------------------------------BJ-故障停机明细---------------------------------//
	@RequestMapping(value = "/noHaltReport")
	public ModelAndView noHaltReport(ModelMap modelMap) {
		return new ModelAndView("report/metalplate/ng_halt_detail");
	}
	
	@ResponseBody
	@RequestMapping("/noHaltReportData")
	public Map<String,Object> noHaltReportData(QueryCriteria criteria){
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		return ActionUtils.handleMap(detailReportService.noHaltReportData(queryCondition));
	}
}
