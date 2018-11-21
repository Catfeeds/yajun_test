package com.wis.mes.production.plan.porder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.service.ToPorderAviBomService;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;

/**
 * @company:上海西信
 *
 * @ClassName: ToPorderAviBomController
 * 
 * @author liuzejun
 *
 * @date 2017年6月1日 上午10:44:18
 * 
 * @Description: 生产序列BOM信息
 */
@Controller
@RequestMapping(value = "/aviBom")
public class ToPorderAviBomController extends BaseController {

	@Autowired
	private ToPorderAviBomService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private ToPorderAviService aviService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap, Integer aviId, HttpServletRequest request) {
		modelMap.addAttribute("aviId", aviId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		Integer plantId = aviService.getPorderPlantIdByAviId(aviId);
		modelMap.addAttribute("params", request.getParameter("params"));
		modelMap.addAttribute("ulocOptions", getUlocOptions(plantId, aviId));
		modelMap.addAttribute("partOptions", getPartOptions(plantId));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/production-plan/porder/bom/avi_bom_list");
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
	public ModelAndView addInput(ModelMap modelMap, Integer aviId) {
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("toPorderAviId", aviId);
		Integer plantId = aviService.getPorderPlantIdByAviId(aviId);
		modelMap.addAttribute("ulocOptions", getUlocOptions(plantId, aviId));
		modelMap.addAttribute("partOptions", getPartOptions(plantId));
		return new ModelAndView("/production-plan/porder/bom/avi_bom_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, ToPorderAviBom bean)
			throws Exception {
		try {
			bean = service.doSave(bean);
			logService.doAddLog("ToPorderAviBom", bean);
			return ActionUtils.handleResult(response);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtils.isNotBlank(errMsg) && errMsg.indexOf("U_TM_PORDER_AVI_BOM_UNIQUE") != -1) {
				throw new BusinessException("U_TM_PORDER_AVI_BOM_UNIQUE");
			} else {
				throw e;
			}
		}
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, Integer aviId, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		Integer plantId = aviService.getPorderPlantIdByAviId(Integer.valueOf(aviId));
		modelMap.addAttribute("ulocOptions", getUlocOptions(plantId, aviId));
		modelMap.addAttribute("partOptions", getPartOptions(plantId));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/production-plan/porder/bom/avi_bom_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletRequest request, HttpServletResponse response, ToPorderAviBom bean)
			throws Exception {
		ToPorderAviBom before = service.findById(bean.getId());
		bean.setToPorderAviId(before.getToPorderAviId());
		try {
			service.doUpdate(bean);
			logService.doUpdateLog("ToPorderAviBom", before, bean);
			return ActionUtils.handleResult(response);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtils.isNotBlank(errMsg) && errMsg.indexOf("U_TM_PORDER_AVI_BOM_UNIQUE") != -1) {
				throw new BusinessException("U_TM_PORDER_AVI_BOM_UNIQUE");
			} else {
				throw e;
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<ToPorderAviBom> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("ToPorderAviBom", list);
		return ActionUtils.handleResult(response);
	}

	private List<DictEntry> getUlocOptions(Integer tmPlantId, Integer aviId) {
		ToPorderAvi avi = aviService.findById(aviId);
		TmUloc uloc = new TmUloc();
		uloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		uloc.setTmPlantId(tmPlantId);
		uloc.setTmLineId(avi.getTmLineId());
		return ulocService.getDictItem(uloc);
	}

	private List<DictEntry> getPartOptions(Integer plantId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<String> typeList = new ArrayList<String>();
		typeList.add(ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED);
		typeList.add(ConstantUtils.ENTRY_CODE_PART_TYPE_PARTS);
		paramMap.put("TYPE", typeList);
		paramMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		paramMap.put("TM_PLANT_ID", plantId.toString());
		return partService.getDictItem(paramMap);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String aviId) throws IOException {

		if (StringUtils.isNotEmpty(aviId)) {
			criteria.getQueryCondition().put("toPorderAviId", aviId);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList("no"));
		final PageResult<ToPorderAviBom> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "工位", "物料", "数量", "顺序号", "是否物料追溯", "是否批次追溯" ,"备注"};
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
