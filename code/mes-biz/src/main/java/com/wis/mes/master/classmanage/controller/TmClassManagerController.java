package com.wis.mes.master.classmanage.controller;

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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;

@Controller
@RequestMapping(value = "/classManager")
public class TmClassManagerController extends BaseController{
	
	@Autowired
	private TmClassManagerService classManagerService;
	
	@Autowired
	private TmPlantService tmPlantService;
	
	@Autowired
	private TmLineService tmLineService;
	
	@Autowired
	private DictEntryService entryService;
	
	@Autowired
	private AuditLogService logService;
	
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter("currentPageId"));
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/classmanager/classmanager_list");
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
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		return ActionUtils.handleListResult(response, classManagerService.findBy(criteria));
	}
	
	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/classmanager/classmanager_add");
	}

	
	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmClassManager bean) {
		if(bean.getEnabled().equals(ConstantUtils.TYPE_CODE_ENABLED_ON)){
			verifyUniqueValues(bean);
		}
		bean = classManagerService.doSave(bean);
		logService.doAddLog("TmClassManager", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", classManagerService.findById(Integer.valueOf(id)));
		modelMapAddAttribute(modelMap);
		return new ModelAndView("/master/classmanager/classmanager_update");
	}
	
	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmClassManager bean) {
		if(bean.getEnabled().equals(ConstantUtils.TYPE_CODE_ENABLED_ON)){
			verifyUniqueValues(bean);
		}
		TmClassManager before = classManagerService.findById(bean.getId());
		classManagerService.doUpdate(bean);
		logService.doUpdateLog("TmClassManager", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, HttpServletRequest request, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmClassManager> list = classManagerService.findAllInIds(deleteIds);
		try {
			classManagerService.doDeleteById(deleteIds);
		} catch (Exception e) {
			throw e;
		}
		logService.doDeleteLog("TmClassManager", list);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/plantTolineToClassManager")
	public List<TmClassManager> plantTolineToClassManager(Integer plantId,Integer lineId){
		return classManagerService.plantTolineToClassManager(plantId, lineId);
	}
	
	public void verifyUniqueValues(TmClassManager bean){
		TmClassManager eg = new TmClassManager();
		eg.setTmPlantId(bean.getTmPlantId());
		eg.setTmLineId(bean.getTmLineId());
		eg.setClassGroup(bean.getClassGroup());
		eg.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		List<TmClassManager> tmClassManagers = classManagerService.findByEg(eg);
		if(null != tmClassManagers && tmClassManagers.size() > 0){
			if(null == bean.getId() || (null != bean.getId() && !tmClassManagers.get(0).getId().equals(bean.getId()))){
				throw new BusinessException("ERROR_KEY","事部，产线，班组已存在,并且是启用状态。");
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/selectLineIdClassManager")
	public List<DictEntry> selectLineIdClassManager(Integer lineId) {
		TmClassManager eg = new TmClassManager();
		eg.setTmLineId(lineId);
		return classManagerService.getDictItemEntry(eg);
	}
	
	private void modelMapAddAttribute(ModelMap modelMap) {
		modelMap.addAttribute("enabledOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		modelMap.addAttribute("classGroupOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP));
		modelMap.addAttribute("classOrderOptions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
		modelMap.addAttribute("plantsOptions", tmPlantService.getDictItem(new TmPlant(ConstantUtils.TYPE_CODE_ENABLED_ON)));
		modelMap.addAttribute("lineOptions", tmLineService.queryDictItem(new QueryCriteria()));
	}
	
}
