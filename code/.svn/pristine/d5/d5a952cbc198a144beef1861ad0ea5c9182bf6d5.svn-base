package com.wis.mes.master.classmanage.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.classmanage.entity.TmClassManagerDetail;
import com.wis.mes.master.classmanage.service.TmClassManagerDetailService;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.employeeno.service.TmEmployeeNoService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;

@Controller
@RequestMapping(value = "/classManagerDetail")
public class TmClassManagerDetailController extends BaseController {

	@Autowired
	private TmClassManagerDetailService classManagerDetailService;

	@Autowired
	private AuditLogService logService;

	@Autowired
	private TmUlocService tmUlocService;

	@Autowired
	private TmEmployeeNoService tmEmployeeNoService;
	
	@Autowired
	private TmClassManagerService tmClassManagerService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, Integer tmClassManagerId,
			ModelMap modelMap) {
		modelMap.addAttribute("tmClassManagerId", tmClassManagerId);
		modelMap.addAttribute("params", request.getParameter("params"));
		modelMap.addAttribute("currentPageId", request.getParameter("currentPageId"));
		return new ModelAndView("/master/classmanager/detail/classmanagerdetail_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setUseCacheSql(false);
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, classManagerDetailService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, Integer tmClassManagerId, ModelMap modelMap) {
		modelMap.addAttribute("ulocOptions", tmUlocService.getUlocAll());
		modelMap.addAttribute("employeeNos", tmEmployeeNoService.getDictItem(null));
		modelMap.addAttribute("tmClassManagerId", tmClassManagerId);
		return new ModelAndView("/master/classmanager/detail/classmanagerdetail_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmClassManagerDetail bean) {
		TmClassManagerDetail classManagerDetail = new TmClassManagerDetail();
		classManagerDetail.setTmUlocId(bean.getTmUlocId());
		classManagerDetail.setTmEmployeeNoId(bean.getTmEmployeeNoId());
		classManagerDetail.setTmClassManagerId(bean.getTmClassManagerId());
		classManagerDetail = classManagerDetailService.findUniqueByEg(classManagerDetail);
		if (classManagerDetail != null) {
			return ActionUtils.handleResult(false, "同一班次内（加工工艺+作业员工）必须唯一!");
		}
		bean = classManagerDetailService.doSave(bean);
		logService.doAddLog("tmClassManagerDetail", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("ulocOptions", tmUlocService.getUlocAll());
		modelMap.addAttribute("employeeNos", tmEmployeeNoService.getDictItem(null));
		modelMap.addAttribute("bean", classManagerDetailService.findById(Integer.valueOf(id)));
		return new ModelAndView("/master/classmanager/detail/classmanagerdetail_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmClassManagerDetail bean) {
		TmClassManagerDetail before = classManagerDetailService.findById(bean.getId());
		if (before == null) {
			return ActionUtils.handleResult(false, "数据已删除，请刷新后在操作！");
		}
		TmClassManagerDetail classManagerDetail = new TmClassManagerDetail();
		classManagerDetail.setTmUlocId(bean.getTmUlocId());
		classManagerDetail.setTmEmployeeNoId(bean.getTmEmployeeNoId());
		classManagerDetail.setTmClassManagerId(bean.getTmClassManagerId());
		classManagerDetail = classManagerDetailService.findUniqueByEg(classManagerDetail);
		if (classManagerDetail != null && !before.getId().equals(classManagerDetail.getId())) {
			return ActionUtils.handleResult(false, "次班次已存在（加工工艺+作业员工）!");
		}
		classManagerDetailService.doUpdate(bean);
		logService.doUpdateLog("tmClassManagerDetail", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, HttpServletRequest request, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmClassManagerDetail> list = classManagerDetailService.findAllInIds(deleteIds);
		try {
			classManagerDetailService.doDeleteById(deleteIds);
		} catch (Exception e) {
			throw e;
		}
		logService.doDeleteLog("tmClassManagerDetail", list);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 
	 * 导出模版
	 */
	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids,String tmClassManagerId) {
		try {
			downName = LoadUtils.urlDecoder("班组详情导出");
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "classmanager" + File.separator + "classmanager_detail.xlsx";
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			setTemplate(wb,tmClassManagerId);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	public void setTemplate(Workbook wb,String tmClassManagerId) {
		TmClassManager manager = tmClassManagerService.findById(Integer.valueOf(tmClassManagerId));
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.setOrderField("no");
		criteria.setOrderDirection(OrderEnum.ASC);
		criteria.getQueryCondition().put("tmLineId", String.valueOf(manager.getTmLineId()));
		List<TmUloc> ulocList = tmUlocService.findBy(criteria).getContent();
		List<TmEmployeeNo> employeeNoList = tmEmployeeNoService.findAll();
		//工位编号+工艺名称
		CellStyle cellStyle = wb.createCellStyle();
		Sheet sheet1 = wb.getSheetAt(0);
		int rowIndex = 1;
		for(int i=0;i<ulocList.size();i++) {
			int cellIndex = -1;
			Row row = LoadUtils.getRow(sheet1, rowIndex);
			cellIndex = LoadUtils.setCell(row,cellIndex,ulocList.get(i).getNo());
			cellIndex = LoadUtils.setCell(row,cellIndex,ulocList.get(i).getName());
			LoadUtils.setCellStyle(sheet1, LoadUtils.setCellBorder(cellStyle), 0, 38, rowIndex);
			rowIndex++;
		}
		//作业员工号
		Sheet sheet2 = wb.getSheetAt(1);
		rowIndex = 1;
		for(int i=0;i<employeeNoList.size();i++) {
			int cellIndex = -1;
			Row row = LoadUtils.getRow(sheet2, rowIndex);
			cellIndex = LoadUtils.setCell(row,cellIndex,employeeNoList.get(i).getNo());
			cellIndex = LoadUtils.setCell(row,cellIndex,employeeNoList.get(i).getName());
			LoadUtils.setCellStyle(sheet2, LoadUtils.setCellBorder(cellStyle), 0, 38, rowIndex);
			rowIndex++;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/exportData")
	public void ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids, String tmClassManagerId) throws IOException {
		if (StringUtils.isNotBlank(tmClassManagerId)) {
			criteria.getQueryCondition().put("tmClassManagerId", tmClassManagerId);
			if (StringUtils.isNotBlank(ids)) {
				criteria.getQueryCondition().put("idIN", ids);
			}
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmClassManagerDetail> findBy = classManagerDetailService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		String[] headers = new String[] { "工位编号", "加工工艺", "作业员工号", "姓名", "备注" };
		classManagerDetailService.exportExcelData(response, findBy.getContent(), downName + ".xlsx", headers);
		ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile, String tmClassManagerId) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = classManagerDetailService.doImportExcelData(book, request, tmClassManagerId);
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
