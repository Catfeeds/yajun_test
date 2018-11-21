package com.wis.mes.dakin.report.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.mes.dakin.report.service.FinMovableRateDetailsService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;

@Controller
@RequestMapping(value = "/finMovableRateDetails")
public class FinMovableRateDetailsController extends BaseController {
	@Autowired
	private FinMovableRateDetailsService finMovableRateDetailsService;
	@Autowired
	private TmWorktimeService workTimeService;
	@Autowired
	private GlobalConfigurationService configurationService;

	@RequestMapping(value = "/listInput")
	public ModelAndView stationAnomalyReport() {
		return new ModelAndView("report/fin/fin_movable_rate_details_list");
	}

	@RequestMapping(value = "/getMovateRateDetails")
	public ModelAndView getMovateRateDetails() {
		return new ModelAndView("report/fin/fin_movable_rate_line");
	}

	@ResponseBody
	@RequestMapping(value = "/getFinMovableRateDetailsInfo")
	public Map<String, String> getFinMovableRateInfo(String shiftNo, String createTimeStart, String createTimeEnd) {
		Map<String, String> result = finMovableRateDetailsService.getFinMovableRateWhiteAndNightData(shiftNo,
				createTimeStart, createTimeEnd);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getDetailsTimeInfo")
	public Map<String, String> getDetailsTimeInfo(String shiftNo, String createTime) {
		Map<String, String> result = finMovableRateDetailsService.getFaultAndNoFaultTime(shiftNo, createTime);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getFaultAndNoFaultInfo")
	public Map<String, String> getFaultAndNoFaultInfo(String shiftNo, String date, int firgure) {
		String lineNo = configurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
		String workTimeShift = null;
		String shiftNoZH_CN= "";
		if ("1".equals(shiftNo)) {
			workTimeShift = "MORNINGSHIFT";
			shiftNoZH_CN = "白班";
		} else if ("2".equals(shiftNo)) {
			workTimeShift = "NIGHTSHIFT";
			shiftNoZH_CN = "晚班";
		} else {
			TmWorktime dateWorkTime = workTimeService.getDateWorkTime(new Date(), lineNo);
			if (dateWorkTime != null) {
				workTimeShift = dateWorkTime.getShiftno();
				if (ConstantUtils.SHIFT_MORNING.equals(workTimeShift)) {
					shiftNo = "1";
					shiftNoZH_CN = "白班";
				} else {
					shiftNo = "2";
					shiftNoZH_CN = "晚班";
				}
			} else {
				workTimeShift = "1";
			}
		}
		TmWorktime workTime = workTimeService.getWorkTimeByDayAndShiftno(date, lineNo, workTimeShift);
		String startTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";
		if (workTime != null) {
			startTime = workTime.getStartTime() + ":00";
			endTime = workTime.getEndTime() + ":59";
		}
		Map<String, String> movableRateAndCalculate = finMovableRateDetailsService.getMovableRateAndCalculate(startTime,
				endTime, firgure, shiftNo);
		movableRateAndCalculate.put("subtitle", date+" " +shiftNoZH_CN);
		return movableRateAndCalculate;
	}
}
