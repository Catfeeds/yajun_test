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
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathUlocSip;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocSipService;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/path/uloc/sip")
public class TmPathUlocSipController extends BaseController {

	@Autowired
	private TmPathUlocSipService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TsParameterService parameterService;
	@Autowired
	private TmPathService pathService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, Integer tmPathUlocId) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/path/sip/path_uloc_sip_list");
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
		return new ModelAndView("/master/path/sip/path_uloc_sip_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, String[] tsParameterId, String[] note,
			Integer tmPathUlocId) {
		checkPathIsComplete(tmPathUlocId);
		List<TmPathUlocSip> beans = service.doSavePathUlocSip(tsParameterId, note, tmPathUlocId);
		logService.doAddLog("TmPathUlocSip", beans);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap, Integer tmPathUlocId) {
		TmPathUlocSip bean = service.findById(Integer.valueOf(id));
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("tsParameter", parameterService.findById(bean.getTsParameterId()));
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		return new ModelAndView("/master/path/sip/path_uloc_sip_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmPathUlocSip bean) {
		TmPathUlocSip before = service.findById(bean.getId());
		checkPathIsComplete(bean.getTmPathUlocId());
		Map<String, TsParameter> checkSipISExiame = getSipIsExiameMap(bean.getTmPathUlocId(), null);
		if (checkSipISExiame.containsKey(before.getTsParameterId().toString())
				&& !before.getTsParameterId().equals(bean.getTsParameterId())) {
			throw new BusinessException("PATH_ULOC_SIP_UPDATE_ERROR",
					checkSipISExiame.get(before.getTsParameterId().toString()).getCode());
		}
		service.doUpdate(bean);
		logService.doUpdateLog("TmPathUlocSip", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmPathUlocSip> list = service.findAllInIds(deleteIds);
		Map<String, TsParameter> checkSipISExiame = getSipIsExiameMap(list.get(0).getTmPathUlocId(), null);
		StringBuffer sipErr = new StringBuffer();
		for (TmPathUlocSip sip : list) {
			checkPathIsComplete(sip.getTmPathUlocId());
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
		logService.doDeleteLog("TmPathUlocSip", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/selectSipList")
	public ModelAndView selectSipList(HttpServletRequest request, ModelMap modelMap, Integer tmPathUlocId,
			Integer tmUlocId, String tsParameterIds) {
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
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/master/path/magnifier/sip_select"));
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
		return ActionUtils.handleListResult(response, service.getEquipmentAndUlocParameter(criteria));
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

	/**
	 * 查询必检参数
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSipParameterExiame")
	public List<TsParameter> getSipParameterExiame(Integer tmPathUlocId, Integer tmUlocId, String tsParameterIds) {
		return service.getSipParameterExamineAndNotInSip(tmPathUlocId, tmUlocId,
				tsParameterIds == null ? null : tsParameterIds.split(","));
	}

	@ResponseBody
	@RequestMapping(value = "/getSipIsExiameList")
	public List<TsParameter> getSipIsExiameList(Integer tmUlocId) {
		List<TsParameter> sipParameterExiame = service.getSipParameterExamine(null, tmUlocId);
		return sipParameterExiame;
	}

	/**
	 * 查询必检项
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	private Map<String, TsParameter> getSipIsExiameMap(Integer tmPathUlocId, Integer tmUlocId) {
		Map<String, TsParameter> map = new HashMap<String, TsParameter>();
		List<TsParameter> sipParameterExiame = service.getSipParameterExamine(tmPathUlocId, tmUlocId);
		for (TsParameter tsParameter : sipParameterExiame) {
			map.put(tsParameter.getId().toString(), tsParameter);
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String tmPathUlocId) throws IOException {
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "质检项编号", "质检项名称", "质检项表达式", "质检项变量", "质检项说明", "备注" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/graph_listInput")
	public ModelAndView graph_listInput(HttpServletRequest request, ModelMap modelMap, Integer tmPathUlocId,
			Integer tmUlocId, String rectSeq) {
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("rectSeq", rectSeq);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		return new ModelAndView("/master/path/path_graph/sip/path_uloc_sip_graph_list");
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
		return new ModelAndView("/master/path/path_graph/sip/path_uloc_sip_graph_add");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_add")
	public JsonActionResult graph_add(HttpServletResponse response, String[] tsParameterId, String[] note,
			Integer tmPathUlocId) {
		if (tmPathUlocId != null) {
			checkPathIsComplete(tmPathUlocId);
			List<TmPathUlocSip> beans = service.doSavePathUlocSip(tsParameterId, note, tmPathUlocId);
			logService.doAddLog("TmPathUlocSip", beans);
			return ActionUtils.handleResult(response);
		} else {
			return ActionUtils.handleResult(service.getPathUlocSipData(tsParameterId, note, tmPathUlocId));
		}

	}

	@RequestMapping(value = "/graph_updateInput")
	public ModelAndView graph_updateInput(String id, TmPathUlocSip bean, ModelMap modelMap, Integer tmPathUlocId,
			Integer tmUlocId, String tsParameterIds) {
		modelMap.addAttribute("bean", bean);
		if (tmPathUlocId != null) {
			bean = service.findById(Integer.valueOf(id));
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute("rectSeq", bean.getRectSeq());
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("tsParameterIds", tsParameterIds);
		modelMap.addAttribute("tsParameter", parameterService.findById(bean.getTsParameterId()));
		modelMap.addAttribute("tmPathUlocId", tmPathUlocId);
		return new ModelAndView("/master/path/path_graph/sip/path_uloc_sip_graph_update");
	}

	@ResponseBody
	@RequestMapping(value = "/graph_update")
	public JsonActionResult graph_update(HttpServletResponse response, TmPathUlocSip bean, Integer tmPathUlocId,
			Integer tmUlocId, Integer beforeTsParameterId) {
		if (tmPathUlocId != null) {
			TmPathUlocSip before = service.findById(bean.getId());
			checkPathIsComplete(bean.getTmPathUlocId());
			Map<String, TsParameter> checkSipISExiame = getSipIsExiameMap(bean.getTmPathUlocId(), null);
			if (checkSipISExiame.containsKey(before.getTsParameterId().toString())
					&& !before.getTsParameterId().equals(bean.getTsParameterId())) {
				throw new BusinessException("PATH_ULOC_SIP_UPDATE_ERROR",
						checkSipISExiame.get(before.getTsParameterId().toString()).getCode());
			}
			service.doUpdate(bean);
			logService.doUpdateLog("TmPathUlocSip", before, bean);
			return ActionUtils.handleResult(response);
		} else {
			Map<String, TsParameter> checkSipISExiame = getSipIsExiameMap(null, tmUlocId);
			if (checkSipISExiame.containsKey(beforeTsParameterId.toString())
					&& !beforeTsParameterId.equals(bean.getTsParameterId())) {
				throw new BusinessException("PATH_ULOC_SIP_UPDATE_ERROR",
						checkSipISExiame.get(beforeTsParameterId.toString()).getCode());
			}
			bean.setParameter(parameterService.findById(bean.getTsParameterId()));
			return ActionUtils.handleResult(bean);
		}

	}
}
