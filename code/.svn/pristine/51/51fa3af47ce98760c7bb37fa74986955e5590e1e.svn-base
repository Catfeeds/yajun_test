package com.wis.mes.master.supplier.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.supplier.entity.TmSupplier;
import com.wis.mes.master.supplier.entity.TmSupplierPart;
import com.wis.mes.master.supplier.service.TmSupplierPartService;
import com.wis.mes.master.supplier.service.TmSupplierService;

@Controller

@RequestMapping(value = "/supplier")
public class TmSupplierController extends BaseController {

	@Autowired
	private TmSupplierService service;
	@Autowired
	private TmSupplierPartService partService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		List<DictEntry> typeEntrys = entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE);
		Iterator<DictEntry> it = typeEntrys.iterator();
		while (it.hasNext()) {
			DictEntry next = it.next();
			if (next.getCode().equals(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED)) {
				it.remove();
			}
		}
		modelMap.addAttribute("typeOptions", typeEntrys);
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/supplier/supplier_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "notes" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/supplier/supplier_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmSupplier bean) {

		try {
			
			bean = service.doSave(bean);
		} catch (Exception e) {
			String uks = getMessage(request, "SUPPLIER_NO_UNIQUE");
			String[] ukArr = uks.split(",");
			for (int i = 0; i < ukArr.length; i++) {
				if (e.getMessage().contains(ukArr[i])) {
					throw new BusinessException(ukArr[i]);
				}
			}
		}

		logService.doAddLog("TmSupplier", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("supplier", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/supplier/supplier_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmSupplier bean) {
		TmSupplier before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmSupplier", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, HttpServletRequest request, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmSupplier> list = service.findAllInIds(deleteIds);

		for (TmSupplier tmSupplier : list) {
			TmSupplierPart eg = new TmSupplierPart();
			eg.setTmSupplierId(tmSupplier.getId());
			List<TmSupplierPart> findByEg = partService.findByEg(eg);
			if (findByEg.size() != 0 && findByEg != null) {
				throw new BusinessException("TM_SUPPLIER_SUPPLIER_PART_CONSTRAINT");
			}
		}
		service.doRemoveByBatch(deleteIds);
		logService.doDeleteLog("TmSupplier", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 数据导出
	 * @author ryy
	 */
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "notes" }));
		final PageResult<TmSupplier> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmSupplier> list = findBy == null ? new ArrayList<TmSupplier>() : findBy.getContent();
		String[] header = new String[] { "供应商编号", "供应商名称", "地址", "联系人1", "电话号码1", "传真号码1", "手机号码1", "邮件地址1", "联系人2",
				"电话号码2", "传真号码2", "手机号码2", "邮件地址2", "备注", "启用" };
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	/**
	 * exportTemplate 下载模版
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "供应商信息导入模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "supplier" + File.separator + "supplier.xlsx";
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			LoadUtils.setContent(request, response, wb, downName);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new BusinessException("下载模板出错！");
		} catch (final IOException e) {
			e.printStackTrace();
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
			msg = service.doImportExcelData(book, request);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
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
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "notes" }));
		final PageResult<TmSupplier> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmSupplier> list = findBy == null ? new ArrayList<TmSupplier>() : findBy.getContent();
		String parentHeader = getMessage(request, "SUPPLIER_EXPORT_TITLE");
		String childHeader = getMessage(request, "SUPPLIER_PART_EXPORT_TITLE");
		service.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

}
