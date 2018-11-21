package com.wis.mes.master.workshop.controller;

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
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Controller
@RequestMapping(value = "/workshop")
public class TmWorkshopController extends BaseController {

	@Autowired
	private TmWorkshopService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService tmPlantService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getPlantWorkshop(modelMap);
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/workshop/workshop_list");
	}

	private ModelMap getPlantWorkshop(ModelMap modelMap) {
		TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		TmWorkshop workshop = new TmWorkshop();
		workshop.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("plant", tmPlantService.getDictItem(tmPlant));
		modelMap.addAttribute("workshop", service.getDictItem(workshop));
		return modelMap;
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "nameCn" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("plant", tmPlantService.getDictItem(tmPlant));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/workshop/workshop_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmWorkshop bean)
			throws Exception {
		try {
			bean = service.doSave(bean);
		} catch (Exception e) {
			String uks = getMessage(request, "WORKSHOP_PLANTID_NO_UNIQUE");
			String[] ukArr = uks.split(",");
			for (int i = 0; i < ukArr.length; i++) {
				if (e.getMessage().contains(ukArr[i])) {
					throw new BusinessException(ukArr[i]);
				}
			}
			throw e;
		}
		logService.doAddLog("TmWorkshop", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("workshop", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("plant", tmPlantService.getDictItem(tmPlant));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/workshop/workshop_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmWorkshop bean) {
		TmWorkshop before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmWorkshop", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmWorkshop> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "WORKSHOP_FKS"); //获取车间下所有的外键
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
			throw e;
		}

		logService.doDeleteLog("TmWorkshop", list);
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
			msg = service.doImportExcelData(book, request);

		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORKSHOP_IMPORT_FAIL");
		}
		return ActionUtils.handleResult(true, msg);
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
		final PageResult<TmWorkshop> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmWorkshop> list = findBy == null ? new ArrayList<TmWorkshop>() : findBy.getContent();
		String[] header = new String[] { "工厂编号-名称", "车间编号", "车间中文名称", "车间英文名称", "车间中文简称", "车间英文简称", "启用" };
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "车间模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "workshop" + File.separator + "workshop.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
