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
import com.wis.mes.production.record.service.TpRecordService;

@Controller
@RequestMapping(value = "/productStatistic")
public class ProductStatisticController extends BaseController {

    @Autowired
    private TpRecordService recordService;

    private static final String ERROR = "ERROR";

    @RequestMapping(value = "/listInput")
    public ModelAndView listInput(final HttpServletRequest request, final QueryCriteria criteria,
            final ModelMap modelMap) {
        return new ModelAndView("/production-record/product_statistic");
    }

    @ResponseBody
    @RequestMapping(value = "/getReportData")
    public String getReportData(final String sn, final String time, final String startTime, final String endTime,
            final String partIds) {
        final Gson json = new Gson();
        try {
            return json.toJson(recordService.getReportData(sn, time, startTime, endTime, partIds));
        } catch (final Exception e) {
            return json.toJson(ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getNext")
    public String getNext(final Integer pageNow) {
        return recordService.getNext(pageNow);
    }

}
