package com.wis.mes.production.record.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.wis.basis.common.controller.BaseController;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.production.record.service.TpRecordUlocNcService;

@Controller
@RequestMapping(value = "/ncTypeStatistic")
public class NcTypeStatisticController extends BaseController {

    @Autowired
    private TpRecordUlocNcService recordUlocNcService;

    @RequestMapping(value = "/listInput")
    public ModelAndView listInput(final HttpServletRequest request, final QueryCriteria criteria,
            final ModelMap modelMap) {
        return new ModelAndView("/production-record/nc/nc_type_statistic");
    }

    @ResponseBody
    @RequestMapping(value = "/getReportData")
    public String getReportData(final String sn, final String type, final String time, final String startTime,
            final String endTime, final String ncIds) {
        final Gson json = new Gson();
        try {
            return json.toJson(recordUlocNcService.getReportData(sn, type, time, startTime, endTime, ncIds));
        } catch (final Exception e) {
            return json.toJson("ERROR");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getNext")
    public String getNext(final String type, final Integer pageNow) {
        return recordUlocNcService.getNext(type, pageNow);
    }

}
