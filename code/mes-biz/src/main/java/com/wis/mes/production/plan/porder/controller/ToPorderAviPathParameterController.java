package com.wis.mes.production.plan.porder.controller;

import java.io.IOException;
import java.util.Arrays;
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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathParameterService;

/**
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathParameterController
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:41:00
 * 
 * @Description: 参数Controller
 */
@Controller
@RequestMapping(value = "/avi/path/parameter")
public class ToPorderAviPathParameterController extends BaseController {

	@Autowired
	private ToPorderAviPathParameterService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap, Integer toPorderAviPathId, String currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		modelMap.addAttribute("params", "queryCondition[toPorderAviPathId]=" + toPorderAviPathId);
		return new ModelAndView("/production-plan/porder/parameter/avi_path_parameter_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer toPorderAviPathId) {
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		return new ModelAndView("/production-plan/porder/parameter/avi_path_parameter_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, Integer toPorderAviPathId, String[] tsParameterId,
			String[] note, String[] isRequeired, String[] enabled) {
		List<ToPorderAviPathParameter> beans = service.doSaveAviPathParameter(toPorderAviPathId, tsParameterId, note,
				isRequeired, enabled);
		logService.doAddLog("ToPorderAviPathParameter", beans);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap, Integer toPorderAviPathId) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id), true));
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/production-plan/porder/parameter/avi_path_parameter_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, ToPorderAviPathParameter bean) {
		ToPorderAviPathParameter before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("ToPorderAviPathParameter", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<ToPorderAviPathParameter> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("ToPorderAviPathParameter", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 参数列表
	 * 
	 * @param request
	 * @param modelMap
	 * @param toPorderAviPathId
	 * @param currentPageId
	 * @return
	 */
	@RequestMapping(value = "/selectParameterList")
	public ModelAndView selectParameterList(HttpServletRequest request, ModelMap modelMap) {
		return ActionUtils.handleSelectResult(request, modelMap,
				new ModelAndView("/production-plan/porder/magnifier/avi_path_parameter_select"));
	}

	/**
	 * 获取参数数据
	 * 
	 * @param response
	 * @param criteria
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getParameterData")
	public JsonActionResult getParameterData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("no", "name"));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getParameterPageResult(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids, String toPorderAviPathId) throws IOException {

		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.getQueryCondition().put("toPorderAviPathId", toPorderAviPathId);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "参数编号", "参数名称", "参数变量", "参数默认值", "参数表达式", "参数说明", "是否必填", "启用", "备注" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
