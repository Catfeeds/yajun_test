package com.wis.mes.master.parametermanage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.parametermanage.entity.TmParameterManage;
import com.wis.mes.master.parametermanage.entity.TmParameterManageDetail;
import com.wis.mes.master.parametermanage.service.TmParameterManageDetailService;
import com.wis.mes.master.parametermanage.service.TmParameterManageService;
import com.wis.mes.master.parametermanage.vo.ParameterManageVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/parameterManage")
public class TmParameterManageController extends BaseController {

	@Autowired
	private TmParameterManageService service;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmParameterManageDetailService parameterManageDetailService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		List<ParameterManageVo> list= service.parameterColumn();
		modelMap.addAttribute("parameterColumn", JSONArray.fromObject(list).toString());
		return new ModelAndView("master/parametermanage/parameter_manage_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response,Integer pageSize, BootstrapTableQueryCriteria criteria) {
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		return ActionUtils.handleListResult(response, service.queryParameterManage(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		List<TmEquipmentParam> list= service.saveOrUpdateColumn();
		modelMap.addAttribute("parameterColumn", JSONArray.fromObject(list).toString());
		return new ModelAndView("master/parametermanage/parameter_manage_add");
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, String  parameterManage) {
		service.doSaveParameter(parameterManage);
		return ActionUtils.handleResult(response);
	}
	
	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		TmParameterManage param = service.findByParameterManageId(id);
		List<TmEquipmentParam> list= service.saveOrUpdateColumn();//获取最新的表头数据
		Map<String,Object> map = new HashMap<String,Object>();
		if(null != param){
			map.put("backNumber", param.getBackNumber());
			map.put("machineOfName", param.getMachineOfName());
			map.put("id", param.getId());
			List<TmParameterManageDetail> details = param.getParameterManageDetail();
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			Map<String,TmParameterManageDetail> manageDetailMap = new HashMap<String, TmParameterManageDetail>();
			if(null != details && details.size()>0){
				for(TmParameterManageDetail detail:details){
					manageDetailMap.put(detail.getParameterId()+"-"+detail.getParameterName(), detail);
				}
			}
				Map<String,Object> detailMap = null;
				for(TmEquipmentParam pementParam:list){
					//最新的表头数据和之前保存到的表头是否存在，存在的话直接拼接MAP，否则的话新增一条数据在进行拼接MAP
					if(manageDetailMap.containsKey(pementParam.getId()+"-"+pementParam.getParameterName())){
						TmParameterManageDetail manageDetail = manageDetailMap.get(pementParam.getId()+"-"+pementParam.getParameterName());
						detailMap = new HashMap<String,Object>();
						detailMap.put("parameterName", manageDetail.getParameterName());
						detailMap.put("parameterId", manageDetail.getParameterId());
						detailMap.put("parameterExplain", manageDetail.getParameterExplain());
						detailMap.put("maxVal", manageDetail.getMaxVal());
						detailMap.put("minVal", manageDetail.getMinVal());
						detailMap.put("id", manageDetail.getId());
						detailMap.put("equipmentName", pementParam.getEquipmentName());
					}else{
						TmParameterManageDetail detailBean = TmParameterManageDetail.createParameterManageDetail(param.getId(), pementParam.getId(), pementParam.getParameterName(), pementParam.getParameterExplain());
						detailBean = parameterManageDetailService.doSave(detailBean);
						detailMap = new HashMap<String,Object>();
						detailMap.put("parameterName", detailBean.getParameterName());
						detailMap.put("parameterId", detailBean.getParameterId());
						detailMap.put("parameterExplain", detailBean.getParameterExplain());
						detailMap.put("maxVal", detailBean.getMaxVal());
						detailMap.put("minVal", detailBean.getMinVal());
						detailMap.put("id", detailBean.getId());
						detailMap.put("equipmentName", pementParam.getEquipmentName());
					}
					listMap.add(detailMap);
				}
			
			map.put("details", listMap);
		}
		modelMap.addAttribute("bean", JSONObject.fromObject(map).toString());
		return new ModelAndView("master/parametermanage/parameter_manage_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, String  parameterManage) {
		service.doUpdateParameter(parameterManage);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TmParameterManage> list = service.findAllInIds(deleteIds);
		service.doDeleteById(deleteIds);
		StringBuffer parameterIds = new StringBuffer();
		for(Integer deleteId:deleteIds){
			parameterIds.append(deleteId+",");
		}
		if(parameterIds.length()>0){
			parameterIds = parameterIds.deleteCharAt(parameterIds.length()-1);
		}
		service.doDeleteParameterDetail(parameterIds.toString());
		logService.doDeleteLog("TmParameterManage", list);
		return ActionUtils.handleResult(response);
	}
}
