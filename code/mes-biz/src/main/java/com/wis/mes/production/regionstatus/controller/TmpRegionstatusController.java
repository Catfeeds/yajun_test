package com.wis.mes.production.regionstatus.controller;

import java.io.IOException;
import java.util.List;

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
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.production.regionstatus.entity.TmpRegionstatus;
import com.wis.mes.production.regionstatus.service.TmpRegionstatusService;

@Controller
@RequestMapping(value = "/regionStatus")
public class TmpRegionstatusController extends BaseController {

	@Autowired
	private TmpRegionstatusService service;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView hsListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer id) {
		modelMap.addAttribute("regionMarks", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
		modelMap.addAttribute("circulateStates", entryService.getEntryByTypeCode(ConstantUtils.SM_CIRCULATE_STATE));
		return new ModelAndView("regionstatus/region_status_list");
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
		PageResult<TmpRegionstatus> findBy = service.findBy(criteria);
		List<TmpRegionstatus> content = findBy.getContent();
		for (TmpRegionstatus bean : content) {
			if (bean.getCreateTime() != null && bean.getUpdateTime() != null) {
				bean.setDuration(DateUtils.getDayFull(bean.getCreateTime(), bean.getUpdateTime()));
			}
		}
		return ActionUtils.handleListResult(response, findBy);
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
		String[] title = new String[] { "日期", "事部", "产线", "班次", "班组", "区域", "运行状态", "开始时间", "结束时间", "时长"};
		service.doExport(request, response, service, "REGION_STATUS", "钣金区域状态信息", title, criteria);
		return ActionUtils.handleResult(response);
	}
}
