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
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.workcalendar.entity.TmWorkSpecialDate;
import com.wis.mes.master.workcalendar.service.TmWorkSpecialDateService;
import com.wis.mes.util.StringUtil;

@Controller

@RequestMapping(value = "/workspecialdate")
public class TmWorkSpecialDateController extends BaseController {

	@Autowired
	private TmWorkSpecialDateService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmLineService lineService;
	
	@Autowired
	private TmClassManagerService classManagerService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap) {
		TmLine tmLine = new TmLine();
		tmLine.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("line", lineService.getDictItem(tmLine));
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("classOrderOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("classGroupOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		modelMap.addAttribute("dateType", entryService.getEntryByTypeCode(ConstantUtils.DATE_TYPE_CODE));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/workcalendar/workspecialdate/workspecialdate_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		TmLine tmLine = new TmLine();
		tmLine.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("line", lineService.getDictItem(tmLine));
		modelMap.addAttribute("classOrderOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("classGroupOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		modelMap.addAttribute("dateType", entryService.getEntryByTypeCode(ConstantUtils.DATE_TYPE_CODE));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/workcalendar/workspecialdate/workspecialdate_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response,HttpServletRequest request, TmWorkSpecialDate bean) {
		try {
			String tmLineId=request.getParameter("tmLineId");
			String classOrder=request.getParameter("classOrder");
			String classGroup=request.getParameter("classGroup");
			if(StringUtil.isBlank(tmLineId) || StringUtil.isBlank(classOrder) || StringUtil.isBlank(classGroup)){
				return ActionUtils.handleResult(false,"请将入录完整数据");
			}
			TmClassManager classManager=new TmClassManager();
			classManager.setTmLineId(Integer.valueOf(tmLineId));
			classManager.setClassName(classOrder);
			classManager.setClassGroup(classGroup);
			classManager =classManagerService.findUniqueByEg(classManager);
			if(classManager==null){
				return ActionUtils.handleResult(false,"该产线的班次班组不存在");
			}
			bean.setTmClassManagerId(classManager.getId());
			bean = service.doSave(bean);
		} catch (Exception e) {
		    String uks = getMessage(request, "WORK_SPECIAL_DATE_UNIQUE"); 
		    String[] ukArr = uks.split(",");
		    for (int i=0; i<ukArr.length; i++) {
		    	if (e.getMessage().contains(ukArr[i])){
		    		throw new BusinessException(ukArr[i]);
		    	}
		    }	
		}
		logService.doAddLog("TmWorkSpecialDate", bean);
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmLine tmLine = new TmLine();
		tmLine.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		modelMap.addAttribute("line", lineService.getDictItem(tmLine));
		modelMap.addAttribute("classOrderOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("classGroupOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		TmWorkSpecialDate tmWorkSpecialDate=service.findById(Integer.valueOf(id));
		TmClassManager classManager=classManagerService.findById(Integer.valueOf(tmWorkSpecialDate.getTmClassManagerId()));
		tmWorkSpecialDate.setTmClassManager(classManager);
		modelMap.addAttribute("workSpecialDate", tmWorkSpecialDate);
		modelMap.addAttribute("dateType", entryService.getEntryByTypeCode(ConstantUtils.DATE_TYPE_CODE));
		modelMap.addAttribute("enabled", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/workcalendar/workspecialdate/workspecialdate_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmWorkSpecialDate bean) {
		TmWorkSpecialDate before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmWorkSpecialDate", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmWorkSpecialDate> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmWorkSpecialDate", list);
		return ActionUtils.handleResult(response);
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
		final PageResult<TmWorkSpecialDate> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
	
		List<TmWorkSpecialDate> list = findBy == null ? new ArrayList<TmWorkSpecialDate>() : findBy.getContent();
		String[] header = new String[] { "日期", "类型", "描述", "启用"};
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}
	/**
	 * @author ryy
	 * @company 上海西信信息科技有限公司
	 * @date 2017年6月1日
	 * @desc 
	 */
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
			throw e;
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("WORK_SCHEDULE_IMPORT_FAIL");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName) {
		try {
			if (downName == null || downName == "") {
				downName = "特殊日历表格模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "workcalendar" + File.separator + "workspecialdate.xlsx";
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
