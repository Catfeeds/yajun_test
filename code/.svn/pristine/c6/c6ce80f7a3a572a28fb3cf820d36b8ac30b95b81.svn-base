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
@RequestMapping(value = "/productQuery")
public class ProductQueryController extends BaseController {
    @Autowired
    private TpRecordService recordService;

    private static final String ERROR = "ERROR";

    @RequestMapping(value = "/listInput")
    public ModelAndView listInput(final HttpServletRequest request, final QueryCriteria criteria,
            final ModelMap modelMap) {
        return new ModelAndView("/production-record/product_query");
    }

    @ResponseBody
    @RequestMapping(value = "/getData")
    public String getData(final String sn, final String partNo, final String partName, final String porder) {
        final Gson json = new Gson();
        try {
            return json.toJson(recordService.findByCondition(sn, partNo, partName, porder));
        } catch (final Exception e) {
            return json.toJson(ERROR);
        }
    }
}
