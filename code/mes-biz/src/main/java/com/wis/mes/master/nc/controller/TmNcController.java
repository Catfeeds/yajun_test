package com.wis.mes.master.nc.controller;

import java.io.File;
import java.io.IOException;
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
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmFaultGradeService;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.nc.vo.NGVo;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/nc")
public class TmNcController extends BaseController {

	@Autowired
	private TmNcService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmNcGroupService ncGroupService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmFaultGradeService faultGradeService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmNcGroupService tmNcGroupService;
	@Autowired
	private TmLineService tmLineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, ModelMap modelMap, Integer tmNcGroupId, String no,
			String name, String extCode) {
		List<NGVo> faultGrades = faultGradeService.getDictItem(null);
		modelMap.addAttribute("faultGrades", JSONArray.fromObject(faultGrades).toString());
		modelMap.addAttribute("optionNgEntrance", JSONArray.fromObject(ulocService.getUlocNgExitEnterMap()).toString());
		modelMap.addAttribute("tmNcGroupId", tmNcGroupId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		modelMap.addAttribute("no", no);
		modelMap.addAttribute("name", name);
		modelMap.addAttribute("extCode", extCode);
		return new ModelAndView("/master/nc/nc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap,
			String no, String name, String extCode) {
		if (StringUtil.isNotBlank(no)) {
			criteria.getQueryCondition().put("no", no);
		}
		if (StringUtil.isNotBlank(name)) {
			criteria.getQueryCondition().put("name", name);
		}
		if (StringUtil.isNotBlank(extCode)) {
			criteria.getQueryCondition().put("extCode", extCode);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "extCode","type"}));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, Integer tmNcGroupId) {
		List<NGVo> faultGrades = faultGradeService.getDictItem(null);
		modelMap.addAttribute("faultGrades", JSONArray.fromObject(faultGrades).toString());
		modelMap.addAttribute("optionNgEntrance", JSONArray.fromObject(ulocService.getUlocNgExitEnterMap()).toString());
		modelMap.addAttribute("optionFaultGrades", faultGrades);
		modelMap.addAttribute("ncGroup", ncGroupService.getDictItem(null));
		modelMap.addAttribute("tmNcGroupId", tmNcGroupId);
		modelMap.addAttribute("ncTypes", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_NC_TYPE));
		return new ModelAndView("/master/nc/nc_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmNc bean) {
		try{
			TmNcGroup ncGroup = tmNcGroupService.findById(bean.getTmNcGroupId());
			TmLine line = new TmLine();
			if(null != ncGroup.getTmLineId()) {
				line = tmLineService.findById(ncGroup.getTmLineId());
			}else {
				return ActionUtils.handleResult(false,"故障机组："+ncGroup.getName()+"没有维护产线。");
			}
			bean.setNo(service.createTemplateNo(line.getNo()+ncGroup.getNo()));
			bean = service.doSave(bean);
		}catch (Exception e) {
			throw new BusinessException("SAVE_DATA_VALID_ERROR_INFO");
		}
		logService.doAddLog(ConstantUtils.AUDIT_TYPE_NC, bean);
		return ActionUtils.handleResult(response);
	}
		
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap, Integer tmNcGroupId) {
		List<NGVo> faultGrades = faultGradeService.getDictItem(null);
		modelMap.addAttribute("faultGrades", JSONArray.fromObject(faultGrades).toString());
		modelMap.addAttribute("optionNgEntrance", JSONArray.fromObject(ulocService.getUlocNgExitEnterMap()).toString());
		modelMap.addAttribute("optionFaultGrades", faultGrades);
		modelMap.addAttribute("nc", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("ncGroup", ncGroupService.getDictItem(null));
		modelMap.addAttribute("tmNcGroupId", tmNcGroupId);
		modelMap.addAttribute("ncTypes", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_NC_TYPE));
		return new ModelAndView("/master/nc/nc_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletRequest request, HttpServletResponse response, TmNc bean) {
		TmNc before = service.findById(bean.getId());
		bean.setTmNcGroupId(before.getTmNcGroupId());
		try{
			service.doUpdate(bean);
		}catch (Exception e) {
			throw new BusinessException("SAVE_DATA_VALID_ERROR_INFO");
		}
		logService.doUpdateLog(ConstantUtils.AUDIT_TYPE_NC, before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		try{
			List<TmNc> list = service.findAllInIds(deleteIds);
			service.doDeleteById(deleteIds);
			logService.doDeleteLog(ConstantUtils.AUDIT_TYPE_NC, list);
			return ActionUtils.handleResult(response);
		}catch (Exception e) {
			throw new BusinessException("SAVE_DATA_VALID_ERROR_INFO");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile,String tmNcGroupId) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = service.doImportExcelData(book, request, tmNcGroupId);
			return ActionUtils.handleResult(true,msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_NC");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(HttpServletRequest request, HttpServletResponse response, QueryCriteria criteria,
			String downName, String ids,String tmNcGroupId) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.getQueryCondition().put("tmNcGroupId", tmNcGroupId);
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "remarks" }));
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		String[] header = new String[] {"故障编号", "故障描述", "故障等级","NG出口","备注"};
		service.exportExcelData(response, service.findBy(criteria).getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "故障代码模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "nc" + File.separator + "nc.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request,response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
}
