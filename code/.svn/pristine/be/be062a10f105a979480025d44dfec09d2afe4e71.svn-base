package com.wis.mes.master.uloc.controller;

import java.io.File;
import java.io.IOException;
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
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.uloc.service.TmUlocSipNcService;

@Controller
@RequestMapping(value = "/ulocSipNc")
public class TmUlocSipNcController extends BaseController {

	@Autowired
	private TmUlocSipNcService service;
	@Autowired
	private TmNcService ncService;
	@Autowired
	private TmNcGroupService ncGroupService;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap,
			Integer tmUlocSipId) {
		modelMap.addAttribute("tmUlocSipId", tmUlocSipId);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMap.addAttribute("params", request.getParameter("params"));
		return new ModelAndView("/master/uloc/sip/nc/uloc_sip_nc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap,
			String tmUlocSipId) {
		criteria.getQueryCondition().put("tmUlocSipId", tmUlocSipId);
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort() == null ? "id" : criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer tmUlocSipId) {
		modelMap.addAttribute("tmUlocSipId", tmUlocSipId);
		modelMap.addAttribute("ncGroupOptions", ncGroupService.getDictItem(null));
		return new ModelAndView("/master/uloc/sip/nc/uloc_sip_nc_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmUlocSipNc bean) {
		TmUlocSipNc ulocSipNcEg = new TmUlocSipNc();
		ulocSipNcEg.setTmUlocSipId(bean.getTmUlocSipId());
		if(service.findByEg(ulocSipNcEg).size() > 0){
			throw new BusinessException("ULOC_SIP_NC_DATA_ONLY_ONE");
		}
		ulocSipNcEg.setTmNcGroupId(bean.getTmNcGroupId());
		ulocSipNcEg.setTmNcId(bean.getTmNcId());
		List<TmUlocSipNc> list = service.findByEg(ulocSipNcEg);
		if (list.size() > 0) {
			throw new BusinessException("ULOC_SIP_NC_DATA_REPEAT");
		} else {
			TmUlocSipNc ulocSipNc = service.doSave(bean);
			logService.doAddLog("TmUlocSipNc", ulocSipNc);
			return ActionUtils.handleResult(response);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getSelectNc")
	public List<TmNc> getSelectNc(@Param(value = "tmNcGroupId") final Integer tmNcGroupId) {
		TmNc ncEg = new TmNc();
		ncEg.setTmNcGroupId(tmNcGroupId);
		return ncService.findByEg(ncEg);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap, Integer tmUlocSipId) {
		TmUlocSipNc ulocSipNc = service.findById(Integer.valueOf(id));
		modelMap.addAttribute("ulocSipNc", ulocSipNc);
		modelMap.addAttribute("tmUlocSipId", tmUlocSipId);
		modelMap.addAttribute("ncGroup", ncGroupService.findById(ulocSipNc.getTmNcGroupId()));
		modelMap.addAttribute("nc", ncService.findById(ulocSipNc.getTmNcId()));
		modelMap.addAttribute("ncGroupOptions", ncGroupService.getDictItem(null));
		TmNc nc = new TmNc();
		nc.setTmNcGroupId(ulocSipNc.getTmNcGroupId());
		modelMap.addAttribute("ncOptions", ncService.getDictItem(nc));
		return new ModelAndView("/master/uloc/sip/nc/uloc_sip_nc_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmUlocSipNc bean) {
		TmUlocSipNc ulocSipNcEg = new TmUlocSipNc();
		ulocSipNcEg.setTmUlocSipId(bean.getTmUlocSipId());
		ulocSipNcEg.setTmNcGroupId(bean.getTmNcGroupId());
		ulocSipNcEg.setTmNcId(bean.getTmNcId());
		List<TmUlocSipNc> list = service.findByEg(ulocSipNcEg);
		if (list.size() > 0) {
			throw new BusinessException("ULOC_SIP_NC_DATA_REPEAT");
		} else {
			TmUlocSipNc before = service.findById(bean.getId());
			bean.setTmUlocSipId(bean.getTmUlocSipId());
			service.doUpdate(bean);
			logService.doUpdateLog("TmUlocSip", before, bean);
			return ActionUtils.handleResult(response);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, HttpServletRequest request, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmUlocSipNc> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmUlocSipNc", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportDate")
	public JsonActionResult exportDate(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName, final String ids, String tmUlocSipId)
			throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmUlocSipId", tmUlocSipId);
		criteria.setOrderField(criteria.getSort() == null ? "id" : criteria.getSort());
		final PageResult<TmUlocSipNc> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		final String[] header = new String[] { "不合格组", "不合格代码", "备注" };
		service.exportExcelData(response, findBy.getContent(), downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "工位质检项不合格模板";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "uloc" + File.separator + "ulocSipNc.xlsx";
			final Workbook wb = service.getWorkbookTemp(downName, templatePath);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/importDataUlocSipNc")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile, String tmUlocSipId) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = service.doImportExcelData(book, request, tmUlocSipId);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("ULOC_SIP_NC_IMPORT_FAIL");
		}
	}
}
