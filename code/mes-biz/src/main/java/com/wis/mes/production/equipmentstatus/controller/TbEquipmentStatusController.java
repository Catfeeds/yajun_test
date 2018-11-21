package com.wis.mes.production.equipmentstatus.controller;

import java.io.IOException;
import java.util.Calendar;
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
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.production.equipmentstatus.entity.TbEquipmentStatus;
import com.wis.mes.production.equipmentstatus.service.TbEquipmentStatusService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/tbEquipmentStatus")
public class TbEquipmentStatusController extends BaseController {

	@Autowired
	private TbEquipmentStatusService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmLineService tmLineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPlantService tmPlantService;
	@Autowired
	private TmEquipmentService tmEquipmentService;

	@RequestMapping(value = "/equipmentstatusRtListInput")
	public ModelAndView equipmentstatusRtListInput(HttpServletResponse response, QueryCriteria criteria,
			ModelMap modelMap) {
		TmLine tmLine = new TmLine();
		tmLine.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("lines", tmLineService.getDictItem(tmLine));
		modelMap.addAttribute("eqcirculateStates", entryService.getEntryByTypeCode(ConstantUtils.EQ_CIRCULATE_STATE));
		modelMap.addAttribute("plants", JSONArray.fromObject(tmPlantService.getDictItem(null)).toString());
		modelMap.addAttribute("equipmentOptions", tmEquipmentService.getDictItem(null));
		return new ModelAndView("equipmentstatus/equipmentstatus_rt_list");
	}

	@RequestMapping(value = "/equipmentstatusHsListInput")
	public ModelAndView equipmentstatusHsListInput(HttpServletResponse response, QueryCriteria criteria,
			ModelMap modelMap) {
		TmLine tmLine = new TmLine();
		tmLine.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("lines", tmLineService.getDictItem(tmLine));
		modelMap.addAttribute("eqcirculateStates", entryService.getEntryByTypeCode(ConstantUtils.EQ_CIRCULATE_STATE));
		List<DictEntry> plantList = tmPlantService.getDictItem(null);
		modelMap.addAttribute("plants", JSONArray.fromObject(plantList).toString());
		modelMap.addAttribute("plantOptions", plantList);
		modelMap.addAttribute("classGroupOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		modelMap.addAttribute("equipmentOptions", tmEquipmentService.getDictItem(null));
		return new ModelAndView("equipmentstatus/equipmentstatus_hs_list");
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
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		String isHistory = queryCondition.get("IS_HISTORY").toString();
		queryCondition.remove("IS_HISTORY");
		if (queryCondition.get("runningState") != null) {
			String runningState = queryCondition.get("runningState").toString();
			queryCondition.remove("runningState");
			if ("1".equals(runningState)) {
				queryCondition.put("runningState", "0001");
			} else if ("2".equals(runningState)) {
				queryCondition.put("runningStateIN", "'0010','0011'");
			} else if ("3".equals(runningState)) {
				queryCondition.put("runningState", "0100");
			} else if ("4".equals(runningState)) {
				queryCondition.put("runningState", "1000");
			}
		}
		PageResult<TbEquipmentStatus> findBy = null;
		if ("YES".equals(isHistory)) {
			ActionUtils.dateConditionDefault(criteria, "createTime", -1);
			findBy = service.findBy(criteria);
		} else {
			if (StringUtils.isNotEmpty(criteria.getSort())) {
				criteria.setOrderField("\"" + criteria.getSort() + "\"");
			} else {
				criteria.setOrderField("\"createTime\"");
			}
			findBy = service.queryPageTbEquipmentStatus(criteria);
		}
		List<TbEquipmentStatus> content = findBy.getContent();
		for (TbEquipmentStatus bean : content) {
			if (bean.getCreateTime() != null && bean.getUpdateTime() != null) {
				bean.setDuration(DateUtils.getDayFull(bean.getCreateTime(), bean.getUpdateTime()));
			}
		}
		return ActionUtils.handleListResult(response, findBy);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbEquipmentStatus> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbEquipmentStatus", list);
		return ActionUtils.handleResult(response);
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
		/* criteria.setFuzzyQueryFileds(Arrays.asList()); */
		String[] title = new String[] { "日期", "班次", "产线", "设备编号", "设备名称", "运行状态", "开始时刻", "结束时刻", "持续时长", "状态编号",
				"状态内容" };
		service.doExport(request, response, service, "EQUIPMENT_STATUS", "设备状态报表", title, criteria);
		return ActionUtils.handleResult(response);
	}

}
