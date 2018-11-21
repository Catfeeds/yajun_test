package com.wis.mes.master.equipment.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;
import com.wis.mes.util.CheckImageUtils;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/equipment")
public class TmEquipmentController extends BaseController {

	@Autowired
	private TmEquipmentService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workShopService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, Integer id) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		return new ModelAndView("/master/equipment/equipment_main");
	}
	
	@RequestMapping(value = "/equipmentListInput")
	public ModelAndView porderListInput(Integer currentPageId,ModelMap modelMap ,HttpServletRequest request) {
		modelMap.addAttribute("operation", request.getParameter("operation"));
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("optionsPlant", plantService.getDictItem(null));
		modelMap.addAttribute("workShopOptions", workShopService.getDictItem(null));
		modelMap.addAttribute("ulocOptions", ulocService.getUlocAll());
		modelMap.addAttribute("optionEN", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_TYPE));
		modelMap.addAttribute("optionST", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_STATUS));
		modelMap.addAttribute("optionENAB", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("optionEquipmentStatus", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_STATUS));
		modelMap.addAttribute("optionEquipmentType", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_TYPE));
		return new ModelAndView("/master/equipment/equipment_list");
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "description" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setQueryRelationEntity(true);
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.DESC : OrderEnum.ASC);
		if (criteria.getQueryCondition().get("no") != null
				&& !ConstantUtils.STRING_EMPTY.equals(criteria.getQueryCondition().get("no"))) {
			String no = (String) criteria.getQueryCondition().get("no");
			String criNo = no.trim();
			criteria.getQueryCondition().put("no", criNo);
		}
		if (criteria.getQueryCondition().get("name") != null
				&& !ConstantUtils.STRING_EMPTY.equals(criteria.getQueryCondition().get("name"))) {
			String name = (String) criteria.getQueryCondition().get("name");
			String criName = name.trim();
			criteria.getQueryCondition().put("name", criName);
		}
		if (criteria.getQueryCondition().get("description") != null
				&& !ConstantUtils.STRING_EMPTY.equals(criteria.getQueryCondition().get("description"))) {
			String description = (String) criteria.getQueryCondition().get("description");
			String criDescription = description.trim();
			criteria.getQueryCondition().put("description", criDescription);
		}
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	
	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			TmEquipment equipment,String positionId) {
		modelMap.addAttribute("plant", plantService.getDictItem(null));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("optionEquipmentStatus", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_STATUS));
		modelMap.addAttribute("optionEquipmentType", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_TYPE));
		modelMap.addAttribute("optionProductStatus",entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_STATUS_PRODUCT));
		return new ModelAndView("/master/equipment/equipment_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmEquipment bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TmEquipment", bean);
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		 TmEquipment equipment =  service.findById(Integer.valueOf(id));
		modelMap.addAttribute("equipment",equipment );
		modelMap.addAttribute("plant", plantService.getDictItem(null));
		/*modelMap.addAttribute("workshop", workShopService.getDictItem(null));*/
		modelMap.addAttribute("optionEquipmentType", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_TYPE));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("optionEquipmentStatus", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_NAME_STATUS));
		modelMap.addAttribute("optionProductStatus",entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_STATUS_PRODUCT));
			TmUloc tmUloc = new TmUloc();
			tmUloc.setTmPlantId(equipment.getTmPlantId());
			modelMap.addAttribute("optionUloc", ulocService.getDictItem(tmUloc));
		return new ModelAndView("/master/equipment/equipment_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmEquipment bean) {
		TmEquipment before = service.findById(bean.getId());
		if(StringUtil.isBlank(bean.getStatus())){
			bean.setStatus("");
		}
		if(StringUtil.isBlank(bean.getProductStatus())){
			bean.setProductStatus("");
		}
		
		service.doUpdate(bean);
		logService.doUpdateLog("TmEquipment", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmEquipment> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmEquipment", list);
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/selectWorkShopNameOrID")
	public List<TmWorkshop> findWorkShopNameOrID(Integer plantId) {
		return workShopService.findWShopNameOrIdById(plantId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/selectUlocNameOrID")
	public List<TmUloc> findUlocNameOrID(Integer tmPlantId) {
		TmUloc tmUloc = new TmUloc();
		tmUloc.setTmPlantId(tmPlantId);
		return ulocService.findByEg(tmUloc);
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkNO")
	public Map<String,String> checkNO(HttpServletResponse response,String no){
		Map<String,String>  map = new HashMap<String,String>();
		TmEquipment tmEquipment = new TmEquipment();
		tmEquipment.setNo(no);
		List<TmEquipment> tmEquipmentList = service.findByEg(tmEquipment);//校验设备编号是否已存在
		if(tmEquipmentList.size()>0){
			map.put("message", "error");
		}else{
			map.put("message", "success");
		}
		return map;
	}
	
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping(value = "/importImage")
	public JsonActionResult importImage(MultipartFile rawFile,HttpServletRequest request,
			HttpServletResponse response, Integer tmEquipmentId) {
		//获取上传图片存储的路径
		String path=globalConfigurationService.getValueByKey(ConstantUtils.IMG_URL_FOR_SYSTEM)
				+ new Date().getTime()+rawFile.getOriginalFilename();
		String realPath = request.getRealPath("/")+path; 
		
		try {
			//校验上传的图片是否合法
			String	msg = CheckImageUtils.checkImage(rawFile,realPath);
			if("".equals(msg)){
					 TmEquipment tmEquipment = new TmEquipment();
					 tmEquipment.setId(tmEquipmentId);
					 List<TmEquipment> equipmentList = service.findByEg(tmEquipment);
					 String newPath = StringUtils.replace(path,"\\", "/");
					 equipmentList.get(0).setPictureAddr(newPath);
					 service.doUpdate(equipmentList.get(0));
				 }
				return ActionUtils.handleResult(true,msg);
		} catch (Exception e) {
			throw new BusinessException("IMPORT_IMAGE_FAIL");
		}
	}

	/**
	 * 数据导入 ryy 17/06/22
	 */
	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {

		Workbook book;
		String msg = null;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			msg = service.doImportExcelData(book, request);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE", e.getParams()[0]);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market:" + e.getMessage(), e);
			throw new BusinessException("BOM_IMPORT_FAIL");
		}
		return ActionUtils.handleResult(true, msg);
	}

	/**
	 * @desc 导出
	 * @author ryy
	 */
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmEquipment> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "事部",  "编号", "名称", "状态", "IP地址", "PLC型号", "PLC品牌", "网络模块型号", "物理位置","设备供应商", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	/**
	 * @desc 下载模板
	 * @author ryy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "设备信息模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "equipment" + File.separator + "equipment.xlsx";
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

}
