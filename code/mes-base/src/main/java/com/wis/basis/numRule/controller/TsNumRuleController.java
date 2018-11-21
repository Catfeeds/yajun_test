package com.wis.basis.numRule.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.numRule.entity.TsNumRule;
import com.wis.basis.numRule.service.TsNumRuleService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/numRule")
public class TsNumRuleController extends BaseController {

	@Autowired
	private TsNumRuleService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, ModelMap modelMap, String type) {
		List<DictEntry> dictEntry = entryService.getEntryByTypeCode(ConstantUtils.ENTRY_TYPE_SERIALIZABLE_TYPE);// 序列类型
		TsNumRule numRule = new TsNumRule();
		if (StringUtils.isNotBlank(type)) {
			numRule.setType(type);
		} else {
			numRule.setType(dictEntry.get(0).getCode());
		}
		modelMap.addAttribute("typeList", dictEntry);
		List<TsNumRule> numRuleEg = service.findByEg(numRule);
		modelMap.addAttribute("ruleRum", numRuleEg.size() > 0 ? numRuleEg.get(0) : numRule);
		modelMap.addAttribute("type", type);
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, "");
		modelMap.addAttribute("checkboxOption", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED));
		return new ModelAndView("/core/numRule/numRule_list");
	}

	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public JsonActionResult update(HttpServletResponse response, String beans) {
		JSONObject json = JSONObject.fromObject(beans);
		TsNumRule bean;
		bean = service.checkAndGetBean(beans);
		if (StringUtils.isNotBlank(json.getString("id"))) {
			TsNumRule before = service.findById(json.getInt("id"));
			service.doUpdate(bean);
			logService.doUpdateLog("TsNumRule", before, bean);
		} else {
			bean = service.doSave(bean);
			logService.doAddLog("TsNumRule", bean);
		}
		return ActionUtils.handleResult(response);
	}

}
