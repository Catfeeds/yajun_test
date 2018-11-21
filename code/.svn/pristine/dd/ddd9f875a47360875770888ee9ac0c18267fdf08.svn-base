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
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.production.record.service.TpRecordUlocNcService;

@Controller
@RequestMapping(value = "/ncQuery")
public class NcQueryController extends BaseController {

    @Autowired
    private TmNcService ncService;
    @Autowired
    private TmNcGroupService ncGroupService;
    @Autowired
    private TpRecordUlocNcService recordUlocNcService;

    private static final String ERROR = "ERROR";

    @RequestMapping(value = "/listInput")
    public ModelAndView listInput(final HttpServletRequest request, final QueryCriteria criteria,
            final ModelMap modelMap) {
        return new ModelAndView("/production-record/nc/nc_query");
    }

    @ResponseBody
    @RequestMapping(value = "/getData")
    public String getData(final String sn, final Integer ncId, final Integer ncGroupId, final String startTime,
            final String endTime) {
        final Gson json = new Gson();
        try {
            return json.toJson(recordUlocNcService.findByCondition(sn, ncId, ncGroupId, startTime, endTime));
        } catch (final Exception e) {
            return json.toJson(ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/initNc")
    public String initNc(final Integer ncGroupId) {
        final Gson json = new Gson();
        try {
            return json.toJson(ncService.findByNcGroupId(ncGroupId));
        } catch (final Exception e) {
            return json.toJson(ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/initNcGroup")
    public String initNcGroup() {
        final Gson json = new Gson();
        try {
            return json.toJson(ncGroupService.findAll());
        } catch (final Exception e) {
            return json.toJson(ERROR);
        }
    }
}
