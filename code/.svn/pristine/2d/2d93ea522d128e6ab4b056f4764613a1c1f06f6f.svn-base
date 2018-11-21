package com.wis.mes.production.untreated.controller;

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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.mes.production.untreated.entity.TpUntreatedNc;
import com.wis.mes.production.untreated.service.TpUntreatedNcService;

import net.sf.json.JSONObject;

/**
 * 未处理缺陷
 * @author tss
 *
 */
@Controller
@RequestMapping(value = "/untratedNc")
public class TpUntreatedNcController extends BaseController {
	@Autowired
	private TsParameterService tsParamenterService;
	@Autowired
	private TpUntreatedNcService service;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, Integer id) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		return new ModelAndView("/production-untratedNc/untratedNc_main");
	}
	@RequestMapping(value = "/untratedNcListInput")
	public ModelAndView untratedNcListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap, String currentPageId) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, currentPageId);
		return new ModelAndView("/production-untratedNc/untratedNc_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {"sn"}));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		//TODO add return view here
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, TpUntreatedNc bean) {
		bean = service.doSave(bean);
		logService.doAddLog("TpUntreatedNc", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", service.findById(Integer.valueOf(id)));
		//TODO add return view here
		return new ModelAndView("");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TpUntreatedNc bean) {
		TpUntreatedNc before = service.findById(bean.getId());
		service.doUpdate(bean);
		logService.doUpdateLog("TpUntreatedNc", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TpUntreatedNc> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TpUntreatedNc", list);
		return ActionUtils.handleResult(response);
	}
	
	/**
	 * 确认 处理完成 
	 * 1) 清除缺陷表中的数据 
	 * 2) TP_WIP 将状态为 “WAIT_REPAIR” 修改成 “PORDUCTION” 
	 * @param response
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toAffirmUntrateNc")
	public JsonActionResult toAffirmUntrateNc(HttpServletResponse response, String id) {
		Integer[] deleteIds = getIds(id);
		List<TpUntreatedNc> list = service.findAllInIds(deleteIds);
		service.changeWipStatus(list);
		service.doDeleteById(deleteIds);
		logService.doDeleteLog("TpUntreatedNc", list);
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/getParamValueByCode")
	public void getParamValueByCode(HttpServletRequest request, HttpServletResponse response, String paramCode,String paramValue) {
		JSONObject result = new JSONObject();
		TsParameter parameter = new TsParameter();
		parameter.setCode(paramCode);
		List<TsParameter> list = tsParamenterService.findByEg(parameter);
		if (list.size()<=0) {
			result.put("status", "n");
		} else {
			String expression = list.get(0).getRegularExpression();
			if(expression.indexOf("/^")>-1 && expression.indexOf("$/")>-1){
				expression = expression.replace("/", "");
				boolean flag = LoadUtils.matchStr(paramValue,expression);
				if(flag){
					result.put("status", "y");
				}else{
					result.put("status", "n");
				}
			}else{//非正则表达式
				result.put("status", "w");
				result.put("expression", expression);
			}
		}
		ActionUtils.handleResult(response, result);
	}
	
}
