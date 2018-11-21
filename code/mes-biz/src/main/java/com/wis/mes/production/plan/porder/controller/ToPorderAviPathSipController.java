package com.wis.mes.production.plan.porder.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathSipService;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathSipController
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:40:15
 * 
 * @Description: 质检项Controller
 */
@Controller
@RequestMapping(value = "/avi/path/sip")
public class ToPorderAviPathSipController extends BaseController {

	@Autowired
	private ToPorderAviPathSipService service;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap, Integer toPorderAviPathId, String currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		modelMap.addAttribute("params", "queryCondition[toPorderAviPathId]=" + toPorderAviPathId);
		return new ModelAndView("/production-plan/porder/sip/avi_path_sip_list");
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
		return new ModelAndView("/production-plan/porder/sip/avi_path_sip_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, String[] tsParameterId, String[] note,
			Integer toPorderAviPathId) {
		List<ToPorderAviPathSip> beans = service.doSavePathUlocSip(tsParameterId, note, toPorderAviPathId);
		logService.doAddLog("ToPorderAviPathSip", beans);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap, Integer toPorderAviPathId) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id), true));
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		return new ModelAndView("/production-plan/porder/sip/avi_path_sip_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, ToPorderAviPathSip bean) {
		ToPorderAviPathSip before = service.findById(bean.getId());
		Map<String, TsParameter> checkSipISExiame = getSipIsExiameMap(bean.getToPorderAviPathId());
		if (checkSipISExiame.containsKey(before.getTsParameterId().toString())) {
			throw new BusinessException("PATH_ULOC_SIP_UPDATE_ERROR",
					checkSipISExiame.get(before.getTsParameterId().toString()).getCode());
		}
		service.doUpdate(bean);
		logService.doUpdateLog("ToPorderAviPathSip", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<ToPorderAviPathSip> list = service.findAllInIds(deleteIds);
		Map<String, TsParameter> checkSipISExiame = getSipIsExiameMap(list.get(0).getToPorderAviPathId());
		StringBuffer sipErr = new StringBuffer();
		for (ToPorderAviPathSip sip : list) {
			if (checkSipISExiame.containsKey(sip.getTsParameterId().toString())) {
				TsParameter tsParameter = checkSipISExiame.get(sip.getTsParameterId().toString());
				sipErr.append(tsParameter.getCode()).append(",");
			}
		}
		//如果是必检项则不能删除
		if (sipErr.length() > 0) {
			throw new BusinessException("PATH_ULOC_SIP_DELEATE_ERROR", sipErr.deleteCharAt(sipErr.length() - 1));
		}
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("ToPorderAviPathSip", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 质检项列表显示
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/selectSipList")
	public ModelAndView selectSipList(HttpServletRequest request, ModelMap modelMap) {
		return ActionUtils.handleSelectResult(request, modelMap,
				new ModelAndView("/production-plan/porder/magnifier/avi_path_sip_select"));
	}

	/**
	 * 查询质检项
	 * 
	 * @param response
	 * @param criteria
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSipData")
	public JsonActionResult getSipData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("no", "name"));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getEquipmentAndUlocParameter(criteria));
	}

	/**
	 * 查询必检参数
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSipParameterExiame")
	public List<TsParameter> getSipParameterExiame(Integer toPorderAviPathId) {
		return service.getSipParameterExamineAndNotInSip(toPorderAviPathId);
	}

	/**
	 * 查询必检项
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	private Map<String, TsParameter> getSipIsExiameMap(Integer toPorderAviPathId) {
		Map<String, TsParameter> map = new HashMap<String, TsParameter>();
		List<TsParameter> sipParameterExiame = service.getSipParameterExamine(toPorderAviPathId);
		for (TsParameter tsParameter : sipParameterExiame) {
			map.put(tsParameter.getId().toString(), tsParameter);
		}
		return map;
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
		String[] header = new String[] { "参数编号", "参数名称", "参数变量", "参数默认值", "参数表达式", "参数说明", "备注" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
