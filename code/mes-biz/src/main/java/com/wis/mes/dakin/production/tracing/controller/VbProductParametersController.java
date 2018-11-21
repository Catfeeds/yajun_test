package com.wis.mes.dakin.production.tracing.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.mes.dakin.production.tracing.service.VdkProductParametersService;
import com.wis.mes.dakin.production.tracing.vo.ProductParameterVo;
import com.wis.mes.master.parametermanage.service.TmParameterManageService;

import net.sf.json.JSONArray;

/**
 * 
 * @author liuzejun
 * 
 * @desc 参数
 * 
 */
@Controller
@RequestMapping(value = "/vbProductParameters")
public class VbProductParametersController extends BaseController {
	@Autowired
	private VdkProductParametersService dkService;
	@Autowired
	private TmParameterManageService tmParameterManageService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, BootstrapTableQueryCriteria criteria, ModelMap modelMap,
			String backNumber) {
		modelMap.addAttribute("params", request.getParameter("params"));
		modelMap.addAttribute("isDk", request.getParameter("isDk"));
		modelMap.addAttribute("parameterRange",
				JSONArray.fromObject(tmParameterManageService.getParameterRange(backNumber)).toString());
		return new ModelAndView("producttracing/paramer/productparamer_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, String isDk) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "params" }));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		if (criteria.getSort() == null) {
			criteria.setOrderField("isConfig");
		} else {
			criteria.setOrderField(criteria.getSort());
		}
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.DESC : OrderEnum.ASC);
		return ActionUtils.handleListResult(response, dkService.findBy(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids)
			throws IOException, InvalidFormatException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList("backNumber", "machineOfName", "machineName"));
		List<ProductParameterVo> queryProductParameters = dkService
				.queryProductParameters(criteria.getQueryCondition());
		String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
				+ "productTracing" + File.separator + "product_parameters.xlsx";
		Workbook wb = WorkbookFactory.create(new File(templatePath));
		dkService.exportProductTracing(queryProductParameters, wb);
		LoadUtils.setContent(request, response, wb, "产品生产技术参数报表");
		return ActionUtils.handleResult(response);
	}

}
