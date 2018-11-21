package com.wis.mes.dakin.report.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.utils.DateUtils;
import com.wis.mes.dakin.report.service.TmMetalPlateReportService;

@Controller
@RequestMapping(value="/metalPlateReport")
public class MetalPlateReportController {
	@Autowired
	TmMetalPlateReportService tmMetalPlateReportService;
	/**
	 * 设备异常分析年报
	 */
	@RequestMapping(value = "/unitExceptionReport")
	public ModelAndView unitExceptionReport() {
		return new ModelAndView("/report/metalplate/unit_exception_report");
	}

	/**
	 * 设备异常分析年报
	 * 
	 * @param type
	 * @param createTime 日期
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/unitExceptionDataReport")
	public Map<String, String> faultMachineData(String type, String createTime) {
		if(StringUtils.isEmpty(createTime)){
			createTime = DateUtils.formatDate(new Date(),DateUtils.FORMAT_DATE_YYYY_MM);
		}
		if ("UNIT_EXCEPTION_BAR".equals(type)) {
			return tmMetalPlateReportService.getMetalPlateReport(createTime,14);
		}else if("UNIT_EXCEPTION_WARN_HEATMAP".equals(type)) {
			return tmMetalPlateReportService.getMetalPlateReportWarnByMouth(createTime,14);//14(code码警告对应位置)
		}else if("UNIT_EXCEPTION_UNUSUAL_HEATMAP".equals(type)) {
			return tmMetalPlateReportService.getMetalPlateReportWarnByMouth(createTime,14);
		}
		return null;
	}
	
	
}
