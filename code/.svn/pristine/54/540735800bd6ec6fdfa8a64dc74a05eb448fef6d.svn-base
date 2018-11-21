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
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/ncGroup")
public class TmNcGroupController extends BaseController {

	@Autowired
	private TmNcGroupService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmLineService tmLineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		return new ModelAndView("/master/nc/nc_main");
	}
	@RequestMapping(value = "/ncGroupInput")
	public ModelAndView ncGroupInput(Integer currentPageId,final ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		return new ModelAndView("/master/nc/nc_group_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(final HttpServletResponse response, final BootstrapTableQueryCriteria criteria,
			final ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "remarks" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		ActionUtils.addUserDataPermission(criteria,"tmLineId");
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		modelMap.addAttribute("lines", tmLineService.queryDictItem(new QueryCriteria()));
		return new ModelAndView("/master/nc/nc_group_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(final HttpServletRequest request, final HttpServletResponse response, TmNcGroup bean) {
		try {
			// 校验
			service.checkUnique(bean, ConstantUtils.STRING_ADD,
					getMessage(request, new String[] { ConstantUtils.STRING_OPERATION_ERROR_EXSIT }));
			bean = service.doSave(bean);
			logService.doAddLog(ConstantUtils.AUDIT_TYPE_NC_GROUP, bean);
			return ActionUtils.handleResult(response);
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE);
		}
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(final HttpServletResponse response, final String id, final ModelMap modelMap) {
		modelMap.addAttribute("ncGroup", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("lines", tmLineService.queryDictItem(new QueryCriteria()));
		return new ModelAndView("/master/nc/nc_group_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(final HttpServletRequest request, final HttpServletResponse response,
			final TmNcGroup bean) {
		try {
			// 校验
			service.checkUnique(bean, ConstantUtils.STRING_UPDATE,
					getMessage(request, new String[] { ConstantUtils.STRING_OPERATION_ERROR_EXSIT }));
			final TmNcGroup before = service.findById(bean.getId());
			service.doUpdate(bean);
			logService.doUpdateLog(ConstantUtils.AUDIT_TYPE_NC_GROUP, before, bean);
			return ActionUtils.handleResult(response);
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			throw new BusinessException(ConstantUtils.STRING_OPERATION_ERROR_TITLE);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(final HttpServletRequest request, final HttpServletResponse response, String ids) {

		Integer[] deleteIds = getIds(ids);
		List<TmNcGroup> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
			logService.doDeleteLog(ConstantUtils.AUDIT_TYPE_NC_GROUP, list);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			String[] foreignKeys = getMessage(request, "NC_GROUP_FOREIGN_KEY").split(",");
			if (StringUtil.isNotBlank(errMsg)) {
				for (String foreignKey : foreignKeys) {
					if (errMsg.indexOf(foreignKey) != -1) {
						throw new BusinessException(foreignKey);
					}
				}
			}
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importDataNcGroup")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = service.doImportExcelData(book, request);
			return ActionUtils.handleResult(true,msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE", e.getParams()[0]);
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_NC_Group");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setUseCacheSql(false);
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "remarks" }));
		downName = LoadUtils.urlDecoder(downName);
		ActionUtils.addUserDataPermission(criteria,"tmLineId");
		final String[] header = new String[] {"产线", "故障组编号", "故障组描述", "备注" };
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "故障组模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "nc" + File.separator + "ncGroup.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request,response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult ExportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException  {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		ActionUtils.addUserDataPermission(criteria,"tmLineId");
		final PageResult<TmNcGroup> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String parentHeader = "id,产线,故障组编号,故障组描述,备注";
		String childHeader = "故障编号,故障描述,故障等级,备注";
		service.exportExcelDataAll(response,findBy.getContent(), parentHeader, childHeader, downName + ".xlsx");
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
			pojo.setParentClassName("com.wis.mes.master.nc.entity.TmNcGroup");
			pojo.setChildClassName("com.wis.mes.master.nc.entity.TmNc");
			pojo.setParentHeader(getMessage(request, "NC_GROUP_IMPORT_TITLE").split(","));
			pojo.setChildHeader(getMessage(request, "NC_IMPORT_TITLE").split(","));
			pojo.setParentField(getMessage(request, "NC_GROUP_IMPORT_ENTITY").split(","));
			pojo.setChildField(getMessage(request, "NC_IMPORT_ENTITY").split(","));
			result = service.doImportExcelDataAll(book, pojo, request);
		} catch (final BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("NC_GROUP_IMPORT_ALL_FAIL", e.getParams()[0]);
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("NC_GROUP_IMPORT_ALL_FAIL");
		}
		return ActionUtils.handleResult(true, result);
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportTemplateAll")
	public JsonActionResult exportTemplateAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "故障组&故障代码导入模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "nc" + File.separator + "ncGroupAll.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request,response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
