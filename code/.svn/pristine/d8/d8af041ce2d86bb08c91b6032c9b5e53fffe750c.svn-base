package com.wis.mes.file.controller;

import java.io.File;
import java.io.IOException;
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
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.file.entity.TsFile;
import com.wis.mes.file.service.TsFileService;

@Controller
@RequestMapping(value = "/tsFile")
public class TsFileController extends BaseController{
	@Autowired
	private TsFileService service;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("/file/file_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "nameCn","no" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}
	
	@RequestMapping(value="/downLoad")
	public void downLoad(HttpServletRequest request,HttpServletResponse response,Integer id) throws IOException{
		TsFile bean = service.findById(id);
		File file = new File(bean.getFilePath());
		LoadUtils.setContent(request, response, file, file.getName());
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TsFile> list = service.findAllInIds(deleteIds);
		for (TsFile tsFile : list) {
			File file = new File(tsFile.getFilePath());
			if(file.exists()) {
				file.delete();
				service.doDeleteById(tsFile.getId());
			}
		}
		return ActionUtils.handleResult(response);
	}
}
