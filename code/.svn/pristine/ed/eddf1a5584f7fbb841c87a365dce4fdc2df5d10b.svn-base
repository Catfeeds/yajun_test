package com.wis.mes.master.uloc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Controller

@RequestMapping(value = "/uloc")
public class TmUlocController extends BaseController {

	@Autowired
	private TmUlocService ulocservice;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService tmPlantService;
	@Autowired
	private TmWorkshopService tmWorkshopService;
	@Autowired
	private TmLineService tmLineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private TmEquipmentService equipmentService;
	@Autowired
	private RedisTemplate<String, TmUloc> template;
	
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/uloc/uloc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("no", "name"));
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "no" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(null != criteria.getSort()?criteria.getSort():"no");
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ?OrderEnum.DESC:OrderEnum.ASC );
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		return ActionUtils.handleListResult(response, ulocservice.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/uloc/uloc_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmUloc bean) {
		HashOperations<String, String,TmUloc> opsForHash = template.opsForHash();
		TmUloc eg = new TmUloc();
		eg.setTmLineId(bean.getTmLineId());
		eg.setNo(bean.getNo());
		List<TmUloc> list = ulocservice.findByEg(eg);
		if (list.size() > 0) {
			throw new BusinessException("U_TM_ULOC_LINE");
		}
		bean = ulocservice.doSave(bean);
		opsForHash.put(ConstantUtils.WIS_REDIS_ULOC_KEY, bean.getNo(), bean);
		logService.doAddLog("TmUloc", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("uloc", ulocservice.findById(Integer.valueOf(id)));
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/uloc/uloc_update");
	}

	
	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmUloc bean) {
		HashOperations<String, String,TmUloc> opsForHash = template.opsForHash();
		TmUloc before = ulocservice.findById(bean.getId());
		ulocservice.doUpdate(bean);
		opsForHash.put(ConstantUtils.WIS_REDIS_ULOC_KEY, bean.getNo(), bean);
		logService.doUpdateLog("TmUloc", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, HttpServletRequest request, String ids)
			throws Exception {
		HashOperations<String, String,TmUloc> opsForHash = template.opsForHash();
		Integer[] deleteIds = getIds(ids);
		List<TmUloc> list = ulocservice.findAllInIds(deleteIds);
		try {
			ulocservice.doDeleteById(deleteIds);
			for(Integer id:deleteIds){
				if(null != opsForHash.get(ConstantUtils.WIS_REDIS_ULOC_KEY, String.valueOf(id))){
					opsForHash.delete(ConstantUtils.WIS_REDIS_ULOC_KEY, String.valueOf(id));
				}
			}
		} catch (Exception e) {
			String fks = getMessage(request, "TM_ULOC_FOREIGN_KEY"); // 获取工位下所有的外键
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
			throw e;
		}
		logService.doDeleteLog("TmUloc", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 
	 * 数据导出
	 */
	@ResponseBody
	@RequestMapping(value = "/exportDataUloc")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmUloc> findBy = ulocservice.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmUloc> list = findBy == null ? new ArrayList<TmUloc>() : findBy.getContent();
		String[] header = new String[] { "工厂", "车间", "产线", "工位编号", "工位名称", "对外系统编码", "备注", "是否质检点", "启用" };
		ulocservice.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	/**
	 * exportTemplate 导出模版
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "工位模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "uloc" + File.separator + "uloc.xlsx";
			final Workbook wb = ulocservice.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		String msg = null;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			msg = ulocservice.doImportExcelData(book, request);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORKSHOP_IMPORT_FAIL");
		}
		return ActionUtils.handleResult(true, msg);
	}

	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult ExportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmUloc> findBy = ulocservice.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmUloc> list = findBy == null ? new ArrayList<TmUloc>() : findBy.getContent();
		String parentHeader = getMessage(request, "ULOC_EXPORT_TITLE");
		String childHeader = getMessage(request, "ULOC_SIP_EXPORT_TITLE");
		ulocservice.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importDataAll")
	public JsonActionResult ImportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		String result = null;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			ExcelImportPojo pojo = new ExcelImportPojo();
			pojo.setParentClassName("com.wis.mes.master.uloc.entity.TmUloc");
			pojo.setChildClassName("com.wis.mes.master.uloc.entity.TmUlocSip");
			pojo.setParentHeader(getMessage(request, "ULOC_IMPORT_TITLE").split(","));
			pojo.setChildHeader(getMessage(request, "ULOC_SIP_IMPORT_TITLE").split(","));
			pojo.setParentField(getMessage(request, "ULOC_IMPORT_ENTITY").split(","));
			pojo.setChildField(getMessage(request, "ULOC_SIP_IMPORT_ENTITY").split(","));
			result = ulocservice.doImportExcelDataAll(book, pojo, request);
		} catch (final BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("ULOC_IMPORT_ALL_FAIL", e.getParams()[0]);
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("ULOC_IMPORT_ALL_FAIL");
		}
		return ActionUtils.handleResult(true, result);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplateUlocAndSip")
	public JsonActionResult exportTemplateUlocAndSip(final HttpServletRequest request,
			final HttpServletResponse response, final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "工位级联导入模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "uloc" + File.separator + "ulocAndUlocSip.xlsx";
			final Workbook wb = ulocservice.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/selectWShopNameOrID")
	public List<TmWorkshop> findWorkShopNameOrID(@Param(value = "plantId") final Integer plantId) {
		final List<TmWorkshop> findWorkShopNameOrID = tmWorkshopService.findWShopNameOrIdById(plantId);
		return findWorkShopNameOrID;
	}

	@ResponseBody
	@RequestMapping(value = "/selectLintNameOrPlantId")
	public List<DictEntry> findLIneNameOrPlantId(@Param(value = "plantId") final String plantId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.getQueryCondition().put("tmPlantId", plantId);
		return tmLineService.queryDictItem(criteria);
	}
	
	private void modelMapAddAttribute(ModelMap modelMap) {
		modelMap.addAttribute("plants", tmPlantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("lines", tmLineService.queryDictItem(new QueryCriteria()));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("positionOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_POSITION));
		modelMap.addAttribute("entranceOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENTRANCE));
		modelMap.addAttribute("optionEquipment", equipmentService.getDictItem(null));
	}

}
