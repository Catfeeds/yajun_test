package com.wis.mes.production.metalplate.controller;

import java.util.Arrays;
import java.util.List;

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
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.badparts.service.TmBadPartsService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;
import com.wis.mes.production.metalplate.entity.TbMetalPlateDefectiveRecord;
import com.wis.mes.production.metalplate.entity.TbMetalPlateProductionRecord;
import com.wis.mes.production.metalplate.service.TbMetalPlateDefectiveRecordService;
import com.wis.mes.production.metalplate.service.TbMetalPlateProductionRecordService;
import com.wis.mes.production.metalplate.service.TbMetalPlateSourceDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/mpProductionRecord")
public class TbMetalPlateRecordController extends BaseController {

	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TbMetalPlateProductionRecordService productionRecordService;
	@Autowired
	private TbMetalPlateDefectiveRecordService defectiveRecordService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmBadPartsService badPartsService;
	@Autowired
	private TmEquipmentService equipmentService;
	@Autowired
	private TbMetalPlateSourceDataService sourceDataService;
	@Autowired
	private TmSheetMetalMaterialService materialService;
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response,HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		modelMap.addAttribute("equipments", JSONArray.fromObject(equipmentService.queryDictItem(criteria)).toString());
		return new ModelAndView("/metalPlate/production-records/pr_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "batchNumber", "model", "press", "moulds" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, productionRecordService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, String id) {
		TbMetalPlateProductionRecord productionRecord = productionRecordService.findById(Integer.parseInt(id));
		modelMap.addAttribute("smBadType", badPartsService.queryDictItem(new QueryCriteria()));
		modelMap.addAttribute("recordId", id);
		modelMap.addAttribute("practicalCycles", productionRecord.getPracticalCycles());
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/metalPlate/production-records/pr_dysadd"));
	}

	@RequestMapping(value = "/viewInput")
	public ModelAndView viewInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, Integer id) {
		modelMap.addAttribute("smBadType", entryService.getEntryByTypeCode(ConstantUtils.SM_BAD_TYPE));
		TbMetalPlateDefectiveRecord eg = new TbMetalPlateDefectiveRecord();
		eg.setProductionRecordId(id);
		modelMap.addAttribute("list", defectiveRecordService.findByEg(eg));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/metalPlate/production-records/pr_dysview"));
	}

	@ResponseBody
	@RequestMapping(value = "/addDefectiveRecord")
	public JsonActionResult addDefectiveRecord( HttpServletResponse response, Integer id,String parameters) throws Exception {
		try {
			
			TbMetalPlateDefectiveRecord dRecord = null;
			JSONArray parameteJsonArray = JSONArray.fromObject(parameters);
			Integer sunDefectiveNumber = 0;
			for(int i=0;i< parameteJsonArray.size();i++) {
				JSONObject object = parameteJsonArray.getJSONObject(i);
				String ids = object.get("defectiveProject").toString();
				Integer defectiveNumber = Integer.valueOf(object.get("defectiveNumber").toString());
				String defectiveProjectText = object.get("defectiveProjectText").toString();
				sunDefectiveNumber+=defectiveNumber;
				dRecord = new TbMetalPlateDefectiveRecord();
				dRecord.setProductionRecordId(id);
				dRecord.setDefectiveProject(defectiveProjectText);
				dRecord.setDefectiveNumber(defectiveNumber);
				dRecord.setTmBadpartsIds(ids);
				defectiveRecordService.doSave(dRecord);
				logService.doAddLog("TbMetalPlateDefectiveRecord", dRecord);
			}
			TbMetalPlateProductionRecord productionRecord = productionRecordService.findById(id);
			if (sunDefectiveNumber.intValue() != 0) {
				productionRecord.setDefectiveNumber(sunDefectiveNumber);
				productionRecord.setStatus(ConstantUtils.BJ_BAD_STATUS_1);
				productionRecordService.doUpdate(productionRecord);
			}
		} catch (Exception e) {
			throw e;
		}
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateDefectiveRecordInput")
	public ModelAndView updateInput(HttpServletResponse response,ModelMap modelMap, String id) {
		TbMetalPlateProductionRecord productionRecord = productionRecordService.findById(Integer.parseInt(id));
		modelMap.addAttribute("practicalCycles", productionRecord.getPracticalCycles());
		TbMetalPlateDefectiveRecord eg = new TbMetalPlateDefectiveRecord();
		eg.setProductionRecordId(Integer.parseInt(id));
		List<TbMetalPlateDefectiveRecord> defectiveRecords = defectiveRecordService.findByEg(eg);
		modelMap.addAttribute("defectiveRecords", defectiveRecords);
		modelMap.addAttribute("indexSize",defectiveRecords.size());
		modelMap.addAttribute("smBadType", badPartsService.queryDictItem(new QueryCriteria()));
		modelMap.addAttribute("recordId", id);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/metalPlate/production-records/pr_dys_update"));
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateDefectiveRecord")
	public JsonActionResult updateDefectiveRecord( HttpServletResponse response, Integer id,String parameters) throws Exception {
		try {
			//修改不良，默认先删除所有不良项在进行新增
			if(null != id) {
				TbMetalPlateDefectiveRecord eg = new TbMetalPlateDefectiveRecord();
				eg.setProductionRecordId(id);
				List<TbMetalPlateDefectiveRecord> defectives = defectiveRecordService.findByEg(eg);
				for(TbMetalPlateDefectiveRecord defective:defectives) {
					defectiveRecordService.doDeleteById(defective.getId());
				}
			}
			TbMetalPlateDefectiveRecord dRecord = null;
			JSONArray parameteJsonArray = JSONArray.fromObject(parameters);
			Integer sunDefectiveNumber = 0;
			for(int i=0;i< parameteJsonArray.size();i++) {
				JSONObject object = parameteJsonArray.getJSONObject(i);
				String ids = object.get("defectiveProject").toString();
				Integer defectiveNumber = Integer.valueOf(object.get("defectiveNumber").toString());
				String defectiveProjectText = object.get("defectiveProjectText").toString();
				sunDefectiveNumber+=defectiveNumber;
				dRecord = new TbMetalPlateDefectiveRecord();
				dRecord.setProductionRecordId(id);
				dRecord.setDefectiveProject(defectiveProjectText);
				dRecord.setDefectiveNumber(defectiveNumber);
				dRecord.setTmBadpartsIds(ids);
				defectiveRecordService.doSave(dRecord);
				logService.doAddLog("TbMetalPlateDefectiveRecord", dRecord);
			}
			if (sunDefectiveNumber.intValue() != 0) {
				TbMetalPlateProductionRecord productionRecord = productionRecordService.findById(id);
				productionRecord.setDefectiveNumber(sunDefectiveNumber);
				productionRecord.setStatus(ConstantUtils.BJ_BAD_STATUS_1);
				productionRecordService.doUpdate(productionRecord);
			}
		} catch (Exception e) {
			throw e;
		}
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkoutStatus")
	public JsonActionResult checkoutStatus(HttpServletResponse response, Integer id,String type) {
		TbMetalPlateProductionRecord productionRecord = productionRecordService.findById(id);
		if(!ConstantUtils.BJ_BAD_STATUS_1.equals(productionRecord.getStatus()) && type.equals("update")) {
			throw new BusinessException("ERROR_KEY","只能编辑状态为[不良待确认的工单]");
		}else if(!ConstantUtils.BJ_BAD_STATUS_0.equals(productionRecord.getStatus()) && type.equals("add")){
			throw new BusinessException("ERROR_KEY","只能录入状态为[生产完成]的工单。");
		}
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/confirmDefectiveRecord")
	public JsonActionResult confirmDefectiveRecord( HttpServletResponse response, Integer id) throws Exception {
		//TODO不良确认，以及之后需要实时计算库存的情况下，只有在进行不良确认在计算库存
		TbMetalPlateProductionRecord productionRecord = productionRecordService.findById(id);
		if(!ConstantUtils.BJ_BAD_STATUS_1.equals(productionRecord.getStatus())) {
			throw new BusinessException("ERROR_KEY","不良确认只能确认状态为[不良待确认的工单]");
		}
		productionRecord.setStatus(ConstantUtils.BJ_BAD_STATUS_3);
		productionRecordService.doUpdate(productionRecord);
		TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
		eg.setMachiningSurface(productionRecord.getModel());
		List<TmSheetMetalMaterial> materialList = materialService.findByEg(eg);
		if(null != materialList && materialList.size() > 0) {
			TmSheetMetalMaterial material = materialList.get(0);
			//剩余库存=剩余库存-不良品
			Integer inventoryNumber = material.getInventoryNumber();
			Integer defectiveNumber = productionRecord.getDefectiveNumber();
			material.setInventoryNumber(inventoryNumber-defectiveNumber);
			materialService.doUpdate(material);
		}
		return ActionUtils.handleResult(response);
	}
	
	private void getMetalMaterialEntry(ModelMap modelMap) {
		modelMap.addAttribute("smToolNumber", entryService.getEntryByTypeCode(ConstantUtils.SM_TOOL_NUMBER));
		modelMap.addAttribute("smCodeClassOrder", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("smProductionState", entryService.getEntryByTypeCode(ConstantUtils.SM_PRODUCTION_STATE));
		modelMap.addAttribute("smSourceDataType", entryService.getEntryByTypeCode(ConstantUtils.SM_SOURCE_DATA_TYPE));
		modelMap.addAttribute("smRegionMark", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
	}

}
