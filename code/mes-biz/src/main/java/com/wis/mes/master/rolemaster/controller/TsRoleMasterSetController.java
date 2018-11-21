package com.wis.mes.master.rolemaster.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.rolemaster.service.TsRoleMasterSetService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Controller
@RequestMapping(value = "/roleMasgerSet")
public class TsRoleMasterSetController extends BaseController {

	@Autowired
	private TsRoleMasterSetService service;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workshopService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private TmUlocService ulocService;

	@RequestMapping(value = "/plantList")
	public ModelAndView plantList(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer roleId,String nameCn) {
		/*TmPlant tmPlant = new TmPlant();
		tmPlant.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		List<TmPlant> tmPlants = plantService.findByEg(tmPlant);*/
		criteria.setQueryPage(false);
		criteria.setQueryRelationEntity(true);
		criteria.getQueryCondition().put("enabled",ConstantUtils.TYPE_CODE_ENABLED_ON);
		if(StringUtils.isNotBlank(nameCn)){
			criteria.getQueryCondition().put("nameCn",nameCn);
			criteria.addFuzzyQueryFiled("nameCn");
		}
		List<TmPlant> tmPlants = plantService.findBy(criteria).getContent();
		List<Integer> plantIds = service.getPlantIds(roleId);
		List<Map<String, Object>> plantList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < tmPlants.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TmPlant plant = tmPlants.get(i);
			map.put("id", plant.getId());
			map.put("no", plant.getNo());
			map.put("name", plant.getNameCn());
			map.put("checked", plantIds.contains(plant.getId()));
			plantList.add(map);
		}
		modelMap.addAttribute("plantList", plantList);
		return new ModelAndView("/core/role/role_plant");
	}

	@RequestMapping(value = "/workshopList")
	public ModelAndView workshopList(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer roleId,String nameCn) {
		/*TmWorkshop workshop = new TmWorkshop();
		workshop.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		List<TmWorkshop> tmWorkshops = workshopService.findByEg(workshop);*/
		criteria.setQueryPage(false);
		criteria.setQueryRelationEntity(false);
		criteria.getQueryCondition().put("enabled",ConstantUtils.TYPE_CODE_ENABLED_ON);
		if(StringUtils.isNotBlank(nameCn)){
			criteria.getQueryCondition().put("nameCn",nameCn);
			criteria.addFuzzyQueryFiled("nameCn");
		}
		List<TmWorkshop> tmWorkshops = workshopService.findBy(criteria).getContent();
		List<Integer> workshopIds = service.getWorkshopIds(roleId);
		List<Map<String, Object>> workshopList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < tmWorkshops.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TmWorkshop tmWorkshop = tmWorkshops.get(i);
			map.put("id", tmWorkshop.getId());
			map.put("no", tmWorkshop.getNo());
			map.put("name", tmWorkshop.getNameCn());
			map.put("checked", workshopIds.contains(tmWorkshop.getId()));
			workshopList.add(map);
		}
		modelMap.addAttribute("workshopList", workshopList);
		return new ModelAndView("/core/role/role_workshop");
	}

	@RequestMapping(value = "/lineList")
	public ModelAndView lineList(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer roleId,String nameCn) {
		/*TmLine line = new TmLine();
		line.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		List<TmLine> tmLines = lineService.findByEg(line);*/
		
		criteria.setQueryPage(false);
		criteria.setQueryRelationEntity(true);
		criteria.getQueryCondition().put("enabled",ConstantUtils.TYPE_CODE_ENABLED_ON);
		if(StringUtils.isNotBlank(nameCn)){
			criteria.addFuzzyQueryFiled("nameCn");
			criteria.addQueryCondition("nameCn",nameCn);
		}
		List<TmLine> tmLines = lineService.findBy(criteria).getContent();
		List<Integer> lineIds = service.getLineIds(roleId);
		List<Map<String, Object>> lineList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < tmLines.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TmLine tmLine = tmLines.get(i);
			map.put("id", tmLine.getId());
			map.put("no", tmLine.getNo());
			map.put("name", tmLine.getNameCn());
			map.put("checked", lineIds.contains(tmLine.getId()));
			lineList.add(map);
		}
		modelMap.addAttribute("lineList", lineList);
		return new ModelAndView("/core/role/role_line");
	}

	@RequestMapping(value = "/ulocList")
	public ModelAndView ulocList(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap,
			Integer roleId,String name) {
		criteria.setQueryPage(false);
		criteria.setQueryRelationEntity(true);
		criteria.getQueryCondition().put("enabled",ConstantUtils.TYPE_CODE_ENABLED_ON);
		if(StringUtils.isNotBlank(name)){
			criteria.addFuzzyQueryFiled("name");
			criteria.addQueryCondition("name",name);
		}
		List<TmUloc> tmUlocs = ulocService.findBy(criteria).getContent();
		List<Integer> ulocIds = service.getUlocIds(roleId);
		List<Map<String, Object>> ulocList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < tmUlocs.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TmUloc tmUloc = tmUlocs.get(i);
			map.put("id", tmUloc.getId());
			map.put("no", tmUloc.getNo());
			map.put("name", tmUloc.getName());
			map.put("checked", ulocIds.contains(tmUloc.getId()));
			ulocList.add(map);
		}
		modelMap.addAttribute("ulocList", ulocList);
		return new ModelAndView("/core/role/role_uloc");
	}

	@ResponseBody
	@RequestMapping(value = "/saveMenu")
	public JsonActionResult saveMenu(HttpServletResponse response, String plantIds, String workshopIds, String lineIds,
			String ulocIds, Integer roleId) {
		Integer[] plantId = getIds(plantIds);
		Integer[] workshopId = getIds(workshopIds);
		Integer[] lineId = getIds(lineIds);
		Integer[] ulocId = getIds(ulocIds);
		service.doSaveRoleMenu(plantId, workshopId, lineId, ulocId, roleId);
		return ActionUtils.handleResult(response);
	}

}
