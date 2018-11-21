package com.wis.mes.master.bom.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.bom.entity.TmBom;
import com.wis.mes.master.bom.service.TmBomService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Controller
@RequestMapping(value = "/bom")
public class TmBomController extends BaseController {

	@Autowired
	private TmBomService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workShopService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPartService partService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		//去掉不必要的字典参数  -zhuzw 2018/3/19
		//modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("workShopOptions", workShopService.getDictItem(null));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(null));
		//modelMap.addAttribute("bomVersionOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		//modelMap.addAttribute("statusOptions",
		//		entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/bom/bom_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("backNumber","machineOfName","machineName"));
		criteria.setUseCacheSql(false);
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//去掉不必要的字典参数  -zhuzw 2018/3/19
		//modelMap.addAttribute("partOptions", partService.getDictItem(
		//		new TmPart(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED, ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("workShopOptions",
		//		workShopService.getDictItem(new TmWorkshop(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(new TmLine(ConstantUtils.TYPE_CODE_ENABLED_ON)));

		//modelMap.addAttribute("bomVersionOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		//modelMap.addAttribute("statusOptions",
		//		entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/bom/bom_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmBom bean, HttpServletRequest request) throws Exception {
		logService.doAddLog("TmBom", service.doSaveBom(bean, getMessage(request, "BOM_VERSION_UNIQUE")));
		return ActionUtils.handleResult(response);
	}

	/**
	 * 复制新增controlle
	 * @param response
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value ="/copyaddInput")
	public ModelAndView copyAddInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmBom bom=service.findById(Integer.valueOf(id));
		modelMap.addAttribute("bom", bom);
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("workShopOptions",
		//		workShopService.getDictItem(new TmWorkshop(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(new TmLine(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("bomVersionOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		//modelMap.addAttribute("statusOptions",
		//		entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/bom/bom_copy_add");
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bom", service.findById(Integer.valueOf(id)));
		//去掉不必要的字典参数  -zhuzw 2018/3/19
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("workShopOptions",
		//		workShopService.getDictItem(new TmWorkshop(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(new TmLine(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		//modelMap.addAttribute("bomVersionOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		//modelMap.addAttribute("statusOptions",
		//		entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/bom/bom_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmBom bean) {
		TmBom before = service.findById(bean.getId());
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getMaxFlag())
				&& ConstantUtils.TYPE_CODE_ENABLED_ON.equals(bean.getEnabled())) {
			TmBom bom = new TmBom();
			bom.setMaxFlag(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
			bom.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
			bom.setTmPartId(bean.getTmPartId());
			List<TmBom> bomEg = service.findByEg(bom);
			if (bomEg.size() > 0 && !bomEg.get(0).getId().toString().equals(bean.getId().toString())) {
				throw new BusinessException("BOM_UNIQUNE_ERROR");
			}
		}
		service.doUpdate(bean);
		logService.doUpdateLog("TmBom", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmBom> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
			logService.doDeleteLog("TmBom", list);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			String[] foreignKeys = getMessage(request, "BOM_FOREIGN_KEY").split(",");
			if (StringUtils.isNotBlank(errMsg)) {
				for (String foreignKey : foreignKeys) {
					if (errMsg.indexOf(foreignKey) != -1) {
						throw new BusinessException(foreignKey);
					}
				}
				throw e;
			}
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/doDuplicate")
	public JsonActionResult doDuplicate(HttpServletRequest request, HttpServletResponse response,TmBom bom)
			throws Exception {
		String[] errMsg = { "BOM_VERSION_UNIQUE" };
		 bom = service.doDuplicate(bom, getMessage(request, errMsg));
		logService.doAddLog("TmBom", bom);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 根据工厂查询物料信息
	 * 
	 * @param plantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectPartByPlantId")
	public List<DictEntry> selectPartByPlantId(Integer plantId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<String> typeList = new ArrayList<String>();
		typeList.add(ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED);
		typeList.add(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED);
		paramMap.put("TYPE", typeList);
		paramMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		paramMap.put("TM_PLANT_ID", plantId);
		return partService.getDictItem(paramMap);
	}

	/**
	 * 根据工厂查询车间信息
	 * 
	 * @param plantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectWorkShopByPlantId")
	public List<DictEntry> selectWorkShopByPlantId(Integer plantId) {
		TmWorkshop workShop = new TmWorkshop();
		workShop.setTmPlantId(plantId);
		workShop.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return workShopService.getDictItem(workShop);
	}

	/**
	 * 根据工厂和车间查询产线信息
	 * 
	 * @param plantId
	 * @param workShopId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectLineByWorkShopIdAndPlantId")
	public List<DictEntry> selectLineByWorkShopIdAndPlantId(Integer plantId, Integer workShopId) {
		TmLine line = new TmLine();
		line.setTmPlantId(plantId);
		line.setTmWorkshopId(workShopId);
		line.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return lineService.getDictItem(line);
	}

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
		final PageResult<TmBom> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "工厂", "成品", "车间", "产线", "版本号", "状态", "是否最大版本", "启用" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	/**
	 * @author ryy
	 * @date 2017-06-05
	 * @desc BOM&BOM明细导出
	 */
	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult ExportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		final PageResult<TmBom> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmBom> list = findBy == null ? new ArrayList<TmBom>() : findBy.getContent();
		String parentHeader = getMessage(request, "BOM_EXPORT_TITLE");
		String childHeader = getMessage(request, "BOM_DETAIL_EXPORT_TITLE");
		service.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

	/**
	 * 数据导入 ryy 17/05/18
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
	 * @author ryy
	 * @date 2016年6月12日
	 * @desc BOM&BOM明细联表导入
	 */
	@ResponseBody
	@RequestMapping(value = "/importDataAll")
	public JsonActionResult importDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		String result = null;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			ExcelImportPojo pojo = new ExcelImportPojo();
			pojo.setParentClassName("com.wis.mes.master.bom.entity.TmBom");
			pojo.setChildClassName("com.wis.mes.master.bom.entity.TmBomDetail");
			pojo.setParentHeader(getMessage(request, "BOM_IMPORT_TITLE").split(","));
			pojo.setChildHeader(getMessage(request, "BOM_DETAIL_IMPORT_TITLE").split(","));
			pojo.setParentField(getMessage(request, "BOM_IMPORT_ENTITY").split(","));
			pojo.setChildField(getMessage(request, "BOM_DETAIL_IMPORT_ENTITY").split(","));
			result = service.doImportExcelDataAll(book, pojo, request);
		} catch (final BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("IMPORT_FAIL_INFO", e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("BOM_IMPORT_ALL_FAIL", getMessage(request, "IMPORT_UNKNOWN_EXCEPTION"));
		}
		return ActionUtils.handleResult(true, result);
	}

	/**
	 * @desc 主表模板导出
	 * @author ryy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "BOM模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "bom" + File.separator + "bom.xlsx";
			final Workbook wb = service.getWorkbookTemp(templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	/**
	 * @desc 全部关联表模板导出
	 * @author ryy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplateAll")
	public JsonActionResult exportTemplateAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "BOM&BOM明细联表模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "bom" + File.separator + "bom_all.xlsx";
			final Workbook wb = service.getWorkbookTemp(templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

}
