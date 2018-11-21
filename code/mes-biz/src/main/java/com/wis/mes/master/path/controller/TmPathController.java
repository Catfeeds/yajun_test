package com.wis.mes.master.path.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.master.path.entity.TmPathUlocSip;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.master.path.service.TmPathUlocService;
import com.wis.mes.master.path.service.TmPathUlocSipService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/path")
public class TmPathController extends BaseController {

	@Autowired
	private TmPathService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private TmWorkshopService workShopService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPathUlocService pathUlocService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmPathUlocSipService pathUlocSipService;

	@RequestMapping(value = "/listInput")
	public ModelAndView mainInput(ModelMap modelMap, Integer id) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		return new ModelAndView("/master/path/path_main");
	}

	@RequestMapping(value = "/pathListInput")
	public ModelAndView pathListInput(ModelMap modelMap, Integer currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		modelMap.addAttribute("partOptions", partService.getDictItem(new TmPart(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("workShopOptions", workShopService.getDictItem(null));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(null));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/master/path/path_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "backNumber" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("plantOptions",
				plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));

		// 成品或者半成品
		Map<String, Object> partParamMap = new HashMap<String, Object>();
		partParamMap.put("TYPE", Arrays.asList(new String[] { ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED,
				ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED }));
		partParamMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("partOptions", partService.getDictItem(partParamMap));

		// 车间
		modelMap.addAttribute("workShopOptions",
				workShopService.getDictItem(new TmWorkshop(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("lineOptions", lineService.getDictItem(new TmLine(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("statusOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		return new ModelAndView("/master/path/path_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmPath bean)
			throws Exception {
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(bean.getStatus())) {
			throw new BusinessException("PATH_UPDATE_ERROR");
		}
		try {
			bean = service.doSave(bean);
			logService.doAddLog("TmPath", bean);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			if (StringUtil.isNotBlank(errMsg) && errMsg.indexOf(getMessage(request, "PATH_NO_UNIQUE")) != -1) {
				throw new BusinessException("PATH_NO_UNIQUE_ERROR");
			}
			throw e;
		}
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("line", lineService.getDictItem(null));
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("statusOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		return new ModelAndView("/master/path/path_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmPath bean) {
		if (ConstantUtils.MAINTAIN_STATUS_COMPLETE.equals(bean.getStatus())) {
			checkIsHaveSip(bean.getId());
			List<Map<String, Object>> pathUloc = pathUlocService.queryByPathIdAndUlocType(bean.getId(),
					"= '" + ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES + "'");
			if (pathUloc == null || pathUloc.size() == 0) {
				throw new BusinessException("PATH_UPDATE_ERROR");
			}
		}

		TmPath before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmPath", before, bean);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 检查是否有质检点
	 * 
	 * @param tmPathId
	 */
	private void checkIsHaveSip(Integer tmPathId) {
		List<TmPathUloc> pathUlocs = pathUlocService.getPathUlocs(tmPathId);
		for (TmPathUloc bean : pathUlocs) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsSip())) {
				List<TmPathUlocSip> pathUloSips = pathUlocSipService.getPathUloSips(bean.getId());
				if (pathUloSips.size() == 0) {
					throw new BusinessException("PATH_ULOC_IS_SIP_NOT_PARAMETER_ERROR",
							ulocService.getUlocById(bean.getTmUlocId()).getNo());
				}
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmPath> list = service.findAllInIds(deleteIds);
		try {
			// 注释老方法，新方法会删除工艺路径及工艺路径下站点和的站点的参数
			// service.doDeleteById(deleteIds);
			service.doDeletePathUlocParam(deleteIds);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			String[] foreignKeys = getMessage(request, "PATH_FOREIGN_KEY").split(",");
			for (String foreign : foreignKeys) {
				if (errMsg.indexOf(foreign) != -1) {
					throw new BusinessException(foreign);
				}
			}
		}
		logService.doDeleteLog("TmPath", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 根据工厂查询物料信息
	 * 
	 * @param plantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPartByPlantId")
	public List<DictEntry> getPartByPlantId(Integer plantId, String operate) {
		if ("search".equals(operate)) {
			TmPart part = new TmPart();
			part.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
			part.setTmPlantId(plantId);
			return partService.getDictItem(part);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 成品或者半成品
		paramMap.put("TYPE", Arrays.asList(new String[] { ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED,
				ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED }));
		paramMap.put("TM_PLANT_ID", plantId);
		paramMap.put("ENABLED", ConstantUtils.TYPE_CODE_ENABLED_ON);
		return partService.getDictItem(paramMap);
	}

	/**
	 * 根据工厂查询车间信息
	 * 
	 * @param plantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkShopByPlantId")
	public List<DictEntry> getWorkShopByPlantId(Integer plantId) {
		TmWorkshop workShop = new TmWorkshop();
		workShop.setTmPlantId(plantId);
		workShop.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return workShopService.getDictItem(workShop);
	}

	/**
	 * 根据工厂和车间查询产线信息
	 * 
	 * @param plantId
	 * @param workShopId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getLineByWorkShopIdAndPlantId")
	public List<DictEntry> getLineByWorkShopIdAndPlantId(Integer plantId, Integer workShopId) {
		TmLine line = new TmLine();
		line.setTmPlantId(plantId);
		line.setTmWorkshopId(workShopId);
		line.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return lineService.getDictItem(line);
	}

	@ResponseBody
	@RequestMapping(value = "/getPlantDictEntry")
	public List<DictEntry> getPlantDictEntry() {
		return plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON));
	}

	@ResponseBody
	@RequestMapping(value = "/getEnabledDictEntry")
	public List<DictEntry> getEnabledDictEntry() {
		return entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
	}

	@ResponseBody
	@RequestMapping(value = "/getUlocDictEntry")
	public List<DictEntry> getUlocDictEntry(Integer plantId, Integer workShopId, Integer lineId) {
		TmUloc uloc = new TmUloc();
		uloc.setTmPlantId(plantId);
		uloc.setTmWorkshopId(workShopId);
		uloc.setTmLineId(lineId);
		uloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return ulocService.getDictItem(uloc);
	}

	@ResponseBody
	@RequestMapping(value = "/getUlocDictEntryMap")
	public List<Map<String, Object>> getUlocDictEntryMap(Integer plantId, Integer workShopId, Integer lineId) {
		TmUloc uloc = new TmUloc();
		uloc.setTmPlantId(plantId);
		uloc.setTmWorkshopId(workShopId);
		uloc.setTmLineId(lineId);
		uloc.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return ulocService.getDictItemMap(uloc);
	}

	@ResponseBody
	@RequestMapping(value = "/getYesOrNoDictEntry")
	public List<DictEntry> getYesOrNoDictEntry() {
		return entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
	}

	@RequestMapping(value = "/doDuplicateInput")
	public ModelAndView doDuplicateInput(ModelMap modelMap, Integer id) {
		modelMap.addAttribute("line", lineService.getDictItem(null));
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("plantOptions", plantService.getDictItem(null));
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("statusOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_MAINTAIN_STATUS));
		return new ModelAndView("/master/path/path_copy");
	}

	@ResponseBody
	@RequestMapping(value = "/doDuplicate")
	public JsonActionResult doDuplicate(HttpServletRequest request, HttpServletResponse response, TmPath bean,
			String id) throws Exception {
		bean = service.doDuplicate(bean, getMessage(request, "PATH_NO_UNIQUE"));
		logService.doAddLog("TmPath", bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name" }));
		final PageResult<TmPath> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] { "工厂", "车间", "产线", "物料", "编号", "名称", "来源工艺路径", "维护状态", "启用" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		ActionUtils.handleResult(response);
	}

	/**
	 * @author ryy
	 * @date 2017-06-06
	 * @desc 工艺路径&站点信息关联导出
	 */
	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult ExportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name" }));
		final PageResult<TmPath> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmPath> list = findBy == null ? new ArrayList<TmPath>() : findBy.getContent();
		String parentHeader = getMessage(request, "PATH_EXPORT_TITLE");
		String childHeader = getMessage(request, "PATH_ULOC_EXPORT_TITLE");
		service.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
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
				downName = "工艺路径模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "path" + File.separator + "path.xlsx";
			final Workbook wb = service.getWorkbookTemp(templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	/**
	 * 数据导入 RYY 17/05/17
	 */
	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {

		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = service.doImportExcelData(book, request);
			return ActionUtils.handleResult(true, msg);
		} catch (BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market:" + e.getMessage(), e);
			throw new BusinessException("PATH_IMPORT_FAIL");
		}

	}

	@RequestMapping(value = "/showGrapPath")
	public ModelAndView showGrapPath(ModelMap modelMap, Integer tmPathId) {
		if (tmPathId != null) {
			TmPath path = service.findById(tmPathId);
			modelMap.addAttribute("tmPathId", tmPathId);
			modelMap.addAttribute("tmWorkshopId", path.getTmWorkshopId());
			modelMap.addAttribute("tmPlantId", path.getTmPlantId());
			modelMap.addAttribute("tmLineId", path.getTmLineId());
		}
		return new ModelAndView("/master/path/path_flow");
	}

	@ResponseBody
	@RequestMapping(value = "/getPathGrapData")
	public String getPathGrapData(Integer tmPathId) {
		return pathUlocService.getGraphJSONData(tmPathId);
	}

	@ResponseBody
	@RequestMapping(value = "/doSavePathAndSons")
	public JsonActionResult doSavePathAndSons(HttpServletRequest request, String data, Integer tmPathId,
			Integer tmPlantId, Integer tmWorkshopId, Integer tmLineId, String partArray, String parameterArray,
			String sipArray, String deleateTmPathUlocIds) throws Exception {
		TmPath pathBean;
		try {
			pathBean = service.doSavePathUlocRaphData(data, tmPathId, tmPlantId, tmWorkshopId, tmLineId, partArray,
					parameterArray, sipArray, getIds(deleateTmPathUlocIds));
			logService.doAddLog("TmPath", pathBean);
		} catch (Exception e) {
			if (StringUtil.isNotBlank(e.getMessage())
					&& e.getMessage().indexOf(getMessage(request, "PATH_NO_UNIQUE")) != -1) {
				throw new BusinessException("PATH_NO_UNIQUE_ERROR");
			}
			throw e;
		}
		return ActionUtils.handleResult(pathBean.getId());
	}
}
