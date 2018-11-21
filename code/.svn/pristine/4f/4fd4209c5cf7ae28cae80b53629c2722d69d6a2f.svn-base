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
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.uloc.entity.TmUlocSip;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.uloc.service.TmUlocSipService;

@Controller
@RequestMapping(value = "/ulocSip")
public class TmUlocSipController extends BaseController {

	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TsParameterService parameterService;
	@Autowired
	private TmUlocSipService ulocSipService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, Integer tmUlocId) {
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/uloc/sip/uloc_sip_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort() == null ? "id" : criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, ulocSipService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer tmUlocId) {
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("parameterOptions", parameterService.getDictItem(null));// 获得参数列表
		modelMap.addAttribute("ulocOptions", ulocService.findById(tmUlocId));// 获得工位信息
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/uloc/sip/uloc_sip_add");
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public JsonActionResult add(HttpServletResponse response, TmUlocSip ulocSip) {
		TmUlocSip eg = new TmUlocSip();
		eg.setTmUlocId(ulocSip.getTmUlocId());
		eg.setTsParameterId(ulocSip.getTsParameterId());
		List<TmUlocSip> list = ulocSipService.findByEg(eg);
		// 判断（工位+参数）是否重复
		if (list.size() > 0) {
			throw new BusinessException("ULOC_SIP_DATA_REPEAT");
		}
		ulocSip = ulocSipService.doSave(ulocSip);
		logService.doAddLog("TmUlocSip", ulocSip);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmUlocSip ulocSip = ulocSipService.findById(Integer.valueOf(id));
		modelMap.addAttribute("ulocSip", ulocSip);
		modelMap.addAttribute("uloc", ulocService.findById(ulocSip.getTmUlocId()));// 获得工位信息
		modelMap.addAttribute("parameters", parameterService.getDictItem(null));
		modelMap.addAttribute("yesOrNoOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));// 是否
		return new ModelAndView("/master/uloc/sip/uloc_sip_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmUlocSip bean) {
		TmUlocSip before = ulocSipService.findById(bean.getId());
		bean.setTmUlocId(before.getTmUlocId());
		ulocSipService.doUpdate(bean);
		logService.doUpdateLog("TmUlocSip", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request,HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmUlocSip> list = ulocSipService.findAllInIds(deleteIds);
		try {
			ulocSipService.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "ULOC_SIP_FKS");
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
		}
		logService.doDeleteLog("TmUloc", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/exportData")
	@ResponseBody
	public JsonActionResult ExportData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			String downName, final String ids, String tmUlocId) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmUlocId", tmUlocId);
		criteria.setOrderField(criteria.getSort() == null ? "id" : criteria.getSort());
		final PageResult<TmUlocSip> findBy = ulocSipService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		final String[] header = new String[] { "参数", "详细信息", "是否必检" };
		ulocSipService.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile, String tmUlocId) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = ulocSipService.doImportExcelData(book, request, tmUlocId);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("ULOC_SIP_IMPORT_FAIL");
		}
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
				downName = "工位质检项模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "uloc" + File.separator + "ulocSip.xlsx";
			final Workbook wb = ulocSipService.getWorkbookTemp(downName, templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importDataAll")
	public JsonActionResult ImportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile, String tmUlocId) {
		Workbook book;
		String result = null;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			ExcelImportPojo pojo = new ExcelImportPojo();
			pojo.setParentClassName("com.wis.mes.master.uloc.entity.TmUlocSip");
			pojo.setChildClassName("com.wis.mes.master.uloc.entity.TmUlocSipNc");
			pojo.setParentHeader(getMessage(request, "ULOC_SIP_IMPORT_TITLE").split(","));
			pojo.setChildHeader(getMessage(request, "ULOC_SIP_NC_TITLE").split(","));
			pojo.setParentField(getMessage(request, "ULOC_SIP_IMPORT_ENTITY").split(","));
			pojo.setChildField(getMessage(request, "ULOC_SIP_NC_IMPORT_ENTITY").split(","));
			result = ulocSipService.doImportExcelDataAll(book, pojo, request, tmUlocId);
		} catch (final BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("ULOC_SIP_IMPORT_ALL_FAIL", e.getParams()[0]);
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("ULOC_SIP_IMPORT_ALL_FAIL");
		}
		return ActionUtils.handleResult(true, result);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplateAll")
	public JsonActionResult exportTemplateUlocAndSip(final HttpServletRequest request,
			final HttpServletResponse response, final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "工位级联导入模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "uloc" + File.separator + "ulocSipAndUlocSipNc.xlsx";
			final Workbook wb = ulocSipService.getWorkbookTemp(downName, templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult ExportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids, String tmUlocId) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.getQueryCondition().put("tmUlocId", tmUlocId);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmUlocSip> findBy = ulocSipService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmUlocSip> list = findBy == null ? new ArrayList<TmUlocSip>() : findBy.getContent();
		String parentHeader = getMessage(request, "ULOC_SIP_EXPORT_TITLE");
		String childHeader = getMessage(request, "ULOC_SIP_NC_TITLE");
		ulocSipService.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

}
