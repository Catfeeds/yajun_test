package com.wis.mes.master.path.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathUlocParameter;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocParameterService;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/path/uloc/parameter")
public class TmPathUlocParameterController extends BaseController {

	@Autowired
	private TmPathUlocParameterService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPathService pathService;
	
	@Autowired
	private TmEquipmentService tmEquipmentService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap,
			Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/path/uloc/path_uloc_parameter_list");
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
	public ModelAndView addInput(ModelMap modelMap, Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/path/parameter/path_uloc_parameter_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, Integer tmPathUlocId, String[] tsParameterId,
			String[] note, String[] isRequeired, String[] enabled) {
		checkPathIsComplete(tmPathUlocId);
		List<TmPathUlocParameter> TmPathUlocParameters = service.doSavePathUlocParameter(tmPathUlocId, tsParameterId,
				note, isRequeired, enabled);
		logService.doAddLog("TmPathUlocParameter", TmPathUlocParameters);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer tmPathUlocId, String id, ModelMap modelMap) {
		TmPathUlocParameter bean = service.findById(Integer.valueOf(id));
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
	//	modelMap.addAttribute("tsParameter", parameterService.findById(bean.getTsParameterId()));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/path/parameter/path_uloc_parameter_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmPathUlocParameter bean) {
		TmPathUlocParameter before = service.findById(bean.getId());
		checkPathIsComplete(bean.getTmPathUlocId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmPathUlocParameter", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmPathUlocParameter> list = service.findAllInIds(deleteIds);
		//zhuzw 2018/3/22 改功能暂时不需要
//		for (TmPathUlocParameter parameter : list) {
//			checkPathIsComplete(parameter.getTmPathUlocId());
//		}
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmPathUlocParameter", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/selectParameterList")
	public ModelAndView selectParameterList(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap,
			Integer tmPathUlocId, Integer tmUlocId, String tsParameterIds) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		if (StringUtil.isNotBlank(request.getParameter("params"))) {
			modelMap.addAttribute("params", request.getParameter("params"));
		} else {
			if (tmPathUlocId == null) {
				modelMap.addAttribute("params", "tmUlocId=" + tmUlocId + "&tsParameterIds=" + tsParameterIds);
			} else {
				modelMap.addAttribute("params", "tmPathUlocId=" + tmPathUlocId);
			}
		}
		return ActionUtils.handleSelectResult(request, modelMap,
				new ModelAndView("/master/path/magnifier/parameter_select"));
	}

	/**
	 * 获取参数列表
	 * 
	 * @param response
	 * @param criteria
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getParameterData")
	public JsonActionResult getParameterData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, Integer tmPathUlocId, Integer tmUlocId, String tsParameterIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (tmPathUlocId != null) {
			param.put("tmPathUlocId", tmPathUlocId.toString());
		}
		if (tmUlocId != null) {
			param.put("tmUlocId", tmUlocId.toString());
		}
		if (StringUtil.isNotBlank(tsParameterIds)) {
			param.put("tsParameterIds", tsParameterIds.split(","));
		}
		criteria.getQueryCondition().putAll(param);
		criteria.setFuzzyQueryFileds(Arrays.asList("no", "name"));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getParameterPageResult(criteria));
	}

	/**
	 * 检查工艺路径是否已经维护完成
	 * 
	 * @param tmPathUlocId
	 */
	private void checkPathIsComplete(Integer tmPathUlocId) {
		TmPath path = pathService.getByPathUlocId(tmPathUlocId);
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(path.getStatus())) {
			throw new BusinessException("PATH_ULOC_DELETE_ERROR");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String tmPathUlocId) throws IOException {
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "参数编号", "参数名称", "参数变量", "参数默认值", "参数说明", "是否必填", "启用", "备注" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/graph_listInput")
	public ModelAndView graph_listInput(HttpServletRequest request, ModelMap modelMap, Integer tmPathUlocId,
			String rectSeq, Integer tmUlocId, String tsParameterIds) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("rectSeq", rectSeq);
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("tsParameterIds", tsParameterIds);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		return new ModelAndView("/master/path/path_graph/parameter/path_uloc_parameter_list_graph");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_list")
	public JsonActionResult graph_list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			Integer tmPathUlocId) {
		if (tmPathUlocId == null) {
			return ActionUtils.handleResult(response);
		}
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId.toString());
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/graph_addInput")
	public ModelAndView graph_addInput(ModelMap modelMap, Integer tmPathUlocId, String rectSeq, Integer tmUlocId,
			String tsParameterIds) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("rectSeq", rectSeq);
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("tsParameterIds", tsParameterIds);
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/path/path_graph/parameter/path_uloc_parameter_add_graph");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_add")
	public JsonActionResult graph_add(HttpServletResponse response, Integer tmPathUlocId, String[] tsParameterId,
			String[] note, String[] isRequeired, String[] enabled, String rectSeq) {
		if (tmPathUlocId != null) {
			checkPathIsComplete(tmPathUlocId);
			List<TmPathUlocParameter> TmPathUlocParameters = service.doSavePathUlocParameter(tmPathUlocId,
					tsParameterId, note, isRequeired, enabled);
			logService.doAddLog("TmPathUlocParameter", TmPathUlocParameters);
			return ActionUtils.handleResult(response);
		} else {
			List<TmPathUlocParameter> pathUlocParameterData = service.getPathUlocParameterData(tmPathUlocId,
					tsParameterId, note, isRequeired, enabled, rectSeq);
			return ActionUtils.handleResult(pathUlocParameterData);
		}
	}

	@RequestMapping(value = "/graph_updateInput")
	public ModelAndView graph_updateInput(Integer tmPathUlocId, TmPathUlocParameter bean, String id, ModelMap modelMap,
			String tsParameterIds, Integer tmUlocId) {
		modelMap.addAttribute("bean", bean);
		if (tmPathUlocId != null) {
			bean = service.findById(Integer.valueOf(id));
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute("rectSeq", bean.getRectSeq());
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("tsParameterIds", tsParameterIds);
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		//modelMap.addAttribute("tsParameter", parameterService.findById(bean.getTsParameterId()));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/path/path_graph/parameter/path_uloc_parameter_update_graph");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_update")
	public JsonActionResult graph_update(HttpServletResponse response, TmPathUlocParameter bean, Integer tmPathUlocId) {
		if (tmPathUlocId != null) {
			TmPathUlocParameter before = service.findById(bean.getId());
			checkPathIsComplete(bean.getTmPathUlocId());
			service.doUpdate(bean);
			logService.doUpdateLog("TmPathUlocParameter", before, bean);
			return ActionUtils.handleResult(response);
		} else {
			//bean.setParameter(parameterService.findById(bean.getTsParameterId()));
			return ActionUtils.handleResult(bean);
		}

	}
	
	
	/**
	 * 获取参数列表
	 * 
	 * @param response
	 * @param criteria
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPathUlocParamListData")
	public JsonActionResult getPathUlocParamListData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, Integer tmPathUlocId, Integer tmUlocId, String tsParameterIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (tmPathUlocId != null) {
			param.put("tmPathUlocId", tmPathUlocId.toString());
		}
		criteria.getQueryCondition().putAll(param);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		return ActionUtils.handleListResult(response, service.pagePathUlocParamList(criteria));
	}
	
	
	@RequestMapping(value = "/addParamsInput")
	public ModelAndView addParamsInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap,
			Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("params", request.getParameter("params"));
		TmEquipment equipment=new TmEquipment();
		equipment.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("tmEquipment", tmEquipmentService.getDictItem(equipment));
		return new ModelAndView("/master/path/uloc/path_uloc_parameter_add");
	}
	
	
	/**
	 * 获取参数列表
	 * 
	 * @param response
	 * @param criteria
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEquipmentByParameterData")
	public JsonActionResult getEquipmentByParameterData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap, Integer tmPathUlocId, Integer tmUlocId, String tsParameterIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (tmPathUlocId != null) {
			param.put("tmPathUlocId", tmPathUlocId.toString());
		}
		criteria.getQueryCondition().putAll(param);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		return ActionUtils.handleListResult(response, service.pageEquipmentByParameterList(criteria));
	}
	
	
	/**
	 * zhuzw 2018/3/21 
	 * 工艺路径站点新增参数
	 * @param response
	 * @param tmPathUlocId
	 * @param tmPathUlocIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addPathUlocParam")
	public JsonActionResult addPathUlocParam(HttpServletResponse response, Integer tmPathUlocId, String tmPathUlocIds) {
		List<TmPathUlocParameter> TmPathUlocParameters = service.doSavePathUlocParameterNew(tmPathUlocId, getStringIds(tmPathUlocIds));
		logService.doAddLog("TmPathUlocParameter", TmPathUlocParameters);
		return ActionUtils.handleResult(response);
	}

}
