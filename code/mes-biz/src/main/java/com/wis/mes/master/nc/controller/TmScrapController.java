package com.wis.mes.master.nc.controller;

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
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.nc.entity.TmScrap;
import com.wis.mes.master.nc.service.TmScrapService;

@Controller
@RequestMapping(value = "/scrap")
public class TmScrapController extends BaseController {

	@Autowired
	private TmScrapService service;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		return new ModelAndView("/master/nc/scrap_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(final HttpServletResponse response, final BootstrapTableQueryCriteria criteria,
			final ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "note", "extCode" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		return new ModelAndView("/master/nc/scrap_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(final HttpServletRequest request, final HttpServletResponse response, TmScrap bean) {
		try {
			// 校验
			service.checkUnique(bean, ConstantUtils.STRING_ADD,
					getMessage(request, new String[] { ConstantUtils.STRING_OPERATION_ERROR_EXSIT }));
			bean = service.doSave(bean);
			logService.doAddLog(ConstantUtils.AUDIT_TYPE_SCRAP, bean);
			return ActionUtils.handleResult(response);
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE);
		}
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(final HttpServletResponse response, final String id, final ModelMap modelMap) {
		modelMap.addAttribute("scrap", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/master/nc/scrap_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(final HttpServletRequest request, final HttpServletResponse response,
			final TmScrap bean) {
		try {
			// 校验
			service.checkUnique(bean, ConstantUtils.STRING_UPDATE,
					getMessage(request, new String[] { ConstantUtils.STRING_OPERATION_ERROR_EXSIT }));
			final TmScrap before = service.findById(bean.getId());
			service.doUpdate(bean);
			logService.doUpdateLog(ConstantUtils.AUDIT_TYPE_SCRAP, before, bean);
			return ActionUtils.handleResult(response);
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(final HttpServletResponse response, final String ids) {

		Integer[] deleteIds = getIds(ids);
		List<TmScrap> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog(ConstantUtils.AUDIT_TYPE_SCRAP, list);
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
			return ActionUtils.handleResult(true,msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_NC");
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
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "note", "extCode" }));
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		final String[] header = new String[] { "报废编码", "报废描述", "对应外系统编码" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "报废代码模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "nc" + File.separator + "scrap.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request,response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
