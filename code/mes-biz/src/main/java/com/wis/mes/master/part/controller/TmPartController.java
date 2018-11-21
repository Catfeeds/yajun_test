package com.wis.mes.master.part.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;

@Controller
@RequestMapping(value = "/part")
public class TmPartController extends BaseController {

	@Autowired
	private TmPartService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("typeOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE));
		modelMap.addAttribute("uKUnitOptions", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_MAINTENANCE_TIME_TYPE));
		return new ModelAndView("/master/part/part_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(final HttpServletResponse response, final BootstrapTableQueryCriteria criteria,
			final ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "nameCn" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap) {
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("typeOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE));
		modelMap.addAttribute("baseUnitOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_BASE_UNIT));
		modelMap.addAttribute("uKUnitOptions", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_MAINTENANCE_TIME_TYPE));
		return new ModelAndView("/master/part/part_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(final HttpServletRequest request, final HttpServletResponse response, TmPart bean) {
		try {
			// 校验
			service.checkUnique(bean, ConstantUtils.STRING_ADD,
					getMessage(request, new String[] { "PART_UNIQUE_ERROR" }));
			bean = service.doSave(bean);
			logService.doAddLog(ConstantUtils.AUDIT_TYPE_PART, bean);
			return ActionUtils.handleResult(response);
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE);
		}
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap) {
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("typeOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE));
		modelMap.addAttribute("baseUnitOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_BASE_UNIT));
		modelMap.addAttribute("uKUnitOptions", entryService.getEntryByTypeCode(ConstantUtils.EQUIPMENT_MAINTENANCE_TIME_TYPE));
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/master/part/part_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(final HttpServletRequest request, final HttpServletResponse response,
			final TmPart bean) {
		try {
			// 校验
			service.checkUnique(bean, ConstantUtils.STRING_UPDATE,
					getMessage(request, new String[] { "PART_UNIQUE_ERROR" }));
			final TmPart before = service.findById(bean.getId());
			service.doUpdate(bean);
			logService.doUpdateLog(ConstantUtils.AUDIT_TYPE_PART, before, bean);
			return ActionUtils.handleResult(response);
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, final String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmPart> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
			logService.doDeleteLog(ConstantUtils.AUDIT_TYPE_PART, list);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			String[] foreignKeys = getMessage(request, "PART_FOREIGN_KEY").split(",");
			for (String foreignKey : foreignKeys) {
				if (errMsg.indexOf(foreignKey) != -1) {
					throw new BusinessException(foreignKey);
				}
			}
			throw e;
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = service.doImportExcelData(book, request);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market:" + e.getMessage(), e);
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "nameCn" }));
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		final String[] header = new String[] { "公司", "物料编号", "物料名称", "物料类型", "单位", "规格型号", "质保单位", "质保时间", "启用",
				"备注" };

		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 导出模版
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "物料模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "part" + File.separator + "part.xlsx";
			final Workbook wb = service.getWorkbook(templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
