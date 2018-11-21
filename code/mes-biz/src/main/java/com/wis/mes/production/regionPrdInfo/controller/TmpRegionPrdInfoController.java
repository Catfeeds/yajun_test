package com.wis.mes.production.regionPrdInfo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.production.regionPrdInfo.service.TmpRegionPrdInfoService;

@Controller
@RequestMapping(value = "/tmpRegionPrdInfo")
public class TmpRegionPrdInfoController extends BaseController {

	@Autowired
	private TmpRegionPrdInfoService service;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView hsListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer id) {
		modelMap.addAttribute("regionMarks", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
		modelMap.addAttribute("codeClassOrders", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("machiningSurfaces", entryService.getEntryByTypeCode(ConstantUtils.SM_MACHINING_SURFACE));
		return new ModelAndView("regionprdinfo/region_prd_info_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult hslist(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		ActionUtils.dateConditionDefault(criteria, "createTime", -1);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids) throws IOException {

		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		} else {
			ActionUtils.dateConditionDefault(criteria, "createTime", -1);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		String[] title = new String[] { "日期", "事部", "产线", "班次", "班组", "区域","钣金加工用图号", "稼动时间", "加工时间", "故障时间", "非故障时间", "可动率" };
		service.doExport(request, response, service, "REGION_PRD_INFO", "钣金区域生产信息", title, criteria);
		return ActionUtils.handleResult(response);
	}
}
