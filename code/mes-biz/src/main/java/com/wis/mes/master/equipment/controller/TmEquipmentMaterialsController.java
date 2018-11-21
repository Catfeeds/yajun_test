package com.wis.mes.master.equipment.controller;

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
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.equipment.entity.TmEquipmentMaterials;
import com.wis.mes.master.equipment.service.TmEquipmentMaterialsService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.plant.service.TmPlantService;

@Controller
@RequestMapping(value = "/equipmentMaterials")
public class TmEquipmentMaterialsController extends BaseController {

	@Autowired
	private TmEquipmentMaterialsService service;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmEquipmentService equipmentService;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, Integer tmEquipmentId, ModelMap modelMap) {
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		return new ModelAndView("/master/equipment/materials/materials_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMapInteger, Integer tmEquipmentId) {
		criteria.setFuzzyQueryFileds(Arrays.asList("equipment.name", "part.nameCn", "note"));
		criteria.setQueryRelationEntity(true);
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId.toString());
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/partListInput")
	public ModelAndView partListInput(HttpServletRequest request, ModelMap modelMap, Integer tmEquipmentId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("typeOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE));
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return ActionUtils.handleSelectResult(request, modelMap,
				new ModelAndView("/master/equipment/materials/materials_part_list"));
	}

	@ResponseBody
	@RequestMapping(value = "/getPartList")
	public JsonActionResult getPartList(final HttpServletResponse response, final BootstrapTableQueryCriteria criteria,
			final ModelMap modelMap, Integer tmEquipmentId) {
		TmEquipment tmEquipment = new TmEquipment();
		tmEquipment.setId(tmEquipmentId);
		List<TmEquipment> equipment = equipmentService.findByEg(tmEquipment);
		criteria.getQueryCondition().put("tmPlantId", equipment.get(0).getTmPlantId());
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "nameCn" }));
		criteria.getQueryCondition().put("type", ConstantUtils.ENTRY_CODE_PART_TYPE_CONSUMPTIVE.toString());
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getPartPageResult(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, Integer tmEquipmentId, Integer currentPageId,
			ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/equipment/materials/materials_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, Integer tmEquipmentId, String[] tmPartId, String[] note) {
		List<TmEquipmentMaterials> tmEquipmentMaterials = service.doSaveBatch(tmEquipmentId, tmPartId, note);
		logService.doAddLog("TmEquipmentMaterials", tmEquipmentMaterials);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap, Integer tmEquipmentId) {
		modelMap.addAttribute("materials", service.findById(Integer.valueOf(id), true));
		modelMap.addAttribute("tmEquipmentId", tmEquipmentId);
		return new ModelAndView("/master/equipment/materials/materials_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmEquipmentMaterials bean) {
		TmEquipmentMaterials before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmEquipmentMaterials", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmEquipmentMaterials> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmEquipmentMaterials", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * @desc 导出
	 * @author ryy
	 */
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids, Integer tmEquipmentId) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId.toString());
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmEquipmentMaterials> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "辅材", "物料编号", "中文简称", "英文简称", "物料类型", "最大批次数量", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}
}
