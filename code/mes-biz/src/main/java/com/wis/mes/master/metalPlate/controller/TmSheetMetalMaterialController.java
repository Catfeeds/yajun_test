package com.wis.mes.master.metalPlate.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.boardmanage.service.TmBoardManageService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 主数据
 * 
 * @author lenovo
 *
 */
@Controller
@RequestMapping(value = "/metalPlate")
public class TmSheetMetalMaterialController extends BaseController {

	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmSheetMetalMaterialService sheetMetalMaterialService;
	@Autowired
	private TmBoardManageService tmBoardManageService;
	@Autowired
	private TmEquipmentService equipmentService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no" }));
		criteria.getQueryCondition().put("no", ConstantUtils.EQUIPMENT_MOULD_NO);
		modelMap.addAttribute("equipmentMoulds", JSONArray.fromObject(equipmentService.queryDictItem(criteria)).toString());
		getMetalMaterialEntry(modelMap);
		return new ModelAndView("/master/metalPlate/metalPlate_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {"machineTagNames" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, sheetMetalMaterialService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/metalPlate/metalPlate_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmSheetMetalMaterial bean){
		bean = sheetMetalMaterialService.doSave(bean);
		logService.doAddLog("TmLiTmSheetMetalMaterialne", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", sheetMetalMaterialService.findById(Integer.valueOf(id)));
		getMetalMaterialEntry(modelMap);
		return new ModelAndView("/master/metalPlate/metalPlate_update");
	}

	@RequestMapping(value = "/mouldInput")
	public ModelAndView mouldInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmSheetMetalMaterial bean = sheetMetalMaterialService.findById(Integer.valueOf(id));
		QueryCriteria criteria = new QueryCriteria();
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no" }));
		criteria.getQueryCondition().put("no", ConstantUtils.EQUIPMENT_MOULD_NO);
		modelMap.addAttribute("equipmentMoulds", equipmentService.queryDictItem(criteria));
		QueryCriteria criteriaBoard = new QueryCriteria();
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no" }));
		criteria.getQueryCondition().put("no", ConstantUtils.EQUIPMENT_BOARD_NO);
		modelMap.addAttribute("equipmentBoards", equipmentService.queryDictItem(criteriaBoard));
		String machineTags = bean.getMachineTags();
		String toolNumbers = bean.getToolNumber();
		Map<String, String> tagMap = formatTagMap(machineTags, toolNumbers);
		modelMap.addAttribute("tags", tagMap);
		modelMap.addAttribute("bean", bean);
		return new ModelAndView("/master/metalPlate/metalPlate_mould");
	}
	public static void main(String[] args) {
		TmSheetMetalMaterialController sheet = new TmSheetMetalMaterialController();
		Map<String, String> formatTagMap = sheet.formatTagMap("251,248","251:BJ-MOULD-02,BJ-MOULD-03~248:BJ-MOULD-03,BJ-MOULD-04");
		System.out.println(formatTagMap);
	}
	
	public Map<String, String> formatTagMap(String machineTags, String toolNumbers) {
		Map<String, String> tagMap = new HashMap<String, String>();
		//获取设备编号
		String[] machineTagsArray = machineTags.split(",");
		String[] toolArray = toolNumbers != null ? toolNumbers.split("~") : new String[] {};
		for(int i=0;i<machineTagsArray.length;i++) {
			tagMap.put(machineTagsArray[i], new String());
		}
		if(toolArray.length > 0) {
			for(int j=0;j<toolArray.length;j++) {
				String[] toolSrcs = toolArray[j].split(":");
				if(tagMap.containsKey(toolSrcs[0])) {
					tagMap.put(toolSrcs[0], toolSrcs[1]);
				}
			}
		}
		return tagMap;
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmSheetMetalMaterial bean) {
		System.out.println(bean.toString());
		TmSheetMetalMaterial before = sheetMetalMaterialService.findById(bean.getId());
		bean.setOptCounter(before.getOptCounter() + 1);
		bean.setRemoved(before.getRemoved());
		sheetMetalMaterialService.doUpdate(bean);
		logService.doUpdateLog("TmLiTmSheetMetalMaterialne", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/mouldDieMaintenance")
	public JsonActionResult mouldDieMaintenance(HttpServletRequest request, HttpServletResponse response,
			TmSheetMetalMaterial bean) {
		TmSheetMetalMaterial before = sheetMetalMaterialService.findById(bean.getId());
		bean = before;
		String[] tags = bean.getMachineTags().split(",");
		String tools = "";
		for (int i = 0; i < tags.length; i++) {
			String[] value = request.getParameterValues(tags[i]);
			String tv = tags[i]+":";
			for (String v : value) {
				tv += v + ",";
			}
			if (tv.length() >= 0) {
				tv = tv.substring(0, tv.length() - 1);
			}
			tools += tv + "~";
		}
		if (tools.length() >= 0) {
			tools = tools.substring(0, tools.length() - 1);
		}
		bean.setOptCounter(before.getOptCounter() + 1);
		bean.setRemoved(before.getRemoved());
		bean.setToolNumber(tools);
		sheetMetalMaterialService.doUpdate(bean);
		logService.doUpdateLog("TmLiTmSheetMetalMaterialne", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmSheetMetalMaterial> list = sheetMetalMaterialService.findAllInIds(deleteIds);
		try {
			sheetMetalMaterialService.doRemoveByBatch(deleteIds);
		} catch (Exception e) {
			throw e;
		}
		logService.doDeleteLog("TmLiTmSheetMetalMaterialne", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = sheetMetalMaterialService.doImportExcelData(book, request);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("METALPLATE_IMPORT_FAIL");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmSheetMetalMaterial> findBy = sheetMetalMaterialService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmSheetMetalMaterial> list = findBy == null ? new ArrayList<TmSheetMetalMaterial>() : findBy.getContent();
		String[] header = new String[] { "机种大类", "室内机/室外机", "材料图号", "流水线用图号", "流水线用图号等级", "涂装用图号", "涂装用图号等级", "加工図面用涂号",
				"铁板加工用图号等级", "程序号", "累进", "部品名", "后工程", "材质", "板厚", "开料尺寸", "批次数", "批次上限", "工程数", "区域标识", "机台", "模具厂家",
				"完成识别码", "模具编号", "剩余库存数", "标准CT", "PLC编码", "备注" };
		sheetMetalMaterialService.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "钣金原材料模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "metalPlate" + File.separator + "metalPlate.xlsx";
			final Workbook wb = sheetMetalMaterialService.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/checkMachiningSurface")
	public void checkMachiningSurface(HttpServletResponse response, String code, String param) {
		TmSheetMetalMaterial bean = new TmSheetMetalMaterial();
		bean.setMachiningSurface(param);
		Integer num = sheetMetalMaterialService.findByEg(bean).size();
		JSONObject result = new JSONObject();
		if (num > 0 && !param.equals(code)) {
			result.put("status", "n");
			result.put("info", "加工図面[" + param + "]已存在！");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}

	private void getMetalMaterialEntry(ModelMap modelMap) {
		modelMap.addAttribute("smModelType", entryService.getEntryByTypeCode(ConstantUtils.SM_MODEL_TYPE));
		modelMap.addAttribute("smPlaceType", entryService.getEntryByTypeCode(ConstantUtils.SM_PLACE_TYPE));
		modelMap.addAttribute("smDrawingNoLevel", entryService.getEntryByTypeCode(ConstantUtils.SM_DRAWING_NO_LEVEL));
		modelMap.addAttribute("smProgression", entryService.getEntryByTypeCode(ConstantUtils.SM_PROGRESSION));
		modelMap.addAttribute("smComponent", entryService.getEntryByTypeCode(ConstantUtils.SM_COMPONENT));
		modelMap.addAttribute("smRegionMark", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
		modelMap.addAttribute("smAfterProcess", entryService.getEntryByTypeCode(ConstantUtils.SM_AFTER_PROCESS));
	}

	@RequestMapping(value = "/inventorySettingInput")
	public ModelAndView inventorySettingInput(HttpServletResponse response, String ids, ModelMap modelMap) {
		modelMap.addAttribute("beans", sheetMetalMaterialService.findAllInIds(getIds(ids)));
		return new ModelAndView("/master/metalPlate/inventory_setting");
	}

	@ResponseBody
	@RequestMapping(value = "/inventorySetting")
	public JsonActionResult inventorySetting(HttpServletResponse response, Integer[] ids, Integer[] inventoryNumbers) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < ids.length; i++) {
			map.put(ids[i], inventoryNumbers[i]);
		}
		List<TmSheetMetalMaterial> materials = sheetMetalMaterialService.findAllInIds(ids);
		for (TmSheetMetalMaterial material : materials) {
			material.setInventoryNumber(map.get(material.getId()));
			sheetMetalMaterialService.doUpdate(material);
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/findBoardManageInfo")
	public JsonActionResult findBoardManageInfo(String regionMark) {
		return ActionUtils.handleResult(tmBoardManageService.findBoardManageInfo(regionMark));
	}

}
