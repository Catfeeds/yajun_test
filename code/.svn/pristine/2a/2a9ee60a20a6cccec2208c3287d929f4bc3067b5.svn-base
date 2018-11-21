package com.wis.basis.systemadmin.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.vo.TypeVo;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.vo.DataPermissionRelationVo;
import com.wis.basis.systemadmin.vo.DataPermissionVo;
import com.wis.core.dao.OrderEnum;
import com.wis.core.framework.entity.DataPermissionRelation;
import com.wis.core.framework.service.DataPermissionRelationService;
import com.wis.core.framework.service.DictEntryService;

@Controller
@RequestMapping(value = "/dataPermissionRel")
public class DataPermissionRelController extends BaseController {

	@Autowired
	private DataPermissionRelationService service;
	@Autowired
	private DictEntryService entryService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria) {
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "dataPermission.module", "dataPermission.code", "dataPermission.url" }));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage( criteria.getOffset()/criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("asc".equalsIgnoreCase(criteria.getOrder())? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView grantPermissionInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("permissionSetId", id);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/data/data_permission_grant"), addEntry());
	}

	private TypeVo[] addEntry() {
		TypeVo valueType = new TypeVo(ConstantUtils.TYPE_CODE_DATA_PERMISSION_VALUE_TYPE, "options");
		TypeVo permissionModule = new TypeVo(ConstantUtils.TYPE_CODE_DATA_PERMISSION_MUDULE, "moduleOptions");
		return new TypeVo[] { valueType, permissionModule };
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(Integer permissionSetId, ModelMap modelMap) {
		modelMap.addAttribute("permissionSetId", permissionSetId);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/data/data_permission_grant_add"), addEntry());
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(Integer id, ModelMap modelMap) {
		DataPermissionRelation relation = service.findById(id);
		DataPermissionRelationVo vo = (DataPermissionRelationVo) copy(relation, new DataPermissionRelationVo());
		DataPermissionVo dataPermissionVo = (DataPermissionVo) copy(relation.getDataPermission(), new DataPermissionVo());
		vo.setDataPermissionVo(dataPermissionVo);
		modelMap.addAttribute("bean", changeValueByType(vo));
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/core/permission/data/data_permission_grant_update"), addEntry());
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletResponse response, DataPermissionRelationVo rel) {
		service.doSave(changeValueByType((DataPermissionRelation) copy(rel, new DataPermissionRelation()), rel));
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, DataPermissionRelationVo rel) {
		DataPermissionRelation dest = (DataPermissionRelation) copy(rel, service.findById(rel.getId()));
		service.doUpdate(changeValueByType(dest, rel));
		return ActionUtils.handleResult(response);
	}

	private DataPermissionRelation changeValueByType(DataPermissionRelation dest, DataPermissionRelationVo rel) {
		if (ConstantUtils.ENTRY_CODE_DATA_PERMISSION_VALUE_TYPE_SQL.equals(rel.getType())) {
			dest.setValue(rel.getSql());
		} else if (ConstantUtils.ENTRY_CODE_DATA_PERMISSION_VALUE_TYPE_RANGE_DATE.equals(rel.getType())) {
			dest.setValue(rel.getStartTime() + ConstantUtils.DATA_PERMISSION_VALUE_RANGE_SPLIT + rel.getEndTime());
		} else if (ConstantUtils.ENTRY_CODE_DATA_PERMISSION_VALUE_TYPE_RANGE_NORMAL.equals(rel.getType())) {
			dest.setValue(rel.getStart() + ConstantUtils.DATA_PERMISSION_VALUE_RANGE_SPLIT + rel.getEnd());
		}
		return dest;
	}

	private DataPermissionRelationVo changeValueByType(DataPermissionRelationVo rel) {
		if (ConstantUtils.ENTRY_CODE_DATA_PERMISSION_VALUE_TYPE_SQL.equals(rel.getType())) {
			rel.setSql(rel.getValue());
		} else if (ConstantUtils.ENTRY_CODE_DATA_PERMISSION_VALUE_TYPE_RANGE_DATE.equals(rel.getType())) {
			String[] arr = StringUtils.splitPreserveAllTokens(rel.getValue(), ConstantUtils.DATA_PERMISSION_VALUE_RANGE_SPLIT);
			rel.setStartTime(arr[0]);
			rel.setEndTime(arr[1]);
		} else if (ConstantUtils.ENTRY_CODE_DATA_PERMISSION_VALUE_TYPE_RANGE_NORMAL.equals(rel.getType())) {
			String[] arr = StringUtils.splitPreserveAllTokens(rel.getValue(), ConstantUtils.DATA_PERMISSION_VALUE_RANGE_SPLIT);
			rel.setStart(arr[0]);
			rel.setEnd(arr[1]);
		}
		return rel;
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletResponse response, String ids) {
		service.doDeleteById(getIds(ids));
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/selectValue")
	public ModelAndView selectValue(HttpServletRequest request, String url, String pageId) {
		request.setAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, pageId);
		return new ModelAndView("forward:" + url);
	}

	@ResponseBody
	@RequestMapping(value = "/testSQL")
	public JsonActionResult testByCustomSQL(HttpServletRequest request, HttpServletResponse response, Integer permissionId, String sql) {
		try {
			service.testFindByCustomSQL(permissionId, sql);
			return ActionUtils.handleResult(response);
		} catch (Exception e) {
			this.handelException(e, request, response);
			return null;
		}
	}
}
