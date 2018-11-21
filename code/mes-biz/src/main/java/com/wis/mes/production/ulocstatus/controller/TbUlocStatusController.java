package com.wis.mes.production.ulocstatus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.production.ulocstatus.entity.TbUlocStatus;
import com.wis.mes.production.ulocstatus.service.TbUlocStatusService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/tbUlocStatus")
public class TbUlocStatusController extends BaseController {

	@Autowired
	private TbUlocStatusService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPlantService tmPlantService;
	@Autowired
	private TmLineService tmLineService;
	@Autowired
	private TmUlocService tmUlocService;
	
	// 工位实时状态查询
	@RequestMapping(value = "/ulocstatusRtListInput")
	public ModelAndView ulocstatusRtListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("ulocStatus", getUlocStatusOption());
		List<DictEntry> plantList = tmPlantService.getDictItem(null);
		List<DictEntry> lineList = tmLineService.getDictItem(null);
		modelMap.addAttribute("plants", JSONArray.fromObject(plantList).toString());
		modelMap.addAttribute("plantOptions", plantList);
		modelMap.addAttribute("lines", JSONArray.fromObject(lineList).toString());
		modelMap.addAttribute("lineOptions", lineList);
		modelMap.addAttribute("ulocOptions", tmUlocService.getDictItem(null));
		return new ModelAndView("ulocstatus/ulocstatus_rt_list");
	}

	private List<DictEntry> getUlocStatusOption() {
		List<DictEntry> ulocStatus = new ArrayList<DictEntry>();
		DictEntry ulocEntry = new DictEntry();
		ulocEntry.setCode("1");
		ulocEntry.setName("异常");
		ulocStatus.add(ulocEntry);
		ulocEntry = new DictEntry();
		ulocEntry.setCode("2");
		ulocEntry.setName("手动");
		ulocStatus.add(ulocEntry);
		ulocEntry = new DictEntry();
		ulocEntry.setCode("3");
		ulocEntry.setName("通过");
		ulocStatus.add(ulocEntry);
		ulocEntry = new DictEntry();
		ulocEntry.setCode("4");
		ulocEntry.setName("正常");
		ulocStatus.add(ulocEntry);
		return ulocStatus;
	}

	// 工位实时历史查询
	@RequestMapping(value = "/ulocstatusHsListInput")
	public ModelAndView ulocstatusHsListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("ulocStatus", getUlocStatusOption());
		List<DictEntry> plantList = tmPlantService.getDictItem(null);
		List<DictEntry> lineList = tmLineService.getDictItem(null);
		modelMap.addAttribute("plants", JSONArray.fromObject(plantList).toString());
		modelMap.addAttribute("plantOptions", plantList);
		modelMap.addAttribute("lines", JSONArray.fromObject(lineList).toString());
		modelMap.addAttribute("lineOptions", lineList);
		modelMap.addAttribute("classGroupOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		modelMap.addAttribute("ulocOptions", tmUlocService.getDictItem(null));
		return new ModelAndView("ulocstatus/ulocstatus_hs_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		String isHistory = queryCondition.get("IS_HISTORY").toString();
		String stationStateCondition = "";
		if(queryCondition.containsKey("stationState")) {
			stationStateCondition = queryCondition.get("stationState").toString();
		}
		queryCondition.remove("IS_HISTORY");
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.DESC : OrderEnum.ASC);
		PageResult<TbUlocStatus> findBy = null;
		if ("YES".equals(isHistory)) {
			if (StringUtils.isEmpty(criteria.getSort())) {
				criteria.setOrderField("uloc.no");
			}
			ActionUtils.dateConditionDefault(criteria, "createTime", -1);
			findBy = service.findBy(criteria);
		} else {
			if (StringUtils.isNotEmpty(criteria.getSort()) && !criteria.getSort().equals("uloc.no")) {
				criteria.setOrderField("\"" + criteria.getSort() + "\"");
			} else {
				criteria.setOrderField("no");
			}
			findBy = service.queryPageTbUlocStatus(criteria);
		}
		List<TbUlocStatus> content = findBy.getContent();
		for (TbUlocStatus bean : content) {
			if (bean.getCreateTime() != null && bean.getUpdateTime() != null) {
				bean.setDuration(DateUtils.getDayFull(bean.getCreateTime(), bean.getUpdateTime()));
			}
			String stationState = bean.getStationState();
			bean.setStationState(getStationState(stationState,stationStateCondition));
			bean.setStatusContent(getStationStatusName(stationState));
			if(null != bean.getCodeRule()) {
				String codeDesc = bean.getCodeRule().getCodeDesc();
				bean.getCodeRule().setCodeDesc(StringUtils.isNotBlank(codeDesc)?codeDesc:getCodeDesc(stationState));
			}
		}
		return ActionUtils.handleListResult(response, findBy);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TbUlocStatus> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TbUlocStatus", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids)
			throws IOException, InvalidFormatException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}else {
			Map<String, Object> queryCondition = criteria.getQueryCondition();
			if (queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd") == null) {
				Date currentDate = DateUtils.getCurrentDate();
				queryCondition.put("createTimeStart", DateUtils.getPlusDay(currentDate, -30));
				queryCondition.put("createTimeEnd", currentDate);
			}
		}
		criteria.setOrderField(null);
		criteria.getQueryCondition().put("EXPORT", "YES");
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList("uloc.name", "uloc.no"));
		String[] title = new String[] { "日期", "班次", "产线编号", "产线名称", "工位编号", "加工工艺", "作业员工号", "工位状态", "开始时刻", "结束时刻",
				"持续时间", "状态内容", "内容代码", "内容描述" };
		service.doExport(request, response, service, "ULOC_STATUS", "工位状态报表", title, criteria);
		return ActionUtils.handleResult(response);
	}

	private String getStationState(String stationState,String stationStateCondition) {
		if(StringUtils.isNotBlank(stationStateCondition)) {
				if (stationState != null && stationState.length() == 8) {
					if ("1".equals(stationState.substring(4, 5)) || "1".equals(stationState.substring(5, 6))
							|| "1".equals(stationState.substring(7, 8)) || "1".equals(stationState.substring(6, 7))) {
						return "异常";
					} else if ("1".equals(stationState.substring(1, 2)) && stationStateCondition.equals("3")) {
						return "通过";
					} else if ("1".equals(stationState.substring(2, 3))&& stationStateCondition.equals("2")) {
						return "手动";
					} else if ("1".equals(stationState.substring(3, 4))&& stationStateCondition.equals("4")) {
						return "正常";
					}
				}
		}else {
			if (stationState != null && stationState.length() == 8) {
				if ("1".equals(stationState.substring(4, 5)) || "1".equals(stationState.substring(5, 6))
						|| "1".equals(stationState.substring(7, 8)) || "1".equals(stationState.substring(6, 7))) {
					return "异常";
				} else if ("1".equals(stationState.substring(1, 2))) {
					return "通过";
				} else if ("1".equals(stationState.substring(2, 3))) {
					return "手动";
				} else if ("1".equals(stationState.substring(3, 4))) {
					return "正常";
				}
			}
		}
		return "";
	}
	
	private String getStationStatusName(String station) {
		if (station != null && station.length() == 8) {
			if ("1".equals(station.substring(4, 5)) || "1".equals(station.substring(5, 6))
					|| "1".equals(station.substring(7, 8))) {
				return "异常";
			} else if ("1".equals(station.substring(0, 1)) || "1".equals(station.substring(6, 7))) {
				return "警告";
			}
		}
		return "";
	}
	
	private String getCodeDesc(String station) {
		if("1".equals(station.substring(4, 5))){
			return "紧停";
		}
		if("1".equals(station.substring(5, 6))){
			return "暂停";
		}
		return "";
	}
}
