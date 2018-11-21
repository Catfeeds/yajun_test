package com.wis.mes.master.plant.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;

@Controller
@RequestMapping(value = "/plant")
public class TmPlantController extends BaseController {

	@Autowired
	private TmPlantService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("plant", service.getDictItem(null));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/plant/plant_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "nameCn","no" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/plant/plant_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmPlant bean) throws Exception {
		try {
			bean = service.doSave(bean);
		} catch (Exception e) {
			String uks = getMessage(request, "PLANT_NO_UNIQUE");
			String[] ukArr = uks.split(",");
			for (int i = 0; i < ukArr.length; i++) {
				if (e.getMessage().contains(ukArr[i])) {
					throw new BusinessException(ukArr[i]);
				}
			}
			throw e;
		}
		logService.doAddLog("TmPlant", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("plant", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/plant/plant_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmPlant bean) {
		TmPlant before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmPlant", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmPlant> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "PLANT_FKS"); //获取工厂下所有的外键
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
			return ActionUtils.handleResult(false,"请先删除该公司下面的子数据");
		}

		logService.doDeleteLog("TmPlant", list);
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
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("PLANT_IMPORT_FAIL");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "nameCn" }));
		final PageResult<TmPlant> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmPlant> list = findBy == null ? new ArrayList<TmPlant>() : findBy.getContent();
		String[] header = new String[] { "编号", "中文名称", "英文名称", "中文简称", "英文简称", "中文地址1", "中文地址2", "英文地址1", "英文地址2",
				"启用" };
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "工厂模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "plant" + File.separator + "plant.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
