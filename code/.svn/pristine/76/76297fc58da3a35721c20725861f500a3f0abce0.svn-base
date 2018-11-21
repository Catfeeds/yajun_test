package com.wis.mes.production.pmc.controller;

import java.util.List;

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
import com.wis.mes.production.pmc.entity.TbPmcPmst;
import com.wis.mes.production.pmc.entity.TbPmcPmstLog;
import com.wis.mes.production.pmc.service.TbPmcPmstLogService;
import com.wis.mes.production.pmc.service.TbPmcPmstService;

@Controller
@RequestMapping(value = "/tbPmcPmst")
public class TbPmcPmstController extends BaseController {

	@Autowired
	private TbPmcPmstService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TbPmcPmstLogService pmcPmstLog;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		List<TbPmcPmst> pmcPmsts = service.findAll();
		TbPmcPmst pmstAffiche = new TbPmcPmst();
		TbPmcPmst finPunchAffiche = new TbPmcPmst();
		TbPmcPmst finPunchPAarameter = new TbPmcPmst();
		TbPmcPmst pmstWorkParameter = new TbPmcPmst();//工作参数
		TbPmcPmst pmstRecessParameter = new TbPmcPmst();//休息参数
		TbPmcPmst earlyInspectionBean = new TbPmcPmst();//休息参数
		if(null != pmcPmsts && pmcPmsts.size() > 0){
			for(TbPmcPmst bean:pmcPmsts){
				if(ConstantUtils.PMST_AFFICHE.equals(bean.getModelType())){
					pmstAffiche = bean;
				}else if(ConstantUtils.FIN_PUNCH_AFFICHE.equals(bean.getModelType())){
					finPunchAffiche = bean;
				}else if(ConstantUtils.FIN_PUNCH_PARAMETER.equals(bean.getModelType())){
					finPunchPAarameter = bean;
				}else if(ConstantUtils.PMST_WORK_PARAMETER.equals(bean.getModelType())){
					pmstWorkParameter = bean;
				}else if(ConstantUtils.PMST_RECESS_PARAMETER.equals(bean.getModelType())){
					pmstRecessParameter = bean;
				}else if(ConstantUtils.EARLY_INSPECTION_TIME.equals(bean.getModelType())) {
					earlyInspectionBean = bean;
				}
			}
		}
		modelMap.addAttribute("pmstAffiche",pmstAffiche);
		modelMap.addAttribute("finPunchAffiche",finPunchAffiche);
		modelMap.addAttribute("finPunchPAarameter",finPunchPAarameter);
		modelMap.addAttribute("pmstWorkParameter",pmstWorkParameter);
		modelMap.addAttribute("pmstRecessParameter",pmstRecessParameter);
		modelMap.addAttribute("earlyInspectionBean",earlyInspectionBean);
		return new ModelAndView("pmc/pmc_pmst_info");
	}

	
	@ResponseBody
	@RequestMapping(value = "/pmstParameterList")
	public JsonActionResult pmstParameterList(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}
	
	@ResponseBody
	@RequestMapping(value = "/updatePmcParameter")
	public JsonActionResult updatePmcParameter(HttpServletResponse response, TbPmcPmst bean) {
		TbPmcPmst before = service.findById(bean.getId());
		service.doUpdate(bean);
		TbPmcPmstLog pmsLog = new TbPmcPmstLog();
		copy(bean, pmsLog,"id");
		pmcPmstLog.doSave(pmsLog);
		logService.doUpdateLog("TbPmcPmst", before, bean);
		service.sendParameterToOPC(bean);
		return ActionUtils.handleResult(response);
	}
	@RequestMapping(value = "/pmstr5UpdateInput")
	public ModelAndView pmstr5UpdateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		return new ModelAndView("/pmc/pmc_r5pmst_update");
	}

	@ResponseBody
	@RequestMapping(value = "/pmstr5Update")
	public JsonActionResult pmstr5Update(HttpServletResponse response, TbPmcPmst bean) {
		TbPmcPmst before = service.findById(bean.getId());
		service.doUpdate(bean);
		TbPmcPmstLog pmsLog = new TbPmcPmstLog();
		copy(bean, pmsLog,"id");
		pmcPmstLog.doSave(pmsLog);
		logService.doUpdateLog("TbPmcPmst", before, bean);
		return ActionUtils.handleResult(response);
	}
	
}
