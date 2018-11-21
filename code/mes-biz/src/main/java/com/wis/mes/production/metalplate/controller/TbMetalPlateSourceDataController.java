package com.wis.mes.production.metalplate.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSourceData;
import com.wis.mes.production.metalplate.service.TbMetalPlateSourceDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/mpSourceData")
public class TbMetalPlateSourceDataController extends BaseController {

	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TbMetalPlateSourceDataService sourceDataService;
	@Autowired
	private TmSheetMetalMaterialService sheetMetalMaterialService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmWorktimeService worktimeService;
	@Autowired
	private TmEquipmentService equipmentService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		String leftMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_LEFT_MACHINING_SURFACE);
		String rightMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_RIGHT_MACHINING_SURFACE);
		List<String> list = new ArrayList<String>(Arrays.asList(leftMs.split("/")));
		list.addAll(new ArrayList<String>(Arrays.asList(rightMs.split("/"))));
		modelMap.addAttribute("allGlobalJson", JSONArray.fromObject(list).toString());
		modelMap.addAttribute("equipments", JSONArray.fromObject(equipmentService.queryDictItem(criteria)).toString());
		return new ModelAndView("/metalPlate/production-schedule/ps_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "model"}));
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, sourceDataService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/metalPlate/production-schedule/ps_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TbMetalPlateSourceData bean)
			throws Exception {
		try {
			bean.setStatus(0);
			TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
			eg.setMachiningSurface(bean.getModel());
			eg = sheetMetalMaterialService.findUniqueByEg(eg);
			if (eg != null) {
				bean.setMaterialId(eg.getId());
			}
			bean = sourceDataService.doSave(bean);
		} catch (Exception e) {
			throw e;
		}
		logService.doAddLog("TbMetalPlateSourceData", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		TbMetalPlateSourceData bean = sourceDataService.findById(Integer.valueOf(id));
		List<String> allList = getAllMs();
		if (!allList.contains(bean.getModel())) {
			modelMap.addAttribute("bean", bean);
			return new ModelAndView("/metalPlate/production-schedule/ps_update");
		} else {
			modelMap.addAttribute("beans", getAllMsBeans(bean, allList));
			modelMap.addAttribute("models", JSONArray.fromObject(allList).toString());
		}
		return new ModelAndView("/metalPlate/production-schedule/ps_updateMulti");
	}

	@RequestMapping(value = "/updateInventoryInput")
	public ModelAndView updateInventoryInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TbMetalPlateSourceData bean = sourceDataService.findById(Integer.valueOf(id));
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("material", sheetMetalMaterialService.findById(bean.getMaterialId()));
		getMetalMaterialEntry(modelMap);
		return new ModelAndView("/metalPlate/production-schedule/ps_updateInventory");
	}

	@RequestMapping(value = "/singleInput")
	public ModelAndView singleInput(HttpServletResponse response, String id, ModelMap modelMap) {

		modelMap.addAttribute("smMachiningSurface",
				entryService.getEntryByTypeCode(ConstantUtils.SM_MACHINING_SURFACE));
		modelMap.addAttribute("smCodeClassOrder", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		List<String> allList = getAllMs();
		TbMetalPlateSourceData bean = sourceDataService.findById(Integer.valueOf(id));
		//根据型号进行判断
		if (!allList.contains(bean.getModel())) {
			modelMap.addAttribute("bean", bean);
			TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
			eg.setMachiningSurface(bean.getModel());
			eg = sheetMetalMaterialService.findUniqueByEg(eg);
			modelMap.addAttribute("msbean", eg);
			return new ModelAndView("/metalPlate/production-schedule/ps_single");
		}
		List<TbMetalPlateSourceData> list = getAllMsBeans(bean, allList);
		modelMap.addAttribute("beans", list);
//		modelMap.addAttribute("taskDate", list.get(0).getTaskDate());
		modelMap.addAttribute("taskDate",bean.getTaskDate());
		return new ModelAndView("/metalPlate/production-schedule/ps_singleMulti");
	}
	//初始化List进行匹配
	private List<String> getAllMs() {
		String leftMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_LEFT_MACHINING_SURFACE);
		String rightMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_RIGHT_MACHINING_SURFACE);
		List<String> leftList = Arrays.asList(leftMs.split("/"));
		List<String> rightList = Arrays.asList(rightMs.split("/"));
		List<String> allList = new ArrayList<String>();
		allList.addAll(leftList);
		allList.addAll(rightList);
		return allList;
	}

	private List<TbMetalPlateSourceData> getAllMsBeans(TbMetalPlateSourceData bean, List<String> allList) {
		TbMetalPlateSourceData eg = new TbMetalPlateSourceData(bean.getTaskDate(), bean.getDataType());
		eg.setModel(getModelByList(allList));
		List<TbMetalPlateSourceData> list = sourceDataService.findByCollectEg(eg);
		List<TbMetalPlateSourceData> beans = new ArrayList<TbMetalPlateSourceData>();
		for (String s : allList) {
			boolean isExist = false;
			for (TbMetalPlateSourceData sdBean : list) {
				if (sdBean.getModel().equals(s)) {
					beans.add(sdBean);
					isExist = true;
				}
			}
			if (isExist == false) {
				TbMetalPlateSourceData sdBean = new TbMetalPlateSourceData();
				sdBean.setModel(s);
				sdBean.setApcTotalNumber(0);
				sdBean.setPlannedProduction(0);
				beans.add(sdBean);
			}
		}
		return beans;
	}

	private String getModelByList(List<String> allList) {
		String model = "'";
		for (String s : allList) {
			model += s + "','";
		}
		return model.substring(0, model.length() - 2);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TbMetalPlateSourceData bean) {
		TbMetalPlateSourceData before = sourceDataService.findById(bean.getId());
		bean.setOptCounter(before.getOptCounter() + 1);
		bean.setRemoved(before.getRemoved());
		bean.setStatus(before.getStatus());
		TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
		eg.setMachiningSurface(bean.getModel());
		eg = sheetMetalMaterialService.findUniqueByEg(eg);
		if (eg != null) {
			bean.setMaterialId(eg.getId());
		}
		sourceDataService.doUpdate(bean);
		logService.doUpdateLog("TbMetalPlateSourceData", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/updateMulti")
	public JsonActionResult updateMulti(HttpServletResponse response, HttpServletRequest request) {
		List<String> allList = getAllMs();
		int tempId = 0;
		for (String ms : allList) {
			String plannedProduction = request.getParameter(ms);
			String id = request.getParameter(ms + "_id");
			if (StringUtils.isEmpty(plannedProduction) || StringUtils.isEmpty(id)) {
				continue;
			}
			if (Integer.parseInt(id) == 0) {
				if (Integer.parseInt(plannedProduction) == 0) {
					continue;
				}
				if (tempId == 0) {
					for (String t : allList) {
						String tId = request.getParameter(t + "_id");
						if (Integer.parseInt(tId) != 0) {
							tempId = Integer.parseInt(tId);
							break;
						}
					}
				}
				TbMetalPlateSourceData tempBean = sourceDataService.findById(tempId);
				TbMetalPlateSourceData bean = new TbMetalPlateSourceData();
				copy(tempBean, bean);
				bean.setId(null);
				bean.setModel(ms);
				bean.setInventoryNumber(0);
				bean.setApcTotalNumber(0);
				bean.setStatus(0);
				bean.setPlannedProduction(Integer.parseInt(plannedProduction));
				sourceDataService.doSave(bean);
				logService.doAddLog("TbMetalPlateSourceData", bean);
				continue;
			}
			TbMetalPlateSourceData before = sourceDataService.findById(Integer.parseInt(id));
			if (before.getPlannedProduction().intValue() == Integer.parseInt(plannedProduction)) {
				continue;
			}
			if (Integer.parseInt(plannedProduction) == 0) {
				sourceDataService.doDeleteById(before.getId());
				logService.doDeleteLog("TbMetalPlateSourceData", before);
			}
			TbMetalPlateSourceData update = new TbMetalPlateSourceData();
			copy(before, update);
			update.setOptCounter(before.getOptCounter() + 1);
			update.setRemoved(before.getRemoved());
			update.setPlannedProduction(Integer.parseInt(plannedProduction));
			update.setStatus(0);
			sourceDataService.doUpdate(update);
			logService.doUpdateLog("TbMetalPlateSourceData", before, update);
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TbMetalPlateSourceData> list = sourceDataService.findAllInIds(deleteIds);
		try {
			for (TbMetalPlateSourceData eg : list) {
				// APC获取的数据不能删除
				if ("1".equals(eg.getDataType())) {
					throw new Exception("APC获取的数据不能删除");
				}
			}
			sourceDataService.doDeleteById(deleteIds);
		} catch (Exception e) {
			throw e;
		}
		logService.doDeleteLog("TbMetalPlateSourceData", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/modelChange")
	public void modelChange(HttpServletRequest request, HttpServletResponse response, String model) {
		JSONObject result = new JSONObject();
		result.put("status", "n");
		TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
		eg.setMachiningSurface(model);
		eg = sheetMetalMaterialService.findUniqueByEg(eg);
		if (eg == null) {
			result.put("msg", "型号" + model + "未维护[钣金原材料]主数据！");
		} else {
			result.put("status", "y");
			result.put("inventoryNumber", 0);
		}
		ActionUtils.handleResult(response, result);
	}

	@RequestMapping(value = "/addDataCheck")
	public void addDataCheck(HttpServletRequest request, HttpServletResponse response, String taskDate, String model,
			String workSchedule, String dataType, Integer id) {
		JSONObject result = new JSONObject();
		result.put("status", "y");
		try {
			TbMetalPlateSourceData eg = new TbMetalPlateSourceData();
			eg.setModel(model);
			// eg.setWorkSchedule(workSchedule);
			eg.setTaskDate(taskDate);
			eg.setDataType(dataType);
			List<TbMetalPlateSourceData> sdatas = sourceDataService.findByEg(eg);
			if (sdatas != null) {
				boolean isequ = false;
				for (TbMetalPlateSourceData sd : sdatas) {
					if (sd.getStatus() == 2) {
						continue;
					} else if (id != null && sd.getId().intValue() == id.intValue()) {
						continue;
					} else {
						isequ = true;
					}
				}
				if (isequ) {
					throw new Exception("数据重复，请先把之前的源数据派工后再新增！");
				}
			}
		} catch (Exception ex) {
			result.put("status", "n");
			result.put("info", ex.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

	@RequestMapping(value = "/workScheduleChange")
	public void workScheduleChange(HttpServletRequest request, HttpServletResponse response, String workSchedule,
			String taskDate) {
		String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.MP_LINE_NO);
		JSONObject result = new JSONObject();
		result.put("status", "n");
		TmWorktime workTime = worktimeService.getWorkTimeByDayAndShiftno(taskDate, lineNo, workSchedule);
		if (workTime == null) {
			result.put("msg", "此班次[" + taskDate + "]未维护[钣金冲压线]工作时段！");
		} else {
			result.put("status", "y");
			String startTime = workTime.getStartTime().substring(workTime.getStartTime().indexOf(" "));
			String endTime = workTime.getEndTime().substring(workTime.getEndTime().indexOf(" "));
			result.put("sendPeriodTime", startTime + " -" + endTime);
			result.put("tmWorktimeId", workTime.getId());
		}
		ActionUtils.handleResult(response, result);
	}

	@RequestMapping(value = "/apcTotalNumberChange")
	public void apcTotalNumberChange(HttpServletRequest request, HttpServletResponse response, Integer apcTotalNumber,
			String model, Integer inventoryNumber) {
		JSONObject result = new JSONObject();
		result.put("status", "n");
		TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
		eg.setMachiningSurface(model);
		eg = sheetMetalMaterialService.findUniqueByEg(eg);
		if (eg == null) {
			result.put("msg", "型号" + model + "未维护[钣金原材料]主数据！");
		} else {
			if (inventoryNumber == null) {
				inventoryNumber = 0;
			}
			if (apcTotalNumber == null) {
				apcTotalNumber = 0;
			}
			apcTotalNumber = apcTotalNumber - inventoryNumber;
			if (apcTotalNumber < 0) {
				apcTotalNumber = 0;
			}
			// 计算计划生产数
			/*int plannedProduction = eg.getBatchNumber()
					* (int) Math.ceil(apcTotalNumber.doubleValue() / eg.getBatchNumber().doubleValue());*/
			int plannedProduction = apcTotalNumber.intValue();
			result.put("status", "y");
			result.put("plannedProduction", plannedProduction);
		}
		ActionUtils.handleResult(response, result);

	}

	@ResponseBody
	@RequestMapping(value = "/checkSingleMulti")
	public void checkSingleMulti(HttpServletResponse response, Integer id) {
		JSONObject result = new JSONObject();
		result.put("status", "y");
		try {
			String leftMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_LEFT_MACHINING_SURFACE);
			String rightMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_RIGHT_MACHINING_SURFACE);
			TbMetalPlateSourceData bean = sourceDataService.findById(Integer.valueOf(id));
			// List<String> allList = getAllMs();
			TbMetalPlateSourceData eg = new TbMetalPlateSourceData(bean.getTaskDate(), bean.getDataType());
			// eg.setModel(getModelByList(allList));
			// List<TbMetalPlateSourceData> list = sourceDataService
			// .findByCollectEg(eg);
			// for (TbMetalPlateSourceData psd : list) {
			// if (psd.getStatus() != 0) {
			// throw new Exception("关联的机型[" + psd.getModel()
			// + "]已派工或部分派工，该批数据不可修改！");
			// }
			// }
			List<String> leftList = Arrays.asList(leftMs.split("/"));
			List<String> rightList = Arrays.asList(rightMs.split("/"));
			if (leftList.contains(bean.getModel()) || rightList.contains(bean.getModel())) {
				eg.setModel(getModelByList(leftList));
				Integer leftSum = sourceDataService.findBySumPlannedProductionByModel(eg);
				eg.setModel(getModelByList(rightList));
				Integer rightSum = sourceDataService.findBySumPlannedProductionByModel(eg);
				if (leftSum.intValue() != rightSum.intValue()) {
					throw new Exception("左侧[" + getModelByList(leftList) + "],与右侧[" + getModelByList(rightList)
							+ "]计划生产数不一致，请手动维护！");
				}
			}
		} catch (Exception ex) {
			result.put("status", "n");
			result.put("info", ex.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

	@ResponseBody
	@RequestMapping(value = "/checkUpdateMulti")
	public void checkUpdateMulti(HttpServletResponse response, Integer id) {
		JSONObject result = new JSONObject();
		result.put("status", "y");
		try {
			TbMetalPlateSourceData bean = sourceDataService.findById(Integer.valueOf(id));
			List<String> allList = getAllMs();
			if (allList.contains(bean.getModel())) {
				TbMetalPlateSourceData eg = new TbMetalPlateSourceData(bean.getTaskDate(), bean.getDataType());
				eg.setModel(getModelByList(allList));
				List<TbMetalPlateSourceData> list = sourceDataService.findByCollectEg(eg);
				for (TbMetalPlateSourceData psd : list) {
					if (psd.getStatus() == 1 || psd.getStatus() == 2) {
						throw new Exception("关联的机型[" + psd.getModel() + "]已派工或部分派工，该批数据不可修改！");
					}
				}
			}
		} catch (Exception ex) {
			result.put("status", "n");
			result.put("info", ex.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

	@ResponseBody
	@RequestMapping(value = "/checkPlannedProduction")
	public void checkPlannedProduction(HttpServletResponse response, String model, String param) {
		JSONObject result = new JSONObject();
		result.put("status", "y");
		try {
			TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
			eg.setMachiningSurface(model);
			eg = sheetMetalMaterialService.findUniqueByEg(eg);

			if (!StringUtils.isEmpty(param) && Integer.parseInt(param) != 0) {
				if (Integer.parseInt(param) % eg.getBatchNumber() != 0) {
					throw new Exception("加工図面[" + model + "]计划数量需要为批次数[" + eg.getBatchNumber() + "]的倍数！");
				}
			}
		} catch (Exception ex) {
			result.put("status", "n");
			result.put("info", ex.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

	private void getMetalMaterialEntry(ModelMap modelMap) {
		modelMap.addAttribute("smMachiningSurface",
				entryService.getEntryByTypeCode(ConstantUtils.SM_MACHINING_SURFACE));
		modelMap.addAttribute("smMachineTags", entryService.getEntryByTypeCode(ConstantUtils.SM_MACHINE_TAGS));
		modelMap.addAttribute("smToolNumber", entryService.getEntryByTypeCode(ConstantUtils.SM_TOOL_NUMBER));
		modelMap.addAttribute("smCodeClassOrder", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("smProductionState", entryService.getEntryByTypeCode(ConstantUtils.SM_PRODUCTION_STATE));
		modelMap.addAttribute("smSourceDataType", entryService.getEntryByTypeCode(ConstantUtils.SM_SOURCE_DATA_TYPE));
		modelMap.addAttribute("smRegionMark", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
	}

}
