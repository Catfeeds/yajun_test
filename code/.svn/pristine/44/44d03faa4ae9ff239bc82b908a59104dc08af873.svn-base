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
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathPart;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathPartService;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathPartController
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:40:41
 * 
 * @Description: 关键件controller
 */
@Controller
@RequestMapping(value = "/avi/path/part")
public class ToPorderAviPathPartController extends BaseController {

	@Autowired
	private ToPorderAviPathPartService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private ToPorderAviPathService aviPathService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap, String currentPageId, Integer toPorderAviPathId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		modelMap.addAttribute("params", "queryCondition[toPorderAviPathId]=" + toPorderAviPathId);
		return new ModelAndView("/production-plan/porder/part/avi_path_part_list");
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
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		Integer tmPlantId = aviPathService.getPorderPlantIdByAviPathId(toPorderAviPathId);
		modelMap.addAttribute("partOptions", getPartDictItem(tmPlantId));
		return new ModelAndView("/production-plan/porder/part/avi_path_part_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, ToPorderAviPathPart bean) {
		ToPorderAviPathPart eg = new ToPorderAviPathPart();
		eg.setTmPartId(bean.getTmPartId());
		eg.setToPorderAviPathId(bean.getToPorderAviPathId());
		if (service.findByEg(eg).size() > 0) {
			throw new BusinessException("PATH_ULOC_PART_UNIQUE_ERROR");
		}

		bean = service.doSave(bean);
		logService.doAddLog("ToPorderAviPathPart", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap, Integer toPorderAviPathId) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("toPorderAviPathId", toPorderAviPathId);
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		Integer tmPlantId = aviPathService.getPorderPlantIdByAviPathId(toPorderAviPathId);
		modelMap.addAttribute("partOptions", getPartDictItem(tmPlantId));
		return new ModelAndView("/production-plan/porder/part/avi_path_part_update");
	}

	private List<DictEntry> getPartDictItem(Integer tmPlantId) {
		TmPart part = new TmPart();
		part.setTmPlantId(tmPlantId);
		Map<String, Object> partMap = new HashMap<String, Object>();
		partMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		partMap.put("TYPE", Arrays.asList(ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED,
				ConstantUtils.ENTRY_CODE_PART_TYPE_PARTS));
		partMap.put("TM_PLANT_ID", tmPlantId.toString());
		return partService.getDictItem(partMap);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, ToPorderAviPathPart bean) {
		ToPorderAviPathPart before = service.findById(bean.getId());
		ToPorderAviPathPart eg = new ToPorderAviPathPart();
		eg.setTmPartId(bean.getTmPartId());
		eg.setToPorderAviPathId(bean.getToPorderAviPathId());
		List<ToPorderAviPathPart> findByEg = service.findByEg(eg);
		for (ToPorderAviPathPart toPorderAviPathPart : findByEg) {
			if (!toPorderAviPathPart.getId().equals(bean.getId())) {
				throw new BusinessException("PATH_ULOC_PART_UNIQUE_ERROR");
			}
		}
		service.doUpdate(bean);
		logService.doUpdateLog("ToPorderAviPathPart", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<ToPorderAviPathPart> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("ToPorderAviPathPart", list);
		return ActionUtils.handleResult(response);
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
		String[] header = new String[] { "物料", "数量", "顺序号", "是否追溯", "是否批次追溯","备注" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
