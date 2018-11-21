package com.wis.mes.master.workcalendar.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workcalendar.entity.TmWorkschedule;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleService;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;

@Controller
@RequestMapping(value = "/worktime")
public class TmWorktimeController extends BaseController {

	@Autowired
	private TmWorktimeService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private TmWorkscheduleService workscheduleService;
	@Autowired
	private TmClassManagerService classManagerService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, Integer id, ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/workcalendar/worktime/worktime_main");
	}

	@RequestMapping(value = "/workTimelistInput")
	public ModelAndView workTimelistInput(HttpServletResponse response, QueryCriteria criteria, Integer currentPageId,
			ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		modelMap.addAttribute("classOrderOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("classGroupOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		modelMap.addAttribute("workschedule", workscheduleService.getDictItem(new TmWorkschedule(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("weekValue", entryService.getEntryByTypeCode("WEEK_VALUE"));
		modelMap.addAttribute("shiftType", entryService.getEntryByTypeCode("SHIFT_TYPE"));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/workcalendar/worktime/worktime_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap,
			Integer currentPageId) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		ActionUtils.addUserDataPermission(criteria,"tmLineId");
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/workcalendar/worktime/worktime_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, HttpServletRequest request, TmWorktime bean) {
		try {
			updateEnabledStatus(bean);
			bean = service.doSave(bean);
		} catch (Exception e) {
			String uks = getMessage(request, "PLANT_WORKSHOP_LINE_WORKDATE_SHIFTNO_UNIQUE");
			String[] ukArr = uks.split(",");
			for (int i = 0; i < ukArr.length; i++) {
				if (e.getMessage().contains(ukArr[i])) {
					throw new BusinessException(ukArr[i]);
				}
			}
		}
		logService.doAddLog("TmWorktime", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("worktime", service.findById(Integer.valueOf(id)));
		modelMapAddAttribute(modelMap);
		TmWorkschedule workschedule = new TmWorkschedule();
		workschedule.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		return new ModelAndView("/workcalendar/worktime/worktime_update");
	}

	private void updateEnabledStatus(TmWorktime bean){
		if(bean.getEnabled().equals(ConstantUtils.TYPE_CODE_ENABLED_ON)){
			TmWorktime eg = new TmWorktime();
			eg.setTmLineId(bean.getTmLineId());
			eg.setWorkDate(bean.getWorkDate());
			eg.setShiftno(bean.getShiftno());
			eg.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
			List<TmWorktime> list = service.findByEg(eg);
			if(null == bean.getId()){
				for(TmWorktime workTime:list){
					workTime.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_OFF);
					service.doUpdate(workTime);
				}
			}else{
				for(TmWorktime workTime:list){
					if(!bean.getId().equals(workTime.getId())){
						workTime.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_OFF);
						service.doUpdate(workTime);
					}
				}
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, HttpServletRequest request, TmWorktime bean) {
		updateEnabledStatus(bean);
		TmWorktime before = service.findById(bean.getId());
		try {
			service.doUpdate(bean);
		} catch (Exception e) {
			String uks = getMessage(request, "PLANT_WORKSHOP_LINE_WORKDATE_SHIFTNO_UNIQUE");
			String[] ukArr = uks.split(",");
			for (int i = 0; i < ukArr.length; i++) {
				if (e.getMessage().contains(ukArr[i])) {
					throw new BusinessException(ukArr[i]);
				}
			}
		}
		logService.doUpdateLog("TmWorktime", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, HttpServletRequest request, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmWorktime> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "WORK_TIME_FKS");
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
		}
		logService.doDeleteLog("TmWorktime", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteWorkTimeARestByIds")
	public JsonActionResult deleteWorkTimeARestByIds(HttpServletResponse response, String ids) {
		String[] workTimeIds = ids.split(",");
		String ss = Arrays.toString(workTimeIds).replace("[", "").replace("]", "");
		workTimeIds = ss.split(",");
		Integer[] deleteIds = new Integer[workTimeIds.length];
		for (int i = 0; i < workTimeIds.length; i++) {
			deleteIds[i] = Integer.valueOf(workTimeIds[i].trim());
		}
		service.deleteWorkTimeARestByIds(deleteIds);
		return ActionUtils.handleResult(response);
	}

	private ModelMap modelMapAddAttribute(ModelMap modelMap) {
		TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("plant", plantService.getDictItem(tmPlant));
		modelMap.addAttribute("line", lineService.queryDictItem(new QueryCriteria()));
		modelMap.addAttribute("classOrderOptions",
				entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("classGroupOptions", classManagerService.getDictItemEntry(null));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return modelMap;
	}

	@ResponseBody
	@RequestMapping("getShiftAndTimeByConditions")
	public JsonActionResult getShiftAndTimeByConditions(TmWorktime worktime,Integer workTimeId,String enabledNo) {
		Map<String, Object> map = service.getEveryShiftTime(worktime,workTimeId,enabledNo);
		return ActionUtils.handleResult(map);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmWorktime> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);

		List<TmWorktime> list = findBy == null ? new ArrayList<TmWorktime>() : findBy.getContent();
		String[] header = new String[] { "工厂", "车间", "产线", "工作日期", "班次", "星期", "开始时间", "结束时间", "计划上线数量", "计划下线数量",
				"参考JPH", "启用" };
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult ExportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmWorktime> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmWorktime> list = findBy == null ? new ArrayList<TmWorktime>() : findBy.getContent();
		String parentHeader = getMessage(request, "WORK_TIME_EXPORT_TITLE");
		String childHeader = getMessage(request, "WORK_TIME_REST_EXPORT_TITLE");
		service.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName) {
		try {
			if (downName == null || downName == "") {
				downName = "工作日历导入";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "workcalendar" + File.separator + "worktime.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplateAll")
	public JsonActionResult exportTemplateAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName) {
		try {
			if (downName == null || downName == "") {
				downName = "工作日历&休息时间导入";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "workcalendar" + File.separator + "worktime_all.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	/**
	 * @author zhf
	 * @date 2017年6月10日
	 * @desc 导入主表数据
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
		} catch (final BusinessException e) {
			throw e;
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORK_TIME_IMPORT_FAIL");
		}

	}

	/**
	 * @author zhf
	 * @date 2017年6月10日
	 * @desc 导入联表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/importDataAll")
	public JsonActionResult importDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		String result = null;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			ExcelImportPojo pojo = new ExcelImportPojo();
			pojo.setParentClassName("com.wis.mes.master.workcalendar.entity.TmWorktime");
			pojo.setChildClassName("com.wis.mes.master.workcalendar.entity.TmWorktimeRest");
			pojo.setParentHeader(getMessage(request, "WORK_TIME_IMPORT_TITLE").split(","));
			pojo.setChildHeader(getMessage(request, "WORK_TIME_REST_IMPORT_TITLE").split(","));
			pojo.setParentField(getMessage(request, "WORK_TIME_IMPORT_ENTITY").split(","));
			pojo.setChildField(getMessage(request, "WORK_TIME_REST_IMPORT_ENTITY").split(","));
			result = service.doImportExcelDataAll(book, pojo, request);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_FAIL_INFO", "<br/>" + e.getMessage());
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORK_TIME_IMPORT_ALL_FAIL", getMessage(request, "IMPORT_UNKNOWN_EXCEPTION"));
		}
		return ActionUtils.handleResult(true, result);
	}

}
