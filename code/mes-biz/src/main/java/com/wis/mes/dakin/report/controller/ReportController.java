package com.wis.mes.dakin.report.controller;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.mes.dakin.report.service.ReportService;
import com.wis.mes.dakin.report.service.StationDataAnalysisService;

@Controller
@RequestMapping(value = "/report")
public class ReportController extends BaseController {

	@Autowired
	private ReportService service;
	@Autowired
	private StationDataAnalysisService stationDataAnalysisService;

	/**
	 * 故障机报表 15
	 * 
	 * @return
	 */
	@RequestMapping(value = "/faultMachine")
	public ModelAndView faultMachine() {
		return new ModelAndView("/report/fault_machine_list");
	}

	/**
	 * 故障机报表 数据
	 * 
	 * @param type
	 * @param createTimeStart
	 *            开始时间
	 * @param createTimeEnd
	 *            结束时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/faultMachineData")
	public JsonActionResult faultMachineData(String type, String createTimeStart, String createTimeEnd) {
		if ("NG_SHIFTNO_REALALATION".equals(type)) {
			return ActionUtils.handleResult(service.ngAndShiftNoRealationReport(createTimeStart, createTimeEnd));
		} else if ("NG_COUNT_REALATION".equals(type)) {
			return ActionUtils.handleResult(service.ngAndCountRealationReport(createTimeStart, createTimeEnd));
		} else if ("NG_INFO_ORIGIN_COUNT".equals(type)) {
			return ActionUtils.handleResult(service.ngInfoOriginReport(createTimeStart, createTimeEnd));
		} else if ("NG_INFO_ORIGIN_COUNT_PIE".equals(type)) {
			return ActionUtils.handleResult(service.ngInfoOriginReportPIE(createTimeStart, createTimeEnd));
		}
		return null;
	}

	/**
	 * 工位异常报表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/stationAnomalyReport")
	public ModelAndView stationAnomalyReport() {
		return new ModelAndView("/report/station_anomaly_report");
	}

	/**
	 * 工位异常报表 数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/stationAnomalyData")
	public JsonActionResult stationAnomalyData(String type, String createTimeStart, String createTimeEnd) {
		if ("ULOC_STATUS_TIME_COUNT".equals(type)) {
			return ActionUtils.handleResult(service.ulocStatusTimeCountReport(createTimeStart, createTimeEnd));
		} else if ("ULOC_STATUS_COUNT_DETAIL".equals(type)) {
			return ActionUtils.handleResult(service.ulocStatusCountAndDetailReport(createTimeStart, createTimeEnd));
		} else if ("ULOC_STATUS_ABNORMAL_SOURCE_COUNT_MINTUE_E".equals(type)) {
			return ActionUtils
					.handleResult(service.ulocStatusAbnormalSourceCountAndMintue("E", createTimeStart, createTimeEnd));
		} else if ("ULOC_STATUS_ABNORMAL_SOURCE_COUNT_MINTUE_L".equals(type)) {
			return ActionUtils
					.handleResult(service.ulocStatusAbnormalSourceCountAndMintue("L", createTimeStart, createTimeEnd));
		} else if ("ULOC_STATUS_ABNORMAL_SOURCE_COUNT_MINTUE_W".equals(type)) {
			return ActionUtils
					.handleResult(service.ulocStatusAbnormalSourceCountAndMintue("W", createTimeStart, createTimeEnd));
		} else if ("ULOC_STATUS_YEAR_DENSITY".equals(type)) {
			String year = "";
			if (StringUtils.isNotEmpty(createTimeStart)) {
				year = createTimeStart.split("-")[0];
			} else {
				Calendar cal = Calendar.getInstance();
				year = String.valueOf(cal.get(Calendar.YEAR));
			}
			createTimeStart=year+"-01-01 00:00";
			createTimeEnd=year+"-12-31 23:59";
			return ActionUtils.handleResult(service.ulocStatusYearDensity(year, createTimeStart, createTimeEnd));
		}
		return null;
	}

	// 工位数据分析报表
	@RequestMapping(value = "/ulocDataReport")
	public ModelAndView ulocDataReport() {
		return new ModelAndView("/report/uloc_data_report_list");
	}

	// 工位数据分析报表数据
	@ResponseBody
	@RequestMapping(value = "/getUlocData")
	public JsonActionResult getUlocData(HttpServletResponse response, String type, String createTimeStart,
			String createTimeEnd) {
		JsonActionResult result = new JsonActionResult();
		result.setData(stationDataAnalysisService.stationDataAnalysisData(type, createTimeStart, createTimeEnd));
		result.setSuccess(true);
		return result;
	}

	@RequestMapping(value = "/pmcEcpReportInput")
	public ModelAndView pmcEcpReportInput() {
		return new ModelAndView("/report/pmc_ecp_report_list");
	}

	@ResponseBody
	@RequestMapping(value = "/pmcEcpReportData")
	public JsonActionResult pmcEcpReportData(HttpServletResponse response, String type, String timeFormate,
			String createTimeStart, String createTimeEnd) {
		if ("pillarTyearEC".equals(type)) {
			if (StringUtils.isNotEmpty(createTimeStart)) {
				String year = createTimeStart.substring(0, 4);
				createTimeStart = year + "-01";
				createTimeEnd = year + "-12";
			} else {
				Calendar calendar = Calendar.getInstance();
				int i = calendar.get(Calendar.YEAR);
				createTimeStart = i + "-01";
				createTimeEnd = i + "-12";
			}
			return ActionUtils.handleResult(service.energReportBar(timeFormate, createTimeStart, createTimeEnd));
		} else if ("pillarMonthEC".equals(type)) {
			return ActionUtils.handleResult(service.energReportBar(timeFormate, createTimeStart, createTimeEnd));
		} else if ("pieDayEC".equals(type)) {
			return ActionUtils.handleResult(service.energReportBar(timeFormate, createTimeStart, createTimeEnd));
		} else {
			if (StringUtils.isNotEmpty(createTimeStart)) {
				String year = createTimeStart.substring(0, 4);
				createTimeStart = year + "-01";
				createTimeEnd = year + "-12";
			} else {
				Calendar calendar = Calendar.getInstance();
				int i = calendar.get(Calendar.YEAR);
				createTimeStart = i + "-01";
				createTimeEnd = i + "-12";
			}
			return ActionUtils.handleResult(service.energReportPic(timeFormate, createTimeStart, createTimeEnd));
		}
	}
}
