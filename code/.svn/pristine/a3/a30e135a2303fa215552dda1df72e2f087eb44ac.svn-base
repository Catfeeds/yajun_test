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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.bom.entity.TmBom;
import com.wis.mes.master.bom.entity.TmBomDetail;
import com.wis.mes.master.bom.service.TmBomDetailService;
import com.wis.mes.master.bom.service.TmBomService;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;

@Controller
@RequestMapping(value = "/bomDetail")
public class TmBomDetailController extends BaseController {

	@Autowired
	private TmBomDetailService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmBomService bomService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap, Integer tmBomId, HttpServletRequest request) {
		modelMap.addAttribute("tmBomId", tmBomId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/bom/bom_detail_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("optionIsTrac", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("optionIsBatchrac", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/bom/bom_detail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmBomDetail bean)
			throws Exception {

		TmBom bom = bomService.findById(bean.getTmBomId());
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(bom.getStatus())) {
			throw new BusinessException("BOM_STATUS_FINSHED_ERROR");
		}
		try {
			bean = service.doSave(bean);
			logService.doAddLog("TmBomDetail", bean);
			return ActionUtils.handleResult(response);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtils.isNotBlank(errMsg) && errMsg.indexOf(getMessage(request, "BOM_DETAIL_UNIQUE")) != -1) {
				throw new BusinessException("BOM_DETAIL_UNIQUNE_ERROR");
			} else {
				throw e;
			}
		}
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("ulocOptions", ulocService.getDictItem(new TmUloc(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("optionIsTrac", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		modelMap.addAttribute("optionIsBatchrac", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		return new ModelAndView("/master/bom/bom_detail_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletRequest request, HttpServletResponse response, TmBomDetail bean)
			throws Exception {
		TmBomDetail before = service.findById(bean.getId());
		bean.setTmBomId(before.getTmBomId());
		TmBom bom = bomService.findById(bean.getTmBomId());
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(bom.getStatus())) {
			throw new BusinessException("BOM_STATUS_FINSHED_ERROR");
		}
		try {
			service.doUpdate(bean);
			logService.doUpdateLog("TmBomDetail", before, bean);
			return ActionUtils.handleResult(response);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtils.isNotBlank(errMsg) && errMsg.indexOf(getMessage(request, "BOM_DETAIL_UNIQUE")) != -1) {
				throw new BusinessException("BOM_DETAIL_UNIQUNE_ERROR");
			} else {
				throw e;
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmBomDetail> list = service.findAllInIds(deleteIds);
		TmBom bom = bomService.findById(list.get(0).getTmBomId());
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(bom.getStatus())) {
			throw new BusinessException("BOM_STATUS_FINSHED_ERROR");
		}
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmBomDetail", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids, String tmBomId) throws IOException {
		if (StringUtils.isNotBlank(tmBomId)) {
			criteria.getQueryCondition().put("tmBomId", tmBomId);
			if (StringUtils.isNotBlank(ids)) {
				criteria.getQueryCondition().put("idIN", ids);
			}
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmBomDetail> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] headers = new String[] { "工位", "物料", "数量", "顺序号", "是否物料追溯", "是否批次追溯" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", headers);
		ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/getUlocOptions")
	public List<DictEntry> getUlocOptions(Integer bomId) {
		TmBom bom = bomService.findById(bomId);
		TmUloc uloc = new TmUloc();
		uloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		uloc.setTmPlantId(bom.getTmPlantId());
		uloc.setTmWorkshopId(bom.getTmWorkshopId());
		uloc.setTmLineId(bom.getTmLineId());
		return ulocService.getDictItem(uloc);
	}

	@ResponseBody
	@RequestMapping(value = "/getPartOptions")
	public List<DictEntry> getPartOptions(Integer bomId) {
		TmBom bom = bomService.findById(bomId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<String> typeList = new ArrayList<String>();
		typeList.add(ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED);
		typeList.add(ConstantUtils.ENTRY_CODE_PART_TYPE_PARTS);
		paramMap.put("TYPE", typeList);
		paramMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		paramMap.put("TM_PLANT_ID", bom.getTmPlantId().toString());
		return partService.getDictItem(paramMap);
	}

	/**
	 * 
	 * 导出模版
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "BOM明细模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "bom" + File.separator + "bom_detail.xlsx";
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile, String tmBomId) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = service.doImportExcelData(book, request, tmBomId);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE", getMessage(request, e.getMessage()));
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("PLANT_IMPORT_FAIL");
		}
	}

}
