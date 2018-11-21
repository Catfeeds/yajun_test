package com.wis.mes.master.badparts.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.badparts.entity.TmBadParts;
import com.wis.mes.master.badparts.service.TmBadPartsService;
import com.wis.mes.master.line.service.TmLineService;

@Controller
@RequestMapping(value = "/badParts")
public class TmBadPartsController extends BaseController {

	@Autowired
	private TmBadPartsService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmLineService tmLineService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		return new ModelAndView("/master/badparts/bad_parts_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(final HttpServletResponse response, final BootstrapTableQueryCriteria criteria,
			final ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no", "name", "remarks" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(final HttpServletResponse response, final QueryCriteria criteria,
			final ModelMap modelMap) {
		modelMap.addAttribute("lines", tmLineService.queryDictItem(new QueryCriteria()));
		return new ModelAndView("/master/badparts/bad_parts_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(final HttpServletRequest request, final HttpServletResponse response, TmBadParts bean) {
		checkNo(TmBadParts.createNo(bean.getNo()),null);
		bean = service.doSave(bean);
		logService.doAddLog("TmBadParts", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(final HttpServletResponse response, final String id, final ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMap.addAttribute("lines", tmLineService.queryDictItem(new QueryCriteria()));
		return new ModelAndView("/master/badparts/bad_parts_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(final HttpServletRequest request, final HttpServletResponse response,
			final TmBadParts bean) {
		checkNo(TmBadParts.createNo(bean.getNo()),bean.getId());
		final TmBadParts before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TmBadParts", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(final HttpServletRequest request, final HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmBadParts> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmBadParts", list);
		return ActionUtils.handleResult(response);
	}
	
	private void checkNo(TmBadParts eg,Integer id){
		List<TmBadParts> list = service.findByEg(eg);
		if(null != list && list.size() > 0){
			if(null == id){
				throw  new BusinessException("ERROR_KEY","部品不良编号："+eg.getNo()+",已存在。");
			}else{
				for(TmBadParts bean:list){
					if(!bean.getId().equals(id)){
						throw  new BusinessException("ERROR_KEY","部品不良编号："+eg.getNo()+",已存在。");
					}
				}
			}
		}
	}
}
