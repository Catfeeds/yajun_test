package com.wis.mes.master.equipment.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.equipment.entity.TmpEquipmentStatus;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.equipment.service.TmEquipmentStatusService;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.service.TmPlantService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/mpEquipment")
public class TmpEquipmentStatusController extends BaseController {

	@Autowired
	private TmEquipmentStatusService service;
	@Autowired
	private TmEquipmentService tmEquipmentService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService tmLineService;
	@Autowired
	private TmPlantService tmPlantService;

	@RequestMapping(value = "/rtListInput")
	public ModelAndView rtListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer id) {
		modelMap.addAttribute("equipmentOptions", tmEquipmentService.getDictItem(null));
		modelMap.addAttribute("eqcirculateStates", entryService.getEntryByTypeCode(ConstantUtils.SM_CIRCULATE_STATE));
		modelMap.addAttribute("lines", tmLineService.getDictItem(null));
		modelMap.addAttribute("plants", JSONArray.fromObject(tmPlantService.getDictItem(null)).toString());
		return new ModelAndView("equipmentstatus/mpequipmentstatus_rt_main");
	}

	@RequestMapping(value = "/hsListInput")
	public ModelAndView hsListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer id) {
		modelMap.addAttribute("equipmentOptions", tmEquipmentService.getDictItem(null));
		modelMap.addAttribute("eqcirculateStates", entryService.getEntryByTypeCode(ConstantUtils.SM_CIRCULATE_STATE));
		modelMap.addAttribute("lines", tmLineService.getDictItem(null));		
		List<DictEntry> plantList = tmPlantService.getDictItem(null);
		modelMap.addAttribute("plants", JSONArray.fromObject(plantList).toString());
		modelMap.addAttribute("plantOptions", plantList);
		modelMap.addAttribute("classGroupOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		return new ModelAndView("equipmentstatus/mpequipmentstatus_hs_main");
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
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		String isHistory = queryCondition.get("IS_HISTORY").toString();
		queryCondition.remove("IS_HISTORY");
		PageResult<TmpEquipmentStatus> findBy = null;
		if ("YES".equals(isHistory)) {
			ActionUtils.dateConditionDefault(criteria, "createTime", -1);
			findBy = service.findBy(criteria);
		} else {
			if (StringUtils.isNotEmpty(criteria.getSort())) {
				criteria.setOrderField("t." + criteria.getSort());
			} else {
				criteria.setOrderField("t.create_time");
			}
			findBy = service.queryPageTbEquipmentStatus(criteria);
		}
		List<TmpEquipmentStatus> content = findBy.getContent();
		for (TmpEquipmentStatus bean : content) {
			if (bean.getCreateTime() != null && bean.getUpdateTime() != null) {
				bean.setDuration(DateUtils.getDayFull(bean.getCreateTime(), bean.getUpdateTime()));
			}
		}
		return ActionUtils.handleListResult(response, findBy);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids)
			throws IOException, InvalidFormatException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		} else {
			Map<String, Object> queryCondition = criteria.getQueryCondition();
			if (queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd") == null) {
				Date currentDate = DateUtils.getCurrentDate();
				queryCondition.put("createTimeStart", DateUtils.getPlusDay(currentDate, -30));
				queryCondition.put("createTimeEnd", currentDate);
			}
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		String[] title = new String[] { "日期", "班次", "产线", "设备编号", "设备名称", "运行状态", "开始时刻", "结束时刻", "持续时长", "状态编号","状态内容" };
		service.doExport(request, response, service, "EQUIPMENT_STATUS", "钣金设备状态报表", title, criteria);
		return ActionUtils.handleResult(response);
	}
}
