/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.systemadmin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.entity.Orgnization;
import com.wis.basis.systemadmin.entity.User;
import com.wis.basis.systemadmin.model.OrgnizationTree;
import com.wis.basis.systemadmin.service.OrgnizationService;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.basis.systemadmin.vo.OrgnizationVo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.framework.service.DictEntryService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/orgnization")
public class OrgnizationController extends BaseController {

	@Autowired
	private OrgnizationService service;
	@Autowired
	private UserService userService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listTreeInput")
	public ModelAndView listTreeInput(ModelMap modelMap) {
		addEntry(modelMap);
		return new ModelAndView("/core/orgnization/orgnization_main");
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(Integer parentId, String dimension, ModelMap modelMap) {
		modelMap.addAttribute("parentId", parentId);
		modelMap.addAttribute("dimension", dimension);
		addEntry(modelMap);
		return new ModelAndView("/core/orgnization/orgnization_list");
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(Integer parentId, String dimension, ModelMap modelMap) {
		modelMap.addAttribute("parent", service.findById(parentId));
		modelMap.addAttribute("dimension", dimension);
		addEntry(modelMap);
		return new ModelAndView("/core/orgnization/orgnization_add");
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		Orgnization orgnization = service.findById(id);
		if (orgnization.getParentId() != null) {
			Orgnization parent = service.findById(orgnization.getParentId());
			modelMap.addAttribute("parent", parent);
		}
		if (orgnization.getLeaderId() != null) {
			User user = userService.findById(orgnization.getLeaderId());
			orgnization.setLeader(user);
		}
		modelMap.addAttribute("orgnization", orgnization);
		addEntry(modelMap);
		return new ModelAndView("/core/orgnization/orgnization_update");
	}

	@RequestMapping(value = "/viewInput")
	public ModelAndView viewInput(Integer id, ModelMap modelMap) {
		Orgnization orgnization = service.findById(id);
		if (orgnization.getParentId() != null) {
			Orgnization parent = service.findById(orgnization.getParentId());
			modelMap.addAttribute("parent", parent);
		}
		if (orgnization.getLeaderId() != null) {
			User user = userService.findById(orgnization.getLeaderId());
			orgnization.setLeader(user);
		}
		modelMap.addAttribute("orgnization", orgnization);
		addEntry(modelMap);
		return new ModelAndView("/core/orgnization/orgnization_view");
	}

	@RequestMapping(value = "/departmentSelect")
	public String departmentSelect() {
		return "/core/orgnization/department_select";
	}

	@RequestMapping(value = "/listTree")
	public void listTree(HttpServletResponse response, String dimension) {
		List<OrgnizationTree> trees = service.getTree(dimension);
//		Map<String, OrgnizationTree> trees = service.getMapTree(dimension);
		JSONArray array = JSONArray.fromObject(trees);
		ActionUtils.handleResult(response, array);
	}

	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(HttpServletRequest request, ModelMap modelMap) {
		addEntry(modelMap);
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/orgnization/orgnization_select"));
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap, String listSelect) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "code", "parent.name" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		criteria.setQueryRelationEntity(true);
		Map<String, Object> map = criteria.getQueryCondition();
		if (map.containsKey("parentId")) {
			if (Integer.valueOf(map.get("parentId").toString()) == 0) {
				map.put("parentAll", "all");
				map.remove("parentId");
			}
		}
		if (map.containsKey("type") && map.get("type").equals("all")) {
			map.remove("type");
		} else if (!StringUtils.isEmpty(listSelect) && !map.containsKey("type")) {
			map.put("type", ConstantUtils.ENTRY_CODE_PART);
		}

		addEntry(modelMap);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, OrgnizationVo orgnizationVo) {
		try {
			service.doAdd(orgnizationVo);
			return ActionUtils.handleResult(response);
		} catch (Exception e) {
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, OrgnizationVo orgnizationVo) {
		Orgnization orgnization = service.findById(orgnizationVo.getId());
		orgnization.setCode(orgnizationVo.getCode());
		orgnization.setName(orgnizationVo.getName());
		orgnization.setDescription(orgnizationVo.getDescription());
		orgnization.setType(orgnizationVo.getType());
		orgnization.setLeaderId(orgnizationVo.getLeaderId());
		service.doSaveOrUpdate(orgnization);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		service.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}

	private void addEntry(ModelMap modelMap) {
		modelMap.addAttribute("options", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ORGNIZATION_TYPE));
		modelMap.addAttribute("dimensions", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ORGNIZATION_DIMENSION));
	}

	@RequestMapping(value = "/checkCode")
	public void checkCode(HttpServletResponse response, String code, String param) {
		Integer id = service.checkCode(param);
		JSONObject result = new JSONObject();
		if (id != null && !param.equals(code)) {
			result.put("status", "n");
			result.put("info", "组织代码已存在！");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}

	@ResponseBody
	@RequestMapping(value="/addUser")
	public JsonActionResult addUser(HttpServletResponse response, Integer orgnizationId, String userIds) {
		service.doAddUser(orgnizationId, getIds(userIds));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/removeUser")
	public JsonActionResult removeUser(HttpServletResponse response,String userIds,Integer orgnizationId){
		service.doRemoveUser(orgnizationId,getIds(userIds));
		return ActionUtils.handleResult(response);
	}
}
	