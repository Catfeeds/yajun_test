package com.wis.mes.master.line.controller;

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
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Controller
@RequestMapping(value = "/line")
public class TmLineController extends BaseController {

	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService tmPlantService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmWorkshopService tmWorkshopService;
	@Autowired
	private TmLineService lineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getPlantWorkshopLine(modelMap);
		return new ModelAndView("/master/line/line_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "nameCn", "no","remark" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, lineService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getPlantWorkshop(modelMap);
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/line/line_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmLine bean) throws Exception {
		try {
			bean = lineService.doSave(bean);
		} catch (Exception e) {
			String uks = getMessage(request, "LINENO_WORKSHOPID_UNIQUE");
			String[] ukArr = uks.split(",");
			for (int i = 0; i < ukArr.length; i++) {
				if (e.getMessage().contains(ukArr[i])) {
					throw new BusinessException(ukArr[i]);
				}
			}
			throw e;
		}

		logService.doAddLog("TmLine", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		getPlantWorkshop(modelMap);
		modelMap.addAttribute("line", lineService.findById(Integer.valueOf(id)));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/line/line_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmLine bean) {
		TmLine before = lineService.findById(bean.getId());
		lineService.doUpdate(bean);
		logService.doUpdateLog("TmLine", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmLine> list = lineService.findAllInIds(deleteIds);
		try {
			lineService.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "LINE_FKS"); // 获取产线下所有的外键
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
			throw e;
		}
		logService.doDeleteLog("TmLine", list);
		return ActionUtils.handleResult(response);
	}

	private ModelMap getPlantWorkshopLine(ModelMap modelMap) {
		TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		TmWorkshop workshop = new TmWorkshop();
		workshop.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		TmLine tmLine = new TmLine();
		tmLine.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("plant", tmPlantService.getDictItem(tmPlant));
		modelMap.addAttribute("workshop", tmWorkshopService.getDictItem(workshop));
		modelMap.addAttribute("line", lineService.getDictItem(tmLine));
		modelMap.addAttribute("optionEnab", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return modelMap;
	}

	@ResponseBody
	@RequestMapping(value = "/selectWShopNameOrID")
	public List<TmWorkshop> findWorkShopNameOrID(@Param(value = "plantId") final Integer plantId) {
		final List<TmWorkshop> findWorkShopNameOrID = tmWorkshopService.findWShopNameOrIdById(plantId);
		return findWorkShopNameOrID;
	}

	@ResponseBody
	@RequestMapping(value = "/selectLineNameOrId")
	public List<DictEntry> findLineNameOrID(String plantId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.getQueryCondition().put("tmPlantId", plantId);
		return lineService.queryDictItem(criteria);
	}

	/**
	 * 当产线为空时查询工位 ajax处理
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findLineNameOrId")
	public List<TmLine> findWorkSectionName() {
		final List<TmLine> findLineNameOrId = lineService.findLineNameOrId();
		return findLineNameOrId;

	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = lineService.doImportExcelData(book, request);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("LINE_IMPORT_FAIL");
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
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmLine> findBy = lineService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmLine> list = findBy == null ? new ArrayList<TmLine>() : findBy.getContent();
		String[] header = new String[] { "工厂", "车间", "产线编号", "中文名称", "英文名称", "启用" };
		lineService.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "产线模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "line" + File.separator + "line.xlsx";
			final Workbook wb = lineService.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	private ModelMap getPlantWorkshop(ModelMap modelMap) {
		TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		TmWorkshop workshop = new TmWorkshop();
		workshop.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("plant", tmPlantService.getDictItem(tmPlant));
		modelMap.addAttribute("workshop", tmWorkshopService.getDictItem(workshop));
		return modelMap;
	}
}
