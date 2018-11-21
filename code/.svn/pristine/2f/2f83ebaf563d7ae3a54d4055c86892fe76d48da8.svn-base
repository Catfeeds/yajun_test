package com.wis.basis.systemadmin.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/dataSource")
public class DataSourceController {

	@RequestMapping(value = "/druid")
	public ModelAndView druid(HttpServletResponse response, ModelMap modelMap) {
		return new ModelAndView("/core/datasource/index");
	}

}
