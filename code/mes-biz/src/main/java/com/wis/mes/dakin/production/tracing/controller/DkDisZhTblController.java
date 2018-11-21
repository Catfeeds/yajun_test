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
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.dakin.production.tracing.service.DkDisZhTblService;
import com.wis.mes.dakin.production.tracing.vo.ProductPartVo;

/**
 * 
 * @author liuzejun
 * 
 * @Desc 物料绑定数据
 */
@Controller
@RequestMapping(value = "/dkDisZhTbl")
public class DkDisZhTblController extends BaseController {
	@Autowired
	private DkDisZhTblService service;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("params", request.getParameter("params"));
		modelMap.addAttribute("isDk", request.getParameter("isDk"));
		return new ModelAndView("producttracing/part/productpart_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, String isDk) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
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
		List<ProductPartVo> queryProductPart = service.queryProductPart(criteria.getQueryCondition());
		String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
				+ "productTracing" + File.separator + "product_part.xlsx";
		Workbook wb = WorkbookFactory.create(new File(templatePath));
		service.exportProductPart(queryProductPart, wb);
		LoadUtils.setContent(request, response, wb, "关键件信号查询");
		return ActionUtils.handleResult(response);
	}
}
