/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.systemadmin.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.entity.Position;
import com.wis.basis.systemadmin.entity.User;
import com.wis.basis.systemadmin.service.OrgnizationService;
import com.wis.basis.systemadmin.service.PositionService;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.dao.OrderEnum;

import net.sf.json.JSONObject;

/**
 * @author vincent
 *
 */
@Controller
@RequestMapping(value="/position")
public class PositionController extends BaseController{
	@Autowired
	private PositionService service;
	@Autowired
	private OrgnizationService orgnizationService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/listInput")
	public ModelAndView listInput(Integer orgnizationId,ModelMap modelMap){
		modelMap.put("orgnizationId", orgnizationId);
		return  new ModelAndView("/core/position/position_list");
	}
	@RequestMapping(value="/listSelect")
	public ModelAndView listSelect(Integer orgnizationId,ModelMap modelMap,HttpServletRequest request){
		if (orgnizationId != null) {
			modelMap.put("orgnizationId", orgnizationId);
		}else {
			modelMap.put("orgnizationId", "");
		}
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/position/position_select") );  
	}
	@RequestMapping(value="/listView")
	public ModelAndView listView(Integer userId,ModelMap modelMap,HttpServletRequest request){
		return ActionUtils.handleSelectResult(request, modelMap, new ModelAndView("/core/position/position_view") );  
	}
	@RequestMapping(value = "userListInput")
	public ModelAndView userListInput(Integer id, ModelMap modelMap) {
		modelMap.put("positionId", id);
		return new ModelAndView("/core/position/user_list");
	}
	@RequestMapping(value="/addInput")
	public ModelAndView addInput(Integer orgnizationId,String orgnizationName,ModelMap modelMap){
		modelMap.put("orgnizationId", orgnizationId);
		modelMap.put("orgnization", orgnizationService.findById(orgnizationId));
		return  new ModelAndView("/core/position/position_add");
	}
	@RequestMapping(value="/updateInput")
	public ModelAndView updateInput(HttpServletResponse response,String id,ModelMap modelMap){
		Position position = service.findById(Integer.valueOf(id));
		modelMap.put("orgnization", orgnizationService.findById(position.getOrgnizationId()));
		if (position.getParentId() != null) {
			modelMap.put("parent", service.findById(position.getParentId()));
		}
		modelMap.addAttribute("position", position);
		return  new ModelAndView("/core/position/position_update");
	}
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "name", "code", "parent.name"}));
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		Map<String, Object> params = criteria.getQueryCondition();
		if (params.containsKey("orgnizationId")) {
			if (!StringUtils.hasText(params.get("orgnizationId").toString())) {
				params.remove("orgnizationId");
			}
		}
		criteria.setQueryRelationEntity(true);
		return ActionUtils.handleListResult(response, service.findBy(criteria));

	}
	@ResponseBody
	@RequestMapping(value="/add")
	public JsonActionResult add(HttpServletResponse response,Position position){
		service.doSave(position);
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/update")
	public JsonActionResult update(HttpServletResponse response,Position position){
		service.doSaveOrUpdate(position);
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonActionResult delete(HttpServletResponse response,String ids){
		service.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/updateUser")
	public JsonActionResult updateUser(HttpServletResponse response,Integer positionId,String userIds){
		service.doUpdatePositionUser( positionId,getIds(userIds));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/updatePosition")
	public JsonActionResult updatePosition(HttpServletResponse response,String positionIds,Integer userId){
		service.doUpdateUserPosition(userId,getIds(positionIds));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/deleteUser")
	public JsonActionResult deleteUser(HttpServletResponse response,String ids,Integer positionId){
		//service.doDeleteUser(positionId,getIds(ids));
		service.doDeleteRelationUser(positionId,getIds(ids));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value="/deletePosition")
	public JsonActionResult deletePosition(HttpServletResponse response,String ids,Integer userId){
		service.doDeletePosition(userId,getIds(ids));
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value = "/userAdd")
	public JsonActionResult userAdd(HttpServletResponse response, User user, String positionId) {
		User entity = userService.doSave(user);
		if (StringUtils.hasText(positionId)) {
			service.doUpdateUserPosition(entity.getId(),getIds(positionId));
		}
		return ActionUtils.handleResult(response);
	}
	@RequestMapping(value="/checkCode")
	public void checkCode(HttpServletResponse response,String code,String param){
		Integer id=service.checkCode(param);
		JSONObject result=new JSONObject();
		if (id !=null&& !param.equals(code)) {
			result.put("status", "n");
			result.put("info","岗位代码已存在！" );
		}else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response,result);
	}
}
