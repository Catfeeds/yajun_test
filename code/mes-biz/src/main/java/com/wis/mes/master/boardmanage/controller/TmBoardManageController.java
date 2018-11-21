package com.wis.mes.master.boardmanage.controller;

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
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.boardmanage.entity.TmBoardManage;
import com.wis.mes.master.boardmanage.service.TmBoardManageService;
import com.wis.mes.master.equipment.service.TmEquipmentService;

@Controller
@RequestMapping(value = "/boardManage")
public class TmBoardManageController extends BaseController {

	@Autowired
	private TmBoardManageService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmEquipmentService equipmentService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/boardmanage/board_manage_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/boardmanage/board_manage_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmBoardManage bean)
			throws Exception {
		checkNo(TmBoardManage.createRegionMark(bean.getRegionMark()), null);
		bean = service.doSave(bean);
		service.sendParameterToOPC(bean);
		logService.doAddLog("TmBoardManage", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/boardmanage/board_manage_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmBoardManage bean) {
		checkNo(TmBoardManage.createRegionMark(bean.getRegionMark()), bean.getId());
		TmBoardManage before = service.findById(bean.getId());
		service.doUpdate(bean);
		service.sendParameterToOPC(bean);
		logService.doUpdateLog("TmBoardManage", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmBoardManage> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TmBoardManage", list);
		return ActionUtils.handleResult(response);
	}

	private void checkNo(TmBoardManage eg, Integer id) {
		List<TmBoardManage> list = service.findByEg(eg);
		if (null != list && list.size() > 0) {
			if (null == id) {
				throw new BusinessException("ERROR_KEY", "区域不能重复。");
			} else {
				for (TmBoardManage bean : list) {
					if (!bean.getId().equals(id)) {
						throw new BusinessException("ERROR_KEY", "区域不能重复。");
					}
				}
			}
		}
	}

	private void modelMapAddAttribute(ModelMap modelMap) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no" }));
		criteria.getQueryCondition().put("no", ConstantUtils.EQUIPMENT_BOARD_NO);
		modelMap.addAttribute("equipments", equipmentService.queryDictItem(criteria));
		modelMap.addAttribute("smRegionMarks", entryService.getEntryByTypeCode(ConstantUtils.SM_REGION_MARK));
	}
}
