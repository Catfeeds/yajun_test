package com.wis.mes.master.employeeno.controller;

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
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.employeeno.service.TmEmployeeNoService;

@Controller
@RequestMapping(value = "/tmEmployeeNo")
public class TmEmployeeNoController extends BaseController {
	@Autowired
	private TmEmployeeNoService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("sexOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_SEX));
		return new ModelAndView("/master/employeeno/employeeno_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "no" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("sexOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_SEX));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/employeeno/employeeno_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TmEmployeeNo bean)
			throws Exception {
		checkNo(TmEmployeeNo.createNo(bean.getNo()),null);
		//checkName(TmEmployeeNo.createName(bean.getName()),null);
		bean = service.doSave(bean);
		logService.doAddLog("TmPlant", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("sexOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_USER_SEX));
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/master/employeeno/employeeno_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmEmployeeNo bean) {
		checkNo(TmEmployeeNo.createNo(bean.getNo()),bean.getId());
		//checkName(TmEmployeeNo.createName(bean.getName()),bean.getId());
		TmEmployeeNo before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmEmployeeNo", before, bean);
		return ActionUtils.handleResult(response);
	}

	private void checkName(TmEmployeeNo eg,Integer id){
		List<TmEmployeeNo> list = service.findByEg(eg);
		if(null != list && list.size() > 0){
			if(null == id){
				throw  new BusinessException("ERROR_KEY","姓名："+eg.getName()+",已存在。");
			}else{
				for(TmEmployeeNo bean:list){
					if(!bean.getId().equals(id)){
						throw  new BusinessException("ERROR_KEY","姓名："+eg.getName()+",已存在。");
					}
				}
			}
		}
	}
	
	private void checkNo(TmEmployeeNo eg,Integer id){
		List<TmEmployeeNo> list = service.findByEg(eg);
		if(null != list && list.size() > 0){
			if(null == id){
				throw  new BusinessException("ERROR_KEY","工号："+eg.getNo()+",已存在。");
			}else{
				for(TmEmployeeNo bean:list){
					if(!bean.getId().equals(id)){
						throw  new BusinessException("ERROR_KEY","工号："+eg.getNo()+",已存在。");
					}
				}
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmEmployeeNo> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmEmployeeNo", list);
		return ActionUtils.handleResult(response);
	}
	
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
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("ERROR_KEY","数据导入异常，请联系系统管理员。");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name" }));
		final PageResult<TmEmployeeNo> findBy = service.findBy(criteria);
		List<TmEmployeeNo> list = findBy == null ? new ArrayList<TmEmployeeNo>() : findBy.getContent();
		String[] header = new String[] { "工号", "姓名", "工龄", "性别"};
		service.exportExcelData(response, list, "作业员工号" + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "作业员工号模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "employeeNo" + File.separator + "employeeNo.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
