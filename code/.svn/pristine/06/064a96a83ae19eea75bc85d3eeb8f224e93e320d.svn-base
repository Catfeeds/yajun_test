package com.wis.mes.master.supplier.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.supplier.entity.TmSupplierPart;
import com.wis.mes.master.supplier.service.TmSupplierPartService;

@Controller

@RequestMapping(value = "/supplierPart")
public class TmSupplierPartController extends BaseController {

	@Autowired
	private TmSupplierPartService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, Integer tmSupplierId) {
		modelMap.addAttribute("tmSupplierId", tmSupplierId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/supplier/supplier_part/supplier_part_list");
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
		PageResult<TmSupplierPart> findBy = service.findBy(criteria);
		return ActionUtils.handleListResult(response, findBy);
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap, Integer tmSupplierId) {
		modelMap.addAttribute("tmSupplierId", tmSupplierId);
		return new ModelAndView("/master/supplier/supplier_part/supplier_part_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, Integer tmSupplierId, String[] tmPartId,
			String[] enabled) {
		List<TmSupplierPart> tmSupplierParts = service.doSaveBatch(tmSupplierId, tmPartId, enabled);
		logService.doAddLog("TmSupplierPart", tmSupplierParts);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/partListInput")
	public ModelAndView partListInput(HttpServletRequest request, ModelMap modelMap, Integer tmSupplierId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		List<DictEntry> typeEntrys = entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE);
		Iterator<DictEntry> it = typeEntrys.iterator();
		while (it.hasNext()) {
			DictEntry next = it.next();
			if (next.getCode().equals(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED)) {
				it.remove();
			}
		}
		modelMap.addAttribute("typeOptions", typeEntrys);
		modelMap.addAttribute("tmSupplierId", tmSupplierId);
		return ActionUtils.handleSelectResult(request, modelMap,
				new ModelAndView("/master/supplier/supplier_part/supplier_part_select"));
	}

	@ResponseBody
	@RequestMapping(value = "/getPartList")
	public JsonActionResult getPartList(final HttpServletResponse response, final BootstrapTableQueryCriteria criteria,
			final ModelMap modelMap, Integer tmSupplierId) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "nameCn", "type" }));
		criteria.getQueryCondition().put("tmSupplierId", tmSupplierId);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.getPartPageResult(criteria));
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap, Integer tmSupplierId) {
		modelMap.addAttribute("supplierPart", service.findById(Integer.valueOf(id), true));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("tmSupplierId", tmSupplierId);
		return new ModelAndView("/master/supplier/supplier_part/supplier_part_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmSupplierPart bean) {
		TmSupplierPart before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmSupplierPart", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmSupplierPart> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmSupplierPart", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/exportData")
	@ResponseBody
	public JsonActionResult ExportData(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			String downName, final String ids, String tmSupplierId) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmSupplierId", tmSupplierId);
		criteria.setOrderField(criteria.getSort() == null ? "id" : criteria.getSort());
		final PageResult<TmSupplierPart> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		final String[] header = new String[] { "零件编号", "零件名称", "类型", "启用" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

}
