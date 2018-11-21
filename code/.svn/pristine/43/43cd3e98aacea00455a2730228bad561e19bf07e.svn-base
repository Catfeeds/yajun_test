package com.wis.mes.master.workcalendar.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workcalendar.entity.TmWorkschedule;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleService;
import com.wis.mes.master.workshop.service.TmWorkshopService;

import net.sf.json.JSONObject;

@Controller

@RequestMapping(value = "/workschedule")
public class TmWorkscheduleController extends BaseController {

	@Autowired
	private TmWorkscheduleService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workshopService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService lineService;
	

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response,HttpServletRequest request, ModelMap modelMap) {
		getPlantWorkshopLine(modelMap);
		modelMap.addAttribute("weekValue", entryService.getEntryByTypeCode("WEEK_VALUE"));
		modelMap.addAttribute("shiftType", entryService.getEntryByTypeCode("SHIFT_TYPE"));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		return new ModelAndView("/workcalendar/workschedule/workschedule_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no"}));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getPlantWorkshopLine(modelMap);
		modelMap.addAttribute("weekValue", entryService.getEntryByTypeCode("WEEK_VALUE"));
		modelMap.addAttribute("shiftType", entryService.getEntryByTypeCode("SHIFT_TYPE"));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/workcalendar/workschedule/workschedule_add");
	}
	
	/**
	 * 编号唯一性的controller
	 *
	 * @param response
	 * @param code
	 * @param param
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCode")
	public void checkCode(HttpServletResponse response, String code, String param) {
		TmWorkschedule tmWorkschedule = new TmWorkschedule();
		tmWorkschedule.setNo(param);
		Integer num = service.findByEg(tmWorkschedule).size();
		JSONObject result = new JSONObject();
		if (num>0 && !param.equals(code)) {
			result.put("status", "n");
			result.put("info", "编号已存在！");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response,HttpServletRequest request, TmWorkschedule bean,String isCover) throws Exception {
		String[] errors = {"MORNING_SHIFT_IN_NOON_NIGHT","NOON_SHIFT_IN_MORNING_NIGHT","NIGHT_SHIFT_IN_MORNING_NOON"};
		try {
			bean.setNo(createTemplateNo(bean.getTmLineId(),bean.getShiftnoId()));
			bean.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
			//service.checkTime(bean, errors);
			bean = service.doSave(bean);
			logService.doAddLog("TmWorkschedule", bean);
		} catch (BusinessException e) {
			throw e;
	    } catch (Exception e) {
			String uks = getMessage(request, "WORK_SCHEDULE_NO_UNIQUE"); 
		    String[] ukArr = uks.split(",");
		    for (int i=0; i<ukArr.length; i++) {
		    	if (e.getMessage().contains(ukArr[i])){
		    		throw new BusinessException(ukArr[i]);
		    	}
		    }	
		    throw e;
		}
		return ActionUtils.handleResult(response);
	}
	
	//创建模板编号
	private String createTemplateNo(Integer lineId,String shiftno){
		StringBuffer buffer = new StringBuffer();
		TmLine line = lineService.findById(lineId);
		buffer.append(line.getNo()+"-");
		buffer.append(shiftno.equals(ConstantUtils.SHIFT_MORNING)?"A":"B");
		String nextValue =String.valueOf( SerialNumUtil.getInstance().nextInt(buffer.toString()));
		switch (nextValue.length()) {
		case 1:
			nextValue = "00"+nextValue;
			break;
		case 2:
			nextValue = "0"+nextValue;
			break;
		default:
			break;
		}
		return buffer.append(nextValue).toString();
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		getPlantWorkshopLine(modelMap);
		modelMap.addAttribute("weekValue", entryService.getEntryByTypeCode("WEEK_VALUE"));
		modelMap.addAttribute("shiftType", entryService.getEntryByTypeCode("SHIFT_TYPE"));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		TmWorkschedule bean = service.findById(Integer.valueOf(id));
		modelMap.addAttribute("workschedule", bean);
		return new ModelAndView("/workcalendar/workschedule/workschedule_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmWorkschedule bean, String isCover) {
		String[] errors = {"MORNING_SHIFT_IN_NOON_NIGHT","NOON_SHIFT_IN_MORNING_NIGHT","NIGHT_SHIFT_IN_MORNING_NOON"};
		//verifyUniqueValues(bean);	
		//service.checkTime(bean, errors);
		TmWorkschedule before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmWorkschedule", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response,HttpServletRequest request, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmWorkschedule> list = service.findAllInIds(deleteIds);
		try {
			service.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "WORK_SCHEDULE_REST_FKS"); 
		    String[] fkArr = fks.split(",");
		    for (int i=0; i<fkArr.length; i++) {
		    	if (e.getMessage().contains(fkArr[i])){
		    		throw new BusinessException(fkArr[i]);
		    	}
		    }	
		}
		logService.doDeleteLog("TmWorkschedule", list);
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value = "/generateCurrentWeekTemplete")
	public String generateCurrentWeekTemplete(HttpServletResponse response, String ids, String isCoveredValue) {
	    String result = service.generateCurrentWeekTemplete(ids, isCoveredValue);
	    JSONObject returnMsg = new JSONObject();
		returnMsg.put("returnMsg", result);
		return String.valueOf(returnMsg);
	}
	@ResponseBody
	@RequestMapping(value = "/generateNextWeekTemplete")
	public String generateNextWeekTemplete(HttpServletResponse response, String ids) {
	    String result = service.generateNextWeekTemplete(ids);
		JSONObject returnMsg = new JSONObject();
		returnMsg.put("returnMsg", result);
		return String.valueOf(returnMsg);
	}
	@RequestMapping(value="/checkWorkScheduleNo")
	public void checkWorkScheduleNo(HttpServletResponse response,HttpServletRequest request, String code,String param){
		String result = service.checkWorkScheduleNo(code, param);
		JSONObject resultObj = new JSONObject();
		String[] errors = {"TM_WORKSCHEDULE_NO"};
		if ("fail".equals(result)) {
			resultObj.put("info", getMessage(request, errors));
		}
		ActionUtils.handleResult(response, resultObj);
	}
	
	@SuppressWarnings("unused")
	private void verifyUniqueValues(TmWorkschedule tmWorkschedule){
		TmWorkschedule eg = new TmWorkschedule();
		eg.setTmPlantId(tmWorkschedule.getTmPlantId());
		eg.setTmLineId(tmWorkschedule.getTmLineId());
		eg.setShiftnoId(tmWorkschedule.getShiftnoId());
		List<TmWorkschedule> workschedules = service.findByEg(eg);
		if(null != workschedules && workschedules.size() > 0){
			TmWorkschedule bean = workschedules.get(0);
			if(null == tmWorkschedule.getId()){
				throw new BusinessException("ERROR_KEY","已经存在事部，产线，班次相同的数据。");
			}else if(!tmWorkschedule.getId().equals(bean.getId())){
				throw new BusinessException("ERROR_KEY","已经存在事部，产线，班次相同的数据。");
			}
		}
	}
	@ResponseBody
	@RequestMapping(value = "/checkIsGenerateWorkTime")
	public String checkIsGenerateWorkTime(HttpServletRequest request,String ids, String isCoveredValue){
		String workTimeIds = service.checkIsGenerateWorkTime(ids, isCoveredValue);
		JSONObject returnMsg = new JSONObject();
		if (StringUtils.isNotBlank(workTimeIds)) {
			returnMsg.put("msg", getMessage(request, "TM_WORKTIME_IS_EXIST"));
			returnMsg.put("workTimeIds", workTimeIds);
		}
		return String.valueOf(returnMsg);
	}
	private  ModelMap getPlantWorkshopLine( ModelMap modelMap) {
		modelMap.addAttribute("plant", plantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("line", lineService.queryDictItem(new QueryCriteria()));
		return modelMap;
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
		final PageResult<TmWorkschedule> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmWorkschedule> list = findBy == null ? new ArrayList<TmWorkschedule>() : findBy.getContent();
		String[] header = new String[] { "模板编号", "工厂", "车间", "产线", "班次", "开始时间","结束时间",
				"星期一","星期二","星期三","星期四","星期五","星期六","星期日","计划上线数","计划下线数","参考JPH","启用"};
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}
	
	/**
	 * @author ryy
	 * @date 2017年6月11日
	 * @desc 工艺路径&站点信息导出
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
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmWorkschedule> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmWorkschedule> list = findBy == null ? new ArrayList<TmWorkschedule>() : findBy.getContent();
		String parentHeader = getMessage(request, "WORK_SCHEDULE_EXPORT_TITLE");
		String childHeader = getMessage(request, "WORK_SCHEDULE_REST_EXPORT_TITLE");
		service.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
		    String msg = service.doImportExcelData(book,request);
		    return ActionUtils.handleResult(true,msg);
		} catch (final BusinessException e) {
			e.printStackTrace();
			throw e;
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORK_SCHEDULE_IMPORT_FAIL");
		}
	}
	
	/**
	 * @author zhf
	 * @date 2017年6月14日
	 * @desc 工作日历模板及休息时间导入
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
			pojo.setParentClassName("com.wis.mes.master.workcalendar.entity.TmWorkschedule");
			pojo.setChildClassName("com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest");
			pojo.setParentHeader(getMessage(request, "WORK_SCHEDULE_IMPORT_TITLE").split(","));
			pojo.setChildHeader(getMessage(request, "WORK_SCHEDULE_REST_IMPORT_TITLE").split(","));
			pojo.setParentField(getMessage(request, "WORK_SCHEDULE_IMPORT_ENTITY").split(","));
			pojo.setChildField(getMessage(request, "WORK_SCHEDULE_REST_IMPORT_ENTITY").split(","));
			result = service.doImportExcelDataAll(book,pojo,request);

		} catch (final BusinessException e) {
			throw new BusinessException("WORK_SCHEDULE_IMPORT_ALL_FAIL", "<br/>" + e.getMessage());
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORK_SCHEDULE_IMPORT_ALL_FAIL",
					getMessage(request, "IMPORT_UNKNOWN_EXCEPTION"));
		}
		return ActionUtils.handleResult(true, result);
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName) {
		try {
			if (downName == null || downName == "") {
				downName = "工作日历模板导入";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "workcalendar" + File.separator + "workschedule.xlsx";
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
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
				downName = "工作日历模板&休息时间联表模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "workcalendar" + File.separator + "workschedule_all.xlsx";
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
